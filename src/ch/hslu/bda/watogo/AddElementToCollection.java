/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

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
        MongoClient mongoClient = new MongoClient(new ServerAddress(),
                Arrays.asList(MongoCredential.createCredential("admin", "test", "password".toCharArray())));
        try {
            for (String databaseName : mongoClient.listDatabaseNames()) {
                System.out.println("Database: " + databaseName);
            }
            MongoDatabase db = mongoClient.getDatabase("test");
            MongoCollection coll = db.getCollection("test2");

            Document doc = Document.parse(jsonString);
            coll.insertOne(doc);
        } finally {
            mongoClient.close();
        }
    }
}
