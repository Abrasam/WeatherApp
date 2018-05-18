package uk.ac.cam.ia.group14.summary;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.List;

/**
 * Class which provides various functionalities for the SummaryPanel (but including them there would make a mess)
 */

public class SummaryUtil {

    private static final String[] CONSTANTS_weekStrings = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    public static List<String> makeList(String... strings) {
        ArrayList<String> ret = new ArrayList<>();
        for (String s : strings) {
            ret.add(s);
        }
        return ret;
    }

    // Shorcut for setting constraints
    public static GridBagConstraints getGridBagConstraints(int fill, int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints ret = new GridBagConstraints();

        ret.fill = fill;
        ret.gridx = gridx;
        ret.gridy = gridy;
        ret.weightx = weightx;
        ret.weighty = weighty;

        return ret;
    }

    // Gets the day number where Monday is 0 and Sunday is 6 out of a Calendar date
    public static int getDayNumber(GregorianCalendar calDate) {

        int sub = (calDate.getFirstDayOfWeek() == Calendar.SUNDAY) ? 1 : 0; // Usually it starts on Sunday, so we need to subtract 1

        int dayOfWeek = calDate.get(GregorianCalendar.DAY_OF_WEEK) - sub - 1; // We also need to subtract another 1 because indices are 0-based
        while (dayOfWeek < 0) dayOfWeek += 7; // Because of this subtraction, dayOfWeek might've gone out of bounds 0~6
        while (dayOfWeek >= 7) dayOfWeek -= 7;

        return dayOfWeek;
    }

    // the same but from a date
    public static int getDayNumber(Date date) {
        GregorianCalendar calDate = new GregorianCalendar();
        calDate.setTime(date);
        return getDayNumber(calDate);
    }

    public static String getDayOfWeekString(GregorianCalendar calDate) {
        return CONSTANTS_weekStrings[ getDayNumber(calDate) ];
    }
    public static String getDayOfWeekString(Date Date) {
        return CONSTANTS_weekStrings[ getDayNumber(Date) ];
    }

    // parse dayOfWeek integer to a String
    public static String getDayOfWeekString(int dayOfWeek) {
        dayOfWeek %= 7;
        return CONSTANTS_weekStrings[ dayOfWeek ];
    }
}
