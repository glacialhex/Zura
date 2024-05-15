//package Server;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoDatabase;
//
//public class MongoDBManager {
//    private static final String CONNECTION_STRING = "mongodb://localhost:7704";
//
//    private ZuraClient mongoClient;
//    private ZuraDatabase database;
//
//    public MongoDBManager() {
//
//        this.mongoClient = MongoClients.create(new ConnectionString(CONNECTION_STRING));
//
//        this.database = mongoClient.getDatabase("ZuraDB");
//    }
//
//    public ZuraDatabase getDatabase() {
//        return database;
//    }
//
//    public void close() {
//        mongoClient.close(); // Close the connection when done
//    }
//}
