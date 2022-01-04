package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.rci.Message;

public class NotificationMessage implements Message {
    private int notificationType;//0 for PM and 1 for Post
    private String postingUser;
    private String content;
    private int opCode;

    public NotificationMessage(int notificationType, String postingUser, String content) {
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
        this.opCode = 9;
    }

}
