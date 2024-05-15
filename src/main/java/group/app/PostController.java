package group.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PostController {
    @FXML
    private TextField commentInput;
    @FXML
    private Label commentLabel;
    @FXML
    private VBox commentsContainer;
    @FXML
    private Label content;
    @FXML
    private Label date;
    @FXML
    private Label nbComments;
    @FXML
    private Label nbLikes;
    @FXML
    private Label title;
    @FXML
    private Label username;
    @FXML
    private Label likeLabel;
    Post currentPost;
    User author;
    private homeController hController;
    private ProfileController pController;
    public void setData(Post post, homeController controller) {
        hController = controller;
        for (User user : post.getLikes()){
            if (user.getUsername().equals(Info.getCurrentUser().getUsername())) {
                likeLabel.setText("Liked");
                likeLabel.setTextFill(Color.color(0,0,1));
                break;
            }
        }
        currentPost = post;
        author = post.getAuthor();
        username.setText(post.getAuthor().getUsername());
        title.setText(post.getTitle());
        content.setText(post.getContent());
        date.setText(post.getTimestamp().toString());
        nbComments.setText(String.valueOf(post.getComments().size()) + " comments");
        nbLikes.setText(String.valueOf(post.getLikes().size()) + " likes");
    }
    public void setData(Post post, ProfileController controller) throws IOException {
        pController = controller;
        for (User user : post.getLikes()){
            System.out.println(user.getUsername() + " - " + Info.getCurrentUser().getUsername());
            if (user.getUsername().equals(Info.getCurrentUser().getUsername())) {
                likeLabel.setText("Liked");
                likeLabel.setTextFill(Color.color(0,0,1));
                break;
            }
        }

        author = post.getAuthor();
        username.setText(post.getAuthor().getUsername());
        title.setText(post.getTitle());
        content.setText(post.getContent());
        date.setText(post.getTimestamp().toString());
        nbComments.setText(String.valueOf(post.getComments().size()) + " comments");
        nbLikes.setText(String.valueOf(post.getLikes().size()) + " likes");
    }

    public void onLikeClicked(MouseEvent mouseEvent) throws IOException {
        if (likeLabel.getText().equals("Like")){
            likeLabel.setText("Liked");
            likeLabel.setTextFill(Color.color(0,0,1));
            if (hController != null) hController.likePost(Info.getCurrentUser(), currentPost, mouseEvent);
            if (pController != null) pController.likePost(Info.getCurrentUser(), currentPost, mouseEvent);
        }
        else if (likeLabel.getText().equals("Liked")){
            likeLabel.setText("Like");
            likeLabel.setTextFill(Color.color(0,0,0));
            if (hController != null) hController.dislikePost(Info.getCurrentUser(), currentPost, mouseEvent);
            if (pController != null) pController.dislikePost(author, currentPost, mouseEvent);
        }
    }

    public void onCommentClicked(MouseEvent mouseEvent) throws IOException {
        if (commentInput.getText().isEmpty()) commentInput.setPromptText("You can't leave an empty comment");
        else {
            hController.commentOnPost(commentInput.getText(), currentPost, mouseEvent);
        }
    }

    public void onProfileClicked(MouseEvent mouseEvent) throws IOException {
        Info.setProfileUser(author);
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
