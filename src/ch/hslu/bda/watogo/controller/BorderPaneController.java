package ch.hslu.bda.watogo.controller;

import ch.hslu.bda.watogo.Main;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class BorderPaneController {

    @FXML
    private void handleSettings() {
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("view/Settings.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load Settings",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
