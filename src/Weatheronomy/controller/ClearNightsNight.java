package Weatheronomy.controller;

import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.weathericons.WeatherIcons;


/*
The Controlling class for a singular upcoming clear night in the 'Cloudless Nights soon' widget.
@author Daniel Sääw
@version 1.1
 */

public class ClearNightsNight {

	// Member Variables each representing a part of the main pane

	private String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	@FXML
	public Text weekday, date, coverPercentage;

	@FXML
	public Pane content; // Main Pane which will be inserted into the ClearNights Pane

	@FXML
	public FontIcon ikon;



	//Self-Explanatory Methods

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
			coverPercentage.setText((int)(100.0*p) + "%");
	}

	public void setIcon(WeatherIcons i){
		if(i != null)
			ikon.setIconCode(i);
		else
			ikon = null;
	}

}
