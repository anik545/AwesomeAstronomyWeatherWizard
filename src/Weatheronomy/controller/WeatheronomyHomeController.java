package Weatheronomy.controller;

import apis.Weather;
import java.time.Instant;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private WeatherInfo dateLocationPaneController;

    @FXML
    private Pane cloudBarPane;

    @FXML
    private Pane clearNightsPane;

    @FXML
    private Pane nextWeekPane;

    @FXML
    private Pane astronomicalEventsPane;

    private final double default_lat = 52.210891;
    private final double default_lon = 0.091732;

    private CloudBar dayCloudBarController;
    private CloudBar nightCloudBarController;


    @FXML
    //JavaFX default method - run after FXML fields are instantiated
    public void initialize() {
        try {
            // Load the weather info pane
            FXMLLoader dateLocationLoader = new FXMLLoader(ClassLoader.getSystemResource("weatherInfo.fxml"));
            Pane loadedDateLocationPane = dateLocationLoader.load();
            dateLocationPaneController = dateLocationLoader.getController();
            dateLocationPaneController.setLocationUpdateListener(this);

            dateLocationPane.getChildren().add(loadedDateLocationPane);

            // Pretend the location has been updated for the sake of initialisation.
            locationUpdateListener();

        }
        catch (IOException ioe) {
            System.out.println("error");
            ioe.printStackTrace();
        } catch (ForecastException e) {
            e.printStackTrace();
        }
    }

    //called to pass home screen instance to Navigation
    public AnchorPane getHomeScreen() {
        return outerHomeAnchor;
    }

    /**
     * Replaces the cloud bars with the current selected location
     * @param lon
     * @param lat
     * @throws IOException
     */
    public void updateCloudBars(double lon, double lat)
        throws  IOException {
        cloudBarPane.getChildren().removeAll(cloudBarPane.getChildren());
        FXMLLoader dayCloudBar = new FXMLLoader(getClass().getClassLoader().getResource("cloudBar.fxml"));
        Pane dayCloudBarPane = dayCloudBar.load();
        dayCloudBarController = dayCloudBar.getController();

        dayCloudBarController.updateTimes(
            Weather.getSunrise(lon, lat).toInstant(),
            Weather.getSunset(lon, lat).toInstant(),
            WeatherIcons.SUNRISE,
            WeatherIcons.SUNSET,
            lon,
            lat
        );

        dayCloudBarController.setTimeUpdateListener(this);
        dayCloudBarController.setTitle("Day");

        FXMLLoader nightCloudBar = new FXMLLoader(getClass().getClassLoader().getResource("cloudBar.fxml"));
        Pane nightCloudBarPane = nightCloudBar.load();
        nightCloudBarController = nightCloudBar.getController();

        nightCloudBarController.updateTimes(
            Weather.getSunset(lon, lat).toInstant(),
            Weather.getSunrise(lon, lat).toInstant().plus(Duration.ofDays(1).plusMinutes(2)),
            WeatherIcons.SUNSET,
            WeatherIcons.SUNRISE,
            lon,
            lat
        );

        nightCloudBarController.setTimeUpdateListener(this);
        nightCloudBarController.setTitle("Night");

        VBox cloudBarWrapper = new VBox();
        cloudBarWrapper.getChildren().addAll(dayCloudBarPane, nightCloudBarPane);
        cloudBarPane.getChildren().add(cloudBarWrapper);
    }

    /**
     * Updates clearNights to reflect the current locatoin
     * @param lon
     * @param lat
     * @throws ForecastException
     * @throws IOException
     */
    public void updateClearNights(double lon, double lat)
        throws ForecastException, IOException {
        clearNightsPane.getChildren().removeAll(clearNightsPane.getChildren());

        ClearNights clearNights = new ClearNights(lon, lat);
        Pane loadedClearNightsPane = clearNights.backgroundpane;
        clearNightsPane.getChildren().add(loadedClearNightsPane);
        clearNightsPane.setMinHeight(clearNights.nightCount*32);
    }

    /**
     * updates next week with the current location data
     * @param lon
     * @param lat
     * @throws IOException
     * @throws ForecastException
     */
    public void updateNextWeek(double lon, double lat)
        throws IOException, ForecastException {
        nextWeekPane.getChildren().removeAll(nextWeekPane.getChildren());

        UpcomingDays nextWeek = new UpcomingDays(lon, lat);
        Pane loadedNextWeekPane = nextWeek.finalPane;
        nextWeekPane.getChildren().add(loadedNextWeekPane);
        nextWeekPane.setMinHeight(80);
    }

    /**
     * Updates all the components if the location updates
     * @throws IOException
     * @throws ForecastException
     */
    public void locationUpdateListener()
        throws IOException, ForecastException {
        double lon = dateLocationPaneController.Longitude;
        double lat = dateLocationPaneController.Latitude;
        updateCloudBars(lon, lat);
        updateClearNights(lon, lat);
        updateNextWeek(lon, lat);
    }

    /**
     * updates components if the time updates
     * @param time
     * @throws ForecastException
     */
    public void currentTimeUpdateListener(Instant time) throws ForecastException {
        dateLocationPaneController.loadNow2(Date.from(time));
    }

    /**
     * clears cloud bar selected times
     */
    public void clearSelectedTime() {
        dayCloudBarController.clearSelectedTime();
        nightCloudBarController.clearSelectedTime();
    }

    /**
     * Obvious
     * @return
     */
    public double getLat() {return dateLocationPaneController.Latitude;}
    public double getLong() {return dateLocationPaneController.Longitude;}
}
