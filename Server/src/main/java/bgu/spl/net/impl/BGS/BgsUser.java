package bgu.spl.net.impl.BGS;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BgsUser {
    private String userName;
    private String password;
    private String birthday;
    private List<String> followersNames;
    private List<String > followings;
    private List<String> blocked;
    private boolean logIn;
    private int currentConnectionsId;
    private int age;
    private int numOfPosts;
    private List<String> postsSent;
    private HashMap<String , List<String>> pmsSent;

    public BgsUser(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
        this.followersNames = new LinkedList<>();
        this.followings = new LinkedList<>();
        this.blocked = new LinkedList<>();
        int onlyBirthDay = Integer.parseInt(this.birthday.substring(0,2));
        int birthMonth = Integer.parseInt(this.birthday.substring(3,5));
        int birthYear= Integer.parseInt(this.birthday.substring(6,10)); 
        this.age = ((8-onlyBirthDay) + (1-birthMonth)*30 + (2022-birthYear)*365)/365;
        this.numOfPosts = 0;
        this.postsSent = new LinkedList<>();
        this.pmsSent = new HashMap<>();
    }

    public boolean follow(BgsUser user){
        boolean success = false;
        if(!followings.contains(user.getUserName())){
            followings.add(user.getUserName());
            user.addToFollowers(getUserName());
            success = true;
        }
        return success;
    }
    public boolean unFollow(BgsUser user){
        boolean success = false;
        if(followings.contains(user.getUserName())){
            followings.remove(user.getUserName());
            user.removeFromFollowers(getUserName());
            success = true;
        }
        return success;
    }

    public boolean isFollow(String userName){
        return followings.contains(userName);
    }

    public boolean isLogIn(){
        return logIn;
    }

    public void logIn(int connectionId){
        this.currentConnectionsId = connectionId;
        logIn = true;

    }
    public void logOut(){
        currentConnectionsId = 0;
        logIn = false;
    }



    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public List<String> getFollowersNames() {
        return followersNames;
    }

    public void addToFollowers(String userName){
        followersNames.add(userName);
    }
    public void removeFromFollowers(String userName){
        followersNames.remove(userName);
    }

    public List<String> getFollowings() {
        return followings;
    }

    public List<String> getBlocked() {
        return blocked;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getCurrentConnectionsId() {
        return currentConnectionsId;
    }

    public void addPost(String postContent){
        postsSent.add(postContent);
        numOfPosts++;
    }
    public void addPM(String receiveUserName, String PMContent){
        pmsSent.putIfAbsent(receiveUserName, new LinkedList<>());
        pmsSent.get(receiveUserName).add(PMContent);
    }

    public int numOfFollowers(){
        return followersNames.size();
    }

    public int numOfFollowing(){
        return followings.size();
    }

    public int getAge() {
        return age;
    }

    public int getNumOfPosts(){
        return numOfPosts;
    }

    public boolean isBlocked(String userName){
        return blocked.contains(userName);
    }
    public boolean isBlockingMe(BgsUser user){
        return user.isBlocked(getUserName());
    }
    public void block(String userName){
        blocked.add(userName);
    }
}
