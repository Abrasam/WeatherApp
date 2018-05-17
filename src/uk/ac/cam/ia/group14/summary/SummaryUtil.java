package uk.ac.cam.ia.group14.summary;

import java.awt.*;

public class SummaryUtil {
    public static GridBagConstraints getGridBagConstraints(int fill, int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints ret = new GridBagConstraints();

        ret.fill = fill;
        ret.gridx = gridx;
        ret.gridy = gridy;
        ret.weightx = weightx;
        ret.weighty = weighty;

        return ret;
    }
}
