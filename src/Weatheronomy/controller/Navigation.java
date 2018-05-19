package Weatheronomy.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

//SINGLETON CLASS GOVERNING NAVIGATION BETWEEN SCREENS
public class Navigation {
    private static Navigation instance;

    private static AnchorPane homePage;
    private static BorderPane lightPollutionPage;

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

    public void setLightPollutionPage(BorderPane lightPollutionPage) {
        this.lightPollutionPage = lightPollutionPage;
    }

    public void setHomePage(AnchorPane homePage) {
        this.homePage = homePage;
    }
}
