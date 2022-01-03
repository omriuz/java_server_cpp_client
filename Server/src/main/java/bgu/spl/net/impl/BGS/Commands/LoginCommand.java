package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.ErrorManager;

public class LoginCommand implements Command {
    private DataBase dataBase;
    private Connections_Impl connections;
    private int id;
    private String username;
    private String password;
    private boolean captcha;



    public LoginCommand(String username, String password, boolean captcha) {
        this.username = username;
        this.password =password;
        this.captcha = captcha;
    }

    public void execute(DataBase dataBase, Connections_Impl connections, int id){
        boolean login = false;
        if(captcha) {
            login = dataBase.logIn(username, password, id);
            if (login) {
                connections.send(id, new AckMessage());//TODO build the AckMessage
                ConcurrentLinkedQueue<NotificationMessage> messageQueue = dataBase.getmessageQueue(username);
                for (NotificationMessage m : messageQueue) {
                    connections.send(id, m);
                }
            }
        }
        if(!captcha | !login)
            connections.send(id, new ErrorMessage());//TODO build the Error message
    }

}
