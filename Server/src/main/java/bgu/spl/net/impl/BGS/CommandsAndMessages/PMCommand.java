package bgu.spl.net.impl.BGS.CommandsAndMessages;

import bgu.spl.net.impl.BGS.*;

import java.util.LinkedList;

public class PMCommand implements Command {
    private String receiveUserName;
    private String content;
    private int opCode;

    public PMCommand(String receiveUserName, String content){
        this.receiveUserName = receiveUserName;
        this.content = content;
        
    }

    @Override
    public void execute(DataBase dataBase, Connections_Impl connections, int id) {
        this.opCode = 6;
        boolean send = false;
        BgsUser sendingUser = dataBase.getUser(id);
        BgsUser receiveUser = dataBase.getUser(receiveUserName);
        filterMessage(dataBase);
        if (sendingUser.isLogIn()) {
            if (sendingUser.isFollow(receiveUserName)) {
                if (receiveUser.isLogIn()){
                    connections.send(receiveUser.getCurrentConnectionsId(), new NotificationMessage(0, sendingUser.getUserName(), content));
                    connections.send(id, new AckMessage(opCode));
                }
                else
                    dataBase.savePost(content, sendingUser.getUserName(), receiveUserName);
                sendingUser.addPM(receiveUserName,content);
                send = true;
            }
        }
        if(!send)
            connections.send(id, new ErrorMessage(opCode));
    }

    private void filterMessage(DataBase dataBase){
        LinkedList<String> worldToFilter = (LinkedList)dataBase.getWorldToFilter();
        for(String world : worldToFilter){
            content.replaceAll(world,"filtered");
        }
    }



}
