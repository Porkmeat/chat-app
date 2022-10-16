package com.mariano.chatapp.chatappgui;

import com.mariano.chatapp.chatclient.ChatAppClient;
import com.mariano.chatapp.chatclient.FriendListener;
import com.mariano.chatapp.chatclient.MessageListener;
import com.mariano.chatapp.chatclient.RequestListener;
import com.mariano.chatapp.chatclient.StatusListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainScreenController implements StatusListener, MessageListener, RequestListener, FriendListener {

    private ChatAppClient client;
    private String currentChat;
    private final HashMap<String, ListView> activeChats = new HashMap<>();
    private ListView<String> activeChat;
    private String requester;

    @FXML
    private ListView<String> userlist;
    @FXML
    private ListView<String> requestlist;
    @FXML
    private Label mainusername;
    @FXML
    private Label mainchatusername;
    @FXML
    private BorderPane chatscreen;
    @FXML
    private TextField chatinput;
    @FXML
    private ScrollPane chatwindow;
    @FXML
    private TextField addFriendField;
    @FXML
    private Tab requestTab;
    @FXML
    private HBox requestButtons;
    @FXML
    private TabPane mainTabPane;

    public void setupController(ChatAppClient client, String username) throws IOException {
        mainusername.setText(username);
        this.client = client;
        this.client.addStatusListener(this);
        this.client.requestOnlineUsers();
        this.client.addMessageListener(this);
        this.client.addRequestListener(this);
        this.client.addFriendListener(this);
        this.client.fetchRequests();
        this.client.fetchFriends();
        userlist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                currentChat = userlist.getSelectionModel().getSelectedItem();
                if (mainchatusername.getText() != null && !mainchatusername.getText().equals(currentChat)) {

                    Platform.runLater(() -> {
                        activeChat = activeChats.get(currentChat);
                        chatwindow.setContent(activeChat);
                        autoScroll();
                        mainchatusername.setText(currentChat);

                    });
                    if (!chatscreen.isVisible()) {
                        Platform.runLater(() -> {
                            chatscreen.setVisible(true);
                        });
                    }
                }
            }
        });

        requestlist.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                requester = requestlist.getSelectionModel().getSelectedItem();
                System.out.println(requestlist.getItems().toString());
                if (requester != null && requestButtons.isDisable()) {
                    requestButtons.setDisable(false);
                }
            }
        });
    }

    public void logoff(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setContentText("Are you sure you want to log out?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                client.logoff();
                System.out.println("You logged out!");
                Platform.runLater(() -> {
                    stage.close();
                });
            } catch (IOException ex) {
                Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void online(String username) {
        if (!activeChats.containsKey(username)) {
            activeChats.put(username, new ListView<String>());
        }
        Platform.runLater(() -> {
            userlist.getItems().add(username);
        });
    }

    @Override
    public void offline(String username) {
        Platform.runLater(() -> {
            userlist.getItems().remove(username);
        });
    }

    @FXML
    public void sendMsg() throws IOException {
        if (chatinput.getText() != null && !chatinput.getText().isBlank()) {
            String message = chatinput.getText();
            Platform.runLater(() -> {
                activeChat.getItems().add("You: " + message);
                autoScroll();
            });
            client.msg(currentChat, message);
            Platform.runLater(() -> {
                chatinput.clear();
            });
        }
    }

    @Override
    public void messageGet(String fromUser, String message) {
        ListView<String> chatWithUser = activeChats.get(fromUser);
        Platform.runLater(() -> {
            chatWithUser.getItems().add(fromUser + ": " + message);
            if (activeChat == chatWithUser) {
                autoScroll();
            }
        });
    }

    @FXML
    public void addFriend() throws IOException {
        String friendName = addFriendField.getText();
        client.addFriend(friendName);
        Platform.runLater(() -> {
            addFriendField.clear();
        });
    }

    private void autoScroll() {
        if (activeChat != null) {
            activeChat.scrollTo(activeChat.getItems().size());
        }
    }

    @Override
    public void request(String fromUser) {
        if (requestTab.isDisabled()) {
            requestTab.setDisable(false);
        }
        Platform.runLater(() -> {
            requestlist.getItems().add(fromUser);
        });
    }

    @FXML
    public void acceptRequest() {
        try {
            client.respondToRequest(requester, 1);
            System.out.println("Added friend " + requester);
            Platform.runLater(() -> {
                if (requestlist.getItems().remove(requester)) {
                    requester = null;
                }
            });
            closeRequestTab();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void denyRequest() {
        try {
            client.respondToRequest(requester, 2);
            System.out.println("Request denied: " + requester);
            Platform.runLater(() -> {
                if (requestlist.getItems().remove(requester)) {
                    requester = null;
                }
            });
            closeRequestTab();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void blockRequest() {
        try {
            client.respondToRequest(requester, 3);
            System.out.println("Blocked user " + requester);
            Platform.runLater(() -> {
                if (requestlist.getItems().remove(requester)) {
                    requester = null;
                }
            });
            closeRequestTab();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeRequestTab() {
        Platform.runLater(() -> {
            if (requestlist.getItems().isEmpty()) {
                requestButtons.setDisable(true);
                mainTabPane.getSelectionModel().selectFirst();
                requestTab.setDisable(true);
            } else {
                requester = requestlist.getSelectionModel().getSelectedItem();
            }
        });
    }

    @Override
    public void addChat(Friend friend) {
//        Platform.runLater(() -> {
//            userlist.getItems().add(friend);
//        });
    }

}
