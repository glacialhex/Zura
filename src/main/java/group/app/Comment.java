package group.app;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private User author;
    private String content;
    private Date timestamp;
    public Comment(User author, String content) {
        this.author = author;
        this.content = content;
        this.timestamp = new Date();
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
