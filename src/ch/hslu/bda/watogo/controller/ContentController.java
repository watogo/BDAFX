package ch.hslu.bda.watogo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ch.hslu.bda.watogo.Main;
import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.model.Setting;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * Controller für das Hauptfenster.
 *
 * @author Muhamed und Niklaus
 */
public class ContentController {

    @FXML
    private TableView<Job> jobsTable;
    @FXML
    private TableColumn<Job, String> spiderColumn;
    @FXML
    private TableColumn<Job, String> idColumn;
    @FXML
    private TableColumn<Job, String> timeColumn;
    @FXML
    private TableColumn<Job, Integer> numberColumn;
    @FXML
    private TableColumn<Job, String> stateColumn;

    @FXML
    private ComboBox<String> spiderList;
    @FXML
    private Button saveBtn;
    @FXML
    private Button showLogBtn;
    @FXML
    private Button runBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private TextArea logArea;
    @FXML
    private WebView webView;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab logTab;
    @FXML
    private WebView webScrapinhub;

    private SingleSelectionModel<Tab> selectionModel;

    private final Setting setting = Setting.INSTANCE;

    // Reference to the main application.
    private Main main;

    /**
     * Konstruktor.
     */
    public ContentController() {
    }

    /**
     * init Methode wenn das GUI gezeichnet wird.
     */
    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        spiderColumn.setCellValueFactory(cellData -> cellData.getValue().spider);
        idColumn.setCellValueFactory(cellData -> cellData.getValue().jobID);
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().startTime);
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberOfEvents.asObject());
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().state);
        selectionModel = tabPane.getSelectionModel();
    }

    /**
     * ActionListener für den ShowLog Button
     */
    @FXML
    private void showLogButtonPressed() {
        if (jobsTable.getSelectionModel().getSelectedIndex() != -1) {
            main.displayLog(jobsTable.getSelectionModel().getSelectedItem());
            selectionModel.select(logTab);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Please select an item",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ActionListener für den refresh Button
     */
    @FXML
    private void refreshButtonPressed() {
        //Close old window
        Stage stage = (Stage) refreshBtn.getScene().getWindow();
        stage.close();

        //open new window
        Main newMain = new Main();
        newMain.start(new Stage());
    }

    /**
     * ActionListener für den Save to DB Button
     */
    @FXML
    public void saveToDBButtonPressed() {
        MongoDBController save = new MongoDBController();
        save.addToCollection(main.saveToDB(jobsTable.getSelectionModel().getSelectedItem()));
    }

    /**
     * Referenziert auf die Main Methode.
     */
    public void setMain(Main main) {
        this.main = main;

        // Add observable list data to the table
        jobsTable.setItems(main.getJobs());
        spiderList.setItems(main.getSpiderNames());
    }

    /**
     * Fügt einen Text der logArea hinzu.
     *
     * @param text - Text, der hinzugefügt werden soll
     */
    public void addText(String text) {
        logArea.setText(text);
    }

    /**
     * lädt das Admin GUI.
     */
    public void loadAdminGUI() {
        WebEngine webEngine = webView.getEngine();
        webEngine.load("http://" + setting.getServerip() + ":" + setting.getPort());
    }

    /**
     * Lädt die Scheduling Seite von scrapinghub.com
     */
    public void loadScrapingHub() {
        WebEngine webEngine = webScrapinhub.getEngine();
        webEngine.load("https://app.scrapinghub.com/p/" + setting.getProjectId() + "/periodic-jobs?apikey=" + setting.getApiKey());
    }
}
