package Weatheronomy.Test;

import javafx.application.Application;
import Weatheronomy.controller.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tk.plogitech.darksky.forecast.ForecastException;

import java.awt.*;
import java.io.IOException;


public class UpcomingDaysTest extends Application{

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		try{
			UpcomingDays mainUpcomingDays;

			mainUpcomingDays = new UpcomingDays(56.1, 12.75);

			GridPane layout = mainUpcomingDays.backgroundpane;

			primaryStage.setTitle("Testing the pane for upcoming days");
			Scene mainScene = new Scene(layout);


			primaryStage.setScene(mainScene);
			primaryStage.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}


}
