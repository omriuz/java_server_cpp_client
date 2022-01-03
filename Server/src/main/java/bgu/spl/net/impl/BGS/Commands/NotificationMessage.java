package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

public class NotificationMessage implements Command {
    private int notificationType;//0 for PM and 1 for Post
    private String postingUser;
    private String content;

    public NotificationMessage(int notificationType, String postingUser, String content) {
        this.notificationType = notificationType;
        this.postingUser = postingUser;
        this.content = content;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) {

    }
}
