package Weatheronomy.controller;

import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.weathericons.WeatherIcons;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class ClearNightsNight {

	private String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	private ClearNights clearNights;

	@FXML
	public Text weekday, date, coverPercentage;

	@FXML
	public GridPane content;

	@FXML
	public FontIcon ikon;



	public void setDay(Date d){
		if(d != null)
			weekday.setText(weekdays[d.getDay()]);
		else
			weekday.setText("");
	}

	public void setDate(Date d){
		if(d != null)
			date.setText("" + d.getDate());
		else
			date.setText("");
	}

	public void setCoverPercentage(double p){
		if(p == Double.POSITIVE_INFINITY)
			coverPercentage.setText("No clear nights here in the upcoming week, sorry!");
		else
			coverPercentage.setText("Cloud Cover: " + (int)(100.0*p) + "%");
	}

	public void setIcon(WeatherIcons i){
		if(i != null)
			ikon.setIconCode(i);
		else
			ikon = null;
	}

}
