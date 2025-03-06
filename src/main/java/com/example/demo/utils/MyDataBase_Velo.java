package com.example.demo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase_Velo {
    final String url="jdbc:mysql://localhost:3306/cov";
    final String username="root";
    final String pwd="";
    private Connection conx;

    public static MyDataBase_Velo instance;


    public static MyDataBase_Velo getInstance(){
        if (instance == null)
            instance = new MyDataBase_Velo();
        return instance;

    }
    private MyDataBase_Velo(){

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