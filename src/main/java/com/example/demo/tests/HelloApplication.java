package com.example.demo.tests;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static HostServices appHostServices;

    public static HostServices getAppHostServices() {
        return appHostServices;
    }
    @Override
    public void start(Stage stage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));//Dashboard covoitureur
       //Parent root = FXMLLoader.load(getClass().getResource("/resource-Velo/ReserverVelo.fxml"));//Dashboard USER
       Parent root = FXMLLoader.load(getClass().getResource("/resource-Velo/DashboardAdmin.fxml"));//Dashboard Admin

        stage.setTitle("Wasalni | 2025");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}