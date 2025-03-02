package com.example.demo.controllers.controllers_velo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardAdminController {

    public Button btnGestionTransportpublic;
    @FXML
    private Button btnHome;

    @FXML
    private Button btnReservationVelo;

    @FXML
    private AnchorPane view_pages;


    @FXML
    void switchForm(ActionEvent event) throws IOException {
        if(event.getSource()==btnReservationVelo){
            Parent fxml= FXMLLoader.load(getClass().getResource("/resource-Velo/StationAdminManagement.fxml"));
            view_pages.getChildren().removeAll();
            view_pages.getChildren().setAll(fxml);
        }else if(event.getSource()==btnGestionTransportpublic){
            Parent fxml= FXMLLoader.load(getClass().getResource("/Ressource-TP/Accueil-TP.fxml"));
            view_pages.getChildren().removeAll();
            view_pages.getChildren().setAll(fxml);
        }
    }

}
