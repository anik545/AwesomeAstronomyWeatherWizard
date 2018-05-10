package apis;

import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class Weather {

    Weather() throws ForecastException {

        Config conf = new Config();
        String key = conf.getProperty("WEATHER_KEY");

        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey("your-private-key"))
                .time(Instant.now().minus(5, ChronoUnit.DAYS))
                .language(ForecastRequestBuilder.Language.de)
                .units(ForecastRequestBuilder.Units.us)
                .exclude(ForecastRequestBuilder.Block.minutely)
                .extendHourly()
                .location(new GeoCoordinates(new Longitude(13.377704), new Latitude(52.516275))).build();

        DarkSkyClient client = new DarkSkyClient();
        String forecast = client.forecastJsonString(request);

    }

}
