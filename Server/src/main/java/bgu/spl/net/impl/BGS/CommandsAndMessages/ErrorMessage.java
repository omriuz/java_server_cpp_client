package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.rci.Message;

public class ErrorMessage implements Message {
    private int opCode;
    private int messageOpCode;

    public ErrorMessage(int messageOpCode) {
        this.opCode = 11;
        this.messageOpCode = messageOpCode;
    }
    
    public String toString(){
        return String.valueOf(opCode) + " " + String.valueOf(messageOpCode);
    }
}
