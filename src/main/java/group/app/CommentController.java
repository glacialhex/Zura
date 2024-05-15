package group.app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CommentController {
    @FXML
    private Label commentContent;

    @FXML
    private Label commentDate;

    @FXML
    private Label commentUsername;

    public void onProfileClicked(MouseEvent mouseEvent) {

    }

    public void setData(Comment comment) {
        commentContent.setText(comment.getContent());
        commentUsername.setText(comment.getAuthor().getUsername());
        commentDate.setText(comment.getTimestamp().toString());
    }
}
