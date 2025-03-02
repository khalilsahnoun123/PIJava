package com.example.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    final String url="jdbc:mysql://localhost:3306/cov";
    final String username="root";
    final String pwd="";
    private Connection conx;

    public static MyDatabase instance;


    public static MyDatabase getInstance(){
        if (instance == null)
            instance = new MyDatabase();
        return instance;

    }
    private MyDatabase(){

        try {
            conx = DriverManager.getConnection(url, username, pwd);
            System.out.println("Connexion établie");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }



    }

    public Connection getConx() {
        return conx;
    }
}
