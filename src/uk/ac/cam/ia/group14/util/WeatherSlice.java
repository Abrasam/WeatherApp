package uk.ac.cam.ia.group14.util;

import java.util.Date;

public class WeatherSlice {
    private final Date time;
    private final int temp;
    private final int wind;
    private final int rain;
    private final int visibility;
    private final int cloudLevel;
    private final int freezingAltitude;
    private final Status status;

    public WeatherSlice(Date time, int temp, int wind, int rain, int visibility, int cloudLevel, int freezingAltitude, Status status) {
        this.time = time;
        this.temp = temp;
        this.wind = wind;
        this.rain = rain;
        this.visibility = visibility;
        this.cloudLevel = cloudLevel;
        this.freezingAltitude = freezingAltitude;
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public int getTemp() {
        return temp;
    }

    public int getWind() {
        return wind;
    }

    public int getRain() {
        return rain;
    }

    public int getVisibility() {
        return visibility;
    }

    public int getCloudLevel() {
        return cloudLevel;
    }

    public int getFreezingAltitude() {
        return freezingAltitude;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        RAIN,
        CLOUD,
        SCATTERED_CLOUD,
        SNOW,
        SUN,
        HAIL;
        //MORE TO BE ADDED YO
    }
}
