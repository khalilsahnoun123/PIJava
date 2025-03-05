package com.example.demo.controllers.controllers_cov;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button btnCovoiturage;

    @FXML
    private Button btnReservationCov;

    @FXML
    private AnchorPane view_pages;

    @FXML
    void switchForm(ActionEvent event) throws IOException {
        if(event.getSource()== btnCovoiturage){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_cov/gestionCovoiturage.fxml"));
            view_pages.getChildren().removeAll();
            view_pages.getChildren().setAll(fxml);
        }else if(event.getSource()==btnReservationCov){
            Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_cov/gestionReservationCov.fxml"));
            view_pages.getChildren().removeAll();
            view_pages.getChildren().setAll(fxml);
        }
    }

    public void openChatbot(ActionEvent actionEvent) {

    }
}