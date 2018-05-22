package uk.ac.cam.ia.group14.util;

import java.util.Date;

public class WeatherSlice {
    private final Date time;
    private final double temp;
    private final double wind;
    private final double rain;
    private final double visibility;
    private final double cloudLevel;
    private final double humidity;
    private final Status status;

    /**
     * Represents a discrete slice of time for which we have weather data. Note that some fields may be null of they were not available.
     * @param time (start) time of slide
     * @param temp temperatur.
     * @param wind wind speed
     * @param rain rain level
     * @param visibility visibility
     * @param cloudLevel height of clouds
     * @param humidity humidity
     * @param status of type WeatherSlice.Status, this represents the status of the climate, i.e. rain/clear/thumderstorm/etc.
     */
    public WeatherSlice(Date time, double temp, double wind, double rain, double visibility, double cloudLevel, double humidity, Status status) {
        this.time = time;
        this.temp = temp;
        this.wind = wind;
        this.rain = rain;
        this.visibility = visibility;
        this.cloudLevel = cloudLevel;
        this.humidity = humidity;
        this.status = status;
    }

    /**
     * Gets time of slice.
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Gets temperature of slice.
     * @return temperature
     */
    public double getTemp() {
        return temp;
    }

    /**
     * Get wind speed of the slice.
     * @return wind speed
     */
    public double getWind() {
        return wind;
    }

    /**
     * Get the rain level of the slice.
     * @return rain
     */
    public double getRain() {
        return rain;
    }

    /**
     * Get the visibility of the slice.
     * In this prototype these values are incorrect as the data was not available for free.
     * @return visibility
     */
    public double getVisibility() {
        return visibility;
    }

    /**
     * Gets the cloud level of the slice.
     * In this prototype these values are incorrect as the data was not available for free.
     * @return cloud level
     */
    public double getCloudLevel() {
        return cloudLevel;
    }

    /**
     * Gets the freezing altitude of this slice.
     * In this prototype these values are incorrect as the data was not available for free.
     * @return freezing altitude
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Get the status of this slice.
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    @Deprecated
    public double getFreezingAltitude() {
        return 0;
    }

    /**
     * Status enum.
     */
    public enum Status {
        CLOUDS, //HAS CLOUDS (OR ASH STORMS)
        SUN, //CLEAR
        RAIN, //RAIN AND CLOUDS
        SNOW, //SNOW AND CLOUDS
        THUNDERSTORM; //RAIN CLOUDS AND LIGHTNING
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
                ", freezingAltitude=" + humidity +
                ", status=" + status +
                '}';
    }
}
