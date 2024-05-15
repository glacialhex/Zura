package group.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post implements Serializable {
    private int ID;
    private String title;
    private String content;
    private User author;
    private Date timestamp;
    private List<User> likes;
    private List<Comment> comments;

    public Post(int ID,String title, String content, User author) {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = new Date();
        this.likes = new ArrayList<User>();
        this.comments = new ArrayList<Comment>();
    }

    public int getID() {
        return ID;
    }

    public void addLike(User user) {
        if (!this.likes.contains(user)) this.likes.add(user);
    }

    public void removeLike(User user) {
        this.likes.remove(user);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public List<User> getLikes() {
        return likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
