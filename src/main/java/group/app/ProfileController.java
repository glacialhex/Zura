package group.app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

public class ProfileController extends Thread implements Initializable {
    @FXML
    private ImageView addFriendIcon;
    @FXML
    private Label addFriendText;
    @FXML
    private Label bio;
    @FXML
    private Label hello;
    @FXML
    private VBox postsContainer;
    @FXML
    private Label profileBio;
    @FXML
    private Label profileUsername;
    @FXML
    private HBox addFriendPanel;
    List<Post> posts;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    Socket socket;
    @FXML
    public void logout(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void likePost(User user, Post post, MouseEvent mouseEvent) throws IOException {
        String[] inputs = {"like", user.getUsername(), Integer.toString(post.getID())};
        System.out.println(Arrays.toString(inputs));
        out.writeObject(inputs);
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void dislikePost(User user, Post post, MouseEvent mouseEvent) throws IOException {
        String[] inputs = {"dislike", user.getUsername(), Integer.toString(post.getID())};
        System.out.println(Arrays.toString(inputs));
        out.writeObject(inputs);
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
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
                if (input instanceof Post && ((Post) input).getAuthor().getUsername().equals(Info.getProfileUser().getUsername())){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("post.fxml"));
                    VBox vBox = fxmlLoader.load();
                    PostController controller = fxmlLoader.getController();
                    controller.setData((Post) input, ProfileController.this);
                    Platform.runLater(new Runnable(){
                        @Override public void run(){
                            try {
                                postsContainer.getChildren().add(3,vBox);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (input instanceof ArrayList<?>){
                    posts = (ArrayList<Post>) input;
                    Platform.runLater(new Runnable(){
                        @Override public void run(){
                            try {
                                for (Post post : posts){
                                    FXMLLoader fxmlLoader = new FXMLLoader();
                                    fxmlLoader.setLocation(getClass().getResource("post.fxml"));
                                    VBox vBox = fxmlLoader.load();
                                    for (Comment comment : post.getComments()){
                                        FXMLLoader fxmlLoader2 = new FXMLLoader();
                                        fxmlLoader2.setLocation(getClass().getResource("comment.fxml"));
                                        HBox hBox = fxmlLoader2.load();
                                        CommentController commentController = fxmlLoader2.getController();
                                        commentController.setData(comment);
                                        vBox.getChildren().add(hBox);
                                    }
                                    PostController controller = fxmlLoader.getController();
                                    controller.setData(post,ProfileController.this);
                                    postsContainer.getChildren().add(3,vBox);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (input instanceof Boolean && input.equals(true)){
                    Platform.runLater(new Runnable(){
                        @Override public void run(){
                            addFriendPanel.setDisable(true);
                            addFriendText.setText("Already Friends");
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Info.getCurrentUser().getUsername().equals(Info.getProfileUser().getUsername())){
            addFriendPanel.setDisable(true);
            addFriendPanel.setVisible(false);
        }
        hello.setText(Info.getCurrentUser().getUsername());
        bio.setText("Bio: " + Info.getCurrentUser().getBio());
        User user = Info.getProfileUser();
        profileUsername.setText(user.getUsername());
        profileBio.setText(user.getBio());
        String[] inputs = {"getUserPosts", user.getUsername(),Info.getCurrentUser().getUsername()};
        System.out.println(Arrays.toString(inputs));
        try {
            socket = new Socket("localhost", 8787);
            System.out.println("Socket is connected with server!");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.start();
            out.writeObject(inputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(MouseEvent mouseEvent) throws IOException {
        String[] inputs = {"addFriend",Info.getCurrentUser().getUsername(), Info.getProfileUser().getUsername()};
        System.out.println(Arrays.toString(inputs));
        out.writeObject(inputs);
        addFriendText.setText("Friend Request Sent!");
    }

    public void goHome(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToFriends(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("friends.fxml"));
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

    public void goToMyProfile(MouseEvent mouseEvent) throws IOException {
        Info.setProfileUser(Info.getCurrentUser());
        Parent root = FXMLLoader.load(getClass().getResource("profile.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
