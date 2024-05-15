package group.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SocialPlatform {
    private UserManager userManager;
    private ArrayList<Post> posts;
    private int postID;
    public SocialPlatform() {
        userManager = new UserManager();
        posts = new ArrayList<Post>();
    }

    public boolean registerUser(String username, String password) {
       return userManager.registerUser(username, password);
    }
    public User login(String username, String password) {
        return userManager.login(username, password);
    }
    public Post createPost(String title, String content, User author){
        Post createdPost = new Post(postID++,title, content, author);
        posts.add(createdPost);
        System.out.println("POST ID: " + createdPost.getID());
        return createdPost;
    }
    public void likePost(User user, String ID)
    {
        for (Post post : posts){
            if (Integer.toString(post.getID()).equals(ID)) {
                post.addLike(user); break;
            }
        }
    }
    public void dislikePost(User user, String ID){
        for (Post post : posts){
            if (Integer.toString(post.getID()).equals(ID)) {
                post.removeLike(user); break;
            }
        }
    }
    public void commentOnPost(Comment comment, String ID){

        for (Post post : posts){
            if (Integer.toString(post.getID()).equals(ID)) {
                post.addComment(comment); break;
            }
        }
    }
    public User getUser(String username){
        return userManager.getUser(username);
    }
    public User getLoggedUser(){
        return userManager.getLoggedUser();
    }
    public void logout(){
        userManager.logout();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public boolean isUser(String username){
        return userManager.getUsers().containsKey(username);
    }

    public ArrayList<Post> getPostsByUser(User user) {
        ArrayList<Post> arr = new ArrayList<Post>();
        for (Post post : posts){
            if(post.getAuthor().equals(user)) arr.add(post);
        }
//        Collections.reverse(arr);
        return arr;
    }
    public ArrayList<Post> getPostsByUsers(ArrayList<User> users) {
        ArrayList<Post> arr = new ArrayList<Post>();
        for (Post post : posts){
            if(users.contains(post.getAuthor())) arr.add(post);
        }
//        Collections.reverse(arr);
        return arr;
    }
    public HashMap<String, User> getUsers() {
        return userManager.getUsers();
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
