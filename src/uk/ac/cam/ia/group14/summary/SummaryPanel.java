package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.sjs252.WeatherFetcher;
import uk.ac.cam.ia.group14.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class SummaryPanel extends UpdateableJPanel implements MouseListener{

    public static String cardName = "SummaryPanel";

    //Wilson blue D7E1FF
    // Ross green 22B14C
    // Ross blue 00A2E8

    // Number of days for the summary
    public static final int CONSTANTS_daysCount = 5;

    // Colour constants
    public static final Color CONSTANTS_row1FontColour = Color.BLACK;
    public static final Color CONSTANTS_row1Background = Color.decode("#D7E1FF");
    public static final Color CONSTANTS_row2RowForecastsColor = Color.decode("#00A2E8");;
    public static final Color CONSTANTS_row2RowStripesColor = Color.decode("#D7E1FF");

    // Font constants
    public static final String CONSTANTS_defaultFontName = "Courier 10 Pitch";
    public static final Font CONSTANTS_row1LocationNameFont = new Font(CONSTANTS_defaultFontName, Font.BOLD, 40);
    public static final Font CONSTANTS_row1BackButtonFont = new Font(CONSTANTS_defaultFontName, Font.PLAIN, 40);

    public static final Font CONSTANTS_row2DaysOfWeekFont = new Font(CONSTANTS_defaultFontName, Font.BOLD, 19);
    public static final Font CONSTANTS_row2StatsFont1 = new Font(CONSTANTS_defaultFontName, Font.PLAIN, 18);
    public static final Font CONSTANTS_row2StatsFont2 = new Font(CONSTANTS_defaultFontName, Font.PLAIN, 24);

    // Weight-related constants
    public static final double CONSTANTS_row1Weight = 0.1, CONSTANTS_row2Weight = (1.0 - CONSTANTS_row1Weight);
    public static final double CONSTANTS_row1BackButtonWeight = 0.2, CONSTANTS_row1RegionNameWeight = 0.8;

    public static final double CONSTANTS_row2RowForecastWeight = 1.0 / (double)CONSTANTS_daysCount;
    public static final double CONSTANTS_row2DummyForecastRowWeight = 0.1;

    public static final double CONSTANTS_row2DayOfWeekWeight = 0.25;
    public static final double CONSTANTS_row2ForecastIconWeight = 0.25;
    public static final double CONSTANTS_row2SingleStatWeight =
            (1.0 - CONSTANTS_row2DayOfWeekWeight - CONSTANTS_row2ForecastIconWeight)/2.0;

    // Prefixes/suffixes
    public static final String CONSTANTS_row1LocationNamePrefix = "";
    public static final String CONSTANTS_celsius = "°C", CONSTANTS_kmh = " km/h",
            CONSTANTS_cloudLevelSuffix = " ft", CONSTANTS_visibilitySuffix = " %", CONSTANTS_freezingAltitudeSuffix = " ft";

    public static final int CONSTANTS_row2ForecastIconSize = 90;

    // Prefix icons of the left statistics for each row are precomputed here
    public static final ImageIcon CONSTANTS_visibilityIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/vis.png");
    public static final ImageIcon CONSTANTS_couldLevelIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/cloud.png");
    public static final ImageIcon CONSTANTS_freezingIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/freeze.png");

    public static final JLabel[] CONSTANTS_row2LeftTagsArray = {
            new JLabel(CONSTANTS_visibilityIcon),
            new JLabel(CONSTANTS_couldLevelIcon),
            new JLabel(CONSTANTS_freezingIcon)};
    //public static final List<JLabel> CONSTANTS_row2LeftTags = Arrays.asList(CONSTANTS_row2LeftTagsArray);


    // Size of the left&right stats on each row
    public static final int CONSTANTS_row2LeftStatsSize = 3, CONSTANTS_row2RightStatsSize = 2;


    // Weather data
    private RegionID stateRegion;
    private Region stateData;

    // The two main panels in the grid - those are row1 for backButtonAndName and row2 for forecastFragmentsPane
    private JPanel backButtonAndNamePane;
    private JPanel forecastFragmentsPane;

    // The two elements on the first row
    private JLabel row1BackBtn;
    private JLabel row1LocationNameLbl;

    // All the elements on the second row (which contains many rows of fragments itself)
    private ArrayList<RowForecastFragment> row2RowForecastFragments;

    private List<JLabel> getFreshListOfRow2Tags() {
        List<JLabel> ret = new ArrayList<>();

        for (JLabel tag : CONSTANTS_row2LeftTagsArray) {
            ret.add(new JLabel(tag.getIcon()));
        }

        return ret;
    }

    // Initialise the bare bones of the back button and location name
    private void initBackButtonAndLocationName() {
        row1BackBtn = new JLabel("<", SwingConstants.CENTER);
        row1BackBtn.setForeground(CONSTANTS_row1FontColour);
        row1BackBtn.setBorder(null);
        row1BackBtn.setBackground(CONSTANTS_row1Background);
        row1BackBtn.setFont(CONSTANTS_row1BackButtonFont);

        GridBagConstraints backButtonConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, CONSTANTS_row1BackButtonWeight, 1.0);
        backButtonAndNamePane.add(row1BackBtn, backButtonConstraints);

        row1LocationNameLbl = new JLabel("", SwingConstants.CENTER);
        row1LocationNameLbl.setForeground(CONSTANTS_row1FontColour);
        row1LocationNameLbl.setFont(CONSTANTS_row1LocationNameFont);
        GridBagConstraints labelLocationConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, CONSTANTS_row1RegionNameWeight, 1.0);
        row1LocationNameLbl.setBackground(CONSTANTS_row1Background);

        backButtonAndNamePane.setBackground(CONSTANTS_row1Background);
        backButtonAndNamePane.add(row1LocationNameLbl, labelLocationConstraints);
    }


    // Initialise the bare bones of a forecastRowFragment
    private RowForecastFragment getRowForecastFragment() {

        String dayOfWeekString = "";
        ImageIcon forecastIconImage = null;

        ArrayList<String> leftStatsData, rightStatsData;
        leftStatsData = new ArrayList<>();
        for (int i = 0; i< CONSTANTS_row2LeftStatsSize; i++) leftStatsData.add("");

        rightStatsData = new ArrayList<>();
        for (int i = 0; i< CONSTANTS_row2RightStatsSize; i++) rightStatsData.add("");

        RowForecastFragment wholeThing =
                new RowForecastFragment(dayOfWeekString, getFreshListOfRow2Tags(), leftStatsData, forecastIconImage, rightStatsData);
        row2RowForecastFragments.add(wholeThing);

        return wholeThing;
    }

    // Initialise the wole forecast pane by adding all RowForecastFragments
    private void initForecastPane() {
        forecastFragmentsPane.setBackground(CONSTANTS_row2RowStripesColor);

        row2RowForecastFragments = new ArrayList<>();

        int gridY=0;

        // Dummy rows are added to the grid in order to separate rows
        // The weight for each dummy row is calculated
        double dummyRowWeight = CONSTANTS_row2DummyForecastRowWeight / (double)(CONSTANTS_daysCount);


        for (int i=0; i<CONSTANTS_daysCount; i++) {
            RowForecastFragment curRow = getRowForecastFragment();

            GridBagConstraints wholeThingConstraints =
                    getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, CONSTANTS_row2RowForecastWeight, 1.0);
            forecastFragmentsPane.add(curRow, wholeThingConstraints);

            if (i+1 < CONSTANTS_daysCount) {

                GridBagConstraints dummyRowConstraints =
                        getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, dummyRowWeight, 1.0);
                JPanel dummy = new JPanel();
                dummy.setBackground(CONSTANTS_row2RowStripesColor);
                forecastFragmentsPane.add(dummy, dummyRowConstraints);
            }
        }
    }

    // Triggers the initialisation of the two main rows in the grid
    private void initBareBones() {

        this.setLayout(new GridBagLayout());

        GridBagConstraints backButtonAndNameConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, 1.0, CONSTANTS_row1Weight);

        backButtonAndNamePane = new JPanel(new GridBagLayout());

        GridBagConstraints forecastConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 1, 1.0, CONSTANTS_row2Weight);
        forecastConstraints.fill = GridBagConstraints.BOTH;


        forecastFragmentsPane = new JPanel(new GridBagLayout());

        this.add(backButtonAndNamePane, backButtonAndNameConstraints);
        this.add(forecastFragmentsPane, forecastConstraints);

        initBackButtonAndLocationName();
        initForecastPane();

        row1BackBtn.addMouseListener(this);

    }

    private MainFrame mainFrame;

    public SummaryPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;

        stateRegion = mainFrame.getDatum().getCurrentMountainRegion();

        if (stateRegion == null) {
            System.out.println("Dude, the region is null..");
        }

        initBareBones();
        update();
    }

    // Updates the data in all row fragments
    private void updateRowFragments() {

        System.out.println(stateData.getDays()[0].getTime());

        Date initDate = stateData.getDays()[0].getTime();

        GregorianCalendar curCalDate = new GregorianCalendar();
        curCalDate.setTime(initDate);
        int dayOfWeek = SummaryUtil.getDayNumber( curCalDate );


        for (int i=0; i<CONSTANTS_daysCount; i++) {
            RowForecastFragment curRowForecast = row2RowForecastFragments.get(i);
            WeatherSlice curWeatherSlice = stateData.getDays()[i];
            ImageIcon curIcon = IconBasket.getResizedIcon(CONSTANTS_row2ForecastIconSize, CONSTANTS_row2ForecastIconSize,
                    true, curWeatherSlice.getStatus());
            String tempString, visString, cloudString, windString, freezeString, curDayOfWeek;
            Integer tempInt, visInt, cloudsInt, windInt, freezeInt;
            List<String> curLeftStats, curRightStats;

            curDayOfWeek = SummaryUtil.getDayOfWeekString(dayOfWeek+i);

            tempInt = (int) Math.round(curWeatherSlice.getTemp());
            visInt = (int) Math.round(curWeatherSlice.getVisibility());
            cloudsInt = (int) Math.round(curWeatherSlice.getCloudLevel());
            windInt = (int) Math.round(curWeatherSlice.getWind());
            freezeInt = (int) Math.round(curWeatherSlice.getFreezingAltitude());


            visString = visInt.toString() + CONSTANTS_visibilitySuffix;
            cloudString = cloudsInt.toString() + CONSTANTS_cloudLevelSuffix;
            freezeString = freezeInt.toString() + CONSTANTS_freezingAltitudeSuffix;

            tempString = tempInt.toString() + CONSTANTS_celsius;
            windString = windInt.toString() + CONSTANTS_kmh;

            curLeftStats = SummaryUtil.makeList(visString, cloudString, freezeString);
            curRightStats = SummaryUtil.makeList(tempString, windString);

            curRowForecast.updateData(curDayOfWeek, curIcon, curLeftStats, curRightStats);

            curCalDate.add(Calendar.DATE, 1);
        }
    }

    // Function, which is called when this screen is accessed. It updates the data to be displayed
    public void update() {
        stateRegion = mainFrame.getDatum().getCurrentMountainRegion();
        stateData = WeatherFetcher.getInstance().getRegion(stateRegion);
        row1LocationNameLbl.setText(CONSTANTS_row1LocationNamePrefix + stateData.getName().toUpperCase());

        updateRowFragments();
    }

    // Function which specifies what to do when the back button is pressed
    // It makes a request to change the active screen and mainFrame takes care of everything else
    private void actionBackButtonPressed() {
        mainFrame.changeCard(DetailPanel.cardName);
    }

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
