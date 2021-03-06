package uk.ac.cam.ia.group14.summary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class RowForecastFragment extends JPanel{

    // Label for the day of the week MON TUE..
    private JLabel dayOfWeekLbl;
    private String dayOfWeekString;

    // Stats on the left of the icon
    private InfoFragment leftStatsFragment;

    // The icon
    private JLabel forecastIconLbl;
    private ImageIcon forecastIcon;

    // Stats on the right of the icon
    private InfoFragment rightStatsFragment;
    private List<String> leftStatsData, rightStatsData;
    private List<ImageIcon> leftStatsTags, rightStatsTags;

    // init the bare bones of the components
    private void initComponents() {
        this.setBackground(SummaryPanel.CONSTANTS_row2RowForecastsColor);

        dayOfWeekLbl = new JLabel("", SwingConstants.CENTER);
        dayOfWeekLbl.setFont(SummaryPanel.CONSTANTS_row2DaysOfWeekFont);
        dayOfWeekLbl.setForeground(SummaryPanel.CONSTANTS_row2RowForecastFontColor);

        GridBagConstraints dayOfWeekConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, SummaryPanel.CONSTANTS_row2DayOfWeekWeight, 1.0);
        this.add(dayOfWeekLbl, dayOfWeekConstraints);

        forecastIconLbl = new JLabel("", SwingConstants.CENTER);
        GridBagConstraints forecastIconConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 2, 0, SummaryPanel.CONSTANTS_row2ForecastIconWeight, 1.0);
        this.add(forecastIconLbl, forecastIconConstraints);

        leftStatsFragment = new InfoFragment(leftStatsTags, leftStatsData);
        GridBagConstraints leftStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, SummaryPanel.CONSTANTS_row2SingleStatWeight, 1.0);
        this.add(leftStatsFragment, leftStatsConstraints);

        rightStatsFragment = new InfoFragment(rightStatsTags, rightStatsData);
        GridBagConstraints rightStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 3, 0, SummaryPanel.CONSTANTS_row2SingleStatWeight, 1.0);
        this.add(rightStatsFragment, rightStatsConstraints);
    }

    // forecast stats are might be updated multiple times
    private void updateForecastStats() {
        leftStatsFragment.setData(leftStatsData);
        rightStatsFragment.setData(rightStatsData);
    }

    private void updateDayOfWeekAndIcon() {
        dayOfWeekLbl.setText(dayOfWeekString);
        forecastIconLbl.setIcon(forecastIcon);
    }

    private void updateAll() {
        updateDayOfWeekAndIcon();
        updateForecastStats();
    }

    // Constructor which creates a row forecast fragment without right tag images
    public RowForecastFragment(String dayOfWeekString,
                               List<ImageIcon> leftStatsTags, List<String> leftStatsData,
                               ImageIcon forecastIcon,
                               List<String> rightStatsData) {
        super(new GridBagLayout());

        this.dayOfWeekString = dayOfWeekString;
        this.leftStatsTags = leftStatsTags;
        this.leftStatsData = leftStatsData;
        this.forecastIcon = forecastIcon;
        this.rightStatsData = rightStatsData;

        initComponents();

        updateAll();
    }

    // Constructor which creates a row fragment WITH right tag images
    public RowForecastFragment(String dayOfWeekString,
                               List<ImageIcon> leftStatsTags, List<String> leftStatsData,
                               ImageIcon forecastIcon,
                               List<ImageIcon> rightStatsTags, List<String> rightStatsData) {
        super(new GridBagLayout());

        this.dayOfWeekString = dayOfWeekString;
        this.leftStatsTags = leftStatsTags;
        this.leftStatsData = leftStatsData;
        this.forecastIcon = forecastIcon;
        this.rightStatsTags = rightStatsTags;
        this.rightStatsData = rightStatsData;

        initComponents();

        updateAll();
    }

    // Function which is called when data is updated
    public void updateData(String dayOfWeekString, ImageIcon forecastIcon, List<String> leftStatsData, List<String> rightStatsData) {

        this.dayOfWeekString = dayOfWeekString;
        this.forecastIcon = forecastIcon;
        this.leftStatsData = leftStatsData;
        this.rightStatsData = rightStatsData;

        updateAll();
    }
}
