package uk.ac.cam.ia.group14.sjs252;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.ac.cam.ia.group14.util.Region;
import uk.ac.cam.ia.group14.util.RegionID;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeatherFetcher {

    private static WeatherFetcher INSTANCE;
    private static final String QUERY = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&APPID=062a189260e157451fdeaf8cd4b42e13";
    private static final SimpleDateFormat FORMAT_OUTPUT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Random rand = new Random();

    /**
     * Singleton weather Fetcher is used to get the weather data for a region.
     * Data is pulled from the (free version of the) API, or if the internet is unavailable then we look to see if we have cached data.
     * @return
     */
    public static WeatherFetcher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherFetcher();
        }
        return INSTANCE;
    }

    //Singleton so constructor private.
    private WeatherFetcher() {
        update();
    }

    private Map<RegionID, Region> regions = new HashMap<>();

    /**
     * Update method is called to update the weather data by downloading from the API.
     */
    public void update() {
        for (RegionID r : RegionID.values()) {
            try {
                String[] coordinates = r.coord.split(",");
                URL urlHourly = new URL(String.format(QUERY, coordinates[0], coordinates[1])); //generate query url
                String hourlyJSON = readUrl(urlHourly); //HTTP GET
                WeatherSlice[][] data = load(hourlyJSON); //parse JSON
                //System.out.print(hourlyJSON); tee hee hee
                regions.put(r, new Region(r.name, data[0], data[1]));
                BufferedWriter writer = new BufferedWriter(new FileWriter("saves/" + r.name + ".txt")); //cache in case we lose internet
                writer.write(hourlyJSON);
                writer.close();
            } catch (IOException e) {
                //No new data so check for files.
                try {
                    FileReader reader = new FileReader("saves/" + r.name + ".txt"); //attempt to load data if internet is unavailable
                    StringBuffer buffer = new StringBuffer();
                    int read;
                    while ((read = reader.read()) != -1) {
                        buffer.append((char)read);
                    }
                    String json = buffer.toString();
                    WeatherSlice[][] data = load(json); //load cached data
                    regions.put(r, new Region(r.name, data[0], data[1]));
                } catch (IOException e1) {
                    //File fails when it comes to existing, so no cached data found.
                }
            }
        }
    }

    /**
     * Get a region's weather data.
     * @param r the relevant region's ID.
     * @return region object.
     */
    public Region getRegion(RegionID r) {
        return regions.get(r);
    }

    /**
     * Parse a given JSON string and return two WeatherSlice arrays one for daily and one for hourly data.
     * @param json the JSON string
     * @return the weather slices.
     */
    private WeatherSlice[][] load(String json) {
        JSONArray weather = (JSONArray) new JSONObject(json).get("list");
        //System.out.println(json);
        //initialise empty arrays
        WeatherSlice[] hourly = new WeatherSlice[24 * 5];
        WeatherSlice[] daily = new WeatherSlice[6];
        int day = 0;
        //iterate through weather data to generate weather slices.
        for (int i = 0; i < weather.length(); i++) {
            JSONObject hour = null;
            try {
                hour = (JSONObject) weather.get(i); //get the relevant hour's JSON data, if JSON error it will be null.
            } catch (JSONException e) {
            }
            JSONObject main = null;
            try {
                main = (JSONObject) hour.get("main"); //get main object (contains most weather data) of the slice, if JSON error it will be null.
            } catch (JSONException e) {
            }
            JSONObject wind = null;
            try {
                wind = ((JSONObject) hour.get("wind")); //get the wind data of the slice, if JSON error it will be null.
            } catch (JSONException e) {
            }
            JSONObject rain = null;
            try {
                rain = ((JSONObject) hour.get("rain")); //get the rain data of the slice, if JSON error it will be null.
            } catch (JSONException e) {
            }
            JSONObject status = null;
            try {
                status = (JSONObject)((JSONArray)hour.get("weather")).get(0); //get the status by parsing the weather section of the JSON, if JSON error it will be null.
            } catch (JSONException e) {
            }
            Date time = null;
            try {
                time = FORMAT_OUTPUT.parse((String) hour.get("dt_txt")); //get the time the slice begins at, if JSON error it will be null.
            } catch (ParseException e) {
            }
            double temp = 0;
            try {
                temp = main.getDouble("temp"); //get the temperature of the slice, if JSON error it will be 0.
            } catch (JSONException e) {
            }
            double wind_speed = 0;
            try {
                wind_speed = (wind == null ? 0 : wind.getDouble("speed")); //get the wind speed of the slice, if JSON error it will be 0.
            } catch (JSONException e) {
            }
            double rainfall = 0;
            try {
                rainfall = (rain == null ? 0 : rain.getDouble("3h")); //get the rainfall of the slice, if JSON error it will be 0.
            } catch (JSONException e) {
            }
            //NOTE THAT SOME PARAMETERS WERE RANDOMLY GENERATED HERE BECAUSE THE WEATHER API WOULD NOT PROVIDE THEM FOR FREE.
            WeatherSlice slice = new WeatherSlice(time, temp, wind_speed, rainfall, 80 + rand.nextInt(20), rand.nextInt(100)+5000, 10000 + rand.nextInt(500), parseStatus(status.getString("main"))); //generate weather slice.
            hourly[3 * i] = slice;
            hourly[3 * i + 1] = slice;
            hourly[3 * i + 2] = slice; //the free version of the API does 3 hour slices not 1 hour slices, so we simulate this data by assuming the data is constant for the 3 hours.
            if ((day == 0 && time.getHours() > 12) || time.getHours() == 12) {
                daily[day++] = slice; //add daily slices if they are midday.
            }
        }
        return new WeatherSlice[][] {hourly, daily}; //return both daily and hourly.
    }

    //Parse the status information from the weather API into a WeatherSlice.Status object.
    private WeatherSlice.Status parseStatus(String status) {
        switch (status) {
            case "Thunderstorm":
                return WeatherSlice.Status.THUNDERSTORM;
            case "Drizzle":
            case "Rain":
                return WeatherSlice.Status.RAIN;
            case "Snow":
                return WeatherSlice.Status.SNOW;
            case "Clear":
                return WeatherSlice.Status.SUN;
            case "Atmosphere":
            case "Clouds":
            default:
                return WeatherSlice.Status.CLOUDS;
        }
    }

    //Download from API.
    private String readUrl(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer buffer = new StringBuffer();
        int read;
        while ((read = reader.read()) != -1) {
            buffer.append((char)read); //Read data from stream.
        }
        return buffer.toString();
    }

    /*public static void main(String[] args) {
        System.out.println(getInstance().getRegion(RegionID.BREACONS));
    }*/
}
