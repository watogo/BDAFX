package ch.hslu.bda.watogo.controller;

import ch.hslu.bda.watogo.model.Setting;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

/**
 * Controller für die Settings.
 *
 * @author Muhamed und Niklaus
 */
public class SettingsController implements Initializable {

    @FXML
    private TextField textFieldApi;
    @FXML
    private TextField textFieldProId;
    @FXML
    private TextField textFieldSerIp;
    @FXML
    private TextField textFieldPort;
    @FXML
    private TextField textFieldDBName;
    @FXML
    private TextField textFieldDBCollection;
    @FXML
    private TextField textFieldDBUsername;
    @FXML
    private TextField textFieldDBPassword;
    @FXML
    private TextField textFieldDateVon;
    @FXML
    private TextField textFieldDateBis;
    @FXML
    private CheckBox checkBoxParseDate;

    /**
     * Initialmethode für die Settings. Hier werden die Settings geladen.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPrefs();
    }

    /**
     * Lädt die Einstellungen von der 'settings.properties' Datei, die sich im
     * Root-Verzeichnis befindet.
     */
    private void loadPrefs() {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("settings.properties");
            prop.load(input);

            textFieldApi.setText(prop.getProperty("apiKey"));
            textFieldProId.setText(prop.getProperty("projectId"));
            textFieldSerIp.setText(prop.getProperty("serverIp"));
            textFieldPort.setText(prop.getProperty("port"));
            textFieldDBName.setText(prop.getProperty("dbName"));
            textFieldDBCollection.setText(prop.getProperty("dbCollection"));
            textFieldDBUsername.setText(prop.getProperty("dbUsername"));
            textFieldDBPassword.setText(prop.getProperty("dbPassword"));
            textFieldDateVon.setText(prop.getProperty("dbBezDatumVon"));
            textFieldDateBis.setText(prop.getProperty("dbBezDatumBis"));

            if (prop.getProperty("isParseDate") != null) {
                if (prop.getProperty("isParseDate").equals("1")) {
                    checkBoxParseDate.setSelected(true);
                } else {
                    checkBoxParseDate.setSelected(false);
                }
            } else {
                checkBoxParseDate.setSelected(false);
            }

        } catch (IOException ex) {
            System.out.println("Noch keine Datei gefunden!");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Noch keine Datei gefunden!");
                }
            }
        }
    }

    /**
     * Speichert die Einstellungen in die 'settings.properties' Datei.
     */
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
            prop.setProperty("dbName", textFieldDBName.getText());
            prop.setProperty("dbCollection", textFieldDBCollection.getText());
            prop.setProperty("dbUsername", textFieldDBUsername.getText());
            prop.setProperty("dbPassword", textFieldDBPassword.getText());
            prop.setProperty("dbBezDatumVon", textFieldDateVon.getText());
            prop.setProperty("dbBezDatumBis", textFieldDateBis.getText());
            prop.setProperty("isParseDate", checkBoxParseDate.isSelected() ? "1" : "0");

            prop.store(output, null);

            JOptionPane.showMessageDialog(null,
                    "Erfolgreich gespeichert");

        } catch (IOException io) {
            System.out.println("IOException!!!!");
        } finally {
            if (output != null) {
                try {
                    output.close();
                    Setting.INSTANCE.updateSettings(); //Settings updaten
                } catch (IOException e) {
                    System.out.println("IOException!!!!");
                }
            }
        }
    }

    /**
     * Gibt den Wert eines Settings zurück.
     *
     * @param settingName - Settingname
     * @return Settingwert
     */
    public String getSetting(String settingName) {
        Properties prop = new Properties();
        InputStream input = null;
        String property = "";
        try {
            input = new FileInputStream("settings.properties");
            prop.load(input);
            property = prop.getProperty(settingName);
        } catch (IOException ex) {
            System.out.println("Noch keine Datei gefunden!");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Noch keine Datei gefunden!");
                }
            }
        }
        return property;
    }

    /**
     * Schliesst das Fenster.
     *
     * @param event - Event der ausgelöst wird.
     */
    @FXML
    public void handleClose(Event event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Überprüft die Verbindung zum Server. Der Server wird in den Settings
     * definiert.
     */
    @FXML
    public void handleTryConnection() {
        Boolean reachable = false;
        try {
            reachable = InetAddress.getByName(this.textFieldSerIp.getText()).isReachable(5000);
        } catch (Exception e) {
            System.out.println("Fehler ist aufgetreten beim der Server Ip");
        }

        if (reachable) {
            JOptionPane.showMessageDialog(null,
                    "Der Server ist erreichbar");
        } else {
            JOptionPane.showMessageDialog(null,
                    "Ungültige Server IP",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
