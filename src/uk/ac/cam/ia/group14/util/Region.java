package uk.ac.cam.ia.group14.util;

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
}
