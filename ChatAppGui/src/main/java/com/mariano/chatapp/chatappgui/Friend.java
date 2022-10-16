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
    private String alias;
    private boolean friendIsSender;
    private int unseenChats;
//    private String profilePicture;
    private String lastMsg;
    private String timestamp;

    public Friend(String username, String alias, boolean friendIsSender, int unseenChats, String lastMsg, String timestamp) {
        this.username = username;
        this.alias = alias;
        this.friendIsSender = friendIsSender;
        this.unseenChats = unseenChats;
        this.lastMsg = lastMsg;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isFriendIsSender() {
        return friendIsSender;
    }

    public void setFriendIsSender(boolean friendIsSender) {
        this.friendIsSender = friendIsSender;
    }

    public int getUnseenChats() {
        return unseenChats;
    }

    public void setUnseenChats(int unseenChats) {
        this.unseenChats = unseenChats;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    

}
