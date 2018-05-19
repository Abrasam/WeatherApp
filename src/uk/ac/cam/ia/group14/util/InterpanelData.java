package uk.ac.cam.ia.group14.util;

/**
 * Used to stored data that is needed between screens
 * The only example we needed in the end was which mountain region had been chosen
 * The rest we loaded on demand from the API
 */
public class InterpanelData {
    private RegionID currentMountainRegion;

    public RegionID getCurrentMountainRegion() {
        return currentMountainRegion;
    }

    public void setCurrentMountainRegion(RegionID currentMountainRegion) {
        this.currentMountainRegion = currentMountainRegion;
    }
}
