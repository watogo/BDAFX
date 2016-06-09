/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.model.Settings;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddElementToCollection {

    public AddElementToCollection() {

    }

    public void addToCollection(JSONArray jsonArray) {
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
            
            
            
            /*
            DBCollection collection = (DBCollection) db.getCollection(mySetting.dbCollection);
            
            DBObject dbObject = (DBObject)JSON.parse(jsonString);
            collection.insert(dbObject);
            */
            
            //JSONArray jsonArray = new JSONArray(jsonArrayString);
            
            List<Document> list = new ArrayList<>();
            
            for (int i=0; i < jsonArray.length(); i++) {
                try {
                    Document doc = Document.parse(jsonArray.getJSONObject(i).toString());
                    doc.append("verified", "false");
                    list.add(doc);
                } catch (JSONException ex) {
                    System.out.println("Kann nicht in Liste einfügen!");
                }
            }
            
            coll.insertMany(list);
               
        } finally {
            mongoClient.close();
        }
    }
}
