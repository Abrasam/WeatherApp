package uk.ac.cam.ia.group14.summary;

import uk.ac.cam.ia.group14.util.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SummaryPanel extends JPanel{

    public static String cardName = "SummaryPanel";

    MainFrame mainFrame;
    //JFrame testFrame;

    JPanel backButtonAndNamePane;
    JPanel forecastPane;

    public SummaryPanel(MainFrame mainFrame){
        this.mainFrame = mainFrame;


        this.setLayout(new GridBagLayout());

        GridBagConstraints backButtonAndNameConstraints = new GridBagConstraints();
        backButtonAndNameConstraints.fill = GridBagConstraints.VERTICAL;
        backButtonAndNameConstraints.gridx = 0;
        backButtonAndNameConstraints.gridy = 0;

        backButtonAndNamePane = new JPanel();
        backButtonAndNamePane.setBackground(Color.RED);

        GridBagConstraints forecastConstraints = new GridBagConstraints();
        forecastConstraints.fill = GridBagConstraints.BOTH;
        forecastConstraints.gridx = 0;
        forecastConstraints.gridy = 1;

        forecastPane = new JPanel();
        forecastPane.setBackground(Color.GREEN);

        this.add(backButtonAndNamePane, backButtonAndNameConstraints);
        this.add(forecastPane, forecastConstraints);


        //backButtonAndNamePane = new JPanel(new BoxLayout(rootPane))
    }

    /*public SummaryPanel(JFrame testFrame) {
        this.testFrame = testFrame;
    }*/


    public void update() {

    }

}
