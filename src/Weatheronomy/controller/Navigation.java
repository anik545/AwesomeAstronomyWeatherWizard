package Weatheronomy.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

//SINGLETON CLASS GOVERNING NAVIGATION BETWEEN SCREENS
public class Navigation {
    private static Navigation instance;

    private static AnchorPane homePage;
    private static Pane lightPollutionPage;

    public static void displayLightPollutionPage() {
        homePage.setVisible(false);
        lightPollutionPage.setVisible(true);
    }

    public static void displayHomePage() {
        homePage.setVisible(true);
        lightPollutionPage.setVisible(false);
    }

    public static Navigation getInstance() {
        if (instance == null)
            instance = new Navigation();

        return instance;
    }

    public void setLightPollutionPage(Pane lightPollutionPage) {
        this.lightPollutionPage = lightPollutionPage;
    }

    public void setHomePage(AnchorPane homePage) {
        this.homePage = homePage;
    }
}
