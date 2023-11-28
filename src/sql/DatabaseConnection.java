package sql;

import java.sql.DriverManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


public class DatabaseConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
	 
    public void MongoDBOperations() {

    }
    
    public static MongoDatabase getConnection() {
    	if(database == null) {
    		mongoClient = MongoClients.create("mongodb://localhost:27017");
    		database = mongoClient.getDatabase("labdatabase");
    	}
    	return database;
    }
    
    public static void closeConnection() {
    	mongoClient.close();
    }
}
