package main.java.bgu.spl.net.impl.BGS;

import main.java.bgu.spl.net.impl.BGS.Commands.NotificationMessage;
import main.java.bgu.spl.net.impl.BGS.Commands.RegisterCommand;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase {

    private ConcurrentHashMap<String, BgsUser> users;
    private ConcurrentHashMap<Integer, BgsUser> usersByConnectionId;
    private ConcurrentHashMap<BgsUser, ConcurrentLinkedQueue<NotificationMessage>> messagesForUsers;

    public DataBase() {
        this.users = new ConcurrentHashMap<>();
        this.usersByConnectionId = new ConcurrentHashMap<>();
        this.messagesForUsers = new ConcurrentHashMap<>();
    }
    public boolean register(RegisterCommand registerCommand,int connectionId){
        if(users.containsKey(registerCommand.getUserName()))
            return false;
        BgsUser newUser = new BgsUser(registerCommand.getUserName(),registerCommand.getPassword(),registerCommand.getBirthday());
        users.put(registerCommand.getUserName(),newUser);
        messagesForUsers.put(newUser,new ConcurrentLinkedQueue<>());
        return true;
    }

    public boolean logIn(String userName, String pasword, int connectionId){
        boolean logIn = false;
        BgsUser user = users.get(userName);
        usersByConnectionId.put(connectionId,user);
        if(user != null){
            if(user.getPassword().equals(pasword) & !user.isLogIn()){
                logIn = true;
                user.logIn(connectionId);
            }
        }
        return logIn;
    }

    public boolean logOut(int connectionID){
        BgsUser user = usersByConnectionId.get(connectionID);
        if(user == null || !user.isLogIn()) return false;
        user.logOut();
        usersByConnectionId.remove(connectionID);
        return true;
    }

    public boolean follow(int connectionId, String userName){
        BgsUser askUser = usersByConnectionId.get(connectionId);
        boolean success = false;
        if(askUser != null)
            success = askUser.follow(userName);
        return success;
    }

    public boolean unFollow(int connectionId, String userName){
        BgsUser askUser = usersByConnectionId.get(connectionId);
        boolean success = false;
        if(askUser != null)
            success = askUser.unFollow(userName);
        return success;
    }
    public ConcurrentLinkedQueue<NotificationMessage> getmessageQueue(String userName){
        return messagesForUsers.get(users.get(userName));
    }

    public boolean valid(int connectionId){
        BgsUser user = usersByConnectionId.get(connectionId);
        if(user == null || !user.isLogIn()) return false;
        return true;
    }
    public BgsUser getUser(String userName){
        return users.get(userName);
    }
    public BgsUser getUser(int connectionId){
        return usersByConnectionId.get(connectionId);
    }

    public void savePost(String content, String sentFrom, String sentTo) {
        BgsUser saveForUser = users.get(sentTo);
        messagesForUsers.get(saveForUser).add(new NotificationMessage(1,sentFrom,content));
    }
    public boolean isLogin(String userName){
        BgsUser user = users.get(userName);
        boolean isLogin = false;
        if(user != null) {
            isLogin = user.isLogIn();
        }
        return isLogin;
    }
}
