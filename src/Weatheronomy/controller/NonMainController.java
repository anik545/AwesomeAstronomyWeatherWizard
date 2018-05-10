package Weatheronomy.controller;

//ALL CONTROLLERS THAT ARE NOT MAIN CONTROLLER INHERIT FROM ME
public class NonMainController {
    private MainController mainController;

    public void init(MainController main) {
        mainController = main;
    }

    public MainController getMainController() {
        return mainController;
    }
}
