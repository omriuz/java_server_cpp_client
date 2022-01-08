package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.*;

public class FollowUnfollowCommand implements Command {
    private String userName;
    private boolean followUnfollow;
    private int opCode;

    public FollowUnfollowCommand(String userName, boolean followUnfollow){
        this.userName = userName;
        this.followUnfollow = followUnfollow;

    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) { //TODO crash when doing follow to someone not register
        this.opCode = 4;
        boolean success = false;
        Message backMessage = null;
        if(dataBase.isRegistered(userName)){
            if(followUnfollow & !dataBase.getUser(id).isBlockingMe(dataBase.getUser(userName))) // check if blocked
                success = dataBase.follow(id, userName);
            else
                success = dataBase.unFollow(id, userName);
        }
        if(success)
            backMessage = new AckMessage(opCode);
        else
            backMessage = new ErrorMessage(opCode);

        connections.send(id,backMessage);

    }
}
