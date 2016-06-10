package ch.hslu.bda.watogo.controller;

import ch.hslu.bda.watogo.Main;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * Controller für das BorderPane.
 *
 * @author Muhamed und Niklaus
 */
public class BorderPaneController {

    /**
     * ActionListener für den Klick auf die Settings.
     */
    @FXML
    private void handleSettings() {
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("view/Settings.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("images/watogoLogo.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to load Settings",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * ActionListener für den Klick auf Exit.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
