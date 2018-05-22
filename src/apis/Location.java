package apis;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import java.io.IOException;

public class Location {
    //get key from config file
    static final Config conf = new Config();
    static final String key = conf.getProperty("GEOAPI_KEY");
    //context created once for lifetime of application
    static final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(key)
            .build();


    //get an address string from a (long,lat) coordinate
    public static String cityFromCoord(Double lon, Double lat) throws InterruptedException, ApiException, IOException, LocationNotFoundException {
        //make a reverse geocoding request
        GeocodingResult[] results = GeocodingApi.reverseGeocode(context,new LatLng(lat,lon)).await();
        if (results.length<=0) {
            throw new LocationNotFoundException();
        }
        System.out.println(results[0].formattedAddress);
        return results[0].formattedAddress;
    }

    //get a (long,lat) coordinate from a address string.
    public static GeoCoordinates coordFromCity(String address) throws LocationNotFoundException, InterruptedException, ApiException, IOException {
        GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
        if (results.length<=0) {
            throw new LocationNotFoundException();
        }
        double lat = results[0].geometry.location.lat;
        double lon = results[0].geometry.location.lng;
        return new GeoCoordinates(new Longitude(lon), new Latitude(lat));
    }
}