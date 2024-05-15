package Server;

import group.app.*;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {

    private ArrayList<ClientHandler> clients;
    private SocialPlatform platform;
    private Socket socket;
//    private BufferedReader reader;
//    private PrintWriter writer;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients, SocialPlatform platform) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            this.platform = platform;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String[] request;
            while ((request = (String[]) in.readObject()) != null) {
                switch (request[0]) {
                    case "login" -> {
                        User currentUser = platform.login(request[1], request[2]);
                        if(currentUser == null) {
                            System.out.println("Incorrect username or password");
                            System.out.println(request[1] + " - " + request[2].hashCode());
                            out.writeObject(null);
                        } else {
                            System.out.println(request[1] + " - Logged in");
                            out.writeObject(currentUser);
                        }
                    }
                    case "register" -> {
                        Boolean registered = platform.registerUser(request[1], request[2]);
                        if (registered){
                            System.out.println(request[1] + " - User created");
                            out.writeObject(true);
                        } else out.writeObject(false);
                        System.out.println(platform.getUsers());
                        System.out.println(platform.getUserManager().getPassword(request[1]));
                    }
                    case "post" ->{
                        Post createdPost = platform.createPost(request[1],request[2],platform.getUser(request[3]));
                        for (ClientHandler cl : clients) {
                            if (!cl.socket.isClosed()) cl.out.writeObject(createdPost);
                        }
                    }
                    case "getPosts" -> {
                        ArrayList<Post> allPosts = platform.getPosts();
                        out.writeObject(allPosts);
                    }
                    case "getUserPosts" -> {
                        ArrayList<Post> userPosts = platform.getPostsByUser(platform.getUser(request[1]));
                        out.writeObject(userPosts);
                        Boolean isFriends = platform.getUser(request[2]).getFriends().contains(platform.getUser(request[1]));
                        out.writeObject(isFriends);
                    }
                    case "addFriend" -> {
                        if(platform.getUser(request[2]).addRequest(platform.getUser(request[1]))){
                            for (ClientHandler cl : clients) {
                                if (!cl.socket.isClosed()) cl.out.writeObject("friendRequest" + request[2]);
                            }
                        }
                    }
                    case "getFriendReq" -> {
                        List<User> requests = platform.getUser(request[1]).getRequests();
                        out.writeObject("requests");
                        out.writeObject(requests);
                    }
                    case "getFriends" -> {
                        List<User> requests = platform.getUser(request[1]).getFriends();
                        out.writeObject("friends");
                        out.writeObject(requests);
                    }
                    case "removeFriendReq" -> {
                        User user = platform.getUser(request[1]);
                        User declinedUser = platform.getUser(request[2]);
                        List<User> requests = user.declineRequest(declinedUser);
                        out.writeObject("requests");
                        out.writeObject(requests);
                    }
                    case "acceptFriendReq" -> {
                        User user = platform.getUser(request[1]);
                        User acceptedUser = platform.getUser(request[2]);
                        user.acceptRequest(acceptedUser);
                    }
                    case "like" -> {
                        platform.likePost(platform.getUser(request[1]), request[2]);
                    }
                    case "dislike" -> {
                        platform.dislikePost(platform.getUser(request[1]), request[2]);
                    }
                    case "comment" -> {
                        User user = platform.getUser(request[2]);
                        platform.commentOnPost(new Comment(user,request[1]),request[3]);
                    }
                    case "editBio" -> {
                        platform.getUser(request[1]).setBio(request[2]);
                    }
                    default -> {

                    }
                }

//                if (msg.equalsIgnoreCase( "exit")) {
//                    break;
//                }
//                for (ClientHandler cl : clients) {
//                    cl.out.writeObject("New user logging in: " + ((User) request).getUsername());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
//                reader.close();
//                writer.close();
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

