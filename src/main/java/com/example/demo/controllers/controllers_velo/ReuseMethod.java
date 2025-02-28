package com.example.demo.controllers.controllers_velo;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.control.Button;

public class ReuseMethod {
    public void changeOnlyAnchorPane(String fxml, AnchorPane mainAnchorPane) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        AnchorPane anchorPane = loader.load();
        mainAnchorPane.getChildren().removeAll();
        mainAnchorPane.getChildren().setAll(anchorPane);


    }
    public void clickChangeColoerBorderColor(Button clickedButton, Button... otherButtons) {
        // Set active button style
        clickedButton.setStyle(
                "-fx-border-color: #ABD7FF;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-color: rgba(255,255,255,0.2);"
        );

        // Reset other buttons
        for (Button btn : otherButtons) {
            btn.setStyle(
                    "-fx-border-color: transparent;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-background-color: rgba(255,255,255,0.1);"
            );
        }
    }
}
