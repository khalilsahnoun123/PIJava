package com.example.demo.controllers.controllers_cov;

import com.example.demo.models.models_cov.Covoiturage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import com.example.demo.services.service_cov.ServiceCovoiturage;
import com.example.demo.services.service_cov.ServiceReservationCov;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class statistiquesController implements Initializable {

    @FXML
    private LineChart<String, Integer> lineChartCovoiturages;

    @FXML
    private AnchorPane statPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            statistique();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void statistique() throws SQLException {
        ServiceCovoiturage sc = new ServiceCovoiturage();
        ServiceReservationCov src = new ServiceReservationCov();

        List<Covoiturage> covs = sc.Show();

        // Créer les axes pour le graphique
        final NumberAxis yAxis = new NumberAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Point Depart");
        yAxis.setLabel("Nombre des reservations");

        // Créer la série de données à afficher
        XYChart.Series series = new XYChart.Series();
        series.setName("Statistiques des covoiturages selon leur nombre des reservations");
        for (Covoiturage c : covs) {
            series.getData().add(new XYChart.Data<>(c.getPoint_de_depart(), src.getNbReservationByIdCov(c.getId())));
        }

        // Créer le graphique et ajouter la série de données
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Statistiques des Covoiturages");
        lineChart.getData().add(series);

        // Afficher le graphique dans votre scène
        lineChartCovoiturages.setCreateSymbols(false);
        lineChartCovoiturages.getData().add(series);
    }

}
