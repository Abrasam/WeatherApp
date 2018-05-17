package uk.ac.cam.ia.group14.summary;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryUtil {

    private static final String[] CONSTANTS_weekStrings = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    public static GridBagConstraints getGridBagConstraints(int fill, int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints ret = new GridBagConstraints();

        ret.fill = fill;
        ret.gridx = gridx;
        ret.gridy = gridy;
        ret.weightx = weightx;
        ret.weighty = weighty;

        return ret;
    }

    public static int getDayNumber(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("u");
        int dayOfWeek = Integer.parseInt( simpleDateFormat.format(date) );

        return dayOfWeek;
    }
    public static String getDayOfWeekString(Date date) {
        int dayOfWeek = getDayNumber(date);
        while (dayOfWeek < 1) dayOfWeek += 7;
        while (dayOfWeek > 7) dayOfWeek -= 7;

        return CONSTANTS_weekStrings[dayOfWeek];
    }

    public static String getDayOfWeekString(int dayOfWeek) {
        while (dayOfWeek < 1) dayOfWeek += 7;
        while (dayOfWeek > 7) dayOfWeek -= 7;

        return CONSTANTS_weekStrings[dayOfWeek];
    }
}
