package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.BgsUser;
import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

import java.util.LinkedList;

public class StatsCommand implements Command {
    private int opCode;
    private LinkedList<String> userNames;

    public StatsCommand(LinkedList<String> userNames){
        this.userNames = userNames;
    }
    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) {
        this.opCode = 8;
        boolean stat = false;
        BgsUser activeUser = dataBase.getUser(id);
        if(activeUser != null && activeUser.isLogIn()){
            for(String userName : userNames){
                if(dataBase.isRegistered(userName)) {
                    BgsUser user = dataBase.getUser(userName);
                    String message = user.getAge() + " " + user.getNumOfPosts() + " " + user.numOfFollowers() + " " + user.numOfFollowing();
                    connections.send(id, new AckMessage(opCode,message));
                    stat = true;
                }
            }
        }
        if (!stat)
            connections.send(id, new ErrorMessage(opCode));
    }
}
