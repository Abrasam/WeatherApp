package uk.ac.cam.ia.group14.testing;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.rjt80.display.MapPanel;
import uk.ac.cam.ia.group14.summary.SummaryPanel;
import uk.ac.cam.ia.group14.util.MainFrame;

import javax.swing.*;
import java.awt.*;

public class RossTest {
    public static void main(String args[]) {
        MainFrame frame = new MainFrame();

        MapPanel mapPanel = new MapPanel(frame);
        //SummaryPanel summaryPanel = new SummaryPanel();
        //DetailPanel detailPanel = new DetailPanel();
        frame.setContentPane(mapPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 800);
        frame.setResizable(false);
        //frame.setMinimumSize(new Dimension(500, 500));
        //frame.setPreferredSize(new Dimension(500, 500));
    }
}
