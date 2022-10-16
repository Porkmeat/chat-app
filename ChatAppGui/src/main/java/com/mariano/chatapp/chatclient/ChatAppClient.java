/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mariano.chatapp.chatclient;

import com.mariano.chatapp.chatappgui.Friend;
import com.mariano.chatapp.chatappgui.MainScreenController;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/**
 *
 * @author Mariano
 */
public class ChatAppClient {

    private final int port;
    private final String serverName;
    private Socket socket;
    private OutputStream serverOut;
    private InputStream serverIn;
    private BufferedReader reader;
    private final ArrayList<StatusListener> statusListeners = new ArrayList<>();
    private final ArrayList<MessageListener> messageListeners = new ArrayList<>();
    private final ArrayList<RequestListener> requestListeners = new ArrayList<>();
    private final ArrayList<FriendListener> friendListeners = new ArrayList<>();

    public ChatAppClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    public static void main(String[] args) throws IOException {

        ChatAppClient client = new ChatAppClient("localhost", 8818);

//        client.addStatusListener(new StatusListener() {
//            @Override
//            public void online(String username) {
//                System.out.println("ONLINE: " + username);
//            }
//
//            @Override
//            public void offline(String username) {
//                System.out.println("OFFLINE: " + username);
//            }
//        });
//
//        // functional interface Lambda
//        client.addMessageListener((String fromUser, String message) -> {
//            System.out.println("You got a message from " + fromUser + ": " + message);
//        });
        // try to connect
        if (!client.connect()) {
            System.err.println("Connection failed!");
        } else {
            System.out.println("Connection successful!");
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, port);
            System.out.println("Client port is " + socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean login(String username, String password) throws IOException {
        String cmd = "login " + username + " " + password + "\r\n";
        serverOut.write(cmd.getBytes());
        String response = reader.readLine();

        //start listening for server messages  
        if (response.equals("ok login")) {
            startServerListener();
            return true;
        } else {
            return false;
        }
    }

    public void requestOnlineUsers() throws IOException {
        String cmd = "getusers\r\n";
        serverOut.write(cmd.getBytes());
    }

    public void logoff() throws IOException {
        String cmd = "logoff\r\n";
        serverOut.write(cmd.getBytes());
    }

    public void addStatusListener(StatusListener listener) {
        statusListeners.add(listener);
    }

    public void removeStatusListener(StatusListener listener) {
        statusListeners.remove(listener);
    }

    public void addRequestListener(RequestListener listener) {
        requestListeners.add(listener);
    }

    public void removeRequestListener(RequestListener listener) {
        requestListeners.remove(listener);
    }

    private void startServerListener() {
        Thread t = new Thread() {
            @Override
            public void run() {
                serverListenLoop();
            }
        };
        t.start();
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }
    
    public void addFriendListener(FriendListener listener) {
        friendListeners.add(listener);
    }

    public void removeFriendListener(FriendListener listener) {
        friendListeners.remove(listener);
    }

    private void serverListenLoop() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        handleOnline(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        handleOffline(tokens);
                    } else if ("request".equalsIgnoreCase(cmd)) {
                        handleRequest(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokensMsg);
                    } else if ("friend".equalsIgnoreCase(cmd)) {
                        String[] tokensMsg = StringUtils.split(line, null, 2);
                        handleFriend(tokensMsg[1]);
                    } else {
                        System.out.println("command unknown");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(ChatAppClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void handleOnline(String[] tokens) {
        String username = tokens[1];
        for (StatusListener listener : statusListeners) {
            listener.online(username);
        }
    }

    private void handleOffline(String[] tokens) {
        String username = tokens[1];
        for (StatusListener listener : statusListeners) {
            listener.offline(username);
        }
    }

    public void msg(String recipient, String message) throws IOException {
        String cmd = "msg " + recipient + " " + message + "\r\n";
        serverOut.write(cmd.getBytes());
    }

    public boolean createUser(String username, String password) throws IOException {
        String cmd = "newuser " + username + " " + password + "\r\n";
        serverOut.write(cmd.getBytes());
        String response = reader.readLine();

        return response.equals("account created");
    }

    private void handleMessage(String[] tokensMsg) {
        String fromUser = tokensMsg[1];
        String message = tokensMsg[2];
        for (MessageListener listener : messageListeners) {
            listener.messageGet(fromUser, message);
        }
    }

    public void addFriend(String friendname) throws IOException {
        String cmd = "addfriend " + friendname + "\r\n";
        serverOut.write(cmd.getBytes());
    }

    private void handleRequest(String[] tokens) {
        String username = tokens[1];
        for (RequestListener listener : requestListeners) {
            listener.request(username);
        }
    }

    public void fetchFriends() throws IOException {
        String cmd = "getfriends\r\n";
        serverOut.write(cmd.getBytes());
    }

    public void fetchRequests() throws IOException {
        String cmd = "getrequests\r\n";
        serverOut.write(cmd.getBytes());
    }

    public void respondToRequest(String requester, int response) throws IOException {
        switch (response) {
            case 1: {
                System.out.println("top kek " + requester);
                String cmd = "acceptrequest " + requester + "\r\n";
                serverOut.write(cmd.getBytes());
                break;
            }
            case 2: {
                System.out.println("no kek " + requester);
                String cmd = "denyrequest " + requester + "\r\n";
                serverOut.write(cmd.getBytes());
                break;
            }
            case 3: {
                System.out.println("blocked " + requester);
                String cmd = "blockrequest " + requester + "\r\n";
                serverOut.write(cmd.getBytes());
                break;
            }
            default:
                break;
        }
    }

    private void handleFriend(String string) {
        JSONObject jsonobject = new JSONObject(string);
        System.out.println(jsonobject.toString());
        Friend friend = new Friend(jsonobject.getString("user_login"), jsonobject.getString("contact_alias"),
                jsonobject.getBoolean("friend_is_sender"), jsonobject.getInt("unseen_chats"),
                jsonobject.getString("last_message"), jsonobject.getString("last_message_time"));

    }

    
}
