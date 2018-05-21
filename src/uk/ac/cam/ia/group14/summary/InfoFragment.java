package uk.ac.cam.ia.group14.summary;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends JPanel {

    private Font defFont;

    private List<JLabel> tagLbl;
    private List<String> statsData;
    private List<JLabel> statsLbl;

    // Initialise the stats and tags (if there are such)
    private void initComponents() {

        this.setBackground(SummaryPanel.CONSTANTS_row2RowForecastsColor);

        int gridX=0, gridY=0;

        double totalRowsWeight = 0.2;
        double singlePadWeight = (1.0 - totalRowsWeight)/2.0; // Put dummy rows on the top and bottom to compress labels/data

        double rowWeight = totalRowsWeight / (double) statsData.size();

        if (tagLbl != null) {
            gridY = 0;

            GridBagConstraints upperPaddingConstraint =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 2.0, singlePadWeight);
            this.add(new JLabel(), upperPaddingConstraint);

            gridY++;
            for (JLabel label : tagLbl) {
                GridBagConstraints constraints =
                        SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 2.0, rowWeight);

                this.add(label, constraints);

                gridY++;
            }


            GridBagConstraints lowerPaddingConstraint =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
            this.add(new JLabel(), lowerPaddingConstraint );

            gridX++;
        }

        statsLbl = new ArrayList<>();
        gridY = 0;


        GridBagConstraints upperPaddingConstraint =
                SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
        this.add(new JLabel(), upperPaddingConstraint);

        gridY++;
        for (String datum : statsData) {

            int positioning = (tagLbl == null) ? SwingConstants.CENTER : SwingConstants.LEFT;

            JLabel label = new JLabel(datum, positioning);
            label.setFont(defFont);
            label.setForeground(SummaryPanel.CONSTANTS_row2RowForecastFontColor);;

            GridBagConstraints constraints =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, rowWeight);

            this.add(label, constraints);
            statsLbl.add(label);

            gridY++;
        }


        GridBagConstraints lowerPaddingConstraint =
                SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
        this.add(new JLabel(), lowerPaddingConstraint );
    }

    private void updateData() {
        for (int i = 0; i< statsData.size(); i++) {

            JLabel label = statsLbl.get(i);
            String data = statsData.get(i);

            label.setText(data);
        }
    }

    // Function called by all constructors after fields are initialised
    private void init() {

        this.setLayout(new GridBagLayout());

        defFont = (statsData.size() <= 2) ? SummaryPanel.CONSTANTS_row2StatsFont2 : SummaryPanel.CONSTANTS_row2StatsFont1;

        initComponents();
        updateData();
    }

    public InfoFragment(List<String> statsData) {
        this.statsData = statsData;
        init();
    }

    public InfoFragment(List<ImageIcon> tagIcons, List<String> statsData) {
        this.statsData = statsData;
        if (tagIcons != null) {
            // Turn those icons into labels

            tagLbl = new ArrayList<>();
            for (ImageIcon tagIcon : tagIcons) {
                tagLbl.add(new JLabel(tagIcon));
            }
        }

        init();
    }

    public void setData(List<String> infoData) {
        this.statsData = infoData;
        updateData();
    }

}
