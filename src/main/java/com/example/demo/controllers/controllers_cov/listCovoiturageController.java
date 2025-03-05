package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.Covoiturage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.example.demo.services.service_cov.ServiceCovoiturage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;

public class listCovoiturageController implements Initializable {

    @FXML
    private AnchorPane listCovoituragePane;

    @FXML
    private VBox vBox;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ServiceCovoiturage sc = new ServiceCovoiturage();
            List<Covoiturage> covs = sc.Show();

            for (int i = 0; i < covs.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ressource_cov/itemCovoiturage.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    HBox hBox = (HBox) anchorPane.getChildren().get(0);
                    itemCovoiturageController itemController = fxmlLoader.getController();
                    itemController.setData(covs.get(i));
                    vBox.getChildren().add(hBox);
                } catch (IOException ex) {
                    Logger.getLogger(itemCovoiturageController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void genererPDF(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            ServiceCovoiturage rs = new ServiceCovoiturage();
            List<Covoiturage> covList = rs.Show();

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                // Créer un tableau pour structurer le logo et le titre
                PdfPTable headerTable = new PdfPTable(2);
                headerTable.setWidthPercentage(100);
                headerTable.setWidths(new float[]{1, 3});

                // Charger le logo
                String imagePath = "src/main/resources/ressource_cov//img/logo.png";
                Image logo = Image.getInstance(imagePath);
                logo.scaleToFit(70, 70);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);

                // Ajouter le titre "Wasalni"
                Font wasalniFont = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLD, new BaseColor(52, 69, 158));
                Paragraph wasalniTitle = new Paragraph("Wasalni", wasalniFont);
                wasalniTitle.setAlignment(Element.ALIGN_LEFT);
                PdfPCell titleCell = new PdfPCell(wasalniTitle);
                titleCell.setBorder(Rectangle.NO_BORDER);
                titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                titleCell.setPaddingLeft(10);

                headerTable.addCell(logoCell);
                headerTable.addCell(titleCell);
                document.add(headerTable);

                // Ajouter la date et le lieu à droite
                Font fontDate = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                BaseColor color = new BaseColor(52, 69, 158);
                fontDate.setColor(color);

                LocalDate today = LocalDate.now();
                Paragraph arianaDate = new Paragraph("Ariana\n" + today.toString(), fontDate);
                arianaDate.setAlignment(Element.ALIGN_RIGHT);
                arianaDate.setSpacingBefore(-50);
                document.add(arianaDate);

                // Ajouter le titre principal
                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD);
                BaseColor titleColor = new BaseColor(52, 69, 158);
                font.setColor(titleColor);

                Paragraph title = new Paragraph("Liste des Covoiturages", font);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(50);
                title.setSpacingAfter(20);
                document.add(title);

                // Table des covoiturages
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setSpacingBefore(30f);
                table.setSpacingAfter(30f);

                // En-têtes de colonnes
                Font hrFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
                BaseColor hrColor = new BaseColor(255, 255, 255);
                hrFont.setColor(hrColor);
                BaseColor bgColor = new BaseColor(52, 69, 158);

                PdfPCell cell1 = new PdfPCell(new Paragraph("Départ", hrFont));
                cell1.setBackgroundColor(bgColor);
                cell1.setBorderColor(titleColor);
                cell1.setPaddingTop(20);
                cell1.setPaddingBottom(20);
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell2 = new PdfPCell(new Paragraph("Destination", hrFont));
                cell2.setBackgroundColor(bgColor);
                cell2.setBorderColor(titleColor);
                cell2.setPaddingTop(20);
                cell2.setPaddingBottom(20);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell3 = new PdfPCell(new Paragraph("Prix", hrFont));
                cell3.setBackgroundColor(bgColor);
                cell3.setBorderColor(titleColor);
                cell3.setPaddingTop(20);
                cell3.setPaddingBottom(20);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cell4 = new PdfPCell(new Paragraph("Nombre de Place", hrFont));
                cell4.setBackgroundColor(bgColor);
                cell4.setBorderColor(titleColor);
                cell4.setPaddingTop(20);
                cell4.setPaddingBottom(20);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);

                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);

                // Ajouter les données des covoiturages
                Font hdFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
                for (Covoiturage cov : covList) {
                    PdfPCell cellR1 = new PdfPCell(new Paragraph(cov.getPoint_de_depart(), hdFont));
                    cellR1.setBorderColor(titleColor);
                    cellR1.setPaddingTop(10);
                    cellR1.setPaddingBottom(10);
                    cellR1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR1);

                    PdfPCell cellR2 = new PdfPCell(new Paragraph(cov.getPoint_de_destination(), hdFont));
                    cellR2.setBorderColor(titleColor);
                    cellR2.setPaddingTop(10);
                    cellR2.setPaddingBottom(10);
                    cellR2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR2);

                    PdfPCell cellR3 = new PdfPCell(new Paragraph(String.valueOf(cov.getPrix()), hdFont));
                    cellR3.setBorderColor(titleColor);
                    cellR3.setPaddingTop(10);
                    cellR3.setPaddingBottom(10);
                    cellR3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR3);

                    PdfPCell cellR4 = new PdfPCell(new Paragraph(String.valueOf(cov.getNbr_place()), hdFont));
                    cellR4.setBorderColor(titleColor);
                    cellR4.setPaddingTop(10);
                    cellR4.setPaddingBottom(10);
                    cellR4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cellR4);
                }

                table.setSpacingBefore(20);
                document.add(table);
                document.close();

                System.out.println("Le fichier PDF a été généré avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}