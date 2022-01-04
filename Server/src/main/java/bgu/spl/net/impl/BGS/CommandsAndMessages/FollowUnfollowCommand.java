package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.impl.rci.Message;

public class FollowUnfollowCommand implements Command {
    private String userName;
    private boolean followUnfollow;
    private int opCode;

    public void FollowUnfollowCommand(String userName, boolean followUnfollow){
        this.userName = userName;
        this.followUnfollow = followUnfollow;
        this.opCode = 4;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) {
        boolean success = false;
        Message backMessage = null;
        if(followUnfollow & !dataBase.getUser(id).isBlockingMe(dataBase.getUser(userName))) // check if blocked
            success = dataBase.follow(id, userName);
        else
            success = dataBase.unFollow(id, userName);
        if(success)
            backMessage = new AckMessage(opCode);
        else
            backMessage = new ErrorMessage(opCode);

        connections.send(id,backMessage);

    }
}
