package Weatheronomy.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class WeatheronomyHomeController {
    @FXML
    private Pane dateLocationPane;

    @FXML
    private Pane cloudBarPane;

    @FXML
    private Pane clearNightsPane;

    @FXML
    private Pane nextWeekPane;

    @FXML
    private Pane astronomicalEventsPane;


    @FXML
    public void initialize() {
        try {
            Pane loadedDateLocationPane = FXMLLoader.load(getClass().getClassLoader().getResource("weatherInfo.fxml"));
            dateLocationPane.getChildren().add(loadedDateLocationPane);

            Pane loadedCloudBarPane = FXMLLoader.load(getClass().getClassLoader().getResource("cloudBar.fxml"));
            cloudBarPane.getChildren().add(loadedCloudBarPane);

            Pane loadedClearNightsPane = FXMLLoader.load(getClass().getClassLoader().getResource("upcomingGoodWeather.fxml"));
            clearNightsPane.getChildren().add(loadedClearNightsPane);

            Pane loadedNextWeekPane = FXMLLoader.load(getClass().getClassLoader().getResource("upcomingDays.fxml"));
            nextWeekPane.getChildren().add(loadedNextWeekPane);

            Pane loadedAstronomicalEventsPane = FXMLLoader.load(getClass().getClassLoader().getResource("upcomingEvents.fxml"));
            astronomicalEventsPane.getChildren().add(loadedAstronomicalEventsPane);
        }
        catch (IOException ioe) {
            System.out.println("error");
            ioe.printStackTrace();
        }
    }
}
