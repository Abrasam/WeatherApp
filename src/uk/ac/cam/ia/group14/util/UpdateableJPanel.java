package uk.ac.cam.ia.group14.util;

import javax.swing.*;

/**
 * So all panels can be updated before they are shown
 * and so they can be stored in a data structure together and have their update function called
 */
public abstract class UpdateableJPanel extends JPanel implements Updateable {
    public abstract void update();
}
