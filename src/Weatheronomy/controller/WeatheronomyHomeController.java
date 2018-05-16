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
            Pane loadedDateLocationPane = FXMLLoader.load(getClass().getResource("/Weatheronomy/view/weatherInfo.fxml"));
            dateLocationPane.getChildren().add(loadedDateLocationPane);

            Pane loadedCloudBarPane = FXMLLoader.load(getClass().getResource("/Weatheronomy/view/cloudBar.fxml"));
            cloudBarPane.getChildren().add(loadedCloudBarPane);

            Pane loadedClearNightsPane = FXMLLoader.load(getClass().getResource("/Weatheronomy/view/upcomingGoodWeather.fxml"));
            clearNightsPane.getChildren().add(loadedClearNightsPane);

            Pane loadedNextWeekPane = FXMLLoader.load(getClass().getResource("/Weatheronomy/view/upcomingDays.fxml"));
            nextWeekPane.getChildren().add(loadedNextWeekPane);

            Pane loadedAstronomicalEventsPane = FXMLLoader.load(getClass().getResource("/Weatheronomy/view/upcomingEvents.fxml"));
            astronomicalEventsPane.getChildren().add(loadedAstronomicalEventsPane);
        }
        catch (IOException ioe) {
            System.out.println("error");
            ioe.printStackTrace();
        }
    }
}
