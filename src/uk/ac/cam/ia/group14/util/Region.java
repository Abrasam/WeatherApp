package uk.ac.cam.ia.group14.util;

import java.util.Arrays;

public class Region {

    private final String name;
    private final WeatherSlice[] hours;
    private final WeatherSlice[] days;

    /**
     * Stores information about the weather in a particular mountain region.
     * @param name name of the mountain range (e.g. Brecon Beacons)
     * @param hours the weather slices representing the hourly data
     * @param days the weather slices representing the daily data
     */
    public Region(String name, WeatherSlice[] hours, WeatherSlice[] days) {
        this.name = name;
        this.hours = hours;
        this.days = days;
    }

    /**
     * Get the name of the region.
     * @return region
     */
    public String getName() {
        return name;
    }

    /**
     * Get the array of hourly time slices.
     * @return the array
     */
    public WeatherSlice[] getHours() {
        return hours;
    }

    /**
     * Get the array of daily time slices.
     * @return the array
     */
    public WeatherSlice[] getDays() {
        return days;
    }

    @Override
    public String toString() {
        return "Region{" +
                "name='" + name + '\'' +
                ", hours=" + Arrays.toString(hours) +
                ", days=" + Arrays.toString(days) +
                '}';
    }
}
