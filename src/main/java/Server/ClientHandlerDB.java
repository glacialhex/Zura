//package Server;
//
//import group.app.*;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bson.Document;
//
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.Updates;
//
//public class ClientHandler extends Thread {
//
//    private ArrayList<ClientHandler> clients;
//    private SocialPlatform platform;
//    private Socket socket;
//    private ObjectOutputStream out;
//    private ObjectInputStream in;
//    private MongoDatabase database;
//
//    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients, SocialPlatform platform, MongoDatabase database) {
//        try {
//            this.socket = socket;
//            this.clients = clients;
//            this.out = new ObjectOutputStream(socket.getOutputStream());
//            this.in = new ObjectInputStream(socket.getInputStream());
//            this.platform = platform;
//            this.database = database; // Pass the MongoDB database instance
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            String[] request;
//            while ((request = (String[]) in.readObject()) != null) {
//                switch (request[0]) {
//                    case "login" -> {
//                        // Handle login using MongoDB
//                        User currentUser = login(request[1], request[2]);
//                        if(currentUser == null) {
//                            System.out.println("Incorrect username or password");
//                            System.out.println(request[1] + " - " + request[2].hashCode());
//                            out.writeObject(null);
//                        } else {
//                            System.out.println(request[1] + " - Logged in");
//                            out.writeObject(currentUser);
//                        }
//                    }
//                    case "register" -> {
//                        // Handle user registration using MongoDB
//                        Boolean registered = registerUser(request[1], request[2]);
//                        if (registered){
//                            System.out.println(request[1] + " - User created");
//                            out.writeObject(true);
//                        } else out.writeObject(false);
//                    }
//                    // Other cases...
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            try {
//                out.close();
//                in.close();
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // MongoDB operations
//
//    private User login(String username, String password) {
//        // Example of querying user from MongoDB
//        MongoCollection<Document> usersCollection = database.getCollection("users");
//        Document userDoc = usersCollection.find(Filters.and(Filters.eq("username", username), Filters.eq("password", password))).first();
//        if (userDoc != null) {
//            return new User(userDoc.getString("username"), userDoc.getString("password")); // Create User object
//        } else {
//            return null;
//        }
//    }
//
//    private Boolean registerUser(String username, String password) {
//        // Example of inserting user into MongoDB
//        MongoCollection<Document> usersCollection = database.getCollection("users");
//        Document newUser = new Document("username", username)
//                .append("password", password);
//        try {
//            usersCollection.insertOne(newUser);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    // Other MongoDB operations for handling posts, comments, etc.
//}
