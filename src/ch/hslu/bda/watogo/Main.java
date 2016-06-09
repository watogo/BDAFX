/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.model.Setting;
import ch.hslu.bda.watogo.view.ContentController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONArray;

/**
 *
 * @author Niklaus
 */
public class Main extends Application {
    private ScrapinghubController scrapinghub;
    private ContentController contentControl;
    private Stage primaryStage;
    private BorderPane rootLayout;
    //private Settings setting;
    
    @Override
    public void start(Stage primaryStage) {
        
        //setting = Settings.getInstance();
        Setting setting = Setting.INSTANCE;
        setting.updateSettings(); //Settings auslesen
        
        this.scrapinghub = new ScrapinghubController(setting.getApiKey(), setting.getProjectId());  
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/BorderPane.fxml"));
        try {
            rootLayout = (BorderPane) loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Watogo Eventmanager");
        primaryStage.show();
        
        showContent();
    }
    
    private void showContent() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Content.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(personOverview);
            
            this.contentControl = loader.getController();
            contentControl.setMain(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public ObservableList<Job> getJobs() {
        return scrapinghub.retrieveJobs();
    }

    public ObservableList<String> getSpiderNames() {
        return scrapinghub.getSpiderNames();
    }
    
    public void displayLog(Job job) {
        scrapinghub.showLog(contentControl, job);
    }
    
    public JSONArray saveToDB(Job job){
        return scrapinghub.getJsonArray(contentControl, job);
    }
}
