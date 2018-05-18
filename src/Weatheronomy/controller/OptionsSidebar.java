package Weatheronomy.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class OptionsSidebar {
    @FXML
    private JFXButton homeB;

    @FXML
    private JFXButton astronomicalEventsB;

    @FXML
    private JFXButton weeklyForecastB;

    @FXML
    private JFXButton lightPollutionB;

    @FXML
    private JFXButton settingsB;

    @FXML
    private JFXButton infoB;


    //If anyone knows of a nicer way to call methods in an FXML file that aren't from its controller, that'd be nice,
    //but I can't find any way other than using wrapper functions :(
    public void goToHome() {
        Navigation.displayHomePage();
    }

    public void goToAstronomicalEvents() {

    }

    public void goToWeeklyForecast() {

    }

    public void goToLightPollution() {
        Navigation.getInstance().displayLightPollutionPage();
    }

    public void goToSettings() {

    }

    public void goToInfo() {

    }

}
