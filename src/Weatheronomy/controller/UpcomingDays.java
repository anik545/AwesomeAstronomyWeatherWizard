package Weatheronomy.controller;

import apis.Weather;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.weathericons.WeatherIcons;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.model.DailyDataPoint;

//The Class that interfaces between the main page and the singular upcoming days

public class UpcomingDays {
	public GridPane backgroundpane = new GridPane();
	public Pane finalPane = new Pane();		//The pane that will be inserted into the main page
	public GridPane daysGrid = new GridPane();	//The pane that holds all the days
	public Parent[] days;
	public UpcomingDaysDay[] controllers;

	public double lon, lat;

	private final int DAY_COUNT = 5;

	public UpcomingDays(double lo, double la) throws IOException, ForecastException {
		//Coordinates
		lon = lo;
		lat = la;

		List<DailyDataPoint> upcomingWeather = Weather.getWeek(lon, lat);

		days = new Parent[DAY_COUNT];
		controllers = new UpcomingDaysDay[DAY_COUNT];
		for (int i = 0; i < DAY_COUNT; i++){
			DailyDataPoint iWeather = upcomingWeather.get(i);

			String suggIcon = iWeather.getIcon();

			Map<String, WeatherIcons> sugIkons = new HashMap<>();
			//Suggested Icons
			sugIkons.put("clear-day", WeatherIcons.DAY_SUNNY);
			sugIkons.put("clear-night", WeatherIcons.NIGHT_CLEAR);
			sugIkons.put("partly-cloudy-day", WeatherIcons.DAY_CLOUDY_HIGH);
			sugIkons.put("partly-cloudy-night",WeatherIcons.NIGHT_CLOUDY_HIGH);
			sugIkons.put("cloudy",WeatherIcons.CLOUDY);
			sugIkons.put("rain",WeatherIcons.RAIN);
			sugIkons.put("sleet", WeatherIcons.SLEET);
			sugIkons.put("snow", WeatherIcons.SNOW);
			sugIkons.put("wind",WeatherIcons.WINDY);
			sugIkons.put("fog",WeatherIcons.FOG);

			//Set data for each day
			FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("upcomingDaysDay.fxml"));
			days[i] = loader.load();
			controllers[i] = loader.getController();
			controllers[i].setDay(Date.from(Instant.now().plusSeconds(60*60*24*(i))));
			controllers[i].setDate(Date.from(Instant.now().plusSeconds(60*60*24*(i))));

			controllers[i].setIcon(sugIkons.get(suggIcon));

			daysGrid.add(controllers[i].content, i, 0);
		}
		backgroundpane.setStyle("-fx-background-color: #303030");
		backgroundpane.setPadding(new javafx.geometry.Insets(2,5,2,5));
		backgroundpane.add(daysGrid, 0, 0);

		//Finish insertiable pane
		ScrollPane scroller = new ScrollPane(backgroundpane);
		scroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scroller.setMaxWidth(450);
		scroller.setStyle("-fx-background-color:transparent;");
		finalPane.setBackground(Background.EMPTY);
		finalPane.getChildren().add(scroller);
	}

}
