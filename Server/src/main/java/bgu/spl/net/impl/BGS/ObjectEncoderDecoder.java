package bgu.spl.net.impl.BGS;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.BGS.CommandsAndMessages.*;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.impl.rci.Communication;
import bgu.spl.net.impl.rci.Message;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.sound.sampled.SourceDataLine;

public class ObjectEncoderDecoder implements MessageEncoderDecoder<Communication> {

    private final ByteBuffer lengthBuffer = ByteBuffer.allocate(2);
    private final ByteBuffer opCodeBuffer = ByteBuffer.allocate(2);
    private short opCode;
    private byte[] objectBytes = null;
    private int objectBytesIndex = 0;

    @Override
    public Communication decodeNextByte(byte nextByte) {
        if (objectBytes == null) { //indicates that we are still reading the length and opCode
            if(lengthBuffer.hasRemaining())
                lengthBuffer.put(nextByte);
            else if (opCodeBuffer.hasRemaining())
                opCodeBuffer.put(nextByte);
            if(!lengthBuffer.hasRemaining() && !opCodeBuffer.hasRemaining()) { //we read 4 bytes and therefore can take the length and op code
                lengthBuffer.flip();
                objectBytes = new byte[lengthBuffer.getShort()];
                objectBytesIndex = 0;
                lengthBuffer.clear();
                opCodeBuffer.flip();
                opCode = opCodeBuffer.getShort();
                opCodeBuffer.clear();
            }
        }else {
            objectBytes[objectBytesIndex] = nextByte;
            if (++objectBytesIndex == objectBytes.length) {
                Command result = deserializeObject(opCode);
                objectBytes = null;
                return result;
            }
        }
        return null;
    }

    @Override
    public byte[] encode(Communication message) {
        //TODO: decide between sending a String or sending a json formatted String
        String toReturn = ((Message)message).toString() +"\0";
        // System.out.println("reached the encoder encode method " + toReturn);
        return toReturn.getBytes(StandardCharsets.UTF_8);//TODO
    }

    private Command deserializeObject(short opCode) {
        String json = new String(objectBytes, StandardCharsets.UTF_8);
        // System.out.println(json); 
        Gson gson = new Gson();
        Command command = null;
        switch (opCode){
            case 1:
                command = gson.fromJson(json, RegisterCommand.class);
                // System.out.println(((RegisterCommand)command).toString());
                break;
            case 2:
                command = gson.fromJson(json, LoginCommand.class);
                // System.out.println(((LoginCommand)command).toString());
                break;
            case 3:
                command = gson.fromJson(json, LogoutCommand.class);
                break;
            case 4:
                command = gson.fromJson(json, FollowUnfollowCommand.class);
                break;
            case 5:
                command = gson.fromJson(json, PostCommand.class);
                break;
            case 6:
                command = gson.fromJson(json, PMCommand.class);
                break;
            case 7:
                command = gson.fromJson(json, LogStatCommand.class);
                break;
            case 8:
                command = gson.fromJson(json, StatsCommand.class);
                break;
            case 12:
                command = gson.fromJson(json, BlockCommand.class);
                break;

        }
        return command;
    }

    private byte[] serializeObject(Serializable message) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            //placeholder for the object size
            for (int i = 0; i < 4; i++) {
                bytes.write(0);
            }

            ObjectOutput out = new ObjectOutputStream(bytes);
            out.writeObject(message);
            out.flush();
            byte[] result = bytes.toByteArray();

            //now write the object size
            ByteBuffer.wrap(result).putInt(result.length - 4);
            return result;

        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot serialize object", ex);
        }
    }

}
