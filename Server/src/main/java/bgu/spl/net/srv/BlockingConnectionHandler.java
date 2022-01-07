package bgu.spl.net.srv;

import bgu.spl.net.impl.BGS.ObjectEncoderDecoder;
import bgu.spl.net.impl.BGS.RemoteCommandInvocationProtocol;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.impl.rci.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler <Communication> implements Runnable, ConnectionHandler<Communication> {

    private final RemoteCommandInvocationProtocol protocol;
    private final ObjectEncoderDecoder encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;

    public BlockingConnectionHandler(Socket sock, ObjectEncoderDecoder reader, RemoteCommandInvocationProtocol protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;

    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            in = new BufferedInputStream(sock.getInputStream());
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                Command nextMessage = (Command) encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    public synchronized void send(Communication msg) {
        // System.out.println("reached the blocking connection handel send method");
        try {
            Socket sock = this.sock;
            out = new BufferedOutputStream(sock.getOutputStream());
            out.write(encdec.encode((Message)msg));
            out.flush();
            // System.out.println("flushed the string out of the socket");
        }
        catch (IOException ex) {
        ex.printStackTrace();
    }

    }
}
