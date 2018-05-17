package apis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    static final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyA1TRuKvV_6wX0PyTNirgMYp9q5PdUNkuk")
            .build();


    public static void main(String[] args) throws InterruptedException, LocationNotFoundException, ApiException, IOException {
        GeoCoordinates a = coordFromCity("Cambridge");
        System.out.println(a.longitude().value());
        System.out.println(a.latitude().value());
        System.out.println(cityFromCoord(0.121817,52.205337));
    }

    public static String cityFromCoord(Double lon, Double lat) throws InterruptedException, ApiException, IOException, LocationNotFoundException {
        GeocodingResult[] results = GeocodingApi.reverseGeocode(context,new LatLng(lat,lon)).await();
        if (results.length<=0) {
            throw new LocationNotFoundException();
        }
        System.out.println(results[0].formattedAddress);
        return results[0].formattedAddress;
    }

    public static GeoCoordinates coordFromCity(String address) throws LocationNotFoundException, InterruptedException, ApiException, IOException {
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                address).await();
        if (results.length<=0) {
            throw new LocationNotFoundException();
        }
        double lat = results[0].geometry.location.lat;
        double lon = results[0].geometry.location.lng;
        return new GeoCoordinates(new Longitude(lon), new Latitude(lat));
    }


}

