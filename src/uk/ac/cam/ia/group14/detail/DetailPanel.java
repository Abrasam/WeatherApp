package uk.ac.cam.ia.group14.detail;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DetailPanel extends JPanel {

    public static String cardName = "DetailPanel";

    public DetailPanel(){
        JButton button =new JButton("press");
        this.add(button);
    }

    private void drawLine(Graphics g,double x, double y, double w, double h, Color c){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(x,y,w,h,Math.min(w,h)/1.25,Math.min(w,h)/1.25);
        g2d.draw(rec);
        g2d.fill(rec);
        System.out.println("Enter");
    }

    @Override
    public void paint(Graphics g){
        int dateOnScroll = 3;
        //Scroll
        Color scrollColor = new Color(150,150,150);
        drawLine(g,20,20,10,730,scrollColor);
        for (int i=0;i<dateOnScroll;i++){
            if (i==0){
                drawLine(g,20,20,40,10,scrollColor);
            }
        }

    }
}
