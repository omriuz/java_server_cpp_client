package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.*;
import java.util.List;

public class LogStatCommand implements Command {

    private int opCode;

    public LogStatCommand() {

    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        this.opCode = 7;
        boolean valid = dataBase.isValidId(connectionId);
        if(!valid)
            connections.send(connectionId,new ErrorMessage(opCode));
        else
        {
            List<BgsUser> loggedInUsers = dataBase.getLoggedInUsers();
            BgsUser currentUser = dataBase.getUser(connectionId);
            for(BgsUser user :  loggedInUsers){
                if(!currentUser.isBlockingMe(user)){
                String optionalInformation = user.getAge() +  " " + user.getNumOfPosts() + " " + user.numOfFollowers() + " "  + user.numOfFollowing();
                AckMessage toSend = new AckMessage(opCode,optionalInformation);
                connections.send(connectionId,toSend);
                }
            }
        }

    }
}
