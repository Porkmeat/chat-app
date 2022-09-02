package com.mariano.chatapp.chatappserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Mariano
 */
public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private String login = null;
    private final Server server;
    private OutputStream outputStream;
    private final HashSet<String> topicSet = new HashSet<>();

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleCurrentConnections(clientSocket);
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLogin() {
        return login;
    }

    private void handleCurrentConnections(final Socket clientSocket) throws InterruptedException, IOException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        // read incoming commands
        while ((line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equalsIgnoreCase(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if ("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                } else {
                    String msg = "Unknown " + cmd + "\r\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        //clientSocket.close();
    }

    // send msg to users
    private void send(String msg) throws IOException {
        if (login != null) {
            outputStream.write(msg.getBytes());
        }
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String username = tokens[1];
            String password = tokens[2];

            // login check
            if (username.equals("guest") && password.equals("guest") || username.equals("jim") && password.equals("jim")) {
                List<ServerWorker> workerList = server.getWorkerList();
                for (ServerWorker worker : workerList) {
                    if (worker.getLogin() != null && worker.getLogin().equalsIgnoreCase(username)) {
                        String msg = "User already online\r\n";
                        outputStream.write(msg.getBytes());
                        return;
                    }
                }
                String msg = "ok login\r\n";
                System.out.println("User logged in successfully: " + username + "\r\n");
                outputStream.write(msg.getBytes());
                this.login = username;

                // get online user list
                for (ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        if (worker.getLogin() != null) {
                            String msg2 = "online " + worker.getLogin() + "\r\n";
                            send(msg2);
                        }
                    }
                }

                // send online status to other users
                String onlineMsg = "online " + login + "\r\n";
                for (ServerWorker worker : workerList) {
                    if (!worker.equals(this)) {
                        worker.send(onlineMsg);
                    }
                }

            } else {
                String msg = "Wrong username or password\r\n";
                outputStream.write(msg.getBytes());
            }
        } else {
            String msg = "Login error\r\n";
            outputStream.write(msg.getBytes());
        }
    }

    private void handleLogoff() throws IOException {

        if (server.userLogoff(this)) {
            List<ServerWorker> workerList = server.getWorkerList();
            System.out.println("User logged off successfully: " + login + "\r\n");
            String offlineMsg = "offline " + login + "\r\n";
            for (ServerWorker worker : workerList) {
                worker.send(offlineMsg);
            }
            clientSocket.close();
        }
    }

    private void handleMessage(String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String recipient = tokens[1];
            String message = tokens[2];
            boolean isTopic = recipient.charAt(0) == '#';

            List<ServerWorker> workerList = server.getWorkerList();

            // check if msg is for topic or user
            for (ServerWorker worker : workerList) {
                if (isTopic) {
                    if (worker.isMemberOfTopic(recipient) && !worker.getLogin().equals(login)) {
                        String outMsg = "msg " + recipient + ":" + login + " " + message + "\r\n";
                        worker.send(outMsg);
                    }
                } else if (recipient.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + message + "\r\n";
                    worker.send(outMsg);
                }
            }
        } else {
            send("Syntax error\r\n");
        }
    }

    public boolean isMemberOfTopic(String topic) {
        return topicSet.contains(topic);
    }

    private void handleJoin(String[] tokens) throws IOException {
        if (tokens.length == 2 && tokens[1].charAt(0) == '#') {
            String topic = tokens[1];
            if (!isMemberOfTopic(topic)) {
                topicSet.add(topic);
                send("Joined " + topic + "\r\n");
            } else {
                send("Already a member of " + topic + "\r\n");
            }
        } else {
            send("Syntax error\r\n");
        }
    }

    private void handleLeave(String[] tokens) throws IOException {
        if (tokens.length == 2 && tokens[1].charAt(0) == '#') {
            String topic = tokens[1];
            if (isMemberOfTopic(topic)) {
                topicSet.remove(topic);
                send("Left " + topic + "\r\n");
            } else {
                send("Not a member of " + topic + "\r\n");
            }
        } else {
            send("Syntax error\r\n");
        }
    }
}
