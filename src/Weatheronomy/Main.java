package Weatheronomy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Parent root = FXMLLoader.load(ClassLoader.getSystemResource("optionsWrapper.fxml"));
        URL whatthefuckbro = getClass().getClassLoader().getSystemResource("optionsWrapper.fxml");
        Parent root = FXMLLoader.load(getClass()
                .getClassLoader()
                .getSystemResource("optionsWrapper.fxml"));

        primaryStage.setTitle("Hello World");


        primaryStage.setScene(new Scene(root, 450, 700));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

