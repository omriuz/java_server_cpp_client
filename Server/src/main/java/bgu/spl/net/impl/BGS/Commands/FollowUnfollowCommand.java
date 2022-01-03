package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

public class FollowUnfollowCommand implements Command {
    private String userName;
    private boolean followUnfollow;

    public void FollowUnfollowCommand(String userName, boolean followUnfollow){
        this.userName = userName;
        this.followUnfollow = followUnfollow;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) {
        boolean success = false;
        Command backMessage = null;
        if(followUnfollow)
            success = dataBase.follow(id, userName);
        else
            success = dataBase.unFollow(id, userName);
        if(success)
            backMessage = new AckMessage(); //TODO message (ACK-Opcode FOLLOW-Opcode <username>)
        else
            backMessage = new ErrorMessage();//TODO message

        connections.send(id,backMessage);

    }
}
