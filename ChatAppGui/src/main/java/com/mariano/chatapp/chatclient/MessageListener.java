/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mariano.chatapp.chatclient;

/**
 *
 * @author Mariano
 */
public interface MessageListener {
    public void messageGet (String fromUser, String message);
    public void loadMessages (String friendLogin, String message);
}
