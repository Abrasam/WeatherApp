package uk.ac.cam.ia.group14.util;

public class Region {

    private final String name;
    private WeatherSlice[] hours;
    private WeatherSlice[] days;

    public Region(String name) {
        this.name = name;
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
}
