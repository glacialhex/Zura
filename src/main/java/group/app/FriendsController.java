package group.app;

import javafx.application.Platform;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class FriendsController extends Thread implements Initializable {
    @FXML
    private VBox friendReqContainer;
    @FXML
    private VBox friendsContainer;
    @FXML
    private Label bio;
    @FXML
    private Label hello;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    Socket socket;
    List<User> friendRequests;
    List<User> friends;
    String expecting;

    public void goHome(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToFriends(MouseEvent mouseEvent) {
    }

    public void goToMyProfile(MouseEvent mouseEvent) throws IOException {
        Info.setProfileUser(Info.getCurrentUser());
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToEditProfile(MouseEvent mouseEvent) {
    }

    public void logout(MouseEvent mouseEvent) {
    }

    public void acceptRequest(User user, MouseEvent mouseEvent) throws IOException {
        String[] inputs = {"acceptFriendReq",Info.getCurrentUser().getUsername(), user.getUsername()};
        System.out.println(Arrays.toString(inputs));
        out.writeObject(inputs);
        Parent root = FXMLLoader.load(getClass().getResource("friends.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void rejectRequest(User user, MouseEvent mouseEvent) throws IOException {
        String[] inputs = {"removeFriendReq",Info.getCurrentUser().getUsername(), user.getUsername()};
        System.out.println(Arrays.toString(inputs));
        out.writeObject(inputs);
        Parent root = FXMLLoader.load(getClass().getResource("friends.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object input = in.readObject();
                System.out.println("Input: " + input);
                if (input.equals("requests")) expecting = "requests";
                if (input.equals("friends")) expecting = "friends";
                if (input instanceof List<?> && expecting.equals("requests")){
                    friendRequests = (List<User>) input;
                    Platform.runLater(new Runnable(){
                        @Override public void run(){
                            try {
                                friendReqContainer.getChildren().clear();
                                for (User request : friendRequests){
                                    FXMLLoader fxmlLoader = new FXMLLoader();
                                    fxmlLoader.setLocation(getClass().getResource("request.fxml"));
                                    HBox hBox = fxmlLoader.load();
                                    RequestController controller = fxmlLoader.getController();
                                    controller.setData(request, FriendsController.this);
                                    friendReqContainer.getChildren().addFirst(hBox);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (input instanceof List<?> && expecting.equals("friends")){
                    friends = (List<User>) input;
                    Platform.runLater(new Runnable(){
                        @Override public void run(){
                            try {
                                friendsContainer.getChildren().clear();
                                for (User request : friends){
                                    FXMLLoader fxmlLoader = new FXMLLoader();
                                    fxmlLoader.setLocation(getClass().getResource("friend.fxml"));
                                    HBox hBox = fxmlLoader.load();
                                    FriendController controller = fxmlLoader.getController();
                                    controller.setData(request, FriendsController.this);
                                    friendsContainer.getChildren().addFirst(hBox);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else if (input.equals("friendRequest"+Info.getCurrentUser().getUsername())){
                    String[] inputs = {"getFriendReq",Info.getCurrentUser().getUsername()};
                    System.out.println(Arrays.toString(inputs));
                    out.writeObject(inputs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hello.setText(Info.getCurrentUser().getUsername());
        bio.setText("Bio: " + Info.getCurrentUser().getBio());
//        posts = new ArrayList<>(getPosts());

        try {
            socket = new Socket("localhost", 8787);
            System.out.println("Socket is connected with server!");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
//            socket = Info.getSocket();
//            this.out = Info.getOut();
//            this.in = Info.getIn();
            this.start();
            String[] inputs2 = {"getFriends",Info.getCurrentUser().getUsername()};
            System.out.println(Arrays.toString(inputs2));
            out.writeObject(inputs2);
            String[] inputs = {"getFriendReq",Info.getCurrentUser().getUsername()};
            System.out.println(Arrays.toString(inputs));
            out.writeObject(inputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
