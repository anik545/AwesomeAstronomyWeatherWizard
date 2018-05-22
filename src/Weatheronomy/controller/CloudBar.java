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
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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

    // time indicators
    private Timer currentTimeTimer;
    private Instant selectedTime;

    // FXML imports
    @FXML HBox cloudCover;
    @FXML Text startTimeText;
    @FXML Text endTimeText;
    @FXML Line currentTime;
    @FXML Pane tempGraph;
    @FXML FontIcon startTimeIcon;
    @FXML FontIcon endTimeIcon;
    @FXML Line selectedTimeBar;

    // Display lines
    Line[] timeBars = new Line[0];
    Pane[] cloudBars = new Pane[0];

    private WeatheronomyHomeController timeUpdateListener;

    /**
     *
     */
    public void initialize() {
        // Update the width on rescale
        cloudCover.widthProperty().addListener((obs, oldVal, newVal) ->
        {
            updateTimeBars();
            updateCurrentTime();
            updateCloudBars();
        });

        // Select a time if the bar has been clicked.
        tempGraph.setOnMouseClicked(this::setSelectedTime);

        selectedTime = Instant.now();
    }

    /**
     *
     * @param timeUpdateListener
     */
    public void setTimeUpdateListener(WeatheronomyHomeController timeUpdateListener) {
        this.timeUpdateListener = timeUpdateListener;
    }

    /**
     *
     * @param startTime the start time. eg surise
     * @param endTime   the end time.   eg sunset
     * @param startIcon an icon to accompany the start time
     * @param endIcon   an icon to accompany the end time
     * @param lng       longitude of weather request
     * @param lat       latitude of weather request
     */
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

        // Set the icons
        startTimeIcon.setIconCode(startIcon);
        endTimeIcon.setIconCode(endIcon);

        // Draw the stripes and blocks
        startCurrentTimeBar();
        setTimeBars();
        setCloudBars(lng, lat);

        updateTimeBars();
        updateCloudBars();
    }

    /**
     * Draws the grey hour bars, indicating time
     */
    private void setTimeBars() {
        updateCurrentTime();

        // Remove all previous bars during an update
        tempGraph.getChildren().removeAll(timeBars);

        timeBars = new Line[hourDivisions() + 2];

        for (int line = 0; line < timeBars.length; line++) {
            // Create line on each hour
            Line timeBar = new Line(0, 0, 0, 45);
            timeBars[line] = timeBar;

            // Set style
            timeBar.setStrokeWidth(2);
            timeBar.setStroke(Color.LIGHTGRAY);
            tempGraph.getChildren().add(timeBar);
        }
    }

    /**
     * Adjusts the spacing of the time bars
     */
    private void updateTimeBars() {
        Instant startHour = startTime.truncatedTo(ChronoUnit.HOURS);
        int i = 0;
        for (Line timeBar : timeBars) {
            // Adjust time bar x coordinates
            double x = max(0.0, min(cloudCover.getWidth(),
                cloudCover.getWidth() * progression(startHour.plus(Duration.ofHours(i)))));
            timeBar.setEndX(x);
            timeBar.setStartX(x);
            timeBar.setEndY(max(45, cloudCover.getHeight()));
            i++;
        }
    }

    /**
     *
     * @param lng the longitude of the request
     * @param lat the latitude of the request
     */
    private void setCloudBars(double lng, double lat) {
        // Remove any previous blocks
        cloudCover.getChildren().removeAll(cloudBars);

        // Get cloud cover predictions
        cloudBars = new Pane[hourDivisions() + 1];
        List<Double> cloudCovers;
        try {
            cloudCovers = Weather.getCloudCoversFromTime(Date.from(startTime), lng, lat);
        } catch (ForecastException e) {
            System.out.println("couldn't get cloud cover");
            cloudCovers = new ArrayList(Arrays.asList(new double[24]));
        }

        // Draw cloud cover blocks
        for (int bar = 0; bar < cloudBars.length; bar++) {
            cloudBars[bar] = new Pane();

            // Select the colour
            String color;
            switch ((int) Math.floor(cloudCovers.get(bar) * 4)) {
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

    /**
     *  Updates cloud cover bar widths on  size update.
     */
    private void updateCloudBars() {
        for (int bar = 0; bar < cloudBars.length; bar++) {
            cloudBars[bar].setPrefWidth(timeBars[bar + 1].getEndX() - timeBars[bar].getEndX());
        }
    }

    /**
     * Starts the bar that displays the current time.
     */
    private void startCurrentTimeBar() {
        currentTimeTimer = new Timer();
        currentTimeTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                updateCurrentTime();
            }
        }, 0, 1000);
    }

    /**
     * Positions the current time bar according to the current time.
     */
    private void updateCurrentTime() {
        // Is the bar inside the tim range?
        if (Instant.now().isBefore(startTime) || Instant.now().isAfter(endTime)) {
            currentTime.setVisible(false);
            return;
        } else {
            currentTime.setVisible(true);
            currentTime.setLayoutX(cloudCover.getWidth() * progression(Instant.now()));
        }
    }

    /**
     * Returns the x location for a time line at it's
     * appropriate location relative to the start and end times.
     * @param time
     * @return
     */
    private double progression(Instant time) {
        Duration startToEndTime = Duration.between(startTime, endTime);
        Duration startToCurrentTime = Duration.between(startTime, time);
        return (double) startToCurrentTime.toMillis() / (double) startToEndTime.toMillis();
    }

    /**
     * Calculate how many hour lines are between the start and end time.
     * @return
     */
    private int hourDivisions() {
        int hours = (int) Duration.between(startTime, endTime).toHours();
        return startTime.plus(Duration.ofHours(hours)).isBefore(endTime) ? hours : hours - 1;
    }

    /**
     * hides the selected time bar and removes the time set
     */
    public void clearSelectedTime() {
        selectedTimeBar.setVisible(false);
        selectedTime = Instant.now();
    }

    /**
     *
     * @param e
     */
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
