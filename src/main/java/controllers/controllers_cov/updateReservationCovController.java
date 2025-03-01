package controllers.controllers_cov;

import models.models_cov.Covoiturage;
import models.models_cov.ReservationCov;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import services.service_cov.ServiceCovoiturage;
import services.service_cov.ServiceReservationCov;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class updateReservationCovController implements Initializable {
    @FXML
    private Button btnClearReservationCov;

    @FXML
    private Button btnUpdateReservationCov;

    @FXML
    private ComboBox<String> txtCovoiturage;

    @FXML
    private TextField txtNbrPlace;

    @FXML
    private ComboBox<String> txtStatusResCov;

    @FXML
    private AnchorPane updateReservationCovPane;

    ReservationCov rescov;

    ServiceCovoiturage serviceCovoiturage = new ServiceCovoiturage();
    List<Covoiturage> covoiturageList = serviceCovoiturage.Show();
    private int covId=-1;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ressource_cov/itemReservationCov.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            HBox hBox = (HBox) anchorPane.getChildren().get(0);
            itemReservationCovController item = fxmlLoader.getController();
            ServiceReservationCov crs = new ServiceReservationCov();

            rescov = crs.getById(item.getId());
            txtStatusResCov.setValue(rescov.getStatus());
            txtCovoiturage.setValue(String.valueOf(rescov.getId_cov()));
            txtNbrPlace.setText(String.valueOf(rescov.getNbr_place()));

        } catch (IOException ex) {
            Logger.getLogger(itemReservationCovController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> valuesMap = new HashMap<>();
        for(Covoiturage cov : covoiturageList){
            txtCovoiturage.getItems().add(cov.getPoint_de_depart());
            valuesMap.put(cov.getPoint_de_depart(),cov.getId());
        }

        txtCovoiturage.setOnAction(event ->{
            String SelectedOption = null;
            SelectedOption = txtCovoiturage.getValue();
            int SelectedValue = 0;
            SelectedValue = valuesMap.get(SelectedOption);
            covId = SelectedValue;
        });
    }

    @FXML
    void UpdateReservationCov(ActionEvent event) {
        if (txtStatusResCov.getValue().isEmpty() || txtNbrPlace.getText().isEmpty() || covId == -1)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez remplir tous les détails concernant votre Reservation Covoiturage.");
            Optional<ButtonType> option = alert.showAndWait();

        } else {
            modifReservationCov();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié avec succès");
            alert.setHeaderText(null);
            alert.setContentText("Votre Reservation Covoiturage a été modifié avec succès.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    @FXML
    void clearFieldsReservationCov(ActionEvent event) {
        txtCovoiturage.getEditor().clear();
        txtStatusResCov.getEditor().clear();
        txtNbrPlace.clear();
    }

    void modifReservationCov(){
        int id_user = rescov.getId_user();
        String status = txtStatusResCov.getValue();
        int nbr_place = Integer.parseInt(txtNbrPlace.getText());
        int id_cov = covId;


        ReservationCov resCov = new ReservationCov(
                rescov.getId(),
                id_user, id_cov, status, nbr_place);
        ServiceReservationCov crs = new ServiceReservationCov();
        crs.modifier(resCov);
    }


}
