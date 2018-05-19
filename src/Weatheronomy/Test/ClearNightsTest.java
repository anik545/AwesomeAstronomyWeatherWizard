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


public class ClearNightsTest extends Application{

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		try{
			ClearNights mainClearNights;

			mainClearNights = new ClearNights(15.15, 15.15);

			GridPane layout = mainClearNights.backgroundpane;

			primaryStage.setTitle("Testing the pane for the next clear nights!");
			Scene mainScene = new Scene(layout);


			primaryStage.setScene(mainScene);
			primaryStage.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}


}
