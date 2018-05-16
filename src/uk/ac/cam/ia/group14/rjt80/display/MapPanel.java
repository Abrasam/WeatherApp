package uk.ac.cam.ia.group14.rjt80.display;

import uk.ac.cam.ia.group14.rjt80.tools.Polygon2D;
import uk.ac.cam.ia.group14.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MapPanel extends UpdateableJPanel {
    public static final String cardName = "MapPanel";
    private static final String backgroundFile = "src/uk/ac/cam/ia/group14/rjt80/files/ukoutline.png";

    private MainFrame frame;
    private BufferedImage background;
    private Map<RegionID, Polygon2D> mountainRangeButtons;

    public MapPanel(MainFrame frame) {
        super();

        this.frame = frame;

        setBackground(backgroundFile);
        getMountainRanges();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                respondToMouseClick(e.getPoint());
            }
        });


    }

    public void setBackground(String imgFilePath) {
        final int WIDTH = 400;
        final int HEIGHT = 800;
        try {
            File f = new File(imgFilePath);
            background = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            background = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMountainRanges() {
        this.mountainRangeButtons = new HashMap<>();

        for (int i = 0; i < RegionID.values().length; ++i) {
            try {
                mountainRangeButtons.put(RegionID.values()[i], new Polygon2D("src/uk/ac/cam/ia/group14/rjt80/files/"+RegionID.values()[i].toString()+".txt"));
                //mountainRangeButtons.get(RegionID.values()[i]).setScale(10);
                //mountainRangeButtons.get(RegionID.values()[i]).setCentre(new Point(-50, -50));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Could not retrieve file " + RegionID.values()[i].toString()+".txt",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void respondToMouseClick(Point mousePosition) {
        RegionID mountainRangeSelected = null;
        Iterator<RegionID> rangeIterator = mountainRangeButtons.keySet().iterator();
        while(rangeIterator.hasNext() && mountainRangeSelected == null) {
            RegionID thisRange = rangeIterator.next();
            if (mountainRangeButtons.get(thisRange).withinPolygon(mousePosition)) {
                mountainRangeSelected = thisRange;
                System.out.println(thisRange.toString());
            }
        }
        if (mountainRangeSelected != null) {
            loadDetailedPanel(mountainRangeSelected);
        }
    }

    private void loadDetailedPanel(RegionID mountainRangeSelected) {
        frame.getDatum().setCurrentMountainRegion(mountainRangeSelected);
        /*
         * TODO: Call changeCard("DetailedPanel") on frame
         */
    }

    public void update() {
        repaint();
        // TODO (maybe)
    }

    @Override
    public void paintComponent(Graphics g) {
        /*
         * TODO: Add title
         *
         * TODO: Add labels to ranges
         *
         * TODO: Add description of what to click
         *
         * TODO: Maybe add clicking functionality
         */
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.GREEN);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (RegionID region : mountainRangeButtons.keySet()) {
            graphics2D.fill(mountainRangeButtons.get(region).drawShape());
        }
    }
}
