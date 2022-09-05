package com.mariano.chatapp.chatappgui;

import com.mariano.chatapp.chatclient.ChatAppClient;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mariano
 */
public class LoginController {

    /**
     * Initializes the controller class.
     */
    private ChatAppClient client;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void login() {
        try {
            if (client.login(usernameField.getText(), passwordField.getText())) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed!");
            };
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setClient(ChatAppClient client) {
        this.client = client;
    }
}
