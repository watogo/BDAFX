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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONArray;

/**
 * Hauptklasse der Applikation. Hier wird das Programm gestartet.
 *
 * @author Muhamed und Niklaus
 */
public class Main extends Application {

    private ScrapinghubController scrapinghub;
    private ContentController contentControl;
    private BorderPane rootLayout;

    /**
     * Main Funktion.
     *
     * @param args - Argumente
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Das Hauptfenster wird gezeichnet.
     *
     * @param primaryStage - Hauptstage
     */
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
        primaryStage.setTitle("Watogo CrawlerAdmin");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/watogoLogo.png")));
        primaryStage.show();

        showContent();
    }

    /**
     * Ladet den Inhalt vom Fenster.
     */
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

    /**
     * Gibt alle Jobs zurück, die es gibt.
     *
     * @return Liste von Jobs
     */
    public ObservableList<Job> getJobs() {
        return scrapinghub.retrieveJobs();
    }

    /**
     * Gibt alle Spidernamen zurück.
     *
     * @return Liste der Spidernamen
     */
    public ObservableList<String> getSpiderNames() {
        return scrapinghub.getSpiderNames();
    }

    /**
     * Zeigt das Logfile von einem Job an.
     *
     * @param job - Der Job, der angezeigt werden soll.
     */
    public void displayLog(Job job) {
        scrapinghub.showLog(contentControl, job);
    }

    /**
     * Speichert alle Items in die Datenbank.
     *
     * @param job - der Job, der gespeichert werden soll.
     * @return Ein JSON Array aller Items
     */
    public JSONArray saveToDB(Job job) {
        return scrapinghub.getJsonArray(contentControl, job);
    }
}
