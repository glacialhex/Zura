package group.app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RequestController {
    @FXML
    private Label profileUsername1;
    private FriendsController parentController;
    private User user;
    public void logout(MouseEvent mouseEvent) {
    }

    public void rejectRequest(MouseEvent mouseEvent) throws IOException {
        parentController.rejectRequest(this.user, mouseEvent);
    }

    public void acceptRequest(MouseEvent mouseEvent) throws IOException {
        parentController.acceptRequest(this.user, mouseEvent);
    }

    public void setData(User user, FriendsController controller) {
        profileUsername1.setText(user.getUsername());
        this.user = user;
        parentController = controller;
    }
}
