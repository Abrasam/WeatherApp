package uk.ac.cam.ia.group14.sam;

import org.json.simple.JSONObject;
import uk.ac.cam.ia.group14.util.Region;

import java.util.HashMap;
import java.util.Map;

public class WeatherFetcher implements Runnable {

    private static WeatherFetcher INSTANCE;
    private static final String key = "062a189260e157451fdeaf8cd4b42e13";

    //Singleton get method.
    public static WeatherFetcher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherFetcher();
        }
        return INSTANCE;
    }

    //Singleton so constructor private.
    private WeatherFetcher() {
        run();
    }

    private Map<String, Region> regions = new HashMap<String, Region>();

    @Override
    public void run() {
        for (Location r : Location.values()) {
            JSONObject weather;
        }
    }

    private enum Location {
        ;

        String coord;

        Location(String coord) {
            this.coord = coord;
        }
    }
}
