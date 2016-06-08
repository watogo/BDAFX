/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo.view;

import ch.hslu.bda.watogo.AddElementToCollection;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ch.hslu.bda.watogo.Main;
import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.model.Settings;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

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
    private final Settings setting = Settings.getInstance();
    
    
    // Reference to the main application.
    private Main main;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ContentController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
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
    
    @FXML
    private void showLogButtonPressed() {
        main.displayLog(jobsTable.getSelectionModel().getSelectedItem());
        selectionModel.select(logTab);
    }
    
    @FXML
    private void refreshButtonPressed() {
        //Altes Fenster schliessen
        Stage stage = (Stage) refreshBtn.getScene().getWindow();
        stage.close();
        
        //neues Fenster öffnen
        Main newMain = new Main();
        newMain.start(new Stage());
    }
    
    @FXML
    public void saveToDBButtonPressed(){
        AddElementToCollection save = new AddElementToCollection();
        save.addToCollection("{\n" +
"  \"address\": {\n" +
"     \"building\": \"1007\",\n" +
"     \"coord\": [ -73.856077, 40.848447 ],\n" +
"     \"street\": \"Morris Park Ave\",\n" +
"     \"zipcode\": \"10462\"\n" +
"  },\n" +
"  \"borough\": \"Bronx\",\n" +
"  \"cuisine\": \"Bakery\",\n" +
"  \"grades\": [\n" +
"     { \"date\": { \"$date\": 1393804800000 }, \"grade\": \"A\", \"score\": 2 },\n" +
"     { \"date\": { \"$date\": 1378857600000 }, \"grade\": \"A\", \"score\": 6 },\n" +
"     { \"date\": { \"$date\": 1358985600000 }, \"grade\": \"A\", \"score\": 10 },\n" +
"     { \"date\": { \"$date\": 1322006400000 }, \"grade\": \"A\", \"score\": 9 },\n" +
"     { \"date\": { \"$date\": 1299715200000 }, \"grade\": \"B\", \"score\": 14 }\n" +
"  ],\n" +
"  \"name\": \"Morris Park Bake Shop\",\n" +
"  \"restaurant_id\": \"30075445\"\n" +
"}");
    }
    
    public void setMain(Main main) {
        this.main = main;

        // Add observable list data to the table
        jobsTable.setItems(main.getJobs());
        spiderList.setItems(main.getSpiderNames());
    }
    
    public void addText(String text){
        logArea.setText(text);
    }
    
    public void loadAdminGUI(){
        WebEngine webEngine = webView.getEngine();
        webEngine.load("http://"+setting.serverip+":"+setting.port);
    }
    
    public void loadScrapingHub(){
        WebEngine webEngine = webScrapinhub.getEngine();
        webEngine.load("https://app.scrapinghub.com/p/53883/periodic-jobs?apikey="+setting.apiKey);
    }
}
