package Weatheronomy.controller;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LightPollution {
    @FXML
    private BorderPane mainScreen;

    @FXML
    public void initialize() {
        try {
            LightPollutionWebView loadedLightPoll = new LightPollutionWebView(1.0,1.0);
            mainScreen.setCenter(loadedLightPoll);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void goToHomeScreen() {
        //TODO
    }
}

