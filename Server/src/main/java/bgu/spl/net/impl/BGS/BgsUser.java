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

    //TODO: check if needed
//    private Integer connectionID;
//    private int NumPost;
//   private boolean loggedin;
//    private AtomicBoolean loggedin;

    public BgsUser(String userName, String password, String birthday) {
        this.userName = userName;
        this.password = password;
        this.birthday = birthday;
        this.followersNames = new LinkedList<>();
        this.followings = new LinkedList<>();
        this.blocked = new LinkedList<>();
        this.logIn = false;
        //TODO: compute the age of the user
        this.age = 0;
        this.numOfPosts = 0;
        this.postsSent = new LinkedList<>();
        this.pmsSent = new HashMap<>();
    }

    public boolean follow(String userName){
        boolean success = false;
        if(!followings.contains(userName)){
            followings.add(userName);
            success = true;
        }
        return success;
    }
    public boolean unFollow(String userName){
        boolean success = false;
        if(followings.contains(userName)){
            followings.remove(userName);
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
