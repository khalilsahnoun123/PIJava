package com.example.demo.controllers.controllers_velo;

import com.example.demo.models.models_velo.Velo;
import com.example.demo.models.models_velo.VeloType;
import com.example.demo.models.models_velo.StationVelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.example.demo.services.services_velo.VeloService;
import com.example.demo.services.services_velo.VeloTypeService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

public class VeloController implements Initializable {

    @FXML
    private FlowPane veloCardsContainer;
    @FXML
    private ComboBox<VeloType> typeCombo;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ImageView veloImagePreview;
    @FXML
    private Button addButton;
    private ObservableList<VeloType> types = FXCollections.observableArrayList();
    private final VeloService veloService = new VeloService();
    private final VeloTypeService typeService = new VeloTypeService();
    private StationVelo currentStation;
    private String uploadedImagePath;
    private Velo selectedVeloForEdit;
    private static final Path VELO_IMAGE_DIR = Paths.get(
            "C:\\Projet Integration 3em\\buckup files\\PIJava\\images"
    );

    @FXML
    private AnchorPane mainAnchorPane;

    public VeloController() {
        System.out.println("VeloController initialisé");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Méthode initialize() appelée");
        configureTypeComboBox();
        // setupInputRestrictions();
    }

    private void configureTypeComboBox() {
        typeCombo.setCellFactory(param -> new ListCell<VeloType>() {
            @Override
            protected void updateItem(VeloType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTypeName());
            }
        });

        typeCombo.setButtonCell(new ListCell<VeloType>() {
            @Override
            protected void updateItem(VeloType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Choisir un type" : item.getTypeName());
            }
        });

        typeCombo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateTypeDetails(newVal));
    }

    public void setSelectedStation(StationVelo station) {
        this.currentStation = station;
        loadTypeData();
        refreshCards();
    }

    private void loadTypeData() {
        types.setAll(typeService.readAll());
        typeCombo.setItems(types);
    }

    private void updateTypeDetails(VeloType type) {
        if (type != null) {
            // Mettre à jour les champs texte
            descriptionField.setText(type.getDescription());
            priceField.setText(String.format("%.2f ", type.getPrice()));
            //formatPriceField();

            // Charger l'image depuis le type de vélo
            String imagePath = type.getVeloImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        veloImagePreview.setImage(new Image(imageFile.toURI().toString()));
                        uploadedImagePath = imagePath; // Stocker le chemin par défaut
                    } else {
                        loadFallbackImage(veloImagePreview);
                        uploadedImagePath = null;
                    }
                } catch (Exception e) {
                    loadFallbackImage(veloImagePreview);
                    uploadedImagePath = null;
                }
            } else {
                loadFallbackImage(veloImagePreview);
                uploadedImagePath = null;
            }
        }
    }

    private void refreshCards() {
        veloCardsContainer.getChildren().clear();
        List<Velo> velos = veloService.getByStationId(currentStation.getIdStation());
        velos.forEach(velo -> veloCardsContainer.getChildren().add(createVeloCard(velo)));
    }

    private VBox createVeloCard(Velo velo) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(200);
        card.setMaxWidth(200);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-radius: 5;");
        // Image
        ImageView imgView = new ImageView();
        try {
            if (velo.getType().getVeloImage() != null) {
                File imageFile = new File(velo.getType().getVeloImage());
                if (imageFile.exists()) {
                    imgView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    loadFallbackImage(imgView);
                }
            }
        } catch (Exception e) {
            loadFallbackImage(imgView);
        }
        imgView.setFitWidth(150);
        imgView.setFitHeight(100);
        imgView.setPreserveRatio(true);

        // Details
        Label typeLabel = new Label(velo.getType().getTypeName());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label priceLabel = new Label(String.format("Prix: %.2f DT/h", velo.getType().getPrice()));
        Label statusLabel = new Label(velo.isDispo() ? "Disponible" : "Indisponible");
        statusLabel.setStyle(velo.isDispo() ?
                "-fx-text-fill: #4CAF50; -fx-font-weight: bold;" :
                "-fx-text-fill: #f44336; -fx-font-weight: bold;");

        // Buttons
        HBox buttonBox = new HBox(5);  // Réduire l'espacement
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        Button editBtn = new Button("Modifier");
        Button deleteBtn = new Button("Supprimer");
        editBtn.setPrefWidth(85);
        deleteBtn.setPrefWidth(85);
        editBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        editBtn.setOnAction(e -> populateFormForEdit(velo));
        deleteBtn.setOnAction(e -> handleDeleteVelo(velo));

        buttonBox.getChildren().addAll(editBtn, deleteBtn);
        card.getChildren().addAll(imgView, typeLabel, priceLabel, statusLabel, buttonBox);

        return card;
    }

    private void populateFormForEdit(Velo velo) {
        selectedVeloForEdit = velo;
        typeCombo.getSelectionModel().select(velo.getType());
        try {
            if (velo.getType().getVeloImage() != null) {
                File imageFile = new File(velo.getType().getVeloImage());
                veloImagePreview.setImage(new Image(imageFile.toURI().toString()));
                uploadedImagePath = velo.getType().getVeloImage();
            }
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image: " + e.getMessage());
        }
        addButton.setText("Modifier");
    }

    @FXML
    private void handleAddVelo() {
        if (!isFormValid()) return;

        VeloType selectedType = typeCombo.getSelectionModel().getSelectedItem();
        Velo velo = selectedVeloForEdit != null ? selectedVeloForEdit : new Velo();

        velo.setStation(currentStation);
        velo.setType(selectedType); // L'image est déjà dans le type !
        velo.setDispo(true);

        // Supprimer la ligne setVeloImage()
        // velo.setVeloImage(finalImagePath); // <-- À SUPPRIMER

        if (selectedVeloForEdit == null) {
            veloService.insert(velo);
        } else {
            veloService.update(velo);
        }

        clearForm();
        refreshCards();
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(veloImagePreview.getScene().getWindow());
        if (file != null) {
            try {
                Files.createDirectories(VELO_IMAGE_DIR);

                String fileName = System.currentTimeMillis() + "_" + file.getName();
                Path targetPath = VELO_IMAGE_DIR.resolve(fileName);
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                uploadedImagePath = targetPath.toString();
                veloImagePreview.setImage(new Image(targetPath.toUri().toString()));
            } catch (IOException e) {
                showAlert("Erreur", "Échec du téléchargement de l'image");
            }
        }
    }

    private void handleDeleteVelo(Velo velo) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer ce vélo?");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer définitivement ce vélo?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            veloService.delete(velo);
            refreshCards();
        }
    }

    private boolean isFormValid() {
        // Vérification du type
        if (typeCombo.getSelectionModel().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un type de vélo");
            return false;
        }

        // Validation du prix
        try {
            Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Prix invalide. Veuillez entrer un nombre valide.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        typeCombo.getSelectionModel().clearSelection();
        descriptionField.clear();
        priceField.clear();
        veloImagePreview.setImage(null);
        uploadedImagePath = null;
        selectedVeloForEdit = null;
        addButton.setText("Ajouter");
    }

    private void loadFallbackImage(ImageView imgView) {
        try {
            InputStream is = getClass().getResourceAsStream("/images/velo_placeholder.png");
            imgView.setImage(new Image(is));
        } catch (Exception e) {
            imgView.setImage(null);
            System.err.println("Échec du chargement de l'image de remplacement");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // Ajoutez cette méthode dans la classe

    public void setMainAnchorPane(AnchorPane mainAnchorPane) {
        this.mainAnchorPane = mainAnchorPane;
    }

    @FXML
    private void handleReturnToStations() {
        try {
            // Charger la vue des stations
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/StationAdminManagement.fxml"));
            AnchorPane stationPane = loader.load();

            // Accéder au contrôleur parent
            StationController stationController = loader.getController();

            // Rafraîchir les données
            stationController.refreshStations();

            // Remplacer le contenu
            mainAnchorPane.getChildren().clear();
            mainAnchorPane.getChildren().add(stationPane);

        } catch (IOException e) {
            showAlert("Erreur", "Impossible de revenir aux stations : " + e.getMessage());
        }
    }


    private void setupInputRestrictions() {
        // Limite de 200 caractères pour la description
        descriptionField.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 200 ? change : null
        ));



        TextFormatter<Double> priceFormatter = new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (!change.getControlNewText().matches("^\\d*\\.?\\d{0,2}$")) {
                    return null;
                }
            }
            return change;
        });
        priceField.setTextFormatter(priceFormatter);

    }

    private void formatPriceField() {
        try {
            String rawText = priceField.getText().replaceAll("[^\\d.]", "");
            double price = Double.parseDouble(rawText);
            priceField.setText(String.format("%.2f", price));
        } catch (NumberFormatException e) {
            priceField.setText("0.00");
        }
    }
}
