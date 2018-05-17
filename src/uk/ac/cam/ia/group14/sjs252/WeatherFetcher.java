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

public class WeatherFetcher implements Runnable {

    private static WeatherFetcher INSTANCE;
    private static final String QUERY = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&APPID=062a189260e157451fdeaf8cd4b42e13";
    private static final SimpleDateFormat FORMAT_OUTPUT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Random rand = new Random();

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

    private Map<RegionID, Region> regions = new HashMap<>();

    @Override
    public void run() {
        for (RegionID r : RegionID.values()) {
            try {
                String[] coordinates = r.coord.split(",");
                URL urlHourly = new URL(String.format(QUERY, coordinates[0], coordinates[1]));
                String hourlyJSON = readUrl(urlHourly);
                WeatherSlice[][] data = load(hourlyJSON);
                WeatherSlice[] hourly = data[0];
                WeatherSlice[] daily = data[1];
                //System.out.print(hourlyJSON); tee hee hee
                regions.put(r, new Region(r.name, hourly, daily));
                BufferedWriter writer = new BufferedWriter(new FileWriter("saves/" + r.name + ".txt"));
                writer.write(hourlyJSON);
                writer.close();
            } catch (IOException e) {
                //No new data so check for files.
                try {
                    FileReader reader = new FileReader("saves/" + r.name + ".txt");
                    StringBuffer buffer = new StringBuffer();
                    int read;
                    while ((read = reader.read()) != -1) {
                        buffer.append((char)read);
                    }
                    String json = buffer.toString();
                    load(json);
                } catch (IOException e1) {
                    //File fails when it comes to existing, so no cached data found.
                }
            }
        }
    }

    public Region getRegion(RegionID r) {
        return regions.get(r);
    }

    private WeatherSlice[][] load(String json) {
        JSONArray weather = (JSONArray) new JSONObject(json).get("list");
        WeatherSlice[] hourly = new WeatherSlice[24 * 5];
        WeatherSlice[] daily = new WeatherSlice[6];
        int day = 0;
        for (int i = 0; i < weather.length(); i++) {
            JSONObject hour = null;
            try {
                hour = (JSONObject) weather.get(i);
            } catch (JSONException e) {
            }
            JSONObject main = null;
            try {
                main = (JSONObject) hour.get("main");
            } catch (JSONException e) {
            }
            JSONObject wind = null;
            try {
                wind = ((JSONObject) hour.get("wind"));
            } catch (JSONException e) {
            }
            JSONObject rain = null;
            try {
                rain = ((JSONObject) hour.get("rain"));
            } catch (JSONException e) {
            }
            JSONObject status = null;
            try {
                status = (JSONObject)((JSONArray)hour.get("weather")).get(0);
            } catch (JSONException e) {
            }
            Date time = null;
            try {
                time = FORMAT_OUTPUT.parse((String) hour.get("dt_txt"));
            } catch (ParseException e) {
            }
            double temp = 0;
            try {
                temp = main.getDouble("temp");
            } catch (JSONException e) {
            }
            double wind_speed = 0;
            try {
                wind_speed = (wind == null ? 0 : wind.getDouble("speed"));
            } catch (JSONException e) {
            }
            double rainfall = 0;
            try {
                rainfall = (rain == null ? 0 : rain.getDouble("3h"));
            } catch (JSONException e) {
            }
            //NOTE THAT SOME PARAMETERS WERE RANDOMLY GENERATED HERE BECAUSE THE WEATHER API WOULD NOT PROVIDE THEM FOR FREE.
            WeatherSlice slice = new WeatherSlice(time, temp, wind_speed, rainfall, 100, rand.nextInt(100)+5000, 10000 + rand.nextInt(500), parseStatus(status.getString("main")));
            hourly[3 * i] = slice;
            hourly[3 * i + 1] = slice;
            hourly[3 * i + 2] = slice;
            if (day == 0 || time.getHours() == 12) {
                daily[day++] = slice;
            }
        }
        return new WeatherSlice[][] {hourly, daily};
    }

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

    private String readUrl(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer buffer = new StringBuffer();
        int read;
        while ((read = reader.read()) != -1) {
            buffer.append((char)read);
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(getInstance().getRegion(RegionID.BREACONS));
    }
}
