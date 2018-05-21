package Weatheronomy.controller;

import apis.Weather;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import tk.plogitech.darksky.forecast.ForecastException;

import static Weatheronomy.constants.Colours.DARK_GREY;
import static Weatheronomy.constants.Colours.GREY;
import static Weatheronomy.constants.Colours.LIGHT_GREY;
import static Weatheronomy.constants.Colours.WHITE;
import static java.lang.Double.max;
import static java.lang.Double.min;

public class CloudBar extends Pane {
    private Instant startTime;
    private Instant endTime;

    private Timer currentTimeTimer;
    private Instant selectedTime;


    @FXML HBox cloudCover;
    @FXML Text startTimeText;
    @FXML Text endTimeText;
    @FXML Line currentTime;
    @FXML Pane tempGraph;
    @FXML FontIcon startTimeIcon;
    @FXML FontIcon endTimeIcon;
    @FXML Line selectedTimeBar;

    Line[] timeBars = new Line[0];
    Pane[] cloudBars = new Pane[0];

    private WeatheronomyHomeController timeUpdateListener;

    public void initialize() {
        cloudCover.widthProperty().addListener((obs, oldVal, newVal) ->
        {
            updateTimeBars();
            updateCurrentTime();
            updateCloudBars();
        });

        tempGraph.setOnMouseClicked(event -> {
            setSelectedTime(event);
        });

        selectedTime = Instant.now();

    }

    public void setTimeUpdateListener(WeatheronomyHomeController timeUpdateListener) {
        this.timeUpdateListener = timeUpdateListener;
    }

    public void updateTimes(
        Instant startTime, Instant endTime,
        Ikon startIcon, Ikon endIcon,
        double lng, double lat
    ) {
        this.startTime = startTime;
        this.endTime = endTime;

        // Set view start and end times
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
        startTimeText.setText(formatter.format(startTime));
        endTimeText.setText(formatter.format(endTime));

        startTimeIcon.setIconCode(startIcon);
        endTimeIcon.setIconCode(endIcon);

        startCurrentTimeBar();
        setTimeBars();
        setCloudBars(lng, lat);

        updateTimeBars();
        updateCloudBars();
    }

    private void setTimeBars() {
        updateCurrentTime();

        tempGraph.getChildren().removeAll(timeBars);

        timeBars = new Line[hourDivisions() + 2];

        for(int line = 0; line < timeBars.length; line++) {
            // Create line on each hour
            Line timeBar = new Line(0, 0, 0,  45);
            timeBars[line] = timeBar;

            // Set style
            timeBar.setStrokeWidth(2);
            timeBar.setStroke(Color.LIGHTGRAY);
            tempGraph.getChildren().add(timeBar);
        }
    }

    private void updateTimeBars() {
        Instant startHour = startTime.truncatedTo(ChronoUnit.HOURS);
        int i = 0;
        for(Line timeBar: timeBars) {
            double x = max(0.0, min(cloudCover.getWidth(),
                cloudCover.getWidth() * progression(startHour.plus(Duration.ofHours(i)))));
            timeBar.setEndX(x);
            timeBar.setStartX(x);
            timeBar.setEndY(max(45, cloudCover.getHeight()));
            i++;
        }
    }

    private void setCloudBars(double lng, double lat) {
        cloudCover.getChildren().removeAll(cloudBars);
        cloudBars = new Pane[hourDivisions() + 1];
        List<Double> cloudCovers;
        try {
            cloudCovers = Weather.getCloudCoversFromTime(Date.from(startTime), lng, lat);
        } catch (ForecastException e) {
            System.out.println("couldn't get cloud cover");
            cloudCovers = new ArrayList(Arrays.asList(new double[24]));
        }
        for (int bar = 0; bar < cloudBars.length; bar++) {
            cloudBars[bar] = new Pane();
            String color;
            switch((int) Math.floor(cloudCovers.get(bar) * 4)) {
                case 4:
                case 3:
                    color = WHITE;
                    break;
                case 2:
                    color = LIGHT_GREY;
                    break;
                case 1:
                    color = GREY;
                    break;
                case 0:
                default:
                    color = DARK_GREY;
                    break;
            }
            cloudBars[bar].setStyle("-fx-background-color: " + color);
        }
        cloudCover.getChildren().addAll(cloudBars);
    }

    private void updateCloudBars() {
        for (int bar = 0; bar < cloudBars.length; bar++) {
            cloudBars[bar].setPrefWidth(timeBars[bar + 1].getEndX() - timeBars[bar].getEndX());
        }
    }

    private void drawTemperature() {

    }

    private void startCurrentTimeBar() {
        currentTimeTimer = new Timer();
        currentTimeTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run(){
                updateCurrentTime();
            }
        }, 0, 1000);
    }

    private void updateCurrentTime() {
        if(Instant.now().isBefore(startTime) || Instant.now().isAfter(endTime)) {
            currentTime.setVisible(false);
            return;
        } else {
            currentTime.setVisible(true);
            currentTime.setLayoutX(cloudCover.getWidth() * progression(Instant.now()));
        }
    }

    private double progression(Instant time) {
        Duration startToEndTime = Duration.between(startTime, endTime);
        Duration startToCurrentTime = Duration.between(startTime, time);
        return (double) startToCurrentTime.toMillis() / (double) startToEndTime.toMillis();
    }

    private int hourDivisions() {
        int hours = (int) Duration.between(startTime, endTime).toHours();
        return startTime.plus(Duration.ofHours(hours)).isBefore(endTime) ? hours : hours - 1;
    }

    public static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }

    public void clearSelectedTime() {
        selectedTimeBar.setVisible(false);
        selectedTime = Instant.now();
    }

    public void setSelectedTime(MouseEvent e) {
        // Calculate time offset
        double location = e.getX() / tempGraph.getWidth();
        Duration startToEndTime = Duration.between(startTime, endTime);
        double millisAtLocation = startToEndTime.toMillis() * location;

        // Set time
        selectedTime = startTime.plusMillis((long) millisAtLocation);

        // Set current time bar
        selectedTimeBar.setLayoutX(e.getX());
        selectedTimeBar.setVisible(true);

        try {
            timeUpdateListener.currentTimeUpdateListener(selectedTime);
        } catch (ForecastException e1) {
            e1.printStackTrace();
        }
    }
}
