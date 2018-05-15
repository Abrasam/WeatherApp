package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.util.IconBasket;
import uk.ac.cam.ia.group14.util.UpdateableJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SummaryPanel extends UpdateableJPanel{

    public static String cardName = "SummaryPanel";
    private final int CONSTANTS_daysCount = 5;

    private JPanel backButtonAndNamePane;
    private JPanel forecastPane;

    private JButton row1BtnBack;
    private JLabel row1LblLocation;


    private ArrayList<RowForecast> row2RowForecasts;

    private GridBagConstraints getGridBagConstraints(int fill, int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints ret = new GridBagConstraints();

        ret.fill = fill;
        ret.gridx = gridx;
        ret.gridy = gridy;
        ret.weightx = weightx;
        ret.weighty = weighty;
        ret.ipadx = 20;
        ret.ipady = 20;

        return ret;
    }

    private void initBackButtonAndName() {
        row1BtnBack = new JButton("<");
        row1BtnBack.setBorder(null);
        row1BtnBack.setBackground(Color.WHITE);

        GridBagConstraints backButtonConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, 0.2, 1.0);
        backButtonAndNamePane.add(row1BtnBack, backButtonConstraints);

        row1LblLocation = new JLabel("LOCATION NAME HERE", SwingConstants.CENTER);
        GridBagConstraints labelLocationConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, 0.8, 1.0);
        backButtonAndNamePane.add(row1LblLocation, labelLocationConstraints);
    }



    private RowForecast getForecastRow() {
        JPanel wholeThing = new JPanel(new GridBagLayout());

        String dayOfWeekString = "M";
        ImageIcon forecastIconImage =
                IconBasket.getResizedIcon(100, 100,true, false, true, false, false, false);


        JLabel dayOfWeekLbl = new JLabel(dayOfWeekString, SwingConstants.CENTER);
        GridBagConstraints dayOfWeekConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, 0.1, 1.0);

        System.out.println(dayOfWeekLbl.getSize().toString());

        JPanel leftStatsPnl = new JPanel();
        leftStatsPnl.setLayout(new BoxLayout(leftStatsPnl, BoxLayout.Y_AXIS));

        leftStatsPnl.add( new JLabel("leftstat", SwingConstants.CENTER));
        leftStatsPnl.add( new JLabel("leftstat", SwingConstants.CENTER));
        leftStatsPnl.add( new JLabel("leftstat", SwingConstants.CENTER));

        GridBagConstraints leftStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 1, 0, 0.3, 1.0);

        JLabel forecastIconLbl = new JLabel(forecastIconImage);
        GridBagConstraints forecastIconConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 2, 0, 0.3, 1.0);

        JPanel rightStatsPnl = new JPanel();
        GridBagConstraints rightStatsConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 3, 0, 0.3, 1.0);
        rightStatsPnl.setLayout(new BoxLayout(rightStatsPnl, BoxLayout.Y_AXIS));

        rightStatsPnl.add( new JLabel("rightstat", SwingConstants.CENTER));
        rightStatsPnl.add( new JLabel("rightstat", SwingConstants.CENTER));
        rightStatsPnl.add( new JLabel("rightstat", SwingConstants.CENTER));
        rightStatsPnl.add( new JLabel("rightstat", SwingConstants.CENTER));

        wholeThing.add(dayOfWeekLbl, dayOfWeekConstraints);
        wholeThing.add(leftStatsPnl, leftStatsConstraints);
        wholeThing.add(forecastIconLbl, forecastIconConstraints);
        wholeThing.add(rightStatsPnl, rightStatsConstraints);

        return new RowForecast(dayOfWeekLbl, leftStatsPnl, forecastIconLbl, rightStatsPnl, wholeThing);
    }

    private void initForecastPane() {
        row2RowForecasts = new ArrayList<>();

        double eachWeightX = 1.0 / (double)CONSTANTS_daysCount;

        for (int i=0; i<CONSTANTS_daysCount; i++) {
            RowForecast curRow = getForecastRow();
            JPanel wholeThing = curRow.getWholeThing();
            GridBagConstraints wholeThingConstraints =
                    getGridBagConstraints(GridBagConstraints.BOTH, 0, i, eachWeightX, 1.0);

            //wholeThing.setBackground(Color.getHSBColor(i*(float)100.0, i*(float)100.0, i*(float)100.0));
            forecastPane.add(wholeThing, wholeThingConstraints);
        }
    }

    public SummaryPanel(){
        this.setLayout(new GridBagLayout());

        GridBagConstraints backButtonAndNameConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 0, 1.0, 0.1);

        backButtonAndNamePane = new JPanel(new GridBagLayout());
        //backButtonAndNamePane.setBackground(Color.RED);

        GridBagConstraints forecastConstraints =
                getGridBagConstraints(GridBagConstraints.BOTH, 0, 1, 1.0, 0.9);
        forecastConstraints.fill = GridBagConstraints.BOTH;


        forecastPane = new JPanel(new GridBagLayout());
        //forecastPane.setBackground(Color.GREEN);

        this.add(backButtonAndNamePane, backButtonAndNameConstraints);
        this.add(forecastPane, forecastConstraints);

        initBackButtonAndName();
        initForecastPane();
    }

    /*public SummaryPanel(JFrame testFrame) {
        this.testFrame = testFrame;
    }*/


    public void update() {

    }

}
