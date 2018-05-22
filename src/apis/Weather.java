package apis;


import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;
import com.luckycatlabs.sunrisesunset.dto.Location;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.*;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class Weather {

    private Calendar sunrise;
    private Calendar sunset;
    private Forecast dayForecast;

    //Get api key from config file
    static final Config conf = new Config();
    static final String key = conf.getProperty("WEATHER_KEY");


    public static void main(String[] args) {
    }

    public Weather() {

    }

    //Get data at a location for one point in time
    public static Currently getCurrentlyAtTime(Date date, double lon, double lat) throws ForecastException {

        //Build request
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .time(date.toInstant())
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us) //use US imperial units
                .exclude(ForecastRequestBuilder.Block.minutely)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))) //add location
                .build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request);
        return f.getCurrently();
    }

    //Get hourly data at a location from a time onwards (gives 24 hours)
    public static List<HourlyDataPoint> getHourlyFromTime(Date date, double lon, double lat) throws ForecastException {

        //Build request
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .time(date.toInstant())
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us) //use US imperial units
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))) //add location
                .build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request); //use the inbuilt forecast class
        return f.getHourly().getData(); //get the required data
    }

    public static List<DailyDataPoint> getWeek(double lon, double lat) throws ForecastException {
        //Build request
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request);
        return f.getDaily().getData();
    }

    //Calculates the sunrise, then gets hourly data for 24 hours after this time
    public static List<HourlyDataPoint> getHourlyFromSunrise(Date date, double lon, double lat) throws ForecastException {
        Location location = new Location(String.valueOf(lat), String.valueOf(lon));

        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
        Calendar sunrise = calculator.getOfficialSunriseCalendarForDate(Calendar.getInstance());
        Calendar sunset = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());

        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .time(sunrise.getTime().toInstant())
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us)
                .exclude(ForecastRequestBuilder.Block.minutely)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request);
        return f.getHourly().getData().subList(0,24);
    }

    //helper to get the temperature at a particular time
    public static List<Double> getTemperaturesFromTime(Date time, double lon, double lat) throws ForecastException {
        return getHourlyFromTime(time,lon,lat).stream().map(HourlyDataPoint::getTemperature).collect(Collectors.toList());
    }
    //helper to the the cloud cover at a particular time
    public static List<Double> getCloudCoversFromTime(Date time, double lon, double lat) throws ForecastException {
        return getHourlyFromTime(time,lon,lat).stream().map(HourlyDataPoint::getCloudCover).collect(Collectors.toList());
    }

    //Get a list of cloud cover values for 7 days, starting from today
    public static List<Double> getCloudCoversWeek(double lon, double lat) throws ForecastException {
        return getWeek(lon, lat).stream().map(DailyDataPoint::getCloudCover).collect(Collectors.toList());
    }

    public static Date getSunrise(double lon, double lat){
        Location location = new Location(String.valueOf(lat), String.valueOf(lon));

        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault()); //use default timezone of device
        Calendar sunrise = calculator.getOfficialSunriseCalendarForDate(Calendar.getInstance());
        return Date.from(sunrise.toInstant());
    }

    public static Date getSunset(double lon, double lat){
        Location location = new Location(String.valueOf(lat), String.valueOf(lon));

        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault()); //use default timezone of device
        Calendar sunset = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());
        return Date.from(sunset.toInstant());
    }

    //Return the date where cloud cover is less than 0.4
    public static List<Date> getClearNights(double lon, double lat) throws ForecastException {
        return getWeek(lon, lat).stream().filter(e->e.getCloudCover()<0.4).map(e -> Date.from(e.getTime())).collect(Collectors.toList());
    }

}
