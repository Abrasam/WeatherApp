package uk.ac.cam.ia.group14.rjt80.display;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.rjt80.tools.Polygon2D;
import uk.ac.cam.ia.group14.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MapPanel extends UpdateableJPanel {
    public static final String cardName = "MapPanel";
    private static final String backgroundFile = "src/uk/ac/cam/ia/group14/rjt80/files/ukoutline.png";
    private static final String fontName = "Courier New";
    private static final Color textColour = Color.WHITE;

    private MainFrame frame;
    private BufferedImage background;
    private Map<RegionID, Polygon2D> mountainRangeButtons;

    private JLabel regionLabel;

    /**
     *
     * @param frame The MainFrame to add the panel to
     */
    public MapPanel(MainFrame frame) {
        super();

        this.frame = frame;

        //sets the background to show Britain
        setBackground(backgroundFile);
        //places the mountain ranges on the background
        getMountainRanges();

        //Layout manager to position labels
        setLayout(new BorderLayout());
        //Add title label, region label and explanation label
        addLabels();

        //Listener to detect when user clicks on range
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                respondToMouseClick(e.getPoint());
            }
        });

        //Listener to detect if mouse over mountain range to display region label
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                respondToMouseMove(e.getPoint());
            }
        });

        //default mountain range to stop other's frames from crashing
        frame.getDatum().setCurrentMountainRegion(RegionID.BREACONS);
    }

    /**
     *
     * @param imgFilePath file path of the image to use as the background
     */
    private void setBackground(String imgFilePath) {
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

    /**
     * Loads the polygons representing the mountain ranges from their files
     */
    private void getMountainRanges() {
        this.mountainRangeButtons = new HashMap<>();

        for (int i = 0; i < RegionID.values().length; ++i) {
            try {
                //regions stored in a file with a ubiquitous name
                mountainRangeButtons.put(RegionID.values()[i], new Polygon2D("src/uk/ac/cam/ia/group14/rjt80/files/"+RegionID.values()[i].toString()+".txt"));
                //global repositioning done to fit to background
                mountainRangeButtons.get(RegionID.values()[i]).adjustCentre(new Point(80, 120));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Could not retrieve file " + RegionID.values()[i].toString()+".txt",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Adds all on screen labels
     * Default font, colour and centering used
     */
    private void addLabels() {
        // Top Brand Label, top of screen, large text
        JLabel brandLabel = new JLabel("PEAK WEATHER");
        brandLabel.setPreferredSize(new Dimension(400, 120));
        brandLabel.setForeground(textColour);
        brandLabel.setHorizontalAlignment(SwingConstants.CENTER);
        brandLabel.setVerticalAlignment(SwingConstants.CENTER);
        brandLabel.setFont(new Font(fontName, Font.BOLD, 42));
        add(brandLabel, BorderLayout.PAGE_START);


        //To position explanation and region label below map
        JPanel pageEndPanel = new JPanel();
        pageEndPanel.setPreferredSize(new Dimension(400, 170));
        pageEndPanel.setOpaque(false);
        pageEndPanel.setLayout(new BorderLayout());
        add(pageEndPanel, BorderLayout.PAGE_END);

        //Mountain region label, medium sized text
        //Only visible when user is hovering over region
        regionLabel = new JLabel("");
        regionLabel.setForeground(textColour);
        regionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        regionLabel.setFont(new Font(fontName, Font.BOLD, 26));
        pageEndPanel.add(regionLabel, BorderLayout.CENTER);

        //Explanation label, bottom of screen, small text
        JLabel explanationLabel = new JLabel("click a region to access weather");
        explanationLabel.setForeground(textColour);
        explanationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        explanationLabel.setFont(new Font(fontName, Font.BOLD, 16));
        pageEndPanel.add(explanationLabel, BorderLayout.PAGE_END);
    }

    /**
     *
     * @param mousePosition mouse position
     * @return the region (as RegionID value) that the mouse position is in, or null if it isn't
     */
    private RegionID inMountainRange(Point mousePosition) {
        RegionID mountainRangeSelected = null;
        Iterator<RegionID> rangeIterator = mountainRangeButtons.keySet().iterator();
        while(rangeIterator.hasNext() && mountainRangeSelected == null) {
            RegionID thisRange = rangeIterator.next();
            if (mountainRangeButtons.get(thisRange).withinPolygon(mousePosition)) {
                mountainRangeSelected = thisRange;
            }
        }
        return mountainRangeSelected;
    }

    /**
     *
     * @param mousePosition mouse position
     *
     * Used to check if user clicked on a mountain range, and switch screens if so
     */
    private void respondToMouseClick(Point mousePosition) {
        RegionID mountainRangeSelected = inMountainRange(mousePosition);
        if (mountainRangeSelected != null) {
            loadDetailedPanel(mountainRangeSelected);
        }
    }

    /**
     *
     * @param mousePosition mouse position
     * Used to check if user is hovering over mountain range, display range name if so, remove label if not
     */
    private void respondToMouseMove(Point mousePosition) {
        RegionID mountainRangeSelected = inMountainRange(mousePosition);
        if (mountainRangeSelected != null) {
            regionLabel.setText(mountainRangeSelected.toString());
        } else {
            regionLabel.setText("");
        }
    }

    /**
     *
     * @param mountainRangeSelected mountain range clicked on by user
     * Will update mountain range to new location and call MainFrame to get screens switched
     */
    private void loadDetailedPanel(RegionID mountainRangeSelected) {
        frame.getDatum().setCurrentMountainRegion(mountainRangeSelected);
        frame.changeCard(DetailPanel.cardName);
    }

    /**
     * Called when screen is switched to set initial screen layout
     */
    public void update() {
        regionLabel.setText("");
        repaint();
    }

    /**
     *
     * @param g the Graphics object
     * Paints the screen with the background and mountain range polygons
     */
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.GREEN);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (RegionID region : mountainRangeButtons.keySet()) {
            graphics2D.fill(mountainRangeButtons.get(region).drawShape());
        }
    }
}