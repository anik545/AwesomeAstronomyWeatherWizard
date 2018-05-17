package Weatheronomy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.weathericons.WeatherIcons;

import apis.Weather;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.model.Currently;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class WeatherInfo implements Initializable {

    private Double Longitude = 0.091732;
    private Double Latitude = 52.210891;
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
    private JFXButton LocationInput;

    @FXML
    private JFXTextField LongitudeTF;

    @FXML
    private JFXTextField LatitudeTF;

    @FXML
    private Label Temp;

    @FXML
    private Label IconDescriptor;

    @FXML
    private JFXButton LoadCity;

    @FXML
    private JFXTextField CityTF;

    @FXML
    private FontIcon WeatherIconMain;

    @FXML
    void LoadLocationCity(ActionEvent event) throws ForecastException {
        String city = CityTF.getText();
        CityTF.setPromptText(city);
        CityTF.clear();
        //convert city to long lat
        //set new location global
        //set long lat prompts
        LongitudeTF.setPromptText("");
        LatitudeTF.setPromptText("");
        loadNow2();
    }

    @FXML
    void inputLocation(ActionEvent event) throws ForecastException {
        Longitude = Double.parseDouble(LongitudeTF.getText());
        Latitude = Double.parseDouble(LatitudeTF.getText());
        LongitudeTF.setPromptText(Longitude.toString());
        LatitudeTF.setPromptText(Latitude.toString());
        LongitudeTF.clear(); LatitudeTF.clear();
        //convert long lat to city
        String city = "soon...";
        CityTF.setPromptText(city);

        loadNow2();
    }

    @FXML
    void loadNow(ActionEvent event) throws ForecastException {
        loadNow2();
    }

    void loadNow2() throws ForecastException {
        Currently current = Weather.getCurrentlyAtTime(Date.from(Instant.now()), Longitude, Latitude);
        CloudCoverage.setText(current.getCloudCover().toString());
        Visibility.setText(current.getVisibility().toString());
        Double AppTemperature = (((current.getApparentTemperature() - 32)*(5))/9);
        Long AppTemp1 = Math.round(AppTemperature);
        FeelsLike.setText(AppTemp1.toString());
        Rain.setText(current.getPrecipProbability().toString());
        Double Temperature = (((current.getTemperature() - 32)*(5))/9);
        Long Temperature1 = Math.round(Temperature);
        Temp.setText(Temperature1.toString());
        IconDescriptor.setText(current.getIcon());
        WeatherIconMain.setIconCode(Icons.getOrDefault(current.getIcon(), WeatherIcons.ALIEN));
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
        Icons.put("partly-cloudy-night",WeatherIcons.NIGHT_ALT_CLOUDY_HIGH);
        Icons.put("cloudy",WeatherIcons.CLOUDY);
        Icons.put("rain",WeatherIcons.RAIN);
        Icons.put("sleet", WeatherIcons.SLEET);
        Icons.put("snow", WeatherIcons.SNOW);
        Icons.put("wind",WeatherIcons.WINDY);
        Icons.put("fog",WeatherIcons.FOG);

        try {
            loadNow2();
        } catch (ForecastException e) {
            e.printStackTrace();
        }
    }



}
