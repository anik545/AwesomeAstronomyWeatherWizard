package Weatheronomy.controller;

import apis.LocationNotFoundException;
import com.google.maps.errors.ApiException;
import com.jfoenix.controls.JFXTextField;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.weathericons.WeatherIcons;
import apis.Location;
import apis.Weather;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Currently;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class WeatherInfo implements Initializable {

    protected Double Longitude = 0.091732;
    protected Double Latitude = 52.210891;
    private Map<String, WeatherIcons> Icons = new HashMap<>();

    @FXML
    private Label dateBox;

    @FXML
    private Label timeBox;

    @FXML
    private Label FeelsLike;

    @FXML
    private Label CloudCoverage;

    @FXML
    private Label Rain;

    @FXML
    private Label Visibility;

    @FXML
    private Label Wind;

    @FXML
    private JFXTextField LongitudeTF;

    @FXML
    private JFXTextField LatitudeTF;

    @FXML
    private Label Temp;

    @FXML
    private Label IconDescriptor;

    @FXML
    private JFXTextField CityTF;

    @FXML
    private FontIcon WeatherIconMain;

    @FXML
    private Label ErrorBox;

    private WeatheronomyHomeController locationUpdateListener;

    @FXML
    void LoadLocationCity(ActionEvent event) {
        String city = CityTF.getText();
        if (!city.equals("")) {
            CityTF.setPromptText(city);
            CityTF.clear();
            GeoCoordinates coords = null;
            try {
                coords = Location.coordFromCity(city);
            } catch (LocationNotFoundException e) {
                ErrorBox.setText("Location Input is Invalid!");
                e.printStackTrace();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                ErrorBox.setText("There is an Error with our Backend!");
                e.printStackTrace();
            }
            Longitude = coords.longitude().value();
            Latitude = coords.latitude().value();
            LongitudeTF.setPromptText(coords.longitude().value().toString());
            LatitudeTF.setPromptText(coords.latitude().value().toString());
            try {
                loadNow2(Date.from(Instant.now()));
            } catch (ForecastException e) {
                ErrorBox.setText("Location Data Not Valid!");
                e.printStackTrace();
            }
            try {
                locationUpdateListener.locationUpdateListener();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ForecastException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void inputLocation(ActionEvent event) {
        if (!(LongitudeTF.getText().equals("") || LatitudeTF.getText().equals(""))) {
            LongitudeTF.setPromptText(LongitudeTF.getText());
            LatitudeTF.setPromptText(LatitudeTF.getText());
            Longitude = Double.parseDouble(LongitudeTF.getPromptText());
            Latitude = Double.parseDouble(LatitudeTF.getPromptText());
            LongitudeTF.clear();
            LatitudeTF.clear();
            try {
                String city = Location.cityFromCoord(Longitude, Latitude);
                CityTF.setPromptText(city);
            } catch (LocationNotFoundException e) {
                ErrorBox.setText("Location Input is Invalid!");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                ErrorBox.setText("There is an Error with our Backend!");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                loadNow2(Date.from(Instant.now()));
            } catch (ForecastException e) {
                ErrorBox.setText("Location Data Not Valid!");
                e.printStackTrace();
            }
            try {
                locationUpdateListener.locationUpdateListener();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ForecastException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void loadNow(ActionEvent event) {
        try {
            loadNow2(Date.from(Instant.now()));
        } catch (ForecastException e) {
            ErrorBox.setText("Invalid Location Input!");
            e.printStackTrace();
        }
    }

    void loadNow2(Date time) throws ForecastException {
        Currently current = Weather.getCurrentlyAtTime(time, Longitude, Latitude);
        CloudCoverage.setText(current.getCloudCover().toString());
        if(current.getVisibility() != null) {
            Visibility.setText(current.getVisibility().toString());
        } else {
            Visibility.setText(Double.toString((new Random()).nextDouble()));
        }
        Double AppTemperature = (((current.getApparentTemperature() - 32) * (5)) / 9);
        Long AppTemp1 = Math.round(AppTemperature);
        FeelsLike.setText(AppTemp1.toString());
        Rain.setText(current.getPrecipProbability().toString());
        Double Temperature = (((current.getTemperature() - 32) * (5)) / 9);
        Long Temperature1 = Math.round(Temperature);
        Temp.setText(Temperature1.toString());
        IconDescriptor.setText(current.getIcon());
        WeatherIconMain.setIconCode(Icons.getOrDefault(current.getIcon(), WeatherIcons.ALIEN));
        Wind.setText(current.getWindSpeed().toString() + " / " + current.getWindBearing().toString());
        ErrorBox.setText("");
    }

    @FXML
    void UpdateLatTF(MouseEvent event) {
        LatitudeTF.setPromptText(Latitude.toString());
    }

    @FXML
    void UpdateLats(MouseEvent event) {
        LatitudeTF.setPromptText("Latitude");

    }

    @FXML
    void UpdateLongTF(MouseEvent event) {
        LongitudeTF.setPromptText(Longitude.toString());
    }

    @FXML
    void UpdateLongs(MouseEvent event) {
        LongitudeTF.setPromptText("Longitude");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateBox.setText(new Date().toString().substring(0, 10));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            timeBox.setText(sdf.format(new Date()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        Icons.put("clear-day", WeatherIcons.DAY_SUNNY);
        Icons.put("clear-night", WeatherIcons.NIGHT_CLEAR);
        Icons.put("partly-cloudy-day", WeatherIcons.DAY_CLOUDY_HIGH);
        Icons.put("partly-cloudy-night", WeatherIcons.NIGHT_CLOUDY_HIGH);
        Icons.put("cloudy", WeatherIcons.CLOUDY);
        Icons.put("rain", WeatherIcons.RAIN);
        Icons.put("sleet", WeatherIcons.SLEET);
        Icons.put("snow", WeatherIcons.SNOW);
        Icons.put("wind", WeatherIcons.WINDY);
        Icons.put("fog", WeatherIcons.FOG);

        try {
            loadNow2(Date.from(Instant.now()));
        } catch (ForecastException e) {
            ErrorBox.setText("Default Location Data Not Valid!");
            e.printStackTrace();
        }
    }

    public void setLocationUpdateListener(WeatheronomyHomeController locationUpdateListener) {
        this.locationUpdateListener = locationUpdateListener;
    }
}
