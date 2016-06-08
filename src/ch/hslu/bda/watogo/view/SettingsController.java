package ch.hslu.bda.watogo.view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

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
            JOptionPane.showMessageDialog(null,
                "Erfolgreich gespeichert");
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

    public String getSetting(String settingName) {
        Properties prop = new Properties();
        InputStream input = null;
        String property = "";
        try {
            input = new FileInputStream("settings.properties");
            prop.load(input);
            property = prop.getProperty(settingName);
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
        return property;
    }

    @FXML
    public void handleClose(Event event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void handleTryConnection() {
        Boolean reachable = false;
        try {
            reachable = InetAddress.getByName(this.textFieldSerIp.getText()).isReachable(5000);            
        } catch (Exception e) {
            System.out.println("Fehler ist aufgetreten beim der Server Ip");
        }
        
        if (reachable){
            JOptionPane.showMessageDialog(null,
                "Der Server ist erreichbar");
        }else{
            JOptionPane.showMessageDialog(null,
                "Ungültige Server IP",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
