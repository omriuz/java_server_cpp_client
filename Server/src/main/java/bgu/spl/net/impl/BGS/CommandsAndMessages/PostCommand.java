package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.BgsUser;
import bgu.spl.net.impl.BGS.Connections_Impl;
import bgu.spl.net.impl.BGS.DataBase;
import bgu.spl.net.impl.rci.Command;

import java.util.List;

public class PostCommand implements Command {
    private final String content;
    private final List<String> taggedUsersNames;
    private final int opCode;

    public PostCommand(String content) {
        this.content = content;
        this.taggedUsersNames = getTaggedUsersNames(content);
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        this.opCode = 5;
        boolean valid = dataBase.isValid(connectionId);
        connections.send(connectionId,valid ? new AckMessage(opCode) : new ErrorMessage(opCode));
        if(valid){
            BgsUser currentUser = dataBase.getUser(connectionId);
            currentUser.addPost(content);
            sendPost(currentUser.getFollowersNames(),dataBase,currentUser,connections);
            sendPost(taggedUsersNames,dataBase,currentUser,connections);
        }
    }

    public List<String> getTaggedUsersNames(String content){
        //TODO: parse the content string to get the tagged users names
        return null;
    }
    public void  sendPost(List<String> usersNames, DataBase dataBase, BgsUser currentUser,Connections_Impl connections){
        for(String userName : usersNames){
            BgsUser user = dataBase.getUser(userName);
            if(!currentUser.isBlockingMe(dataBase.getUser(userName))) {
                if (user.isLogIn())
                    connections.send(user.getCurrentConnectionsId(), new NotificationMessage(1,currentUser.getUserName(), content));
                else
                    dataBase.savePost(content,currentUser.getUserName(), userName);
            }
        }
    }
}

