package controllers.controllers_cov;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class ChatbotController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField userInput;

    // Use LinkedHashMap to maintain order of patterns
    private static final LinkedHashMap<String, String> responses = new LinkedHashMap<>();
    private static final Map<String, String[]> suggestions = new LinkedHashMap<>(); // Map for follow-up suggestions
    private static final Random random = new Random();

    static {
        // Initialize responses with expanded patterns
        responses.put("réserver|réservation|réserver un trajet|réserver une place|réserver un véhicule",
                "Bien sûr, pour réserver un covoiturage, allez dans la section 'Réservations'.");
        responses.put("annuler|annulation|supprimer une réservation|annulé|annulés|annulée",
                "Vous pouvez annuler votre réservation depuis 'Mes réservations'.");
        responses.put("paiement|payer|mode de paiement",
                "Les paiements se font en ligne ou en espèces, selon votre préférence.");
        responses.put("trajet|trajets|parcours|trajet disponible",
                "Les trajets disponibles sont affichés sur la page principale, filtrez par date ou destination.");
        responses.put("tarif|prix|tarifs",
                "Les tarifs varient selon le trajet et le conducteur, consultez les détails sur chaque trajet.");
        responses.put("inscription|créer un compte|comment m'inscrire",
                "Cliquez sur 'Créer un compte' pour vous inscription rapidement.");
        responses.put("contact|nous contacter|comment vous contacter",
                "Vous pouvez nous contacter via l'application ou par e-mail à support@covoiturage.com.");
        responses.put("comment naviguer|comment utiliser l'application|comment se déplacer dans l'application",
                "Pour naviguer, utilisez le menu en haut pour accéder à la page d'accueil, aux covoiturages, et aux réservations.");
        responses.put("je ne peux pas me connecter|connexion échouée|problème de connexion",
                "Vérifiez vos identifiants ou réinitialisez votre mot de passe via l'option 'Mot de passe oublié'.");
        responses.put("problème de réservation|réservation échouée|je ne peux pas réserver",
                "Si vous avez un problème, vérifiez votre connexion Internet ou contactez-nous pour assistance.");
        responses.put("fonctionnalité|quelles sont les fonctionnalité|fonctionnalité de l'application",
                "Notre application permet de réserver, consulter trajets, annuler réservations, et donner feedback.");
        responses.put("aide|assistance|besoin d'aide",
                "Consultez la section d'aide dans l'application ou contactez-nous via e-mail.");
        responses.put("feedback|retour d'expérience|comment donner un retour",
                "Nous apprécions votre retour ! Envoyez vos suggestions via 'Feedback' dans les paramètres.");
        responses.put("comment trouver un covoiturage",
                "Consultez les trajets disponibles sur la page principale, filtrez par date ou destination.");
        responses.put("quels sont les avantages de l'application",
                "Notre application offre tarifs compétitifs, réservation facile, et support client 24/7.");
        responses.put("comment signaler un problème",
                "Signalez un problème via l'option 'Feedback' dans les paramètres ou contactez-nous.");

        // Initialize suggestions for each pattern
        suggestions.put("réserver|réservation|réserver un trajet|réserver une place|réserver un véhicule",
                new String[]{"Voulez-vous voir les trajets disponibles maintenant?", "Avez-vous besoin d'aide pour choisir un trajet?"});
        suggestions.put("annuler|annulation|supprimer une réservation|annulé|annulés|annulée",
                new String[]{"Souhaitez-vous réserver un autre trajet?", "Avez-vous besoin d'aide pour une nouvelle réservation?"});
        // Add more suggestions for other patterns as needed
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) return;

        // Display user message with styling
        chatArea.appendText("Vous : " + input + "\n");
        chatArea.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 15px; -fx-font-size: 14px;");

        String response = getResponse(input);

        // Display chatbot response with styling
        chatArea.appendText("Chatbot : " + response + "\n");
        chatArea.setStyle("-fx-background-color: #f0f2f5; -fx-text-fill: #333; -fx-padding: 10px; -fx-border-radius: 15px; -fx-font-size: 14px;");

        // Add a random suggestion if available
        String patternKey = findMatchingPatternKey(input);
        if (patternKey != null && suggestions.containsKey(patternKey)) {
            String[] possibleSuggestions = suggestions.get(patternKey);
            if (possibleSuggestions.length > 0) {
                String suggestion = possibleSuggestions[random.nextInt(possibleSuggestions.length)];
                chatArea.appendText("Chatbot : " + suggestion + "\n");
            }
        }

        userInput.clear();
    }



    private String getResponse(String input) {
        input = input.toLowerCase(); // Convert input to lowercase to handle case sensitivity

        // Iterate through each response pattern and check for a match
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            String pattern = entry.getKey();
            String response = entry.getValue();

            // Check if the input matches the regular expression pattern
            if (Pattern.compile(pattern).matcher(input).find()) {
                return response; // Return the matched response
            }
        }
        return "Je ne comprend pas votre demande. Pouvez-vous reformuler ?";
    }

    private String findMatchingPatternKey(String input) {
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            String pattern = entry.getKey();
            if (Pattern.compile(pattern).matcher(input.toLowerCase()).find()) {
                return pattern;
            }
        }
        return null;
    }

    @FXML
    public void initialize() {
        chatArea.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-padding: 10px;");
        userInput.setStyle("-fx-background-color: white; -fx-border-radius: 20px; -fx-padding: 8px;");
    }



}