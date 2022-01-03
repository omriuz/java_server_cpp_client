package main.java.bgu.spl.net.impl.BGS.Commands;

import main.java.bgu.spl.net.impl.BGS.BgsUser;
import main.java.bgu.spl.net.impl.BGS.Connections_Impl;
import main.java.bgu.spl.net.impl.BGS.DataBase;
import main.java.bgu.spl.net.impl.rci.Command;

import java.util.List;

public class PostCommand implements Command {
    private final String content;
    private final List<String> taggedUsersNames;

    public PostCommand(String content,List<String> taggedUsersNames) {
        this.content = content;
        this.taggedUsersNames = taggedUsersNames;

    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        boolean valid = dataBase.valid(connectionId);
        connections.send(connectionId,valid ? new AckMessage() : new ErrorMessage());
        if(valid){
            List<String> followers = dataBase.getUser(connectionId).getFollowersNames();
            List<String> taggedUsers = getTaggedUsersNames(content);
            String postingUserName = dataBase.getUser(connectionId).getUserName();
            for(String userName : followers){
                BgsUser user = dataBase.getUser(userName);
                if(user.isLogIn())
                    connections.send(user.getCurrentConnectionsId(),new NotificationMessage(1,postingUserName,content));
                else
                    dataBase.savePost(content,postingUserName,userName);
            }
            for(String userName : taggedUsers){
                BgsUser user = dataBase.getUser(userName);
                if(user.isLogIn())
                    connections.send(user.getCurrentConnectionsId(),new NotificationMessage(1,postingUserName,content));
                else
                    dataBase.savePost(content,postingUserName,userName);
            }
        }
    }

    public List<String> getTaggedUsersNames(String content){
        //TODO: parse the content string to get the tagged users names
        return null;
    }
}

