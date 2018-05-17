package uk.ac.cam.ia.group14.summary;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends JPanel {


    private final Color CONSTANTS_defColor = new Color(170, 248, 255);

    private String defaultFontName;
    private final Font CONSTANTS_infoFont = new Font(defaultFontName, Font.PLAIN, 16);

    private List<JLabel> tagLbl;
    private List<String> infoData;
    private List<JLabel> infoLbl;


    private void initComponents() {
        this.setBackground(CONSTANTS_defColor);

        int gridX=0, gridY=0;

        double totalRowsWeight = 0.2;
        double singlePadWeight = (1.0 - totalRowsWeight)/2.0; // Put dummy rows on the top and bottom to compress labels/data

        double rowWeight = totalRowsWeight / (double) infoData.size();

        if (tagLbl != null) {
            gridY = 0;

            GridBagConstraints upperPaddingConstraint =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
            this.add(new JLabel(), upperPaddingConstraint);

            gridY++;
            for (JLabel label : tagLbl) {
                GridBagConstraints constraints =
                        SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, rowWeight);

                this.add(label, constraints);

                gridY++;
            }


            GridBagConstraints lowerPaddingConstraint =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
            this.add(new JLabel(), lowerPaddingConstraint );

            gridX++;
        }

        infoLbl = new ArrayList<>();
        gridY = 0;


        GridBagConstraints upperPaddingConstraint =
                SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
        this.add(new JLabel(), upperPaddingConstraint);

        gridY++;
        for (String datum : infoData) {

            JLabel label = new JLabel(datum, SwingConstants.CENTER);
            label.setFont(CONSTANTS_infoFont);

            GridBagConstraints constraints =
                    SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, rowWeight);

            this.add(label, constraints);
            infoLbl.add(label);

            gridY++;
        }


        GridBagConstraints lowerPaddingConstraint =
                SummaryUtil.getGridBagConstraints(GridBagConstraints.BOTH, gridX, gridY, 1.0, singlePadWeight);
        this.add(new JLabel(), lowerPaddingConstraint );
    }

    private void updateData() {
        for (int i=0; i<infoData.size(); i++) {

            JLabel label = infoLbl.get(i);
            String data = infoData.get(i);

            label.setText(data);
        }
    }

    public InfoFragment(String defaultFontName, List<JLabel> tagLbl, List<String> infoData) {

        this.defaultFontName = defaultFontName;
        assert (tagLbl.size() == infoData.size());

        this.setLayout(new GridBagLayout());

        this.tagLbl = tagLbl;
        this.infoData = infoData;

        initComponents();
    }
    public InfoFragment(String defaultFontName, List<String> infoData) {

        this.defaultFontName = defaultFontName;
        this.setLayout(new GridBagLayout());
        this.infoData = infoData;

        initComponents();
        updateData();
    }

    public void setData(List<String> infoData) {
        this.infoData = infoData;
        updateData();
    }

}
