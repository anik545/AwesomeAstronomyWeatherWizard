package Weatheronomy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import javax.swing.plaf.synth.Region;
import java.awt.*;

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Created by Jorik Schellekens, Karen Sarmiento, Anik Roy, Daniel Sääw and Ravi Shah");

        alert.showAndWait();


    }

}
