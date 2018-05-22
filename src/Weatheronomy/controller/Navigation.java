package Weatheronomy.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

//SINGLETON CLASS GOVERNING NAVIGATION BETWEEN SCREENS
public class Navigation {
    private static Navigation instance;

    private static AnchorPane homePage;
    private static WeatheronomyHomeController homeController;

    private static Pane lightPollutionPage;
    private static LightPollution lightPollutionController;

    //displays light pollution page by setting it as visible and all other pages as invisible
    public static void displayLightPollutionPage() {
        homePage.setVisible(false);
        lightPollutionPage.setVisible(true);
        lightPollutionController.move(homeController.getLat(), homeController.getLong());
    }
    //displays home page by setting it as visible and all other pages as invisible
    public static void displayHomePage() {
        homePage.setVisible(true);
        lightPollutionPage.setVisible(false);
    }

    //get singleton instance of Navigation class
    public static Navigation getInstance() {
        if (instance == null)
            instance = new Navigation();

        return instance;
    }

    //Used upon starting app to set the single instance of the home and light pollution page to Navigation.
    public void setLightPollutionPage(Pane lightPollutionPage, LightPollution lightPollutionController) {
        this.lightPollutionPage = lightPollutionPage;
        this.lightPollutionController = lightPollutionController;
    }

    public void setHomePage(AnchorPane homePage, WeatheronomyHomeController homeController) {
        this.homePage = homePage;
        this.homeController = homeController;
    }
}
