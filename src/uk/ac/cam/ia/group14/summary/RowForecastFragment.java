package uk.ac.cam.ia.group14.summary;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class RowForecastFragment extends JPanel{

    private String defaultFontName;

    private final Font CONSTANTS_dayOfWeekFont = new Font(defaultFontName, Font.PLAIN, 19);
    private final double CONSTANTS_dayOfWeekWeight = 0.25,
            CONSTANTS_forecastIconWeight = 0.25,
            CONSTANTS_singleStatWeight = (1.0 - CONSTANTS_dayOfWeekWeight - CONSTANTS_forecastIconWeight)/2.0;

    private JLabel dayOfWeekLbl;
    private InfoFragment leftStatsFragment;
    private JLabel forecastIconLbl;
    private InfoFragment rightStatsFragment;

    private String dayOfWeekString;
    private ImageIcon forecastIconImage;
    private List<String> leftStatsData, rightStatsData;


    private void initComponents() {

        dayOfWeekLbl = new JLabel("", SwingConstants.CENTER);
        dayOfWeekLbl.setFont(CONSTANTS_dayOfWeekFont);
        GridBagConstraints dayOfWeekConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, CONSTANTS_dayOfWeekWeight, 1.0);
        this.add(dayOfWeekLbl, dayOfWeekConstraints);

        forecastIconLbl = new JLabel("", SwingConstants.CENTER);
        GridBagConstraints forecastIconConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 2, 0, CONSTANTS_forecastIconWeight, 1.0);
        this.add(forecastIconLbl, forecastIconConstraints);

        leftStatsFragment = new InfoFragment(defaultFontName, leftStatsData);
        GridBagConstraints leftStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, CONSTANTS_singleStatWeight, 1.0);
        this.add(leftStatsFragment, leftStatsConstraints);

        rightStatsFragment = new InfoFragment(defaultFontName, rightStatsData);
        GridBagConstraints rightStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 3, 0, CONSTANTS_singleStatWeight, 1.0);
        this.add(rightStatsFragment, rightStatsConstraints);
    }

    private void updateForecastStats() {
        leftStatsFragment.setData(leftStatsData);
        rightStatsFragment.setData(rightStatsData);

        leftStatsFragment.setBackground(Color.YELLOW);
        rightStatsFragment.setBackground(Color.RED);
    }
    private void updateDayOfWeekAndIcon() {
        dayOfWeekLbl.setText(dayOfWeekString);
        forecastIconLbl.setIcon(forecastIconImage);
    }

    private void updateAll() {
        updateDayOfWeekAndIcon();
        updateForecastStats();
    }


    public RowForecastFragment(String defaultFontName, String dayOfWeekString, List<String> leftStatsData, ImageIcon forecastIconImage, List<String> rightStatsData) {
        super(new GridBagLayout());

        this.defaultFontName = defaultFontName;
        this.dayOfWeekString = dayOfWeekString;
        this.leftStatsData = leftStatsData;
        this.forecastIconImage = forecastIconImage;
        this.rightStatsData = rightStatsData;

        initComponents();

        updateAll();
    }



}
