package uk.ac.cam.ia.group14.util;

/**
 * An enum to use to identify each mountain range we were using
 * Also stores coordinates to be used to retrieve weather data from the API
 */
public enum RegionID {
    CAIRNGORMS("Cairngorms", "57.060930,-3.607558"), GRAMPIANS("Grampians", "56.917227,-4.001974"), PENNINES("Pennines", "54.703024,-2.489671"), MOURNES("Mournes", "54.166515,-6.083253"), SNOWDONIA("Snowdonia", "52.918107,-3.891689"), BREACONS("Beacons", "51.881470,-3.443505");

    public String coord;
    public String name;

    RegionID(String name, String coord) {
        this.name = name;
        this.coord = coord;
    }
}