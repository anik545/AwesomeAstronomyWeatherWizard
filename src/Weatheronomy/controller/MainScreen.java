package Weatheronomy.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainScreen {
    @FXML
    StackPane stackPane;

    @FXML
    public void initialize() {
        try {
            Pane loadedHome = FXMLLoader.load(getClass().getClassLoader().getResource("weatheronomyHome.fxml"));
            stackPane.getChildren().add(loadedHome);

            /*LightPollutionWebView lightPollution = new LightPollutionWebView(1.0, 1.0);
            stackPane.getChildren().add(lightPollution);*/

            /*BorderPane loadedLightPollution = FXMLLoader.load(getClass().getClassLoader().getResource("lightPollution.fxml"));
            //TODO : add light pollution*/
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
