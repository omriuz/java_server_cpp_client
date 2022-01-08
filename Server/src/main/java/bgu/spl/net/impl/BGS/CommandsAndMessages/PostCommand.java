package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.*;

import java.util.LinkedList;
import java.util.List;

public class PostCommand implements Command {
    private final String content;
    private List<String> taggedUsersNames;
    private int opCode;

    public PostCommand(String content) {
        this.content = content;
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int connectionId) {
        this.opCode = 5;
        this.taggedUsersNames = getTaggedUsersNames(content);
        boolean valid = (dataBase.isValidId(connectionId) & dataBase.isValidUesers(taggedUsersNames));
        connections.send(connectionId,valid ? new AckMessage(opCode) : new ErrorMessage(opCode));
        if(valid){
            BgsUser currentUser = dataBase.getUser(connectionId);
            currentUser.addPost(content);
            sendPost(currentUser.getFollowersNames(),dataBase,currentUser,connections);
            sendPost(taggedUsersNames,dataBase,currentUser,connections);
        }
    }

    public List<String> getTaggedUsersNames(String content){
        int startIndex = 0;
        int endIndex = 0;
        List<String> tagedNames = new LinkedList<>();
        startIndex = content.indexOf("@", endIndex); 
        while(startIndex != -1){
            endIndex = content.indexOf(" ", startIndex);
            endIndex = (endIndex == -1) ? content.length() : endIndex; // chek if the @name is the last word
            tagedNames.add(content.substring(startIndex+1, endIndex));
            startIndex = content.indexOf("@", endIndex);
        }


        return tagedNames;
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

