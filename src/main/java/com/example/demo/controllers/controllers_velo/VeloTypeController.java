package com.example.demo.controllers.controllers_velo;

import com.example.demo.models.models_velo.VeloType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.example.demo.services.services_velo.VeloTypeService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class VeloTypeController implements Initializable {

    @FXML
    private FlowPane typesContainer;
    @FXML private ImageView typeImagePreview;
    @FXML private TextField typeNameField;
    @FXML private TextArea typeDescriptionField;
    @FXML private TextField typePriceField;
    @FXML
    private AnchorPane mainAnchorPane;
    private VeloType selectedType;
    private String uploadedImagePath;
    private final VeloTypeService typeService = new VeloTypeService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTypes();
        //setupInputValidation();
    }

    private void setupInputValidation() {
        // Validation numérique pour le prix
        typePriceField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));
    }

    private void loadTypes() {
        typesContainer.getChildren().clear();
        typeService.readAll().forEach(this::createTypeCard);
    }

    private void createTypeCard(VeloType type) {
        VBox card = new VBox(8);
        card.setPrefWidth(290); // (610px width) / 2 colonnes - 15px hgap
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #ddd;");

        ImageView img = new ImageView(new Image(new File(type.getVeloImage()).toURI().toString()));
        img.setFitWidth(270);
        img.setFitHeight(120);

        Label nameLabel = new Label(type.getTypeName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Label priceLabel = new Label(String.format("%.2f DT/h", type.getPrice()));
        Label descLabel = new Label(type.getDescription());
        descLabel.setWrapText(true);

        HBox buttons = new HBox(10);
        Button editBtn = new Button("Modifier");
        editBtn.setPrefWidth(130);
        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setPrefWidth(130);

        editBtn.setStyle("-fx-background-color: #FFC107;");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        editBtn.setOnAction(e -> populateForm(type));
        deleteBtn.setOnAction(e -> deleteType(type));

        buttons.getChildren().addAll(editBtn, deleteBtn);
        card.getChildren().addAll(img, nameLabel, priceLabel, descLabel, buttons);
        typesContainer.getChildren().add(card);
    }

    private void populateForm(VeloType type) {
        selectedType = type;
        typeNameField.setText(type.getTypeName());
        typeDescriptionField.setText(type.getDescription());
        typePriceField.setText(String.format("%.2f", type.getPrice()));
        typeImagePreview.setImage(new Image(new File(type.getVeloImage()).toURI().toString()));
    }

    @FXML
    private void handleSaveType() {
        if (!validateForm()) return;

        VeloType type = selectedType != null ? selectedType : new VeloType();
        type.setTypeName(typeNameField.getText());
        type.setDescription(typeDescriptionField.getText());
        type.setPrice(Double.parseDouble(typePriceField.getText()));
        type.setVeloImage(uploadedImagePath);

        if (selectedType == null) {
            typeService.insert(type);
        } else {
            typeService.update(type);
        }

        clearForm();
        loadTypes();
    }

    private boolean validateForm() {
        if (typeNameField.getText().isEmpty() ||
                typePriceField.getText().isEmpty() ||
                uploadedImagePath == null) {

            showAlert("Erreur", "Tous les champs sont obligatoires");
            return false;
        }
        return true;
    }

    @FXML
    private void clearForm() {
        selectedType = null;
        typeNameField.clear();
        typeDescriptionField.clear();
        typePriceField.clear();
        typeImagePreview.setImage(null);
        uploadedImagePath = null;
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(typeImagePreview.getScene().getWindow());
        if (file != null) {
            try {
                Path targetDir = Paths.get("C:\\Users\\jihen\\Desktop\\PIJava\\src\\main\\resources\\resource-Velo\\img\\image_velo");
                Files.createDirectories(targetDir);

                String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "_" + file.getName();
                Path targetPath = targetDir.resolve(fileName);
                Files.copy(file.toPath(), targetPath);

                uploadedImagePath = targetPath.toString();
                typeImagePreview.setImage(new Image(targetPath.toUri().toString()));
            } catch (IOException e) {
                showAlert("Erreur", "Échec de l'upload de l'image");
            }
        }
    }

    private void deleteType(VeloType type) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("Confirmer la suppression");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer ce type ?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            typeService.delete(type);
            loadTypes();
        }
    }

    @FXML
    private void handleReturnToStations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/StationAdminManagement.fxml"));
            AnchorPane stationPane = loader.load();

            mainAnchorPane.getChildren().clear();
            mainAnchorPane.getChildren().add(stationPane);
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de revenir aux stations");
        }
    }

    private void showAlert(String title, String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
