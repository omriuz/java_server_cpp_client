package bgu.spl.net.impl.BGS;

import bgu.spl.net.impl.BGS.CommandsAndMessages.NotificationMessage;
import bgu.spl.net.impl.BGS.CommandsAndMessages.RegisterCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase {

    private ConcurrentHashMap<String, BgsUser> users;
    private ConcurrentHashMap<Integer, BgsUser> usersByConnectionId;
    private ConcurrentHashMap<BgsUser, ConcurrentLinkedQueue<NotificationMessage>> messagesForUsers;
    private List<String> worldToFilter;

    public DataBase() {
        this.users = new ConcurrentHashMap<>();
        this.usersByConnectionId = new ConcurrentHashMap<>();
        this.messagesForUsers = new ConcurrentHashMap<>();
        this.worldToFilter = new LinkedList<>();
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
        if(user != null){
            usersByConnectionId.put(connectionId,user);
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
        if(askUser != null){
            success = askUser.follow(userName);
            users.get(userName).addToFollowers(askUser.getUserName());
        }
        return success;
    }

    public boolean unFollow(int connectionId, String userName){
        BgsUser askUser = usersByConnectionId.get(connectionId);
        boolean success = false;
        if(askUser != null)
            success = askUser.unFollow(userName);
            users.get(userName).removeFromFollowers(askUser.getUserName());
        return success;
    }
    public ConcurrentLinkedQueue<NotificationMessage> getmessageQueue(String userName){
        return messagesForUsers.get(users.get(userName));
    }

    public boolean isValidId(int connectionId){
        BgsUser user = usersByConnectionId.get(connectionId);
        if(user == null || !user.isLogIn()) return false;
        return true;
    }
    public boolean isValidUesers(List<String> usersName){
        boolean isValid = true;
        for(String name : usersName){
            if(isValid && !isRegistered(name))
                isValid = false;
        }
        return isValid;
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
    public boolean isRegistered(String userName){
        return users.containsKey(userName);
    }

    public List<BgsUser> getLoggedInUsers(){
        List<BgsUser> loggedInUsers = new LinkedList<>();
        for(BgsUser user : users.values())
            if(user.isLogIn())
                loggedInUsers.add(user);
        return loggedInUsers;
    }

    public List<String> getWorldToFilter(){
        return worldToFilter;
    }

    public void block(int blockingUserId, String blockedUserName){
        //1: add to the users blocked list
        BgsUser currentUser = usersByConnectionId.get(blockingUserId);
        if(!currentUser.isBlocked(blockedUserName))
            currentUser.block(blockedUserName);

        //2: unfollow each other
        currentUser.unFollow(blockedUserName);
        users.get(blockedUserName).unFollow(currentUser.getUserName());
    }


}
