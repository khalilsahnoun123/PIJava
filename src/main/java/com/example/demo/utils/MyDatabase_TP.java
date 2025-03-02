package com.example.demo.utils;


import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class MyDatabase_TP {
    private static MyDatabase_TP instance;
    private final String URL ="jdbc:mysql://127.0.0.1:3306/cov";
    private final String USERNAME ="root";
    private final String PASSWORD = "";
    @Getter
    private Connection cnx ;

    private MyDatabase_TP() {
        try {
            cnx = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            System.out.println("connected ...");
        } catch ( SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static MyDatabase_TP getInstance() {
        if (instance == null)
            instance = new MyDatabase_TP();
        return instance;
    }

}