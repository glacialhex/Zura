package group.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String bio;
    private String profilePic;
    private List<User> friends;
    private List<User> requests;
    private boolean login;
    public User(String username, boolean login){
        this.username = username;
        this.friends = new ArrayList<User>();
        this.requests = new ArrayList<User>();
        this.login = login;
        this.bio = "No Bio Written";
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean addRequest(User user){
        if (!this.requests.contains(user) && !this.friends.contains(user)){
            this.requests.add(user);
            return true;
        }
        return false;
    }

    public List<User> getRequests() {
        return requests;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void acceptRequest(User user){
        this.requests.remove(user);
        this.friends.add(user);
        user.addFriend(this);
    }

    public List<User> declineRequest(User user){
        if (this.requests.remove(user)) return this.requests;
        return null;
    }

    public List<User> getFriends() {
        return friends;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

//    @Override
//    public String toString() {
//        return username + " - " + bio;
//    }
}
