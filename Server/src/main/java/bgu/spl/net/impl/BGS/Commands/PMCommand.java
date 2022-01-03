package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

public class PMCommand implements Command {
    private String receiveUserName;
    private String content;
    private String sendingTime;

    public void PMCommand(String receiveUserName, String content, String sendingTime){
        this.receiveUserName = receiveUserName;
        this.content = content;
        this.sendingTime = sendingTime;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) {
        if(dataBase.isLogin(/*user that want to sand the message*/)){

        }
    }
}
