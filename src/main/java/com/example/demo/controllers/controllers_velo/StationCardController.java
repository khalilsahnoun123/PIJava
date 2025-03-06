package com.example.demo.controllers.controllers_velo;
import com.example.demo.models.models_velo.StationVelo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.demo.services.services_velo.StationVeloService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class StationCardController {

    @FXML private Label nomStation;
    @FXML private Label adresseStation;
    @FXML private Label velosDisponibles;
    @FXML private WebView mapView;  // WebView for map display

    private StationVelo station;
    private StationVeloService serviceStation = new StationVeloService();

    public void setStation(StationVelo station) {
        this.station = station;
        updateUI();

        loadMapForStation(station.getGouvernera() + " " + station.getMunicapilite()+" "+station.getAdresse());
    }

    private void updateUI() {
        nomStation.setText(station.getNameStation());
        adresseStation.setText(station.getAdresse());
        int availableBikes = serviceStation.getAvailableBikesCount(station.getIdStation());
        velosDisponibles.setText("Vélos disponibles : " + availableBikes);
    }

    @FXML
    public void handleSelection(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource-Velo/BikeSelection.fxml"));
            Parent root = loader.load();

            BikeSelectionController controller = loader.getController();
            controller.initializeWithStation(station);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMapForStation(String location) {
        new Thread(() -> {
            try {
                // ✅ Use OpenStreetMap API (Nominatim) instead of Open-Meteo
                String encodedLocation = URLEncoder.encode(location, "UTF-8");
                String geoUrl = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedLocation;

                URL url = new URL(geoUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // ✅ Required by Nominatim

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // ✅ Parse JSON response
                JSONParser parser = new JSONParser();
                JSONArray results = (JSONArray) parser.parse(response.toString());

                if (results.isEmpty()) {
                    System.out.println("No results found for location: " + location);
                    return;  // Stop execution if no results
                }

                JSONObject locationData = (JSONObject) results.get(0);
                double lat = Double.parseDouble(locationData.get("lat").toString());
                double lon = Double.parseDouble(locationData.get("lon").toString());

                // ✅ Update the WebView with the map
                Platform.runLater(() -> loadMapInWebView(lat, lon, location));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadMapInWebView(double lat, double lon, String location) {
        WebEngine webEngine = mapView.getEngine();
        String mapHtml = "<html><head>"
                + "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.7.1/dist/leaflet.css' />"
                + "<script src='https://unpkg.com/leaflet@1.7.1/dist/leaflet.js'></script>"
                + "</head><body>"
                + "<div id='map' style='width: 100%; height: 100%;'></div>"
                + "<script>"
                + "var map = L.map('map').setView([" + lat + ", " + lon + "], 15);"
                + "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);"
                + "L.marker([" + lat + ", " + lon + "]).addTo(map).bindPopup('" + location + "').openPopup();"
                + "</script></body></html>";

        webEngine.loadContent(mapHtml);
    }

}