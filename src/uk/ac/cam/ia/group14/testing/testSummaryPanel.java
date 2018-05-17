package uk.ac.cam.ia.group14.testing;

import uk.ac.cam.ia.group14.summary.SummaryPanel;
import uk.ac.cam.ia.group14.util.MainFrame;

import javax.swing.*;

public class testSummaryPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        MainFrame dummy = null;
        SummaryPanel summaryPanel = new SummaryPanel(dummy);

        frame.setSize(400, 800);
        frame.add(summaryPanel);
        frame.setVisible(true);
    }
}
