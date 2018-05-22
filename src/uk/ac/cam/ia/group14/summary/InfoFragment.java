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

    // Initialise the bare bones of stats and tags (if there are such)
    private void initComponents() {

        this.setBackground(SummaryPanel.CONSTANTS_row2RowForecastsColor);

        int gridX=0, gridY=0;

        double totalRowsWeight = 0.2;
        double singlePadWeight = (1.0 - totalRowsWeight)/2.0; // Put dummy rows on the top and bottom to compress labels/data

        double rowWeight = totalRowsWeight / (double) statsData.size();

        // Only add another column if tags are specified (which they might not be)
        if (tagLbl != null) {
            gridY = 0;

            // Constraints for upper dummy panel used for padding
            GridBagConstraints upperPaddingConstraint =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 2.0, singlePadWeight);
            this.add(new JLabel(), upperPaddingConstraint);

            gridY++;
            for (JLabel label : tagLbl) {
                // Actual
                GridBagConstraints constraints =
                        SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 2.0, rowWeight);

                this.add(label, constraints);

                gridY++;
            }

            // Add the lowe dummy panel
            GridBagConstraints lowerPaddingConstraint =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
            this.add(new JLabel(), lowerPaddingConstraint );

            gridX++;
        }

        // Here is the data to put the actual data
        statsLbl = new ArrayList<>();
        gridY = 0;

        GridBagConstraints upperPaddingConstraint =
                SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
        this.add(new JLabel(), upperPaddingConstraint);

        gridY++;
        for (String datum : statsData) {

            // Put each stat

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

    // Update the text of all the stats (not the tags though, they remain constant)
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

    // Constructor to initialise an info fragment without tags
    public InfoFragment(List<String> statsData) {
        this.statsData = statsData;
        init();
    }

    // Constructor to initialise an info fragment with tags
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

    // function to update the data
    public void setData(List<String> infoData) {
        this.statsData = infoData;
        updateData();
    }

}
