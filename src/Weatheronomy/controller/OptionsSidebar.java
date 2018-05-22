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
    private JFXButton lightPollutionB;

    @FXML
    private JFXButton infoB;

    public void goToHome() {
        Navigation.displayHomePage();
    }

    public void goToLightPollution() {
        Navigation.getInstance().displayLightPollutionPage();
    }

    public void goToInfo() {
        int aei = 0x00E4;
        String ae = Character.toString((char)aei);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Created by Jorik Schellekens, Karen Sarmiento, Anik Roy, Daniel S"+ae+ae+"w and Ravi Shah");

        alert.showAndWait();


    }

}
