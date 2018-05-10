package Weatheronomy.controller;

import javafx.fxml.FXML;

public class MainController {
    /*
        --- PURPOSE OF MAIN CONTROLLER ---
        To allow components in different FXML files to communicate with each other,
        main controller contains references to all other controllers and all other
        controllers contain references to the main controller. It also supports transitions.
        These are instantiated using the --- init() --- command.

        --- NOTE ON INITIALISATION -
        The constructor is called first, then any @FXML annotated fields are populated,
        then initialize() is called.
     */
    private WeatheronomyHomeController homeController;

    //Set up communication for other controllers
    public void initialize() {
        //TODO: Since all controllers have access to same main controlloer, make static in NonMainController@
        homeController.init(this);
    }

}
