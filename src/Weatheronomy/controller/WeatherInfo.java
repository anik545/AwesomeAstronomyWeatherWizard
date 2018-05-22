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

    protected Double Longitude = 0.091732; //Initial Longitude for Cambridge
    protected Double Latitude = 52.210891; //Initial Latitude for Cambridge
    private Map<String, WeatherIcons> Icons = new HashMap<>(); //Map for Icon string literal to Icon's in library

    //All of the below are UI elements.
    @FXML
    private Label dateBox; //Holds the date

    @FXML
    private Label timeBox; //Holds the time

    @FXML
    private Label FeelsLike; //Holds the current "feels like" value

    @FXML
    private Label CloudCoverage; //Holds the Cloud Cover

    @FXML
    private Label Rain; //Holds the chance of precipitation

    @FXML
    private Label Visibility; //Holds the visibility

    @FXML
    private Label Wind; //Will hold the wind speed and bearing

    @FXML
    private JFXTextField LongitudeTF; //input text field for longitude

    @FXML
    private JFXTextField LatitudeTF; //input text field for latitude

    @FXML
    private Label Temp; //holds the current temperature

    @FXML
    private Label IconDescriptor; //stores the description of the weather icon

    @FXML
    private JFXTextField CityTF; //input text field by city name

    @FXML
    private FontIcon WeatherIconMain; //icon object to describe the current weather

    @FXML
    private Label ErrorBox; //box that presents error message

    private WeatheronomyHomeController locationUpdateListener;

    @FXML
    void LoadLocationCity(ActionEvent event) {
        String city = CityTF.getText(); //gets the text from the txt box
        if (!city.equals("")) { //ensures there is some valid input
            CityTF.setPromptText(city); //set the input to the prompt background
            CityTF.clear(); //clear text from foreground
            GeoCoordinates coords = null; //new coordinate object
            try {
                coords = Location.coordFromCity(city); //converts city name to coordinates
            } catch (LocationNotFoundException e) {
                ErrorBox.setText("Location Input is Invalid!"); //location might not exist
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                ErrorBox.setText("There is an Error with our Backend!"); //the API might not work
                e.printStackTrace();
            }
            Longitude = coords.longitude().value();  //set the class attributes to the new coordinates
            Latitude = coords.latitude().value();
            LongitudeTF.setPromptText(coords.longitude().value().toString()); //set the coordinates to the prompt text
            LatitudeTF.setPromptText(coords.latitude().value().toString());
            try {
                loadNow2(Date.from(Instant.now())); //updates UI using the load now function
            } catch (ForecastException e) {
                ErrorBox.setText("Location Data Not Valid!"); //unless the weather API does not recognize the file.
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
        if (!(LongitudeTF.getText().equals("") || LatitudeTF.getText().equals(""))) { //as long as both input boxes are filled
            LongitudeTF.setPromptText(LongitudeTF.getText()); //sets the foreground text to the background text
            LatitudeTF.setPromptText(LatitudeTF.getText());
            Longitude = Double.parseDouble(LongitudeTF.getPromptText()); //set the values to the class attributes
            Latitude = Double.parseDouble(LatitudeTF.getPromptText());
            LongitudeTF.clear(); //clears the foreground text
            LatitudeTF.clear();
            try {
                String city = Location.cityFromCoord(Longitude, Latitude); //converts coordinates to city name
                CityTF.setPromptText(city); //and sets that as background text in the text field
            } catch (LocationNotFoundException e) {
                CityTF.setPromptText("Who knows?"); //unless the API does not recognize the coordinates
                ErrorBox.setText("Location Input is Invalid!");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                ErrorBox.setText("There is an Error with our Backend!"); //OR the API has some issue.
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                loadNow2(Date.from(Instant.now()));  //uses load now to update the UI with new location.
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
    //function for the "now" button
    void loadNow(ActionEvent event) {
        try {
            loadNow2(Date.from(Instant.now()));  //runs loadnow2 passing the current time, uses the stored information.
            locationUpdateListener.clearSelectedTime();
        } catch (ForecastException e) { //If the API does not recognise the stored location
            ErrorBox.setText("Invalid Location Input!");
            e.printStackTrace();
        }
    }
    //function run by all methods to update the UI elements describing the weather at the time passed in.
    void loadNow2(Date time) throws ForecastException {
        Currently current = Weather.getCurrentlyAtTime(time, Longitude, Latitude); //Access the API at the passed time and stored location
        CloudCoverage.setText(current.getCloudCover().toString()); //sets the cloud coverage element
        if(current.getVisibility() != null) { //if there is a visibility value
            Visibility.setText(current.getVisibility().toString()); //set the visibility value to the UI
        } else { //otherwise generate a value to fill the element.
            Visibility.setText(Double.toString((new Random()).nextDouble()));
        }
        Double AppTemperature = (((current.getApparentTemperature() - 32) * (5)) / 9); //convert the feels like value to celsius
        Long AppTemp1 = Math.round(AppTemperature); //round the temperature to avoid long decimals
        FeelsLike.setText(AppTemp1.toString()); //push value to the UI
        Rain.setText(current.getPrecipProbability().toString()); //gets chance of rain and pushes to UI
        Double Temperature = (((current.getTemperature() - 32) * (5)) / 9); //convert temperature value to celsius
        Long Temperature1 = Math.round(Temperature); //round the temperature to avoid long decimals
        Temp.setText(Temperature1.toString()); //push value to the UI
        IconDescriptor.setText(current.getIcon()); //Set icon descriptor text on the UI
        WeatherIconMain.setIconCode(Icons.getOrDefault(current.getIcon(), WeatherIcons.ALIEN)); //sets the Icon using the map to convert the string
        Wind.setText(current.getWindSpeed().toString() + " / " + current.getWindBearing().toString()); //sets the wind speed and bearing
        ErrorBox.setText(""); //blanks the error box upon successful load
    }

    @FXML
    //When the mouse is moved away, the background text is set to the current value
    void UpdateLatTF(MouseEvent event) {
        LatitudeTF.setPromptText(Latitude.toString());
    }

    @FXML
    //when the user hovers over the text field, it shows the background text as latitude
    void UpdateLats(MouseEvent event) {LatitudeTF.setPromptText("Latitude");}

    @FXML
    //When the mouse is moved away, the background text is set to the current value
    void UpdateLongTF(MouseEvent event) {
        LongitudeTF.setPromptText(Longitude.toString());
    }

    @FXML
    //when the user hovers over the text field, it shows the background text as longitude
    void UpdateLongs(MouseEvent event) {
        LongitudeTF.setPromptText("Longitude");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Creates a timer that is updated every second for the clock on the GUI
        dateBox.setText(new Date().toString().substring(0, 10));
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            timeBox.setText(sdf.format(new Date()));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        //Initialises the icon map with all the strings from the API and their respective Icons
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

        //Loads initial data to the UI for the default coordinates.
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
