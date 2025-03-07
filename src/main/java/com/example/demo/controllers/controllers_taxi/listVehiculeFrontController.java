package com.example.demo.controllers.controllers_taxi;


import com.example.demo.models.models_taxi.Vehicule;
import com.example.demo.services.service_taxi.ServiceVehicule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class listVehiculeFrontController implements Initializable {

    @FXML
    private ImageView background;

    @FXML
    private GridPane grid;

    @FXML
    private HBox hbox;

    @FXML
    private AnchorPane listVoitureFront;

    @FXML
    private Pagination pag;

    @FXML
    private HBox vbox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ServiceVehicule sv = new ServiceVehicule();
            List<Vehicule> vecs = sv.Show();
            pag.setPageCount((int) Math.ceil(vecs.size() / 3.0)); // Nombre total de pages nécessaire pour afficher toutes les cartes
            pag.setPageFactory(pageIndex -> {
                HBox hbox = new HBox();
                hbox.setSpacing(10);
                hbox.setAlignment(Pos.CENTER);
                int itemsPerPage = 3; // Nombre des sujets à afficher par page
                int page = pageIndex * itemsPerPage;
                for (int i = page; i < Math.min(page + itemsPerPage, vecs.size()); i++) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("/ressource_taxi/listVehiculeFrontCard.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        anchorPane.getStyleClass().add("ct");
                        listVehiculeFrontCardController itemController = fxmlLoader.getController();
                        itemController.setData(vecs.get(i));
                        hbox.getChildren().add(anchorPane);
                        HBox.setMargin(anchorPane, new Insets(10)); // Marges entre les cartes
                    } catch (IOException ex) {
                        Logger.getLogger(listVehiculeFrontCardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return hbox;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
