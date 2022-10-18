/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappgui;

/**
 *
 * @author julia
 */
import java.time.LocalDateTime;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author Mariano
 */
public class ChatListCell extends ListCell<Chat> {
        private HBox content;
        private Text message;
        private Text timestamp;
        private VBox bubble;

        public ChatListCell() {
            super();
            message = new Text();
            timestamp = new Text();
            bubble = new VBox(message, timestamp);
            content = new HBox(bubble);
            bubble.setSpacing(5);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(Chat item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) { // <== test for null item and empty parameter
                message.setText(item.getMessage());
                LocalDateTime messageTime = item.getTimestamp();
                LocalDateTime now = LocalDateTime.now();
                if (now.toLocalDate().equals(messageTime.toLocalDate())) {
                    timestamp.setText(messageTime.getHour()+":"+messageTime.getMinute());
                } else {
                    timestamp.setText(messageTime.getDayOfMonth()+"/"+messageTime.getMonthValue()+
                            " - "+ messageTime.getHour()+":"+messageTime.getMinute());
                }
                if(item.isUserIsSender()) {
                    content.setAlignment(Pos.CENTER_RIGHT);
                    bubble.setStyle("-fx-background-color: #80deea;");
                } else {
                    content.setAlignment(Pos.CENTER_LEFT);
                    bubble.setStyle("-fx-background-color: #a5d6a7;");
                }
                setGraphic(content);
            } else {
                setGraphic(null);
            }
        }
    }