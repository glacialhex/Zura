package group.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Thread implements Initializable {
    @FXML
    private Label error;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    Socket socket;

    public void goToRegisterScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void loginButtonOnClick(ActionEvent event) throws IOException {
        if (!usernameText.getText().isBlank() && !passwordText.getText().isBlank()) {
            error.setText("I clicked on login");
            out.writeObject(new User(usernameText.getText(), true));
        } else {
            error.setText("Please enter all fields!");
        }
    }
    @Override
    public void run(){

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket("localhost", 8787);
            System.out.println("Socket is connected with server!");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
