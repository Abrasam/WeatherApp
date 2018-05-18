package uk.ac.cam.ia.group14.summary;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class RowForecastFragment extends JPanel{

    private String defaultFontName;

    private Color defColor;

    private final Font CONSTANTS_dayOfWeekFont = new Font(defaultFontName, Font.PLAIN, 19);
    private final double CONSTANTS_dayOfWeekWeight = 0.25,
            CONSTANTS_forecastIconWeight = 0.25,
            CONSTANTS_singleStatWeight = (1.0 - CONSTANTS_dayOfWeekWeight - CONSTANTS_forecastIconWeight)/2.0;

    private JLabel dayOfWeekLbl;
    private InfoFragment leftStatsFragment;
    private JLabel forecastIconLbl;
    private InfoFragment rightStatsFragment;

    private String dayOfWeekString;
    private ImageIcon forecastIcon;
    private List<String> leftStatsData, rightStatsData;


    private void initComponents() {
        this.setBackground(defColor);

        dayOfWeekLbl = new JLabel("", SwingConstants.CENTER);
        dayOfWeekLbl.setFont(CONSTANTS_dayOfWeekFont);
        GridBagConstraints dayOfWeekConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, CONSTANTS_dayOfWeekWeight, 1.0);
        this.add(dayOfWeekLbl, dayOfWeekConstraints);

        forecastIconLbl = new JLabel("", SwingConstants.CENTER);
        GridBagConstraints forecastIconConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 2, 0, CONSTANTS_forecastIconWeight, 1.0);
        this.add(forecastIconLbl, forecastIconConstraints);

        leftStatsFragment = new InfoFragment(defaultFontName, defColor, leftStatsData);
        GridBagConstraints leftStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, CONSTANTS_singleStatWeight, 1.0);
        this.add(leftStatsFragment, leftStatsConstraints);

        rightStatsFragment = new InfoFragment(defaultFontName, defColor, rightStatsData);
        GridBagConstraints rightStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 3, 0, CONSTANTS_singleStatWeight, 1.0);
        this.add(rightStatsFragment, rightStatsConstraints);
    }

    private void updateForecastStats() {
        leftStatsFragment.setData(leftStatsData);
        rightStatsFragment.setData(rightStatsData);

        //leftStatsFragment.setBackground(Color.YELLOW);
        //rightStatsFragment.setBackground(Color.RED);
    }
    private void updateDayOfWeekAndIcon() {
        dayOfWeekLbl.setText(dayOfWeekString);
        forecastIconLbl.setIcon(forecastIcon);
    }

    private void updateAll() {
        updateDayOfWeekAndIcon();
        updateForecastStats();
    }


    public RowForecastFragment(String defaultFontName, Color defColor, String dayOfWeekString, List<String> leftStatsData, ImageIcon forecastIcon, List<String> rightStatsData) {
        super(new GridBagLayout());

        this.defaultFontName = defaultFontName;
        this.defColor = defColor;
        
        this.dayOfWeekString = dayOfWeekString;
        this.leftStatsData = leftStatsData;
        this.forecastIcon = forecastIcon;
        this.rightStatsData = rightStatsData;

        initComponents();

        updateAll();
    }

    public void updateData(String dayOfWeekString, ImageIcon forecastIcon, List<String> leftStatsData, List<String> rightStatsData) {

        this.dayOfWeekString = dayOfWeekString;
        this.forecastIcon = forecastIcon;
        this.leftStatsData = leftStatsData;
        this.rightStatsData = rightStatsData;

        updateAll();
    }
}
