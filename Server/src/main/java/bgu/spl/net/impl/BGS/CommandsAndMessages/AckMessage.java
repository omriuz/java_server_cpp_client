package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.rci.Message;

public class AckMessage implements Message {

    private int opCode;
    private int sentFor;//the code of the command the ackMessage was sent for
    private String optionalInformation;

    public AckMessage(int sentFor, String optionalInformation) {
        this.opCode = 10;
        this.sentFor = sentFor;
        this.optionalInformation = optionalInformation;
    }
    public AckMessage(int sentFor){
        this.opCode = 10;
        this.sentFor = sentFor;
        this.optionalInformation = "";
    }



}
