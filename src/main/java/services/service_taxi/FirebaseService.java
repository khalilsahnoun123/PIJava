package services.service_taxi;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;

import java.io.FileInputStream;


public class FirebaseService {
    public static FirebaseDatabase database;

    static {
        try {
            FileInputStream serviceAccount = new FileInputStream("C:/Users/WSI/Downloads/pijava-ca9ee-firebase-adminsdk-fbsvc-f9072d5118.json"); // Mettez le bon chemin !

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://Pijava.firebaseio.com") // Remplacez par l'URL correcte de votre base Firebase
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { // Éviter l'erreur d'initialisation multiple
                FirebaseApp.initializeApp(options);
                database = FirebaseDatabase.getInstance();
                System.out.println("✅ Firebase a été initialisé avec succès !");
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de l'initialisation de Firebase : " + e.getMessage());
        }
    }
}
