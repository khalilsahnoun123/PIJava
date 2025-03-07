package com.example.demo.controllers.controllers_taxi;


import com.example.demo.models.models_taxi.Vehicule;
import com.example.demo.services.service_taxi.ServiceVehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class itemVehiculeController implements Initializable {

    @FXML
    private Button btnModifierVehicule;

    @FXML
    private Button btnSupprimerVehicule;

    @FXML
    private AnchorPane itemVehiculePane;

    @FXML
    private Label labelChauffeur;

    @FXML
    private Label labelImm;

    @FXML
    private Label labelMarque;

    @FXML
    private Label labelModele;

    @FXML
    private Label labelNumSerie;


    private static int id;

    public int getId(){
        return this.id;
    }

    Vehicule vec;
    public void setData (Vehicule vec){
        this.vec = vec;

        labelMarque.setText(vec.getMarque());
        labelModele.setText(vec.getModele());
        labelImm.setText(String.valueOf(vec.getImmatriculation()));
        labelNumSerie.setText(String.valueOf(vec.getNumero_de_serie()));
        labelChauffeur.setText(String.valueOf(vec.getId_chauffeur()));
        this.id=vec.getId();
    }

    public Vehicule getData (Vehicule vec){
        this.vec = vec;
        return this.vec;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void open_UpdateVehicule(ActionEvent event) throws IOException {
        Parent fxml= FXMLLoader.load(getClass().getResource("/ressource_taxi/updateVehicule.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Update Vehicule");
        stage.setScene(new Scene(fxml));
        stage.showAndWait();

    }

    @FXML
    void supprimerVehicule(ActionEvent event) throws SQLException {
        ServiceVehicule cs = new ServiceVehicule();

        // Afficher une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette Vehicule ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Récupérer l'ID de la Vehicule sélectionnée
            int id = this.vec.getId();

            // Supprimer la Vehicule de la base de données
            cs.supprimer(id);
        }

    }
}
