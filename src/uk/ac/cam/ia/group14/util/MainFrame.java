package uk.ac.cam.ia.group14.util;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.rjt80.display.MapPanel;
import uk.ac.cam.ia.group14.summary.SummaryPanel;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private InterpanelData datum;
    private Map<String, UpdateableJPanel> panels;

    public MainFrame(){
        super("Mountain Weather");
        setSize(400,800);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        datum = new InterpanelData();
        panels = new HashMap<>();
        panels.put(MapPanel.cardName, new MapPanel(this));
    }

    public InterpanelData getDatum() {
        return datum;
    }

    private void getDetailPanel(){
        this.add(new DetailPanel());
    }

    public static void main (String[] args){
        MainFrame mf = new MainFrame();
        mf.setVisible(true);
        //mf.getDetailPanel();
    }
}
