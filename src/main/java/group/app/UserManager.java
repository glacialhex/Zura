package group.app;

import java.util.HashMap;

public class UserManager {
    private HashMap<String, User> users;
    private HashMap<String, Integer> passwords;
    private User loggedUser;
    public UserManager() {
        this.users = new HashMap<String, User>();
        this.passwords = new HashMap<String, Integer>();
    }

    public boolean registerUser(String username, String password) {
        if (this.users.containsKey(username)) return false;
        this.users.put(username, new User(username, false));
        this.passwords.put(username, password.hashCode());
        return true;
    }

    public void deleteUser(String username){
        this.users.remove(username);
        this.passwords.remove(username);
    }

    public User login(String username, String password){
        if(this.passwords.containsKey(username) && this.passwords.get(username).equals(password.hashCode())) loggedUser = this.users.get(username);
        else return null;
        return loggedUser;
    }

    public void logout(){
        loggedUser = null;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public User getUser(String username){
        return users.get(username);
    }
    public Integer getPassword(String username){
        return passwords.get(username);
    }
}
