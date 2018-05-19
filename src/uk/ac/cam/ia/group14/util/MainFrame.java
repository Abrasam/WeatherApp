package uk.ac.cam.ia.group14.util;

import uk.ac.cam.ia.group14.detail.DetailPanel;
import uk.ac.cam.ia.group14.rjt80.display.MapPanel;
import uk.ac.cam.ia.group14.summary.SummaryPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private InterpanelData datum;
    private Map<String, UpdateableJPanel> panels;

    private JPanel cards;

    public MainFrame(){
        super("Mountain Weather");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setSize(400, 800);
        setResizable(false);

        datum = new InterpanelData();
        panels = new HashMap<>();
        panels.put(MapPanel.cardName, new MapPanel(this));
        //To add your panel, write a line exactly the same as one above, except with your panel class, i.e.:
        panels.put(DetailPanel.cardName, new DetailPanel(this));
        panels.put(SummaryPanel.cardName, new SummaryPanel(this));

        cards = new JPanel(new CardLayout());
        for (String cardName : panels.keySet()) {
            cards.add(panels.get(cardName), cardName);
        }
        getContentPane().add(cards);
        ((CardLayout) cards.getLayout()).show(cards, MapPanel.cardName);

        //This needs to go at the bottom, idk why
        setVisible(true);
    }

    /**
     *
     * @param cardName : PanelClass.cardName of panel to switch to
     *
     * This should be called to switch to another panel
     * It implements the update functionality so you don't have to
     * Just update any data in datum that you need to
     */
    public void changeCard(String cardName) {
        panels.get(cardName).update();

        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, cardName);
    }

    public InterpanelData getDatum() {
        return datum;
    }
}
