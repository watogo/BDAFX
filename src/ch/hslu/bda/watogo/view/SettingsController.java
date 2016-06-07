/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo.view;

import ch.hslu.bda.watogo.Main;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Mumi
 */
public class SettingsController implements Initializable {

    private Preferences prefs;

    @FXML
    private TextField textFieldApi;
    @FXML
    private TextField textFieldProId;
    @FXML
    private TextField textFieldSerIp;
    @FXML
    private TextField textFieldPort;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPrefs();
    }

    private void loadPrefs() {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("settings.properties");
            prop.load(input);

            // get the property value and print it out
            textFieldApi.setText(prop.getProperty("apiKey"));
            textFieldProId.setText(prop.getProperty("projectId"));
            textFieldSerIp.setText(prop.getProperty("serverIp"));
            textFieldPort.setText(prop.getProperty("port"));

        } catch (IOException ex) {
            //ex.printStackTrace();
            System.out.println("Noch keine Datei gefunden!");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.out.println("Noch keine Datei gefunden!");
                }
            }
        }
    }
    
    @FXML
    private void saveProp() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("settings.properties");
            prop.setProperty("apiKey", textFieldApi.getText());
            prop.setProperty("projectId", textFieldProId.getText());
            prop.setProperty("serverIp", textFieldSerIp.getText());
            prop.setProperty("port", textFieldPort.getText());
            prop.store(output, null);
        } catch (IOException io) {
            //io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void handleClose(Event event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
