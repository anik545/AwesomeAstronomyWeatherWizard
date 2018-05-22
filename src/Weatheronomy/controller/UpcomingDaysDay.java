package Weatheronomy.controller;

import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.weathericons.WeatherIcons;

public class UpcomingDaysDay {

	private String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	private UpcomingDays UpcomingDays;

	@FXML
	public Text weekday, date;

	@FXML
	public Pane icon;

	@FXML
	public GridPane content;

	@FXML
	public FontIcon ikon;



	public void setDay(Date d){
		weekday.setText(weekdays[d.getDay()]);
	}

	public void setDate(Date d){
		date.setText("" + d.getDate());
	}

	public void setIcon(WeatherIcons i){
		ikon.setIconCode(i);
	}
}
