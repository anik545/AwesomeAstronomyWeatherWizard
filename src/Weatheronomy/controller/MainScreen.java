package Weatheronomy.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainScreen {
    //Stackpane of all screens
    @FXML
    private StackPane stackPane;

    @FXML
    //JavaFX default - run after instantiating FXML fields
    public void initialize() {
        try {
            //load home screen
            FXMLLoader homeLoader = new FXMLLoader(getClass().getClassLoader().getResource("weatheronomyHome.fxml"));
            Pane loadedHome = homeLoader.load();
            stackPane.getChildren().add(loadedHome);

            //load light pollution screen
            FXMLLoader pollutionLoader = new FXMLLoader(getClass().getClassLoader().getResource("lightPollution.fxml"));
            Pane loadedLightPollution = pollutionLoader.load();
            stackPane.getChildren().add(loadedLightPollution);

            //Set up navigation fields
            Navigation.getInstance().setHomePage(
                ((WeatheronomyHomeController) homeLoader.getController()).getHomeScreen(),
                homeLoader.getController());
            Navigation.getInstance().setLightPollutionPage(
                ((LightPollution) pollutionLoader.getController()).getMainScreen(),
                pollutionLoader.getController());

            //Set home screen as default
            Navigation.getInstance().displayHomePage();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
