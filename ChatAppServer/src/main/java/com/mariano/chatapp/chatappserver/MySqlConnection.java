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

public class MySqlConnection {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public boolean isUsernameAvailable(String username) throws Exception {
        try {
            System.out.println("connecting");

            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT COUNT(1) FROM user WHERE user_login = ?;");
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt(1) == 0;

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
    
    public boolean checkPassword(String username, String password) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT user_password FROM user WHERE user_login = ?;");
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();
            
            results.next();
            String userpass = results.getString(1);
            
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

            ResultSet results = preparedStatement.executeQuery();
            
            results.next();
            return results.getInt(1);
            
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }
    }

    private void connect() throws Exception {
        // MySQL driver
        System.out.println("try to connect");
        connect = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/chatapp_schema", "javatest", "Java1test2");
        System.out.println("connected");
    }
    // You need to close the resultSet
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

    public int getUserId(String username) throws Exception {
        try {
            connect();
            preparedStatement = connect
                    .prepareStatement("SELECT user_id FROM user WHERE user_login = ?;");
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();
            
            results.next();
            return results.getInt(1);
            
            
        } catch (Exception ex) {
            throw ex;
        } finally {
            close();
        }   
    }
}
