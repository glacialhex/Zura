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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

public class homeController extends Thread implements Initializable {
    @FXML
    private Label bio;
    @FXML
    private Label hello;
    @FXML
    private VBox postsContainer;
    @FXML
    private TextField postContent;
    @FXML
    private TextField postTitle;
    @FXML
    private Label numPendingRequests;
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

    public void commentOnPost(String commentText, Post post, MouseEvent mouseEvent) throws IOException {
        String[] inputs = {"comment", commentText, Info.getCurrentUser().getUsername(),Integer.toString(post.getID())};
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
                if (input instanceof Post){
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("post.fxml"));
                    VBox vBox = fxmlLoader.load();
                    PostController controller = fxmlLoader.getController();
                    controller.setData((Post) input, homeController.this);
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
                                    controller.setData(post, homeController.this);
                                    postsContainer.getChildren().add(3,vBox);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else if (input.equals("friendRequest"+Info.getCurrentUser().getUsername())){
                    Platform.runLater(new Runnable(){
                        @Override public void run(){
                            try {
                                numPendingRequests.setText("(Pending)");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
        hello.setText(Info.getCurrentUser().getUsername());
        bio.setText("Bio: " + Info.getCurrentUser().getBio());
        try {
            socket = new Socket("localhost", 8787);
            System.out.println("Socket is connected with server!");
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.start();
            String[] inputs = {"getPosts"};
            System.out.println(Arrays.toString(inputs));
            out.writeObject(inputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPost(ActionEvent actionEvent) throws IOException {
        if (!postTitle.getText().isBlank() && !postContent.getText().isBlank()) {
            String[] inputs = {"post",postTitle.getText(),postContent.getText(), Info.getCurrentUser().getUsername()};
            System.out.println(Arrays.toString(inputs));
            out.writeObject(inputs);
        }
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("post.fxml"));
//        VBox vBox = fxmlLoader.load();
//        PostController controller = fxmlLoader.getController();
//        controller.setData(post);
//        postsContainer.getChildren().add(3,vBox);
    }

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
}
