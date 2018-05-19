package Weatheronomy.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainScreen {
    @FXML
    private StackPane stackPane;

    @FXML
    public void initialize() {
        try {
            //load home screen
            FXMLLoader homeLoader = new FXMLLoader(getClass().getClassLoader().getResource("weatheronomyHome.fxml"));
            Pane loadedHome = homeLoader.load();
            stackPane.getChildren().add(loadedHome);

            //load light pollution screen
            FXMLLoader pollutionLoader = new FXMLLoader(getClass().getClassLoader().getResource("lightPollution.fxml"));
            BorderPane loadedLightPollution = pollutionLoader.load();
            stackPane.getChildren().add(loadedLightPollution);

            //Set up navigation fields
            Navigation.getInstance().setHomePage(((WeatheronomyHomeController) homeLoader.getController()).getHomeScreen());
            Navigation.getInstance().setLightPollutionPage(((LightPollution) pollutionLoader.getController()).getMainScreen());

            //Set home screen as default
            Navigation.getInstance().displayHomePage();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
