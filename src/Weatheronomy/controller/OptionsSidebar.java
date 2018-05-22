package Weatheronomy.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class OptionsSidebar {
    @FXML
    private JFXButton homeB;

    @FXML
    private JFXButton lightPollutionB;

    @FXML
    private JFXButton infoB;

    //displays home page - need to create wrapper method in controller to be referenced in FXML file
    public void goToHome() {
        Navigation.displayHomePage();
    }

    //wrapper for displaying light pollution page
    public void goToLightPollution() {
        Navigation.getInstance().displayLightPollutionPage();
    }

    //display info alert
    public void goToInfo() {
        int aei = 0x00E4;
        String ae = Character.toString((char)aei);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Created by Jorik Schellekens, Karen Sarmiento, Anik Roy, Daniel S"+ae+ae+"w and Ravi Shah");

        alert.showAndWait();
    }
}
