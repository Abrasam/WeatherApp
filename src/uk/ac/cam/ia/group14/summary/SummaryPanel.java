package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.sjs252.WeatherFetcher;
import uk.ac.cam.ia.group14.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

import static uk.ac.cam.ia.group14.summary.SummaryUtil.getGridBagConstraints;

public class SummaryPanel extends UpdateableJPanel implements MouseListener{

    public static String cardName = "SummaryPanel";

    // Wilson blue D7E1FF
    // Ross green 22B14C
    // Ross blue 00A2E8

    // whiteish : #dce9ef
    // light blue : 91bbd1
    // dark blue : 3286AA

    // tryBlue : #94a8e6

    // Number of days for the summary
    public static final int CONSTANTS_daysCount = 5;

    // Colour constants
    public static final Color CONSTANTS_row1FontColour = Color.BLACK;
    public static final Color CONSTANTS_row2RowForecastFontColor = Color.BLACK;

    public static final Color CONSTANTS_row1Background = Color.decode("#D7E1FF");
    public static final Color CONSTANTS_row2RowForecastsColor = Color.decode("#94a8e6");;
    public static final Color CONSTANTS_row2RowStripesColor = Color.decode("#D7E1FF");


    // Font constants
    public static final String CONSTANTS_defaultFontName = "Courier New";
    public static final Font CONSTANTS_row1LocationNameFont = new Font(CONSTANTS_defaultFontName, Font.BOLD, 40);

    public static final Font CONSTANTS_row2DaysOfWeekFont = new Font(CONSTANTS_defaultFontName, Font.BOLD, 18);
    public static final Font CONSTANTS_row2StatsFont1 = new Font(CONSTANTS_defaultFontName, Font.PLAIN, 17);
    public static final Font CONSTANTS_row2StatsFont2 = new Font(CONSTANTS_defaultFontName, Font.PLAIN, 23);



    // Weight-related constants
    public static final double CONSTANTS_row1Weight = 0.1, CONSTANTS_row2Weight = (1.0 - CONSTANTS_row1Weight);
    public static final double CONSTANTS_row1BackButtonWeight = 0.15, CONSTANTS_row1RegionNameWeight = 0.95;

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


    // Icons
    public static final ImageIcon CONSTANTS_row1BackButtonIconPressed =
            IconBasket.getResizedIconFromPath(40, 40, "images/general/back-pressed.png");
    public static final ImageIcon CONSTANTS_row1BackButtonIconUnPressed =
            IconBasket.getResizedIconFromPath(40, 40, "images/general/back-unpressed.png");

    // Prefix icons of the left statistics for each row are precomputed below
    public static final ImageIcon CONSTANTS_visibilityIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/vis.png");
    public static final ImageIcon CONSTANTS_couldLevelIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/cloud.png");
    public static final ImageIcon CONSTANTS_freezingIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/freeze.png");

    public static final ImageIcon[] CONSTANTS_row2LeftTagsArray = {
            CONSTANTS_visibilityIcon,
            CONSTANTS_couldLevelIcon,
            CONSTANTS_freezingIcon
    };

    // Prefix icons of the right statistics
    public static final ImageIcon CONSTANTS_temperatureIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/temperature.png");
    public static final ImageIcon CONSTANTS_windIcon =
            IconBasket.getResizedIconFromPath(30, 30, "images/general/wind.png");

    public static final ImageIcon[] CONSTANTS_row2RightTagsArray = {
            CONSTANTS_temperatureIcon,
            CONSTANTS_windIcon
    };

    // Size of the left&right stats on each row
    public static final int CONSTANTS_row2LeftStatsSize = 3, CONSTANTS_row2RightStatsSize = 2;


    // Weather data
    private RegionID stateRegion;
    private Region stateData;

    // The two main panels in the grid - those are row1 for backButtonAndName and row2 for forecastFragmentsPanel
    private JPanel backButtonAndNamePanel;
    private JPanel forecastFragmentsPanel;

    // The two elements on the first row
    private JLabel row1BackBtn;
    private JLabel row1LocationNameLbl;

    // All the elements on the second row (which contains many rows of fragments itself)
    private ArrayList<RowForecastFragment> row2RowForecastFragments;

    // Each row needs to have its own set of tags, despite the fact that they are the same icons
    // This function just copies icons
    /*private List<JLabel> getFreshListOfImages(ImageIcon[] iconsArray) {
        List<JLabel> ret = new ArrayList<>();

        for (ImageIcon tagIcon : iconsArray) {
            ret.add(new JLabel(tagIcon));
        }

        return ret;
    }*/

    // Initialise the bare bones of the back button and location name
    private void initBackButtonAndLocationName() {

        backButtonAndNamePanel.setBackground(CONSTANTS_row1Background);

        // Back Button
        row1BackBtn = new JLabel("", SwingConstants.CENTER);
        row1BackBtn.setIcon(CONSTANTS_row1BackButtonIconUnPressed);
        row1BackBtn.setBorder(null);
        row1BackBtn.setBackground(CONSTANTS_row1Background);

        // Constraints for back button
        GridBagConstraints backButtonConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, CONSTANTS_row1BackButtonWeight, 1.0);

        // Location label
        row1LocationNameLbl = new JLabel("", SwingConstants.CENTER);
        row1LocationNameLbl.setForeground(CONSTANTS_row1FontColour);
        row1LocationNameLbl.setFont(CONSTANTS_row1LocationNameFont);

        // Constraints for location label
        GridBagConstraints labelLocationConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, CONSTANTS_row1RegionNameWeight, 1.0);
        row1LocationNameLbl.setBackground(CONSTANTS_row1Background);

        // Add those to the panel
        backButtonAndNamePanel.add(row1BackBtn, backButtonConstraints);
        backButtonAndNamePanel.add(row1LocationNameLbl, labelLocationConstraints);
    }


    // Initialise the bare bones of a forecastRowFragment
    private RowForecastFragment getRowForecastFragment() {

        // Set some dummy initial values. They will be updated at later stage
        String dayOfWeekString = "";
        ImageIcon forecastIconImage = null;

        // Stats like visibility, cloud level etc. to the left and right of the weather icon
        ArrayList<String> leftStatsData, rightStatsData;
        leftStatsData = new ArrayList<>();
        for (int i = 0; i< CONSTANTS_row2LeftStatsSize; i++) leftStatsData.add("");

        rightStatsData = new ArrayList<>();
        for (int i = 0; i< CONSTANTS_row2RightStatsSize; i++) rightStatsData.add("");

        // Pack everything into a RowForecastFragment
        // The tags lists represent the tag icons next to the data

        List<ImageIcon> leftTagsList = Arrays.asList(CONSTANTS_row2LeftTagsArray);
        List<ImageIcon> rightTagsList = Arrays.asList(CONSTANTS_row2RightTagsArray);

        RowForecastFragment wholeThing =
                new RowForecastFragment(dayOfWeekString,
                        leftTagsList, leftStatsData,
                        forecastIconImage,
                        rightTagsList, rightStatsData);

        row2RowForecastFragments.add(wholeThing);

        return wholeThing;
    }

    // Initialise the wole forecast panel by adding all RowForecastFragments
    private void initForecastPanel() {
        forecastFragmentsPanel.setBackground(CONSTANTS_row2RowStripesColor);

        row2RowForecastFragments = new ArrayList<>();

        // Variable which tracks on which row of the gridBagLayout is the current element to be drawn
        int gridY=0;

        // Dummy rows are added to the grid in order to separate rows
        // The weight for each dummy row is calculated
        double dummyRowWeight = CONSTANTS_row2DummyForecastRowWeight / (double)(CONSTANTS_daysCount);


        for (int i=0; i<CONSTANTS_daysCount; i++) {
            // Generate rowForecastFragment with the function above
            RowForecastFragment curRow = getRowForecastFragment();

            // Set the constraints
            GridBagConstraints wholeThingConstraints =
                    getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, CONSTANTS_row2RowForecastWeight, 1.0);
            forecastFragmentsPanel.add(curRow, wholeThingConstraints);

            // Add a dummy panel so that the rowForecastFragments will appear separated
            GridBagConstraints dummyRowConstraints =
                    getGridBagConstraints(GridBagConstraints.BOTH, 0, gridY++, dummyRowWeight, 1.0);
            JPanel dummy = new JPanel();
            dummy.setBackground(CONSTANTS_row2RowStripesColor);
            forecastFragmentsPanel.add(dummy, dummyRowConstraints);

        }
    }

    // Triggers the initialisation of the two main rows in the grid
    private void initBareBones() {

        this.setLayout(new GridBagLayout());

        // the two panel are created and their constraints are initialised
        GridBagConstraints backButtonAndNameConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, 1.0, CONSTANTS_row1Weight);
        backButtonAndNamePanel = new JPanel(new GridBagLayout());

        GridBagConstraints forecastConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 1, 1.0, CONSTANTS_row2Weight);
        forecastConstraints.fill = GridBagConstraints.BOTH;
        forecastFragmentsPanel = new JPanel(new GridBagLayout());

        // the two panels are added added
        this.add(backButtonAndNamePanel, backButtonAndNameConstraints);
        this.add(forecastFragmentsPanel, forecastConstraints);

        // the panels and their content are initialised
        initBackButtonAndLocationName();
        initForecastPanel();

        row1BackBtn.addMouseListener(this);
    }

    private MainFrame mainFrame;

    public SummaryPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;

        // Initialise panels and everything
        initBareBones();

        // All the panels have no data loaded at the moment. It will be computed when this panel is activated
    }

    // Updates the data in all row fragments
    private void updateRowFragments() {

        Date initDate = stateData.getDays()[0].getTime();

        GregorianCalendar curCalDate = new GregorianCalendar();
        curCalDate.setTime(initDate);
        int dayOfWeek = SummaryUtil.getDayNumber( curCalDate );


        for (int i=0; i<CONSTANTS_daysCount; i++) {
            // Get the current fragment to be updated
            RowForecastFragment curRowForecastFragment = row2RowForecastFragments.get(i);

            // Get the data for this fragment
            WeatherSlice curWeatherSlice = stateData.getDays()[i];

            // Load the image which corresponds to the weather data
            ImageIcon curIcon = IconBasket.getResizedIcon(CONSTANTS_row2ForecastIconSize, CONSTANTS_row2ForecastIconSize,
                    true, curWeatherSlice.getStatus());

            // Strings to be displayed for the stats
            String tempString, visString, cloudString, windString, freezeString, curDayOfWeek;
            Integer tempInt, visInt, cloudsInt, windInt, freezeInt;
            List<String> curLeftStats, curRightStats;

            curDayOfWeek = SummaryUtil.getDayOfWeekString(dayOfWeek+i);

            // Round the values
            tempInt = (int) Math.round(curWeatherSlice.getTemp());
            visInt = (int) Math.round(curWeatherSlice.getVisibility());
            cloudsInt = (int) Math.round(curWeatherSlice.getCloudLevel());
            windInt = (int) Math.round(curWeatherSlice.getWind());
            freezeInt = (int) Math.round(curWeatherSlice.getFreezingAltitude());

            // Add suffixes and make them pretty
            visString = visInt.toString() + CONSTANTS_visibilitySuffix;
            cloudString = cloudsInt.toString() + CONSTANTS_cloudLevelSuffix;
            freezeString = freezeInt.toString() + CONSTANTS_freezingAltitudeSuffix;

            tempString = tempInt.toString() + CONSTANTS_celsius;
            windString = windInt.toString() + CONSTANTS_kmh;

            // Pack those Strings into lists
            curLeftStats = SummaryUtil.makeList(visString, cloudString, freezeString);
            curRightStats = SummaryUtil.makeList(tempString, windString);

            // Once all the fields have been updated, tell the current rowForecastFragment to update its data
            curRowForecastFragment.updateData(curDayOfWeek, curIcon, curLeftStats, curRightStats);

            // Advance to the next day (used for the MON TUE ...)
            curCalDate.add(Calendar.DATE, 1);
        }
    }

    // Function, which is called when this screen is accessed. It updates the data to be displayed
    public void update() {

        stateRegion = mainFrame.getDatum().getCurrentMountainRegion();
        stateData = WeatherFetcher.getInstance().getRegion(stateRegion);

        if (stateRegion == null) {
            // When SummaryPanel is initialised, the data should already be available. If it's null, the caller messed up.
            throw new NoSuchElementException("SUMMARY PANNEL: The region is null. I have no data.");
        }

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


    // Change background of the button accordingly
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        row1BackBtn.setIcon(CONSTANTS_row1BackButtonIconPressed);
        row1BackBtn.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        row1BackBtn.setIcon(CONSTANTS_row1BackButtonIconUnPressed);
        row1BackBtn.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
