/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mariano.chatapp.chatappserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySqlConnection {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public int getUserId(String username) throws Exception {
        try {
            System.out.println("connecting");

            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT user_id FROM user WHERE user_login = ?;");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }

        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }

    public void addNewUser(String username, String password, int salt) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("INSERT INTO user (user_login,user_password,salt) VALUES (?,?,?);");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, salt);

            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }

    }
    
    public void addFriend(int userId,String username,int friendId, String friendName) throws Exception {
        try {
            connect();
            // generate unique id for friends chat
            long chatUuid = (long)Math.max(userId, friendId) << 32 + Math.min(userId, friendId);
            
            preparedStatement = connect
                    .prepareStatement("INSERT INTO user_contacts (contact_user_id,contact_friend_id,contact_alias,contact_status,chat_uuid) "
                            + "VALUES (?,?,?,1,"+chatUuid+"),"
                            + "(?,?,?,2,"+chatUuid+");");
            
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, friendId);
            preparedStatement.setString(3, friendName);
            preparedStatement.setInt(4, friendId);
            preparedStatement.setInt(5, userId);
            preparedStatement.setString(6, username);

            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }

    }
    
    public boolean checkPassword(String username, String password) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT user_password FROM user WHERE user_login = ?;");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            
            resultSet.next();
            String userpass = resultSet.getString(1);
            
            return userpass.equals(password);
            
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }
    
    public int getSalt(String username) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT salt FROM user WHERE user_login = ?;");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();
            
            resultSet.next();
            return resultSet.getInt(1);
            
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }
    
    public ResultSet fetchFriends(int userid) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT uc.contact_friend_id, uc.contact_alias,"
                            + "uc.contact_status, chat_uuid, c.chat_user_sender,"
                            + "c.last_message, c.last_message_time, c.last_message_seen,"
                            + "c.unseen_chats FROM user_contacts uc LEFT JOIN chat c USING (chat_uuid) WHERE uc.contact_user_id = ?;");
            preparedStatement.setInt(1, userid);

            resultSet = preparedStatement.executeQuery();
            
            
            return resultSet;
            
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }

    private void connect() throws Exception {
        System.out.println("try to connect");
        connect = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/chatapp_schema", "javatest", "Java1test2");
        System.out.println("connected");
    }
    
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (SQLException e) {

        }
    }

    public void saveMsg(int userid, int recipientid, String message) throws Exception {
        try {
            connect();
            // generate unique id for friends chat
            long chatUuid = (long)Math.max(userid, recipientid) << 32 + Math.min(userid, recipientid);
            
            preparedStatement = connect
                    .prepareStatement("INSERT INTO message (message_datetime,message_text,chat_uuid,message_user_id,message_seen) "
                            + "VALUES (NOW(),?,"+chatUuid+",?,0);");
            
            preparedStatement.setString(1, message);
            preparedStatement.setInt(2, userid);

            preparedStatement.executeUpdate();
            
            preparedStatement = connect
                    .prepareStatement("UPDATE chat SET chat_user_sender = ?, last_message = ?, last_message_time = NOW(), last_message_seen = 0 WHERE chat_uuid = "+chatUuid+";");
            
            preparedStatement.setInt(1, userid);
            preparedStatement.setString(2, message);

            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }
    
    public ResultSet fetchMessages(int chatid) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT salt FROM user WHERE user_login = ?;");
            preparedStatement.setInt(1, chatid);

            resultSet = preparedStatement.executeQuery();
            
            
            return resultSet;
            
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }

    public ArrayList<String> getRequests(int userid) throws Exception {
        ArrayList<String> requests = new ArrayList<>();
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT contact_alias FROM user_contacts WHERE contact_user_id = ? AND contact_status = 2 ORDER BY contact_alias;");
            preparedStatement.setInt(1, userid);

            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                requests.add(resultSet.getString(1));
            }
            
            return requests;
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }
}
