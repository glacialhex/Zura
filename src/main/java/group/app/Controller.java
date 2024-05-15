package group.app;

import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller extends Thread implements Initializable {
    @FXML
    private Label error;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String phase;
    private ActionEvent currentEvent;
    Socket socket;
    User loggedInUser;

    public void goToRegisterScene(ActionEvent event) throws IOException {
        phase = "register";
        currentEvent = event;
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void loginButtonOnClick(ActionEvent event) throws IOException, ClassNotFoundException {
        phase = "login";
        currentEvent = event;
        if (!usernameText.getText().isBlank() && !passwordText.getText().isBlank()) {
//            error.setText("I clicked on login");
            String[] inputs = {"login",usernameText.getText(),passwordText.getText()};
            System.out.println(Arrays.toString(inputs));
            out.writeObject(inputs);
        } else {
            error.setText("Please enter all fields!");
        }
    }
    public void goToLoginScene(ActionEvent event) throws IOException {
        phase = "login";
        currentEvent = event;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void goToHomeScene() throws IOException {
        phase = "home";
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) currentEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void registerButtonOnClick(ActionEvent event) throws IOException {
        phase = "register";
        currentEvent = event;
        if (usernameText.getText().isBlank() || passwordText.getText().isBlank()) {
            error.setText("Please enter all fields!");
        } else if (passwordText.getText().length() < 8) error.setText("Password must be at least 8 characters");
        else {
//            error.setText("I clicked on register");
            String[] inputs = {"register",usernameText.getText(),passwordText.getText()};
            System.out.println(Arrays.toString(inputs));
            out.writeObject(inputs);
        }
    }

    @Override
    public void run(){
        try {
            while(true){
                Object input = in.readObject();
                System.out.println(phase + ": " + input);
                if (input instanceof User) loggedInUser = (User) input;
                if (input instanceof User) System.out.println(loggedInUser.getUsername());
                switch (phase){
                    case "login" -> {
                        Platform.runLater(new Runnable(){
                            @Override public void run(){
                                try {
                                    if (input == null) error.setText("Incorrect username or password");
                                    else if (input instanceof User){
                                        Info.setCurrentUser((User) input);
                                        goToHomeScene();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    case "register" -> {
                        Platform.runLater(new Runnable(){
                            @Override public void run(){
                                try {
                                    if (input.equals(true)) error.setText("User created successfully");
                                    else error.setText("Username is taken");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    default -> System.out.println(input);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            socket = new Socket("localhost", 8787);
            System.out.println("Socket is connected with server!");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
//            Info.setIn(this.in);
//            Info.setOut(this.out);
//            Info.setSocket(this.socket);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
