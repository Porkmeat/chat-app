/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.appgui;

import com.mariano.chatapp.chatappclient.ChatAppClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class appgui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("chatappfxml.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("fxml.css").toExternalForm());

        stage.setTitle("MacChat");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        ChatAppClient client = new ChatAppClient("localhost", 8818);
        launch(args);
    }
}