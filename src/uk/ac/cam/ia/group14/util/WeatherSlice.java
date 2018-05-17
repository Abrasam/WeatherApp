package uk.ac.cam.ia.group14.util;

import java.util.Date;

public class WeatherSlice {
    private final Date time;
    private final double temp;
    private final double wind;
    private final double rain;
    private final double visibility;
    private final double cloudLevel;
    private final double freezingAltitude;
    private final Status status;

    public WeatherSlice(Date time, double temp, double wind, double rain, double visibility, double cloudLevel, double freezingAltitude, Status status) {
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

    public double getTemp() {
        return temp;
    }

    public double getWind() {
        return wind;
    }

    public double getRain() {
        return rain;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getCloudLevel() {
        return cloudLevel;
    }

    public double getFreezingAltitude() {
        return freezingAltitude;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        CLOUDS,
        SUN,
        RAIN,
        SNOW,
        BOLT;
        //MORE TO BE ADDED YO
    }

    public enum Parameter {
        TEMPERATURE,
        WIND,
        RAIN;
    }

    @Override
    public String toString() {
        return "WeatherSlice{" +
                "time=" + time +
                ", temp=" + temp +
                ", wind=" + wind +
                ", rain=" + rain +
                ", visibility=" + visibility +
                ", cloudLevel=" + cloudLevel +
                ", freezingAltitude=" + freezingAltitude +
                ", status=" + status +
                '}';
    }
}
