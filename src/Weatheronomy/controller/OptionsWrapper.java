package Weatheronomy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OptionsWrapper {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private AnchorPane centreAnchorPane;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    HamburgerBackArrowBasicTransition transition;

    @FXML
    public void initialize() {

        try {
            //Set up side panel
            VBox sidePanelContent = FXMLLoader.load(ClassLoader.getSystemResource("optionsSidebar.fxml"));
            drawer.setSidePane(sidePanelContent);
            drawer.setMouseTransparent(true);
            //Set up home screen
            StackPane mainScreen = FXMLLoader.load(ClassLoader.getSystemResource("mainScreen.fxml"));
            centreAnchorPane.getChildren().add(mainScreen);
            mainScreen.toBack(); //behind drawer (options menu)

            setUpHamburgerTransitions();
            // Close drawer if navigation buttons are clicked
            sidePanelContent.getChildren().stream().forEach(n -> n.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> toggleDrawwer()));

            // Pass mouse clicks through when drawer is closed
            drawer.setOnDrawerClosed(e -> drawer.setMouseTransparent(true));
            drawer.setOnDrawerOpened(e -> drawer.setMouseTransparent(false));
            drawer.setOnDrawerOpening(e -> drawer.setMouseTransparent(true));

            drawer.setDefaultDrawerSize(200);

        } catch (IOException e) {
            //Logger.getLogger(OptionsWrapper.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }

    private void setUpHamburgerTransitions() {
        transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> toggleDrawwer());
    }
    private void toggleDrawwer() {
        transition.setRate(transition.getRate() * -1);
        transition.play();
        drawer.toggle();
    }
}
