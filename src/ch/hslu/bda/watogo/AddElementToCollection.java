/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.model.Setting;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

public class AddElementToCollection {

    public AddElementToCollection() {

    }

    public void addToCollection(JSONArray jsonArray) {
        Setting mySetting = Setting.INSTANCE;
        MongoClient mongoClient = new MongoClient(new ServerAddress(),
                Arrays.asList(MongoCredential.createCredential(mySetting.getDbUsername(), mySetting.getDbName(), mySetting.getDbPassword().toCharArray())));
        try {
            MongoDatabase db = mongoClient.getDatabase(mySetting.getDbName());
            MongoCollection coll = db.getCollection(mySetting.getDbCollection());
            
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
