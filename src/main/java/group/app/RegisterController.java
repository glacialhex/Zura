package group.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Label error;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    public void goToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void registerButtonOnClick(ActionEvent event) {
        if (!usernameText.getText().isBlank() && !passwordText.getText().isBlank()) {
//            Server2.insertUser(usernameText.getText(), passwordText.getText().hashCode());
            error.setText("User has been created!");
        } else {
            error.setText("Please enter all fields!");
        }
    }
}
