package controllers.controllers_taxi;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import models.models_taxi.ReservationTaxi;
import models.models_taxi.User;
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
import services.service_taxi.UserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

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

    private final UserService userService = new UserService();


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
    private void sendConfirmationEmail(String recipientEmail) {
        final String senderEmail = "elazrak.mayssa@gmail.com";
        final String senderPassword = "zfhf lhbd nzun wyuf";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Confirmation de réservation de taxi");
            message.setText("Félicitations ! Votre réservation de taxi a été confirmée. Merci de nous faire confiance.");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void open_ajouterReservation(ActionEvent event) {
        // From Formulaire
        int id_user = 1;
        int id_vec = this.getIdVec();
        String status = "En Attente";


        ReservationTaxi res_taxi = new ReservationTaxi(
                id_vec , status, id_user);
        ServiceReservationTaxi rts = new ServiceReservationTaxi();
        rts.ajouter(res_taxi);
        User user = userService.getUserById(id_user);
        System.out.println(user.getEmail());
        if (user != null && user.getEmail() != null) {
            sendConfirmationEmail(user.getEmail());
        } else {
            System.out.println("⚠ Impossible de récupérer l'email de l'utilisateur !");
        }
        send_sms();
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
    void send_sms(){
        String ACCOUNT_SID = "ACb77418499a96c48513c7ed7c5c6a8837";
        String AUTH_TOKEN = "81169021dcc4ee721385ce4d321e4a83";

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String recepientNumber = "+21695698847";
        String message = "Bonjour Mr, \n"
                +"Nous sommes ravis de vous informer qu'une reservation a été effectuée.\n"
                +"Veuillez contactez l'administration pour plus de details. \n"
                +"Merci de votre fidélité et à bientôt.\n"
                +"Cordialement, \n";

        com.twilio.rest.api.v2010.account.Message twilioMessage = Message.creator(
                new PhoneNumber(recepientNumber),
                new PhoneNumber("+12564459923"),message).create();
        System.out.println("SMS envoyé : "+twilioMessage.getSid());
    }

}

