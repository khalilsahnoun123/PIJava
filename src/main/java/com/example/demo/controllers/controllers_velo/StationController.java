package com.example.demo.controllers.controllers_velo;


import com.example.demo.models.models_velo.StationVelo;
import com.example.demo.models.models_velo.User;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import com.example.demo.services.services_velo.StationVeloService;
import com.example.demo.services.services_velo.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class StationController implements Initializable {

    @FXML private FlowPane cardsFlowPane;
    @FXML private TextField txtNom, txtGouvernera, txtMunicapilite, txtAdresse, txtIdAdmin;
    @FXML private Button addButton;
    @FXML private ImageView imagePreview;

    private ObservableList<StationVelo> stations = FXCollections.observableArrayList();
    private StationVelo selectedStationForEdit = null;
    private final StationVeloService stationService = new StationVeloService();
    private final UserService userService = new UserService();
    private String uploadedImagePath;

    // At the top of your controller class
    private static final Path IMAGE_DIR = Paths.get(
            "C:\\Users\\jihen\\Desktop\\javaFXProject\\javaFXProject-Velo\\images"
    );

    @FXML
    private AnchorPane mainAnchorPane;


    ReuseMethod reuseMethod = new ReuseMethod();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stations.addListener((ListChangeListener<StationVelo>) change -> {
            cardsFlowPane.getChildren().clear();
            stations.forEach(this::createStationCard);
        });
        refreshStations();
    }

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Station Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(imagePreview.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Create target directory if it doesn't exist
                if (!Files.exists(IMAGE_DIR)) {
                    Files.createDirectories(IMAGE_DIR);
                }

                // Generate unique filename
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = IMAGE_DIR.resolve(fileName);

                // Copy file to target directory
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Store absolute file path
                uploadedImagePath = targetPath.toString();
                System.out.println("Stored image path: " + uploadedImagePath);

                // Preview image directly from file system
                imagePreview.setImage(new Image(targetPath.toUri().toString()));

            } catch (IOException e) {
                showAlert("Upload Error", "Failed to save image: " + e.getMessage());
                // Fallback preview from original selection
                imagePreview.setImage(new Image(selectedFile.toURI().toString()));
                uploadedImagePath = selectedFile.getAbsolutePath();
            }
        }
    }

    private void createStationCard(StationVelo station) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefSize(250, 300);
        card.setUserData(station);

        ImageView imgView = new ImageView();
        try {
            String imagePath = station.getStationImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    imgView.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    loadFallbackImage(imgView);
                }
            }
        } catch (Exception e) {
            loadFallbackImage(imgView);
        }

        imgView.setFitWidth(200);
        imgView.setFitHeight(100);
        imgView.setPreserveRatio(true);

        Text name = new Text(station.getNameStation());
        Text gouvernorat = new Text("Gov: " + station.getGouvernera());
        Text municipalite = new Text("Muni: " + station.getMunicapilite());
        Text adresse = new Text(station.getAdresse());
        Text admin = new Text("Admin: " + station.getAdmin().getNom());

        HBox buttonBox = new HBox(10);
        Button editBtn = new Button("Modifier");
        Button deleteBtn = new Button("Supprimer");
        Button manageBtn = new Button("Vélos");

        editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        manageBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");


        editBtn.setOnAction(e -> populateFormForEdit(station));
        deleteBtn.setOnAction(e -> deleteStation(station));
        manageBtn.setOnAction(e -> handleManageVelos(station));


        buttonBox.getChildren().addAll(editBtn, manageBtn, deleteBtn);
        card.getChildren().addAll(imgView, name, gouvernorat, municipalite, adresse, admin, buttonBox);
        cardsFlowPane.getChildren().add(card);
    }
    private void populateFormForEdit(StationVelo station) {
        selectedStationForEdit = station;
        txtNom.setText(station.getNameStation());
        txtGouvernera.setText(station.getGouvernera());
        txtMunicapilite.setText(station.getMunicapilite());
        txtAdresse.setText(station.getAdresse());
        txtIdAdmin.setText(String.valueOf(station.getAdmin().getId_user()));

        // Load existing image
        if (station.getStationImage() != null) {
            try (InputStream is = getClass().getResourceAsStream(station.getStationImage())) {
                imagePreview.setImage(new Image(is));
                uploadedImagePath = station.getStationImage();
            } catch (Exception e) {
                loadFallbackImage(imagePreview);
            }
        }

        addButton.setText("Modifier");
    }
    private void loadFallbackImage(ImageView imgView) {
        try {
            // 1. Clear any existing image
            imgView.setImage(null);

            // 2. Load placeholder from resources
            InputStream is = getClass().getResourceAsStream("/com/example/fi_thnitek/image/station2.png");
            if (is != null) {
                imgView.setImage(new Image(is));
            } else {
                // 3. Show error text in the card
                Text errorText = new Text("Image Missing");
                errorText.setStyle("-fx-fill: #ff4444;");
                imgView.getParent().getChildrenUnmodifiable().add(errorText);
            }
        } catch (Exception e) {
            System.err.println("Failed to load fallback image: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddOrUpdateStation(ActionEvent event) {
        if (isFormValid()) {
            try {
                User admin = userService.readById(Integer.parseInt(txtIdAdmin.getText()));

                if (admin == null) {
                    showAlert("Error", "Admin not found with ID: " + txtIdAdmin.getText());
                    return;
                }

                StationVelo station = selectedStationForEdit != null ?
                        selectedStationForEdit : new StationVelo();

                station.setNameStation(txtNom.getText());
                station.setGouvernera(txtGouvernera.getText());
                station.setMunicapilite(txtMunicapilite.getText());
                station.setAdresse(txtAdresse.getText());
                station.setAdmin(admin);
                System.out.println("Storing image path: " + uploadedImagePath);
                if (uploadedImagePath != null) {
                    station.setStationImage(uploadedImagePath);
                }

                if (selectedStationForEdit == null) {
                    stationService.insert(station);
                } else {
                    stationService.update(station);
                }

                refreshStations();
                clearForm();
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Invalid Admin ID format");
            }
        }
    }

    private void deleteStation(StationVelo station) {
        stationService.delete(station);
        refreshStations();
    }

    public void refreshStations() {
        stations.setAll(stationService.readAll());
    }

    private boolean isFormValid() {
        return !txtNom.getText().isEmpty() &&
                !txtGouvernera.getText().isEmpty() &&
                !txtMunicapilite.getText().isEmpty() &&
                !txtAdresse.getText().isEmpty() &&
                !txtIdAdmin.getText().isEmpty();
    }

    private void clearForm() {
        txtNom.clear();
        txtGouvernera.clear();
        txtMunicapilite.clear();
        txtAdresse.clear();
        txtIdAdmin.clear();
        imagePreview.setImage(null);
        uploadedImagePath = null;
        selectedStationForEdit = null;
        addButton.setText("Ajouter");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleManageVelos(StationVelo station) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/VeloManagement.fxml"));
            AnchorPane veloPane = loader.load();

            VeloController controller = loader.getController();
            controller.setMainAnchorPane(this.mainAnchorPane); // Clé !
            controller.setSelectedStation(station);

            this.changeOnlyAnchorPane(veloPane); // Appel sans paramètre

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Version modifiée de votre méthode pour accepter un AnchorPane chargé
    public void changeOnlyAnchorPane(AnchorPane newPane) {
        // Supprimez le paramètre mainAnchorPane
        this.mainAnchorPane.getChildren().clear();
        this.mainAnchorPane.getChildren().add(newPane);


    }


    @FXML
    private void handleManageTypes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/VeloTypeManagement.fxml"));
            AnchorPane typePane = loader.load();

            mainAnchorPane.getChildren().clear();
            mainAnchorPane.getChildren().add(typePane);
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la gestion des types");
        }
    }
}