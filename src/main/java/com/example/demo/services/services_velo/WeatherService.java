package com.example.demo.services.services_velo;
import javafx.application.Platform;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {
    private static final JSONParser parser = new JSONParser();

    public interface WeatherCallback {
        void onWeatherReceived(WeatherData weatherData);
        void onError(String message);
    }

    public static void getWeatherForLocation(String location, WeatherCallback callback) {
        new Thread(() -> {
            try {
                // Step 1: Get coordinates using Open-Meteo Geocoding API
                String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                        location.replace(" ", "+") + "&count=1&language=en&format=json";

                String geoResponse = getApiResponse(geoUrl);
                JSONObject geoJson = (JSONObject) parser.parse(geoResponse);
                JSONArray results = (JSONArray) geoJson.get("results");

                if (results == null || results.isEmpty()) {
                    Platform.runLater(() -> callback.onError("Location not found"));
                    return;
                }

                JSONObject locationData = (JSONObject) results.get(0);
                double lat = (double) locationData.get("latitude");
                double lon = (double) locationData.get("longitude");

                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat +
                        "&longitude=" + lon +
                        "&current=temperature_2m,precipitation,rain,is_day";

                String weatherResponse = getApiResponse(weatherUrl);
                JSONObject weatherJson = (JSONObject) parser.parse(weatherResponse);
                JSONObject currentWeather = (JSONObject) weatherJson.get("current");

                // Extract all necessary data
                double temperature = (double) currentWeather.get("temperature_2m");
                double precipitation = (double) currentWeather.get("precipitation");
                double rain = (double) currentWeather.get("rain");
                long isDay = (long) currentWeather.get("is_day");

                boolean goodWeather = temperature > 18 && precipitation == 0 && rain == 0;
                boolean daylight = isDay == 1;

                WeatherData weatherData = new WeatherData(
                        temperature,
                        precipitation,
                        rain,
                        daylight,
                        goodWeather
                );

                Platform.runLater(() -> callback.onWeatherReceived(weatherData));
            } catch (Exception e) {
                Platform.runLater(() -> callback.onError("Error fetching weather data"));
            }
        }).start();
    }

    private static String getApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new IOException("API request failed with status: " + conn.getResponseCode());
        }

        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            return response.toString();
        }
    }

    public static class WeatherData {
        private final double temperature;
        private final double precipitation;
        private final double rain;
        private final boolean daylight;
        private final boolean goodForBike;

        public WeatherData(double temperature, double precipitation,
                           double rain, boolean daylight, boolean goodForBike) {
            this.temperature = temperature;
            this.precipitation = precipitation;
            this.rain = rain;
            this.daylight = daylight;
            this.goodForBike = goodForBike;
        }

        // Getters
        public double getTemperature() { return temperature; }
        public double getPrecipitation() { return precipitation; }
        public double getRain() { return rain; }
        public boolean isDaylight() { return daylight; }
        public boolean isGoodForBike() { return goodForBike; }
    }
}