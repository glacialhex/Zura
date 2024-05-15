package group.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class FriendController {
    @FXML
    private Label profileUsername;
    private FriendsController parentController;
    private User user;
    public void logout(MouseEvent mouseEvent) {
    }

    public void setData(User user, FriendsController controller) {
        profileUsername.setText(user.getUsername());
        this.user = user;
        parentController = controller;
    }

    public void viewProfile(MouseEvent mouseEvent) throws IOException {
        Info.setProfileUser(this.user);
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

