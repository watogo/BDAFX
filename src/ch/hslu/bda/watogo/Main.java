package ch.hslu.bda.watogo;

import ch.hslu.bda.watogo.controller.ScrapinghubController;
import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.model.Setting;
import ch.hslu.bda.watogo.controller.ContentController;
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
import javax.swing.JOptionPane;
import org.json.JSONArray;

public class Main extends Application {

    private ScrapinghubController scrapinghub;
    private ContentController contentControl;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Setting setting = Setting.INSTANCE;
        setting.updateSettings();

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
            JOptionPane.showMessageDialog(null,
                    "Failed to load Content",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
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

    public JSONArray saveToDB(Job job) {
        return scrapinghub.getJsonArray(contentControl, job);
    }
}
