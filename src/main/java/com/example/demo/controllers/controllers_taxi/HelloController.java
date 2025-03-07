package com.example.demo.controllers.controllers_taxi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button btnVehicule;

    @FXML
    private Button btnReservationTaxi;

    @FXML
    private AnchorPane view_pages;

    @FXML
    void switchForm(ActionEvent event) throws IOException {
        if(event.getSource()== btnVehicule){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/gestionVehicule.fxml"));
            view_pages.getChildren().removeAll();
            view_pages.getChildren().setAll(fxml);
        }else if(event.getSource()==btnReservationTaxi){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/gestionReservationTaxi.fxml"));
            view_pages.getChildren().removeAll();
            view_pages.getChildren().setAll(fxml);
        }
    }
}