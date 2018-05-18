package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.sjs252.WeatherFetcher;
import uk.ac.cam.ia.group14.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class SummaryPanel extends UpdateableJPanel implements MouseListener{

    public static String cardName = "SummaryPanel";

    private final Color CONSTANTS_row1Background = new Color(19,78,19);
    private final Color CONSTANTS_row2Background = new Color(24,98,24);



    private final String defaultFontName = "Ariel";
    private final Font CONSTANTS_row1LocationNameFont = new Font(defaultFontName, Font.PLAIN, 20);
    private final Font CONSTANTS_row1BackButtonFont = new Font(defaultFontName, Font.PLAIN, 18);
    private final String CONSTANTS_row1LocationNamePrefix = "5-day summary for ";

    private final int CONSTANTS_daysCount = 5, CONSTANTS_forecastIconSize = 90;

    private final double CONSTANTS_rowForecastWeight = 1.0 / (double)CONSTANTS_daysCount;

    private final double CONSTANTS_backButtonWeight = 0.2, CONSTANTS_regionNameWeight = 0.8;
    private final double CONSTANTS_row1Weight = 0.1, CONSTANTS_row2Weight = (1.0 - CONSTANTS_row1Weight);
    private final double CONSTANTS_dummyForecastRowWeight = 0.5;

    private final String CONSTANTS_celsius = "℃", CONSTANTS_kmh = "km/h", CONSTANTS_percent = "%";

    private final int CONSTANTS_leftStatsSize = 2, CONSTANTS_rightStatsSize = 2;

    private RegionID stateRegion;
    private Region stateData;

    private JPanel backButtonAndNamePane;
    private JPanel forecastPane;

    private JLabel row1BackBtn;
    private JLabel row1LocationNameLbl;


    private ArrayList<RowForecastFragment> row2RowForecastFragments;



    private void initBackButtonAndName() {
        row1BackBtn = new JLabel("<", SwingConstants.CENTER);
        row1BackBtn.setForeground(Color.WHITE);
        row1BackBtn.setBorder(null);
        row1BackBtn.setBackground(CONSTANTS_row1Background);
        row1BackBtn.setFont(CONSTANTS_row1BackButtonFont);

        GridBagConstraints backButtonConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, CONSTANTS_backButtonWeight, 1.0);
        backButtonAndNamePane.add(row1BackBtn, backButtonConstraints);

        row1LocationNameLbl = new JLabel("LOCATION NAME HERE", SwingConstants.CENTER);
        row1LocationNameLbl.setForeground(Color.WHITE);
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
        for (int i=0; i<CONSTANTS_leftStatsSize; i++) leftStatsData.add("");

        rightStatsData = new ArrayList<>();
        for (int i=0; i<CONSTANTS_rightStatsSize; i++) rightStatsData.add("");

        RowForecastFragment wholeThing = new RowForecastFragment(defaultFontName, dayOfWeekString, leftStatsData, forecastIconImage, rightStatsData);
        row2RowForecastFragments.add(wholeThing);

        return wholeThing;
    }

    private void initForecastPane() {
        forecastPane.setBackground(CONSTANTS_row2Background);

        row2RowForecastFragments = new ArrayList<>();

        int gridY=0;

        double dummyRowWeight = CONSTANTS_dummyForecastRowWeight / (CONSTANTS_daysCount);


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

    private void initBareBones() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints backButtonAndNameConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, 1.0, CONSTANTS_row1Weight);

        backButtonAndNamePane = new JPanel(new GridBagLayout());

        GridBagConstraints forecastConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 1, 1.0, CONSTANTS_row2Weight);
        forecastConstraints.fill = GridBagConstraints.BOTH;


        forecastPane = new JPanel(new GridBagLayout());

        this.add(backButtonAndNamePane, backButtonAndNameConstraints);
        this.add(forecastPane, forecastConstraints);

        initBackButtonAndName();
        initForecastPane();

        row1BackBtn.addMouseListener(this);

        stateRegion = mainFrame.getDatum().getCurrentMountainRegion();
    }

    private MainFrame mainFrame;

    public SummaryPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        initBareBones();
        update();
    }


    private void updateRowFragments() {

        System.out.println(stateData.getDays()[0].getTime());

        Date initDate = stateData.getDays()[0].getTime();

        GregorianCalendar curCalDate = new GregorianCalendar();
        curCalDate.setTime(initDate);
        int dayOfWeek = SummaryUtil.getDayNumber( curCalDate );


        for (int i=0; i<CONSTANTS_daysCount; i++) {
            RowForecastFragment curRowForecast = row2RowForecastFragments.get(i);
            WeatherSlice curWeatherSlice = stateData.getDays()[i];
            ImageIcon curIcon = IconBasket.getResizedIcon(CONSTANTS_forecastIconSize, CONSTANTS_forecastIconSize,
                    true, curWeatherSlice.getStatus());
            String tempString, visString, cloudString, windString, curDayOfWeek;
            Integer tempInt, visInt, cloudsInt, windInt;
            List<String> curLeftStats, curRightStats;

            curDayOfWeek = SummaryUtil.getDayOfWeekString(dayOfWeek+i);

            tempInt = (int) Math.round(curWeatherSlice.getTemp());
            visInt = (int) Math.round(curWeatherSlice.getVisibility());
            cloudsInt = (int) Math.round(curWeatherSlice.getCloudLevel());
            windInt = (int) Math.round(curWeatherSlice.getWind());

            visString = "Visibility: " + visInt.toString() + " % ";
            cloudString = "Clouds: " + cloudsInt.toString();

            tempString = tempInt.toString() + CONSTANTS_celsius;
            windString = windInt.toString() + CONSTANTS_kmh;

            curLeftStats = SummaryUtil.makeList(visString, cloudString);
            curRightStats = SummaryUtil.makeList(tempString, windString);

            curRowForecast.updateData(curDayOfWeek, curIcon, curLeftStats, curRightStats);

            curCalDate.add(Calendar.DATE, 1);
        }
    }

    public void update() {
        stateData = WeatherFetcher.getInstance().getRegion(stateRegion);
        row1LocationNameLbl.setText(CONSTANTS_row1LocationNamePrefix + stateData.getName());

        updateRowFragments();
    }

    private void actionBackButtonPressed() {
        mainFrame.changeCard(DetailPanel.cardName);
    }

    /*@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == row1BackBtn) {
            actionBackButtonPressed();
        }
    }*/

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        actionBackButtonPressed();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
