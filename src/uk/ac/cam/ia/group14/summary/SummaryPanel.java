package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.util.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel{

    public static String cardName = "SummaryPanel";

    MainFrame mainFrame;
    //JFrame testFrame;

    JPanel rootPanel;
    JPanel backButtonAndNamePanel;

    public SummaryPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;

        rootPanel = new JPanel(new GridBagLayout());

        GridBagConstraints backButtonAndNameConstraints = new GridBagConstraints();
        backButtonAndNameConstraints.fill = GridBagConstraints.NONE;
        backButtonAndNameConstraints.gridx = 0;
        backButtonAndNameConstraints.gridy = 0;

        //backButtonAndNamePanel = new JPanel(new BoxLayout(rootPanel))
    }

    /*public SummaryPanel(JFrame testFrame) {
        this.testFrame = testFrame;
    }*/


    public void update() {

    }

}
