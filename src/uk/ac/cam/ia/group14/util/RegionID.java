package uk.ac.cam.ia.group14.util;

public enum RegionID {
    CAIRNGORMS("Cairngorms", "0,0"), GRAMPIANS("Grampians", "0,0"), PENNINES("Pennines", "0,0"), MOURNES("Mournes", "0,0"), SNOWDONIA("Snowdonia", "0,0"), BREACONS("Beacons", "51.881470,-3.443505");

    public String coord;
    public String name;

    RegionID(String name, String coord) {
        this.name = name;
        this.coord = coord;
    }
}