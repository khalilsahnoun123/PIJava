package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.Covoiturage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.example.demo.services.service_cov.ServiceCovoiturage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class listCovoiturageFrontController implements Initializable {

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
            ServiceCovoiturage sc = new ServiceCovoiturage();
            List<Covoiturage> covs = sc.Show();
            pag.setPageCount((int) Math.ceil(covs.size() / 3.0)); // Nombre total de pages nécessaire pour afficher toutes les cartes
            pag.setPageFactory(pageIndex -> {
                HBox hbox = new HBox();
                hbox.setSpacing(10);
                hbox.setAlignment(Pos.CENTER);
                int itemsPerPage = 3; // Nombre des sujets à afficher par page
                int page = pageIndex * itemsPerPage;
                for (int i = page; i < Math.min(page + itemsPerPage, covs.size()); i++) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        fxmlLoader.setLocation(getClass().getResource("ressource_cov/listCovoiturageFrontCard.fxml"));
                        AnchorPane anchorPane = fxmlLoader.load();
                        anchorPane.getStyleClass().add("ct");
                        listCovoiturageFrontCardController itemController = fxmlLoader.getController();
                        itemController.setData(covs.get(i));
                        hbox.getChildren().add(anchorPane);
                        HBox.setMargin(anchorPane, new Insets(10)); // Marges entre les cartes
                    } catch (IOException ex) {
                        Logger.getLogger(listCovoiturageFrontCardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return hbox;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openChatbot() {
        try {
            // Charge le fichier FXML de la fenêtre du Chatbot
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ressource_cov/chatbot-view.fxml"));
            Parent root = fxmlLoader.load();

            // Crée une nouvelle fenêtre pour afficher le Chatbot
            Stage stage = new Stage();
            stage.setTitle("Chatbot");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Gestion d'erreur si le fichier chatbot-view.fxml n'est pas trouvé
        }
    }
}
