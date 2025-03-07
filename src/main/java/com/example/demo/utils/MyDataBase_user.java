package com.example.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyDataBase_user {

    private static MyDataBase_user instance;


    private static final String URL = "jdbc:mysql://localhost:3306/yosra_javafx";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection cnx;

    private MyDataBase_user() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected!");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public static MyDataBase_user getInstance() {
        if (instance == null) {
            instance = new MyDataBase_user();
        }
        return instance;
    }

    public Connection getConnection() {
        return cnx;
    }


    public void closeConnection() {
        try {
            cnx.close();
            System.out.println("Connection closed!");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
