package Weatheronomy.controller;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class LightPollution {
    @FXML
    private Pane mainScreen;

    @FXML WebView webView;

    WebEngine webEngine;

    @FXML
    public void initialize() {
        double lat = 1.123;
        double lng = 12.3;

        Parent loader = null;
        try {
            loader = FXMLLoader.load(ClassLoader.getSystemResource("loader.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        webEngine = webView.getEngine();

        // Load light pollution
        webEngine.load("https://www.lightpollutionmap.info/");

        // Clean up UI
        webEngine.setJavaScriptEnabled(true);
        String cleanupScript = null;
        try {
            cleanupScript = new String(ClassLoader.getSystemResource("remove.js")
                .openStream()
                .readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Center map
        String finalCleanupScript = cleanupScript;
        Parent finalLoader = loader;
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                webEngine.executeScript(finalCleanupScript);
                move(lat, lng);
                finalLoader.setVisible(false);
            }
        });
    }

    public void goToHome() {
        Navigation.displayHomePage();
    }

    public Pane getMainScreen() {
        return mainScreen;
    }

    public void move(double lat, double lng) {
        webEngine.executeScript(
            String.format(
                "map.getView().animate([%d, %d], 0);",
                (int) lng * 100000,
                (int) lat * 100000
            )
        );
    }
}

