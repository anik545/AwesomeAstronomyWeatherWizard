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
    static final Config conf = new Config();
    static final String key = conf.getProperty("WEATHER_KEY");


    public static void main(String[] args) throws ForecastException {
        double lon = -75.1641667;
        double lat = 39.9522222;
        System.out.println(key);
        double a = Weather.getCurrentlyAtTime(Date.from(Instant.now()),lon, lat).getCloudCover();
        List<HourlyDataPoint> hs = getHourlyFromTime(Date.from(Instant.now()),lon, lat);
        List<HourlyDataPoint> h = getHourlyFromSunrise(Date.from(Instant.now()),lon, lat);
        List<DailyDataPoint> d = getWeek(lon, lat);
        System.out.println(h.size());
    }

    public Weather() throws ForecastException {

    }

    public static Currently getCurrentlyAtTime(Date date, double lon, double lat) throws ForecastException {

        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .time(date.toInstant())
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us)
                .exclude(ForecastRequestBuilder.Block.minutely)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request);
        System.out.println(f.getHourly().getData().size());
        return f.getCurrently();
    }

    public static List<HourlyDataPoint> getHourlyFromTime(Date date, double lon, double lat) throws ForecastException {
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .time(date.toInstant())
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request);
        System.out.println(f.getDaily().getData().size());
        return f.getHourly().getData();
    }

    public static List<DailyDataPoint> getWeek(double lon, double lat) throws ForecastException {
        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(key))
                .language(ForecastRequestBuilder.Language.en)
                .units(ForecastRequestBuilder.Units.us)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(lon), new Latitude(lat))).build();

        DarkSkyJacksonClient client = new DarkSkyJacksonClient();
        Forecast f = client.forecast(request);
        System.out.println(f.getDaily().getData().size());
        System.out.println(f.getDaily().getData().size());
        return f.getDaily().getData();
    }

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

    public static List<Double> getTemperaturesFromTime(Date time, double lon, double lat) throws ForecastException {
        return getHourlyFromTime(time,lon,lat).stream().map(HourlyDataPoint::getTemperature).collect(Collectors.toList());
    }

    public static List<Double> getCloudCoversFromTime(Date time, double lon, double lat) throws ForecastException {
        return getHourlyFromTime(time,lon,lat).stream().map(HourlyDataPoint::getCloudCover).collect(Collectors.toList());
    }

    public static List<Double> getCloudCoversWeek(double lon, double lat) throws ForecastException {
        return getWeek(lon, lat).stream().map(DailyDataPoint::getCloudCover).collect(Collectors.toList());
    }

    public static Date getSunrise(double lon, double lat){
        Location location = new Location(String.valueOf(lat), String.valueOf(lon));

        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
        Calendar sunrise = calculator.getOfficialSunriseCalendarForDate(Calendar.getInstance());
        return Date.from(sunrise.toInstant());
    }

    public static Date getSunset(double lon, double lat){
        Location location = new Location(String.valueOf(lat), String.valueOf(lon));

        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, TimeZone.getDefault());
        Calendar sunset = calculator.getOfficialSunsetCalendarForDate(Calendar.getInstance());
        return Date.from(sunset.toInstant());
    }

    public static List<Date> getClearNights(double lon, double lat) throws ForecastException {
        return getWeek(lon, lat).stream().filter(e->e.getCloudCover()<0.4).map(e -> Date.from(e.getTime())).collect(Collectors.toList());
    }
}
