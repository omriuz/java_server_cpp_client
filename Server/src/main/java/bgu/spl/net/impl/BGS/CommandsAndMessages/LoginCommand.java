package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

import java.util.concurrent.ConcurrentLinkedQueue;

public class LoginCommand implements Command {
    private String userName;
    private String password;
    private boolean captcha;
    private int opCode;

    public LoginCommand(String username, String password, boolean captcha) {
        this.userName = username;
        this.password =password;
        this.captcha = captcha;
        
    }

    public void execute(DataBase dataBase, Connections_Impl connections, int id){
        this.opCode = 2;
        boolean login = false;
        if(captcha) {
            login = dataBase.logIn(userName, password, id);
            if (login) {
                connections.send(id, new AckMessage(opCode));//TODO build the AckMessage
                ConcurrentLinkedQueue<NotificationMessage> messageQueue = dataBase.getmessageQueue(userName);
                for (NotificationMessage m : messageQueue) {
                    connections.send(id, m);
                }
            }
        }
        if(!captcha | !login)
            connections.send(id, new ErrorMessage(opCode));//TODO build the Error message
    }

    public String toString(){
        return "userName: " + userName + "\npassword: " + password + "\ncatpcha: " + String.valueOf(captcha);
    }

}
