package uk.ac.cam.ia.group14.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A class to retrieve a specified weather icon, based on the weather conditions (check images/weather/... for available icons)
 * Current icons location : https://www.flaticon.com/packs/weather-53
 * Icons credit: https://www.freepik.com/
 *
 */

public class IconBasket {

    private static final int constDayHoursFrom = 6, constDayHoursTo = 20;

    private static final String constNight = "night";
    private static final String constCloud = "cloud";
    private static final String constRain = "rain";
    private static final String constSnow = "snow";
    private static final String constBolt = "bolt";
    private static final String constSun = "sun";

    private static final String constInitDir = "images/weather/";
    private static final String constExt = ".png";

    private static final BufferedImage constDefaultImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);

    private static String addSuffix(String old, String toAdd) {
        if (old.isEmpty()) return toAdd;
        else return old+"-"+toAdd;
    }

    private static String getPath(boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = "";

        if (!isDay) path = addSuffix(path, constNight);

        if (hasSun) path = addSuffix(path, constSun);

        if (hasClouds) path = addSuffix(path, constCloud);

        if (hasRain) path = addSuffix(path, constRain);

        if (hasSnow) path = addSuffix(path, constSnow);

        if (hasBolt) path = addSuffix(path, constBolt);

        path = constInitDir + path + constExt;
        return path;
    }
    private static String getPath(boolean isDay, WeatherSlice.Status status) {
        switch (status) {
            case SUN:
                return getPath(isDay, false, true, false, false, false);
            case CLOUDS:
                return getPath(isDay, true, false, false, false, false);
            case RAIN:
                return getPath(isDay, true, false, true, false, false);
            case THUNDERSTORM:
                return getPath(isDay, true, false, true, false, true);
            case SNOW:
                return getPath(isDay, true, false, false, true, false);
            default:
                return getPath(isDay, true, false, false, false, false);
        }
    }

    private static boolean checkIfDay(GregorianCalendar calendar) {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        return (constDayHoursFrom <= hours && hours <= constDayHoursTo);
    }

    // Every other function refers to this one
    private static BufferedImage getImageFromPath(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            img = constDefaultImage;
        }

        return img;
    }

    private static BufferedImage getResizedImageFromPath(int x, int y, String path) {
        BufferedImage img = getImageFromPath(path);
        img = ResizeImage.resize(img, x, y);
        return img;
    }




    public static BufferedImage getImage(boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = getPath(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);
        return getImageFromPath(path);
    }
    public static BufferedImage getImage(boolean isDay, WeatherSlice.Status status) {
        String path = getPath(isDay, status);
        return getImageFromPath(path);
    }
    public static BufferedImage getImage(GregorianCalendar calDate, WeatherSlice.Status status) {
        String path = getPath(checkIfDay(calDate), status);
        return getImageFromPath(path);
    }
    public static BufferedImage getImage(Date date, WeatherSlice.Status status) {
        GregorianCalendar calDate = new GregorianCalendar();
        calDate.setTime(date);
        String path = getPath(checkIfDay(calDate), status);

        return getImageFromPath(path);
    }


    public static BufferedImage getResizedImage(int x, int y, boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = getPath(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);
        return getResizedImageFromPath(x, y, path);
    }
    public static BufferedImage getResizedImage(int x, int y, boolean isDay, WeatherSlice.Status status) {
        String path = getPath(isDay, status);
        return getResizedImageFromPath(x, y, path);
    }
    public static BufferedImage getResizedImage(int x, int y, GregorianCalendar calDate, WeatherSlice.Status status) {
        String path = getPath(checkIfDay(calDate), status);
        return getResizedImageFromPath(x, y, path);
    }
    public static BufferedImage getResizedImage(int x, int y, Date date, WeatherSlice.Status status) {
        GregorianCalendar calDate = new GregorianCalendar();
        calDate.setTime(date);
        String path = getPath(checkIfDay(calDate), status);

        return getResizedImageFromPath(x, y, path);
    }


    public static ImageIcon getIcon(boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        BufferedImage img = getImage(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);
        return new ImageIcon(img);
    }
    public static ImageIcon getIcon(boolean isDay, WeatherSlice.Status status) {
        BufferedImage img = getImage(isDay, status);
        return new ImageIcon(img);
    }
    public static ImageIcon getIcon(GregorianCalendar date, WeatherSlice.Status status) {
        BufferedImage img = getImage(date, status);
        return new ImageIcon(img);
    }
    public static ImageIcon getIcon(Date date, WeatherSlice.Status status) {
        GregorianCalendar calDate = new GregorianCalendar();
        calDate.setTime(date);

        BufferedImage img = getImage(checkIfDay(calDate), status);
        return new ImageIcon(img);
    }


    public static ImageIcon getResizedIcon(int x, int y, boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        BufferedImage img = getResizedImage(x, y, isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);
        return new ImageIcon(img);
    }
    public static ImageIcon getResizedIcon(int x, int y, boolean isDay, WeatherSlice.Status status) {
        BufferedImage img = getResizedImage(x, y, isDay, status);
        return new ImageIcon(img);
    }
    public static ImageIcon getResizedIcon(int x, int y, GregorianCalendar calDate, WeatherSlice.Status status) {
        BufferedImage img = getResizedImage(x, y, checkIfDay(calDate), status);
        return new ImageIcon(img);
    }

    public static ImageIcon getResizedIcon(int x, int y, Date date, WeatherSlice.Status status) {
        GregorianCalendar calDate = new GregorianCalendar();
        calDate.setTime(date);

        BufferedImage img = getResizedImage(x, y, checkIfDay(calDate), status);
        return new ImageIcon(img);
    }
}
