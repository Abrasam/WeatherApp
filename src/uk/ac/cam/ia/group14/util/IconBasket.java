package uk.ac.cam.ia.group14.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A class to retrieve a specified weather icon, based on the weather conditions (check images/weather/... for available icons)
 * Current icons location : https://www.flaticon.com/packs/weather-53
 * Icons credit: https://www.freepik.com/
 *
 */

public class IconBasket {

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

    private static BufferedImage getImageFromPath(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            img = constDefaultImage;
        }

        return img;
    }

    public static BufferedImage getImage(boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = getPath(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);

        BufferedImage img = getImageFromPath(path);

        return img;
    }
    public static BufferedImage getResizedImage(int x, int y, boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = getPath(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);

        BufferedImage img = getImageFromPath(path);
        img = ResizeImage.resize(img, x, y);
        return img;
    }
    public static BufferedImage getResizedImage(int x, int y, boolean isDay, WeatherSlice.Status status) {
        String path = getPath(isDay, status);

        BufferedImage img = getImageFromPath(path);
        img = ResizeImage.resize(img, x, y);
        return img;
    }


    public static ImageIcon getIcon(boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = getPath(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);

        ImageIcon icon = new ImageIcon(path);

        return icon;
    }
    public static ImageIcon getIcon(boolean isDay, WeatherSlice.Status status) {
        String path = getPath(isDay, status);

        BufferedImage img = getImageFromPath(path);
        return new ImageIcon(img);
    }

    public static ImageIcon getResizedIcon(int x, int y, boolean isDay, boolean hasClouds, boolean hasSun, boolean hasRain, boolean hasSnow, boolean hasBolt) {
        String path = getPath(isDay, hasClouds, hasSun, hasRain, hasSnow, hasBolt);

        BufferedImage img = getImageFromPath(path);

        img = ResizeImage.resize(img, x, y);

        return new ImageIcon(img);
    }
    public static ImageIcon getResizedIcon(int x, int y, boolean isDay, WeatherSlice.Status status) {
        String path = getPath(isDay, status);

        System.out.println(path);

        BufferedImage img = getImageFromPath(path);
        img = ResizeImage.resize(img, x, y);
        return new ImageIcon(img);
    }
}
