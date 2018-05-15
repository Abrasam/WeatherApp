package uk.ac.cam.ia.group14.sam;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uk.ac.cam.ia.group14.util.Region;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeatherFetcher implements Runnable {

    private static WeatherFetcher INSTANCE;
    private static final String QUERY_HOURLY = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&APPID=062a189260e157451fdeaf8cd4b42e13";
    private static final String QUERY_DAILY = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&cnt=5&units=metric&APPID=062a189260e157451fdeaf8cd4b42e13";
    private static final SimpleDateFormat FORMAT_OUTPUT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        JSONParser parser = new JSONParser();
        for (Location r : Location.values()) {
            String[] coordinates = r.coord.split(",");
            try {
                URL urlHourly = new URL(String.format(QUERY_HOURLY, coordinates[0], coordinates[1]));
                String hourlyJSON = readUrl(urlHourly);
                JSONArray weather = (JSONArray)((JSONObject)parser.parse(hourlyJSON)).get("list");
                WeatherSlice[] hourly = new WeatherSlice[24*5];
                WeatherSlice[] daily = new WeatherSlice[6];
                int day = 0;
                for (int i = 0; i < weather.size(); i++) {
                    JSONObject hour = (JSONObject)weather.get(i);
                    JSONObject main = (JSONObject)hour.get("main");
                    JSONObject wind = ((JSONObject)main.get("wind"));
                    JSONObject rain = ((JSONObject)hour.get("rain"));
                    Date time = FORMAT_OUTPUT.parse((String)hour.get("dt_txt"));
                    double temp = Double.parseDouble(main.getOrDefault("temp", 0).toString());
                    double wind_speed = (wind == null ? 0 : Double.parseDouble(wind.getOrDefault("speed", 0).toString()));
                    double rainfall = (rain == null ? 0 : Double.parseDouble(rain.getOrDefault("3h", 0).toString()));
                    WeatherSlice slice = new WeatherSlice(time, temp, wind_speed, rainfall, 0, 0, 0, WeatherSlice.Status.CLOUD);
                    hourly[3*i] = slice;
                    hourly[3*i+1] = slice;
                    hourly[3*i+2] = slice;
                    if (day == 0 || time.getHours() == 12) {
                        daily[day++] = slice;
                    }
                }
                System.out.println(Arrays.deepToString(daily));
                System.out.println(Arrays.deepToString(hourly));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private String readUrl(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer buffer = new StringBuffer();
        int read;
        while ((read = reader.read()) != -1) {
            buffer.append((char)read);
        }
        return buffer.toString();
    }

    private enum Location {
        BEACONS("Brecon Beacons", "51.881470,-3.443505");

        String coord;
        String name;

        Location(String name, String coord) {
            this.name = name;
            this.coord = coord;
        }
    }

    public static void main(String[] args) {
        getInstance();
    }
}
