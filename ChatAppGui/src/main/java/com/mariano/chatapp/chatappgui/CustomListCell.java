/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappgui;

import java.time.LocalDateTime;
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
        private VBox textContainer;
        private HBox subtextContainer;

        public CustomListCell() {
            super();
            username = new Text();
            lastMsg = new Text();
            timestamp = new Text();
            profilePicture = new Text();
            subtextContainer = new HBox(lastMsg,timestamp);
            textContainer = new VBox(username, subtextContainer);
            content = new HBox(profilePicture, textContainer);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(Friend item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                username.setText(item.getUsername());
                lastMsg.setText(item.getLastMsg());
                LocalDateTime messageTime = item.getTimestamp();
                LocalDateTime now = LocalDateTime.now();
                if (now.toLocalDate().equals(messageTime.toLocalDate())) {
                    timestamp.setText(messageTime.getHour()+":"+messageTime.getMinute());
                } else {
                    timestamp.setText(messageTime.getDayOfMonth()+"/"+messageTime.getMonthValue()+
                            " - "+ messageTime.getHour()+":"+messageTime.getMinute());
                }
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }