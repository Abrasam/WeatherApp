package uk.ac.cam.ia.group14.testing;

import uk.ac.cam.ia.group14.util.IconBasket;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import javax.swing.*;
import java.awt.*;

public class testIconBasket {

    public static void main(String args[]) {
        ImageIcon icon = IconBasket.getResizedIcon(100, 100, false, WeatherSlice.Status.SUN);

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(512, 512));

        JLabel labelImg = new JLabel(icon);

        frame.add(labelImg);

        frame.setVisible(true);
    }
}
