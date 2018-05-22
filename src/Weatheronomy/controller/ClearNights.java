package Weatheronomy.controller;

import apis.Weather;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.weathericons.WeatherIcons;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.model.DailyDataPoint;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.List;

/*
The class that interfaces between the main classes and the singular clear nights.
 */

public class ClearNights {
	public GridPane backgroundpane = new GridPane(); // The Pane in FXML that will be inserted in the scrollpane later on
	public GridPane nightsGrid = new GridPane();	//Storing the single clear nights
	public Parent[] days;
	public ClearNightsNight[] controllers;

	public double lon, lat;		//Coordinates

	public int nightCount = 0;

	public ClearNights(double lo, double la) throws IOException, ForecastException {

		lon = lo;
		lat = la;

		List<DailyDataPoint> upcomingWeather = Weather.getWeek(lon, lat);
		List<DailyDataPoint> clearNights = new LinkedList<>();
		Map<DailyDataPoint, Integer> cNDates = new HashMap<>();

		// Filter The clear nights

		int tmp = 0;
		for(DailyDataPoint ddp : upcomingWeather) {
			if (ddp.getCloudCover() < .40){
				clearNights.add(ddp);
				cNDates.put(ddp, tmp);
				nightCount++;
			}
			tmp++;
		}

		//INsert all the clear nights

		days = new Parent[nightCount];
		controllers = new ClearNightsNight[nightCount];
		int i = 0;
		for (DailyDataPoint iWeather : clearNights){

			// Assign a cloudiness category

			Integer suggIkon = (int)Math.floor(iWeather.getCloudCover()/0.08);

			Map<Integer, WeatherIcons> sugIkons = new HashMap<>();

			sugIkons.put(0, WeatherIcons.NIGHT_CLEAR);
			sugIkons.put(1, WeatherIcons.NIGHT_PARTLY_CLOUDY);
			sugIkons.put(2, WeatherIcons.NIGHT_ALT_PARTLY_CLOUDY);
			sugIkons.put(3, WeatherIcons.NIGHT_CLOUDY);
			sugIkons.put(4, WeatherIcons.NIGHT_ALT_CLOUDY);




			FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("clearNightsNight.fxml"));
			days[i] = loader.load();
			controllers[i] = loader.getController();

			// Set the information for each day
			controllers[i].setDay(Date.from(Instant.now().plusSeconds(60*60*24*(cNDates.get(iWeather)))));
			controllers[i].setDate(Date.from(Instant.now().plusSeconds(60*60*24*(cNDates.get(iWeather)))));
			controllers[i].setCoverPercentage(iWeather.getCloudCover());
			controllers[i].setIcon(sugIkons.get(suggIkon));


			controllers[i].content.setPadding(new javafx.geometry.Insets(1,1,1,1));
			nightsGrid.add(controllers[i].content, 0, i);
			i++;
		}
		// Fallback in case there are no clear nights
		if(i == 0){
			days = new Parent[1];
			controllers = new ClearNightsNight[1];
			FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("clearNightsNight.fxml"));
			days[i] = loader.load();
			controllers[i] = loader.getController();
			controllers[i].setDay(null);
			controllers[i].setDate(null);
			controllers[i].setCoverPercentage(Double.POSITIVE_INFINITY); // Sentinel Value, triggers if clause in ClearNightsNight.java
			controllers[i].setIcon(null);
			controllers[i].content.setPadding(new javafx.geometry.Insets(1,4,1,4));
			nightsGrid.add(controllers[i].content, 0, i);
		}
		//Fit the panes to complete the layout.
		nightsGrid.setVgap(5.0);
		backgroundpane.setPadding(new javafx.geometry.Insets(2,15,2,15));
		backgroundpane.add(nightsGrid, 0, 0);
		backgroundpane.setMaxWidth(450);
	}

}
