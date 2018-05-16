package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.util.IconBasket;
import uk.ac.cam.ia.group14.util.UpdateableJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class SummaryPanel extends UpdateableJPanel implements ActionListener{

    public static String cardName = "SummaryPanel";

    private final Color CONSTANTS_row1Background = new Color(100, 248, 255);
    private final Color CONSTANTS_row2Background = new Color(210, 248, 255);



    private final String defaultFontName = "Ariel";
    private final Font CONSTANTS_row1LocationNameFont = new Font(defaultFontName, Font.PLAIN, 18);
    private final Font CONSTANTS_row1BackButtonFont = new Font(defaultFontName, Font.PLAIN, 18);
    private final String[] CONSTANTS_weekStrings = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private final int CONSTANTS_daysCount = 5, CONSTANTS_forecastIconSize = 100;

    private final double CONSTANTS_rowForecastWeight = 1.0 / (double)CONSTANTS_daysCount;

    private final double CONSTANTS_backButtonWeight = 0.2, CONSTANTS_regionNameWeight = 0.8;
    private final double CONSTANTS_row1Weight = 0.1, CONSTANTS_row2Weight = (1.0 - CONSTANTS_row1Weight);

    private JPanel backButtonAndNamePane;
    private JPanel forecastPane;

    private JButton row1BackBtn;
    private JLabel row1LocationNameLbl;


    private ArrayList<RowForecastFragment> row2RowForecastFragments;



    private void initBackButtonAndName() {
        row1BackBtn = new JButton("<");
        row1BackBtn.setBorder(null);
        row1BackBtn.setBackground(CONSTANTS_row1Background);
        row1BackBtn.setFont(CONSTANTS_row1BackButtonFont);

        GridBagConstraints backButtonConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, CONSTANTS_backButtonWeight, 1.0);
        backButtonAndNamePane.add(row1BackBtn, backButtonConstraints);

        row1LocationNameLbl = new JLabel("LOCATION NAME HERE", SwingConstants.CENTER);
        row1LocationNameLbl.setFont(CONSTANTS_row1LocationNameFont);
        GridBagConstraints labelLocationConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, CONSTANTS_regionNameWeight, 1.0);
        row1LocationNameLbl.setBackground(CONSTANTS_row1Background);

        backButtonAndNamePane.setBackground(CONSTANTS_row1Background);
        backButtonAndNamePane.add(row1LocationNameLbl, labelLocationConstraints);
    }



    private RowForecastFragment getForecastRow() {

        String dayOfWeekString = "M";
        ImageIcon forecastIconImage =
                IconBasket.getResizedIcon(CONSTANTS_forecastIconSize, CONSTANTS_forecastIconSize,true,
                        false, true, false, false, false);

        ArrayList<String> leftStatsData, rightStatsData;
        leftStatsData = new ArrayList<>();
        leftStatsData.add("leftstat");
        leftStatsData.add("leftstat");
        leftStatsData.add("leftstat");

        rightStatsData = new ArrayList<>();
        rightStatsData.add("rightstat");
        rightStatsData.add("rightstat");

        RowForecastFragment wholeThing = new RowForecastFragment(defaultFontName, dayOfWeekString, leftStatsData, forecastIconImage, rightStatsData);
        row2RowForecastFragments.add(wholeThing);

        return wholeThing;
    }

    private void initForecastPane() {
        forecastPane.setBackground(CONSTANTS_row2Background);

        row2RowForecastFragments = new ArrayList<>();

        int gridY=0;

        double dummyRowWeight = 0.5 / (CONSTANTS_daysCount);


        GridBagConstraints dummyRowConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, dummyRowWeight, 1.0);
        JPanel dummy = new JPanel();
        dummy.setBackground(CONSTANTS_row2Background);
        forecastPane.add(dummy, dummyRowConstraints);

        for (int i=0; i<CONSTANTS_daysCount; i++) {
            RowForecastFragment curRow = getForecastRow();

            GridBagConstraints wholeThingConstraints =
                    getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, CONSTANTS_rowForecastWeight, 1.0);
            forecastPane.add(curRow, wholeThingConstraints);

            dummyRowConstraints =
                    getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, dummyRowWeight, 1.0);
            dummy = new JPanel();
            dummy.setBackground(CONSTANTS_row2Background);
            forecastPane.add(dummy, dummyRowConstraints);

        }
    }

    public SummaryPanel(){
        this.setLayout(new GridBagLayout());

        GridBagConstraints backButtonAndNameConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, 1.0, CONSTANTS_row1Weight);

        backButtonAndNamePane = new JPanel(new GridBagLayout());

        GridBagConstraints forecastConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 1, 1.0, CONSTANTS_row2Weight);
        forecastConstraints.fill = GridBagConstraints.BOTH;


        forecastPane = new JPanel(new GridBagLayout());

        this.add(backButtonAndNamePane, backButtonAndNameConstraints);
        this.add(forecastPane, forecastConstraints);

        initBackButtonAndName();
        initForecastPane();

        row1BackBtn.addActionListener(this);
    }

    /*public SummaryPanel(JFrame testFrame) {
        this.testFrame = testFrame;
    }*/


    public void update() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
