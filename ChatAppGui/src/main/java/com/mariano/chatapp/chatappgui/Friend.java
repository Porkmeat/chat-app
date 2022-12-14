/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappgui;

import java.time.LocalDateTime;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Callback;

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
    private LocalDateTime timestamp;
    public final BooleanProperty isOnline = new SimpleBooleanProperty();

    public Friend(String username, String alias, boolean friendIsSender, int unseenChats, String lastMsg, LocalDateTime timestamp) {
        this.username = username;
        this.alias = alias;
        this.friendIsSender = friendIsSender;
        this.unseenChats = unseenChats;
        this.lastMsg = lastMsg;
        this.timestamp = timestamp;
        this.isOnline.set(false);

    }
    
    public static Callback<Friend, Observable[]> extractor() {
        return (Friend param) -> new Observable[]{param.isOnline};
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIsOnline() {
        return this.isOnline.get();
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline.set(isOnline);
    }
    
    

}
