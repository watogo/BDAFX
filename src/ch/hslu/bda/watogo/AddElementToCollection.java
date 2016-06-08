/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.model.Settings;
import static ch.hslu.bda.watogo.model.Settings.dbUsername;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import org.bson.Document;

public class AddElementToCollection {

    public AddElementToCollection() {

    }

    public void addToCollection(String jsonString) {
        Settings mySetting = Settings.getInstance();
        MongoClient mongoClient = new MongoClient(new ServerAddress(),
                Arrays.asList(MongoCredential.createCredential(mySetting.dbUsername, mySetting.dbName, mySetting.dbPassword.toCharArray())));
        try {
            /*
            for (String databaseName : mongoClient.listDatabaseNames()) {
                System.out.println("Database: " + databaseName);
            }
            */
            MongoDatabase db = mongoClient.getDatabase(mySetting.dbName);
            MongoCollection coll = db.getCollection(mySetting.dbCollection);

            Document doc = Document.parse(jsonString);
            coll.insertOne(doc);
        } finally {
            mongoClient.close();
        }
    }
}
