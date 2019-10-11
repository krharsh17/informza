package in.kumarharsh.informza.models;

import java.util.ArrayList;

public class User {
    ArrayList<String> myPosts;
    String userName;
    String id;

    public User(){

    }

    public User(ArrayList<String> myPosts, String userName, String id) {
        this.myPosts = myPosts;
        this.userName = userName;
        this.id = id;
    }

    public ArrayList<String> getMyPosts() {
        return myPosts;
    }

    public void setMyPosts(ArrayList<String> myPosts) {
        this.myPosts = myPosts;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
