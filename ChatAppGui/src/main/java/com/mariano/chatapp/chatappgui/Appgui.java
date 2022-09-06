package com.mariano.chatapp.chatappgui;

import com.mariano.chatapp.chatclient.ChatAppClient;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Appgui extends Application {
    
    private static Scene scene;
    private static ChatAppClient client;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Appgui.class.getResource("loginfxml.fxml"));
        scene = new Scene(fxmlLoader.load(), 640, 480);
        
        //send client to controller
        LoginController controller = fxmlLoader.getController();
        controller.setClient(client);
        
        scene.getStylesheets().add(getClass().getResource("fxml.css").toExternalForm());

        stage.setTitle("MacChat");
        stage.setScene(scene);
        stage.show();
    }
    
//    static void setRoot(String fxml) throws IOException {
//        scene.setRoot(loadFXML(fxml));
//    }

//    private static Parent loadFXML(String fxml) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
//        LoginController controller = fxmlLoader.getController();
//        controller.setClient(client);
//        return fxmlLoader.load();
//    }


    public static void main(String[] args) {
        client = new ChatAppClient("localhost", 8818);
        client.connect();
        launch(args);
        
    }
}