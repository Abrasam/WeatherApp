package uk.ac.cam.ia.group14.rjt80.display;

import uk.ac.cam.ia.group14.rjt80.tools.Polygon2D;
import uk.ac.cam.ia.group14.util.MainFrame;
import uk.ac.cam.ia.group14.util.RegionID;
import uk.ac.cam.ia.group14.util.Updateable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;

public class MapPanel extends JPanel implements Updateable {
    private static String cardName = "MapPanel";
    private MainFrame frame;
    private Map<RegionID, Polygon2D> mountainRangeButtons;

    public MapPanel(MainFrame frame) {
        super();

        this.frame = frame;
        this.mountainRangeButtons = new HashMap<>();
        for (int i = 0; i < RegionID.values().length; ++i) {
            try {
                mountainRangeButtons.put(RegionID.values()[i], new Polygon2D("src/uk/cam/ia/group14/"+RegionID.values()[i].toString()+".txt"));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Could not retrieve file " + RegionID.values()[i].toString()+".txt",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                respondToMouseClick(e.getPoint());
            }
        });
    }

    private void respondToMouseClick(Point mousePosition) {
        RegionID mountainRangeSelected = null;
        Iterator<RegionID> rangeIterator = mountainRangeButtons.keySet().iterator();
        while(rangeIterator.hasNext() && mountainRangeSelected == null) {
            RegionID thisRange = rangeIterator.next();
            if (mountainRangeButtons.get(thisRange).withinPolygon(mousePosition)) {
                mountainRangeSelected = thisRange;
            }
        }
        if (mountainRangeSelected != null) {
            loadDetailedPanel(mountainRangeSelected);
        }
    }

    private void loadDetailedPanel(RegionID mountainRangeSelected) {
        frame.getDatum().setCurrentMountainRegion(mountainRangeSelected);
        /* TODO: Call update on DetailedPanel
         * TODO: Call setPanel("DetailedPanel") on MainPanel
         */
    }

    public void update() {
        repaint();
        // TODO (maybe)
    }

    @Override
    public void paintComponent(Graphics g) {
        /*
         * TODO: Draw background image
         * g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
         *
         * TODO: Add title
         *
         * TODO: Add labels to ranges
         *
         * TODO: Add description of what to click
         *
         * TODO: Maybe add clicking functionality
         */

        g.setColor(Color.GREEN);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (RegionID region : mountainRangeButtons.keySet()) {
            graphics2D.fill(mountainRangeButtons.get(region).drawShape());
        }
    }
}
