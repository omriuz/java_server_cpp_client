package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.Bidi.BidiMessagingProtocol;
import bgu.spl.net.impl.BGS.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NonBlockingConnectionHandler<Communication> implements ConnectionHandler<Communication> {

   private static final int BUFFER_ALLOCATION_SIZE = 1 << 13; //8k
   private static final ConcurrentLinkedQueue<ByteBuffer> BUFFER_POOL = new ConcurrentLinkedQueue<>();

   private final BidiMessagingProtocol<Communication> protocol;
   private final MessageEncoderDecoder<Communication> encdec;
   private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedQueue<>();
   private final SocketChannel chan;
   private final Reactor reactor;
//
   public NonBlockingConnectionHandler(
           MessageEncoderDecoder<Communication> reader,
           BidiMessagingProtocol<Communication> protocol,
           SocketChannel chan,
           Reactor reactor) {
       this.chan = chan;
       this.encdec = reader;
       this.protocol = protocol;
       this.reactor = reactor;
   }

   public Runnable continueRead() {
       ByteBuffer buf = leaseBuffer();

       boolean success = false;
       try {
           success = chan.read(buf) != -1;
       } catch (IOException ex) {
           ex.printStackTrace();
       }

       if (success) {
           buf.flip();
           return () -> {
               try {
                   while (buf.hasRemaining()) {
                        Communication nextMessage = encdec.decodeNextByte(buf.get());
                        if (nextMessage != null) {
                           protocol.process(nextMessage);
                        //    if (response != null) {
                        //        writeQueue.add(ByteBuffer.wrap(encdec.encode(response)));
                        //        reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        //    }
                       }
                   }
               } finally {
                   releaseBuffer(buf);
               }
           };
       } else {
           releaseBuffer(buf);
           close();
           return null;
       }

   }

   public void close() {
       try {
           chan.close();
       } catch (IOException ex) {
           ex.printStackTrace();
       }
   }

   public boolean isClosed() {
       return !chan.isOpen();
   }

   public void continueWrite() {
       while (!writeQueue.isEmpty()) {
           try {
               ByteBuffer top = writeQueue.peek();
               chan.write(top);
               if (top.hasRemaining()) {
                   return;
               } else {
                   writeQueue.remove();
               }
           } catch (IOException ex) {
               ex.printStackTrace();
               close();
           }
       }

       if (writeQueue.isEmpty()) {
           if (protocol.shouldTerminate()) close();
           else reactor.updateInterestedOps(chan, SelectionKey.OP_READ);
       }
   }

   private static ByteBuffer leaseBuffer() {
       ByteBuffer buff = BUFFER_POOL.poll();
       if (buff == null) {
           return ByteBuffer.allocateDirect(BUFFER_ALLOCATION_SIZE);
       }

       buff.clear();
       return buff;
   }

   private static void releaseBuffer(ByteBuffer buff) {
       BUFFER_POOL.add(buff);
   }

    @Override
public void send(Communication msg) {

            writeQueue.add(ByteBuffer.wrap(encdec.encode(msg)));
            reactor.updateInterestedOps(chan, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        
    }

}
