/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappgui;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author Mariano
 */
public class CustomListCell extends ListCell<Friend> {
        private HBox content;
        private Text profilePicture;
        private Text username;
        private Text lastMsg;
        private Text timestamp;

        public CustomListCell() {
            super();
            username = new Text();
            lastMsg = new Text();
            profilePicture = new Text();
            VBox vBox = new VBox(username, lastMsg);
            content = new HBox(profilePicture, vBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(Friend item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                username.setText(item.getUsername());
                lastMsg.setText(item.getLastMsg());
                //timestamp.setText(item.getTimestamp());
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }