package Weatheronomy.controller;

import java.io.IOException;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class LightPollution {
    WebView lightMap;
    WebEngine webEngine;
    public Pane map;

    public LightPollution(double lat, double lng) throws IOException {
        // Loading Spinner
        //JFXSpinner loader = new JFXSpinner();
        Parent loader = FXMLLoader.load(ClassLoader.getSystemResource("loader.fxml"));

        // Start webView
        lightMap = new WebView();
        webEngine = lightMap.getEngine();

        // Load light pollution
        webEngine.load("https://www.lightpollutionmap.info/");

        // Clean up UI
        webEngine.setJavaScriptEnabled(true);
        String cleanupScript = new String(ClassLoader.getSystemResource("remove.js")
            .openStream()
            .readAllBytes());

        // Center map
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                webEngine.executeScript(cleanupScript);
                move(lat, lng);
                loader.setVisible(false);
            }
        });

        map = new StackPane();
        map.getChildren().addAll(lightMap, loader);
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

