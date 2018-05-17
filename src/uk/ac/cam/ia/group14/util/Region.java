package uk.ac.cam.ia.group14.util;

import java.util.Arrays;

public class Region {

    private final String name;
    private final WeatherSlice[] hours;
    private final WeatherSlice[] days;

    public Region(String name, WeatherSlice[] hours, WeatherSlice[] days) {
        this.name = name;
        this.hours = hours;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public WeatherSlice[] getHours() {
        return hours;
    }

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
