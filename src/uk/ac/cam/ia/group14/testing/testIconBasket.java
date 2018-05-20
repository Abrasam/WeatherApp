package uk.ac.cam.ia.group14.testing;

import sun.swing.SwingAccessor;
import uk.ac.cam.ia.group14.util.IconBasket;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Calendar;

public class testIconBasket {

    public static void main(String args[]) {
        System.out.println(Calendar.getInstance().getTime());
        ImageIcon icon = IconBasket.getResizedIcon(100, 100, Calendar.getInstance().getTime(), WeatherSlice.Status.THUNDERSTORM);

        JFrame frame = new JFrame();
        frame.setSize(new Dimension(512, 512));

        JLabel arialLabel = new JLabel("Awesome - Arial");
        JLabel courierLabel = new JLabel("Awesome - Courier");

        System.out.println(Arrays.toString(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

        Font arial = new Font("Arial", Font.PLAIN, 25);
        Font courier = new Font("Courier 10 Pitch", Font.PLAIN, 25);


        arialLabel.setFont(arial);
        courierLabel.setFont(courier);

        frame.add(arialLabel);
        frame.add(courierLabel);

        frame.setVisible(true);
    }
}
