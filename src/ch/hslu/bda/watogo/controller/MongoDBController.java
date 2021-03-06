package ch.hslu.bda.watogo.controller;

import ch.hslu.bda.watogo.model.Setting;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Controller für die MongoDB.
 *
 * @author Muhamed und Niklaus
 */
public class MongoDBController {

    /**
     * Konstruktor.
     */
    public MongoDBController() {
    }

    /**
     * Fügt das JSON Array in die Datenbank ein.
     *
     * @param jsonArray - Alle Objekte die in die Datenbank gespeichert werden
     */
    public void addToCollection(JSONArray jsonArray) {
        Setting mySetting = Setting.INSTANCE;

        MongoClient mongoClient;
        mongoClient = null;
        try {
            mongoClient = new MongoClient(new ServerAddress(mySetting.getServerip(), 27017),
                    Arrays.asList(MongoCredential.createCredential(mySetting.getDbUsername(), mySetting.getDbName(), mySetting.getDbPassword().toCharArray())),
                    MongoClientOptions.builder().serverSelectionTimeout(1000).build());
            MongoDatabase db = mongoClient.getDatabase(mySetting.getDbName());
            MongoCollection coll = db.getCollection(mySetting.getDbCollection());

            List<Document> list = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Document doc = Document.parse(jsonArray.getJSONObject(i).toString());
                    doc.append("verified", "false");
                    list.add(doc);
                } catch (JSONException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.toString(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            coll.insertMany(list);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Connection Error - Username or Password of MongoDB wrong",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
}
