/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mariano.chatapp.chatappserver;

/**
 *
 * @author Mariano
 */
public class ChatAppServer {

    public static void main(String[] args) {
        int port = 8818;
        Server server = new Server(port);
        
        server.start();
    }
}
