package com.example.demo.controllers.controllers_velo;

import com.example.demo.models.models_velo.User;
import com.example.demo.services.services_velo.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.demo.services.services_velo.WeatherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    private Label locationLabel, temperatureLabel, weatherDescriptionLabel, humidityLabel, recommendationLabel;
    @FXML
    Button buttonVelo;
    @FXML
    private ImageView weatherIcon;
    public void buttonVeloOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resource-Velo/ReserverVelo.fxml"));
        buttonVelo.getScene().setRoot(root);
    }
    public void initialize() {
        loadWeatherData();
    }
    private User user=new User();
    private UserService userService=new UserService();
    private void loadWeatherData() {
        user = userService.readById(1);

String governorat = user.getGouvernorat();
        System.out.println(governorat);
        if (governorat != null && !governorat.isEmpty()) {
            WeatherService.getWeatherForLocation(governorat, new WeatherService.WeatherCallback() {
                @Override
                public void onWeatherReceived(WeatherService.WeatherData weatherData) {
                    Platform.runLater(() -> updateUI(weatherData));
                }

                @Override
                public void onError(String message) {
                    Platform.runLater(() -> showError(message));
                }
            });
        }
    }

    private void updateUI(WeatherService.WeatherData weatherData) {
        locationLabel.setText("Weather in " + user.getGouvernorat());
        temperatureLabel.setText(String.format("Temperature: %.1f°C", weatherData.getTemperature()));

        // Update weather description
        String weatherInfo = String.format("Precipitation: %.1f mm | Rain: %.1f mm", weatherData.getPrecipitation(), weatherData.getRain());
        weatherDescriptionLabel.setText(weatherInfo);

        // Daylight status
        String daylightStatus = weatherData.isDaylight() ? "☀️ Daylight" : "🌙 Nighttime";
        humidityLabel.setText(daylightStatus);

        // Set Weather Icon
        updateWeatherIcon(weatherData);

        // Bike Recommendation
        List<String> recommendedBikes = getRecommendedBikes(weatherData);
        String recommendationText = formatRecommendationMessage(recommendedBikes, weatherData);

        recommendationLabel.setText(recommendationText);
    }

    private void updateWeatherIcon(WeatherService.WeatherData weatherData) {
        String iconPath;
        if (weatherData.getRain() > 0) {
            iconPath = "resource-Velo/img/rain.png"; // Replace with your image path
        } else if (weatherData.isDaylight()) {
            iconPath = "resource-Velo/img/sun.png";
        } else {
            iconPath = "resource-Velo/img/half-moon.png";
        }
        Image image = new Image(getClass().getResourceAsStream("/" + iconPath));
        weatherIcon.setImage(image);    }

    private List<String> getRecommendedBikes(WeatherService.WeatherData weatherData) {
        List<String> recommendedBikes = new ArrayList<>();

        if (weatherData.getRain() > 0 || weatherData.getPrecipitation() > 0) {
            recommendedBikes.add("🚴‍♂️ Vélo électrique - Stable et résistant aux intempéries");
            recommendedBikes.add("🌿 Vélo hybride - Polyvalent pour les conditions humides");
            recommendedBikes.add("🗺️ Vélo de randonnée - Conçu pour les longs trajets dans diverses conditions météorologiques");
        } else if (weatherData.getTemperature() > 25) {
            recommendedBikes.add("🚀 Vélo de route - Léger pour des températures élevées");
            recommendedBikes.add("🏖️ Vélo cruiser - Confortable et facile à utiliser par temps chaud");
        } else if (weatherData.getTemperature() < 10) {
            recommendedBikes.add("📦 Vélo cargo - Protection contre le froid et le vent");
            recommendedBikes.add("📏 Vélo pliant - Compact et pratique pour des trajets courts par temps froid");
        } else {
            recommendedBikes.add("🚵‍♂️ VTT - Idéal pour les terrains variés");
            recommendedBikes.add("🔄 Vélo hybride - Équilibré pour toutes les conditions");
        }

        return recommendedBikes;
    }


    private String formatRecommendationMessage(List<String> bikes, WeatherService.WeatherData weatherData) {
        StringBuilder message = new StringBuilder();

        if (!bikes.isEmpty()) {
            message.append("🚲 **Le vélo idéal pour aujourd'hui :**\n");
            for (String bike : bikes) {
                message.append("- ").append(bike).append("\n");
            }
        } else {
            message.append("❌ Pas idéal pour faire du vélo aujourd'hui. Envisagez un autre mode de transport 🚕");
        }

        // Tips supplémentaires basés sur les conditions
        if (weatherData.getTemperature() > 30) {
            message.append("\n🔥 **Astuce :** Restez hydraté et portez des vêtements légers !");
        } else if (weatherData.getRain() > 0) {
            message.append("\n☔ **Astuce :** Pensez à porter des vêtements imperméables.");
        }

        return message.toString();
    }


    private void showError(String message) {
        locationLabel.setText("⚠️ " + message);
        temperatureLabel.setText("");
        weatherDescriptionLabel.setText("");
        humidityLabel.setText("");
        recommendationLabel.setText("No data available.");
    }
}

