package uk.ac.cam.ia.group14.testing;

import uk.ac.cam.ia.group14.util.IconBasket;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class testIconBasket {

    public static void main(String args[]) {
        System.out.println(Calendar.getInstance().getTime());
        ImageIcon icon = IconBasket.getResizedIcon(100, 100, Calendar.getInstance().getTime(), WeatherSlice.Status.THUNDERSTORM);

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(512, 512));

        JLabel labelImg = new JLabel(icon);

        frame.add(labelImg);

        frame.setVisible(true);
    }
}
