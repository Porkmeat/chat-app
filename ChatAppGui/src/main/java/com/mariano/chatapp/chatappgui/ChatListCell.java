/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappgui;

/**
 *
 * @author julia
 */
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Mariano
 */
public class ChatListCell extends ListCell<Chat> {

    private HBox content;

    private ChatcellfxmlController controller;
    private AnchorPane bubble;

    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Appgui.class.getResource("chatcellfxml.fxml"));
            fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatListCell() {
        super();
        bubble = controller.getChatBubble();
        content = new HBox(bubble);
        content.setSpacing(10);
        content.setPrefWidth(1);

    }

    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) { // <== test for null item and empty parameter
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime messageTime = item.getTimestamp();
            if (item.getMessage().isEmpty()) {

                controller.setMessageText(setTimeString(now, messageTime));
                controller.setTimestampText("");
                controller.setBubbleColor("-fx-background-color: #b0bec5;");
                content.setAlignment(Pos.CENTER);
                bubble = controller.getChatBubble();
            } else {
                controller.setMessageText(item.getMessage());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                controller.setTimestampText(messageTime.format(formatter));
                if (item.isUserIsSender()) {
                    content.setAlignment(Pos.CENTER_RIGHT);
                    controller.setBubbleColor("-fx-background-color: #80deea;");
                } else {
                    content.setAlignment(Pos.CENTER_LEFT);
                    controller.setBubbleColor("-fx-background-color: #a5d6a7;");
                }
                bubble = controller.getChatBubble();
            }
            setGraphic(content);
        } else {
            setGraphic(null);
        }
    }

    private String setTimeString(LocalDateTime now, LocalDateTime messageTime) {
        LocalDate today = now.toLocalDate();
        LocalDate messageDate = messageTime.toLocalDate();
        long days = ChronoUnit.DAYS.between(messageDate, today);
        if (days == 0) {
            return "TODAY";
        } else if (days == 1) {
            return "YESTERDAY";
        } else if (days < 7) {
            return messageDate.getDayOfWeek().toString();
        } else {
            return messageDate.toString();
        }
    }
}
