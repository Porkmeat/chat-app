/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappgui;

/**
 *
 * @author Mariano
 */
public class Friend {
    private String username;
    private String profilePicture;
    private String lastMsg;
    private int timestamp;

    public Friend(String username, String profilePicture, String lastMsg, int timestamp) {
        this.username = username;
        this.profilePicture = profilePicture;
        this.lastMsg = lastMsg;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
