package controllers.controllers_taxi;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


import models.models_taxi.ReservationTaxi;
import models.models_taxi.Vehicule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import services.service_taxi.ServiceReservationTaxi;
import services.service_taxi.ServiceVehicule;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class listVehiculeFrontCardController implements Initializable {

    @FXML
    private Label labelImm;

    @FXML
    private Label labelMarque;

    @FXML
    private Label labelModele;

    @FXML
    private Label labelNumSerie;

    @FXML
    private ImageView qrCodeImg;



    Vehicule vec;
    private static int idVec;

    public int getIdVec(){
        return this.idVec;
    }
    public void setData (Vehicule vec){
        this.vec = vec;
        labelMarque.setText("Marque : "+vec.getMarque());
        labelModele.setText("Modele : "+vec.getModele());
        labelImm.setText(String.valueOf(vec.getImmatriculation()));
        labelNumSerie.setText(String.valueOf(vec.getNumero_de_serie()));
        this.idVec=vec.getId();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void open_ajouterReservation(ActionEvent event) {
        // From Formulaire
        int id_user = 1;
        int id_vec = this.getIdVec();
        String status = "En Attente";


        ReservationTaxi res_taxi = new ReservationTaxi(
                id_user, status, id_vec);
        ServiceReservationTaxi rts = new ServiceReservationTaxi();
        rts.ajouter(res_taxi);


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ajouté avec succès");
        alert.setHeaderText(null);
        alert.setContentText("Votre Reservation a été ajoutée avec succès.");
        Optional<ButtonType> option = alert.showAndWait();
    }

    @FXML
    void generateQrCode(ActionEvent event) {
        ServiceVehicule sv = new ServiceVehicule(); // Replace with service class
        Vehicule vec = null;
        try {
            vec = sv.getById(this.vec.getId());
            if (vec != null) {
                String qrText = "Marque : "+vec.getMarque() + " - Modele : " + vec.getModele(); // Data for QR code
                String filePath = "qrcode_"+vec.getId()+".png"; // File path for QR image

                File qrFile = generateQRCode(qrText, 200, 200, filePath);

                if (qrFile != null && qrFile.exists()) {
                    qrCodeImg.setImage(new Image(qrFile.toURI().toString())); // Load QR code image
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public static File generateQRCode(String data, int width, int height, String filePath) {
        try {
            // Define encoding options
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Generate QR code matrix
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // Define file path
            File qrFile = new File(filePath);
            Path path = qrFile.toPath();

            // Save QR code as image file (PNG format)
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            // Return the generated QR code file
            return qrFile;

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

