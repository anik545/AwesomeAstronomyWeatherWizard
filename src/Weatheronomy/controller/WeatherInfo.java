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
import java.util.ResourceBundle;

public class WeatherInfo implements Initializable {

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
    void inputLocation(ActionEvent event) throws ForecastException {
        Double longitude = Double.parseDouble(LongitudeTF.getText());
        Double latitude = Double.parseDouble(LatitudeTF.getText());
        Currently current = Weather.getCurrentlyAtTime(Date.from(Instant.now()),longitude, latitude);
        CloudCoverage.setText(current.getCloudCover().toString());
        Visibility.setText(current.getVisibility().toString());
        Double AppTemperature = (current.getApparentTemperature()-32)*(5/9);
        FeelsLike.setText(AppTemperature.toString());
        Rain.setText(current.getPrecipProbability().toString());
        Double Temperature = (current.getTemperature()-32)*(5/9);
        Temp.setText(Temperature.toString());
        IconDescriptor.setText(current.getIcon());

    }

    @FXML
    void loadNow(ActionEvent event) throws ForecastException {
        double longitude = 0.091732;
        double latitude = 52.210891;
        Currently current = Weather.getCurrentlyAtTime(Date.from(Instant.now()), longitude, latitude);
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

    }



}
