package group.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class EditController extends Thread implements Initializable {
    @FXML
    private Label bio;
    @FXML
    private Label hello;
    @FXML
    private TextField bioContent;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    Socket socket;
    public void goHome(MouseEvent mouseEvent) {
    }

    public void goToFriends(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("friends.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToMyProfile(MouseEvent mouseEvent) throws IOException {
        Info.setProfileUser(Info.getCurrentUser());
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToEditProfile(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("edit.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeBio(ActionEvent actionEvent) throws IOException {
        if (bioContent.getText().isBlank()) bioContent.setPromptText("You can't have an empty bio!");
        else {
            String[] inputs = {"editBio",Info.getCurrentUser().getUsername(), bioContent.getText()};
            System.out.println(Arrays.toString(inputs));
            out.writeObject(inputs);
            Info.getCurrentUser().setBio(bioContent.getText());
            bio.setText(bioContent.getText());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hello.setText(Info.getCurrentUser().getUsername());
        bio.setText("Bio: " + Info.getCurrentUser().getBio());
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
