package Weatheronomy.controller;

import apis.Weather;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.weathericons.WeatherIcons;
import tk.plogitech.darksky.forecast.ForecastException;

import java.io.IOException;
import java.time.Duration;


public class WeatheronomyHomeController {
    @FXML
    private AnchorPane outerHomeAnchor;

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
        double lon = 4.3;
        double lat = 3.2;
        try {
            Pane loadedDateLocationPane = FXMLLoader.load(getClass().getClassLoader().getResource("weatherInfo.fxml"));
            dateLocationPane.getChildren().add(loadedDateLocationPane);

            FXMLLoader dayCloudBar = new FXMLLoader(getClass().getClassLoader().getResource("cloudBar.fxml"));
            Pane dayCloudBarPane = dayCloudBar.load();
            CloudBar dayCloudBarController = dayCloudBar.getController();

            dayCloudBarController.setBoundryTimes(
                    Weather.getSunrise(lon, lat).toInstant(),
                    Weather.getSunset(lon, lat).toInstant(),
                    WeatherIcons.SUNRISE,
                    WeatherIcons.SUNSET);

            FXMLLoader nightCloudBar = new FXMLLoader(getClass().getClassLoader().getResource("cloudBar.fxml"));
            Pane nightCloudBarPane = nightCloudBar.load();
            CloudBar nightCloudBarController = nightCloudBar.getController();

            nightCloudBarController.setBoundryTimes(
                    Weather.getSunset(lon, lat).toInstant(),
                    Weather.getSunrise(lon, lat).toInstant().plus(Duration.ofDays(1)),
                    WeatherIcons.SUNSET,
                    WeatherIcons.SUNRISE);

            VBox cloudBarWrapper = new VBox();
            cloudBarWrapper.getChildren().addAll(dayCloudBarPane, nightCloudBarPane);
            cloudBarPane.getChildren().add(cloudBarWrapper);

            ClearNights clearNights = new ClearNights(lon, lat);
            Pane loadedClearNightsPane = clearNights.backgroundpane;
            clearNightsPane.getChildren().add(loadedClearNightsPane);

            Pane loadedNextWeekPane = FXMLLoader.load(getClass().getClassLoader().getResource("upcomingDays.fxml"));
            nextWeekPane.getChildren().add(loadedNextWeekPane);

            Pane loadedAstronomicalEventsPane = FXMLLoader.load(getClass().getClassLoader().getResource("upcomingEvents.fxml"));
            astronomicalEventsPane.getChildren().add(loadedAstronomicalEventsPane);


        }
        catch (IOException ioe) {
            System.out.println("error");
            ioe.printStackTrace();
        } catch (ForecastException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getHomeScreen() {
        return outerHomeAnchor;
    }
}
