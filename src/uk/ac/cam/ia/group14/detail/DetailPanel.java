package uk.ac.cam.ia.group14.detail;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class DetailPanel extends JPanel {

    public static String cardName = "DetailPanel";

    public DetailPanel(){
    }

    private void drawLine(Graphics g,double x, double y, double w, double h, Color c){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(x,y,w,h,Math.min(w,h)/1.25,Math.min(w,h)/1.25);
        g2d.draw(rec);
        g2d.fill(rec);
    }

    private void drawRec(Graphics g,double x, double y, double w, double h, Color c, boolean fill){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(x,y,w,h,0,0);
        if (!fill){
            g2d.setStroke(new BasicStroke(5));
        }
        g2d.draw(rec);
        if (fill) g2d.fill(rec);
    }

    private void drawCircle(Graphics g,double x, double y, double w, double h, Color c){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        Ellipse2D.Double cir = new Ellipse2D.Double(x, y, w, h);
        g2d.draw(cir);
    }

    @Override
    public void paint(Graphics g){
        //screen size: x: 0-393, y: 0-770
        int CONST_SCREENRIGHT = 393;
        int CONST_SCREENBOTTOM = 770;

        //Scroll - param
        int dateOnScroll = 6;
        Color scrollColor = new Color(150,150,150);
        int scrollBarX = 50;
        int scrollBarVertSpace = 20;
        int scrollBarThickness = 3;
        int scrollDivWidth = 16;
        double scrollDivRatio = 0.8;
        //Scroll - impl
        drawLine(g,scrollBarX-scrollBarThickness/2,scrollBarVertSpace,scrollBarThickness,CONST_SCREENBOTTOM-scrollBarVertSpace*2,scrollColor);
        for (int i=0;i<dateOnScroll;i++){
            if (i==0){
                drawLine(g,scrollBarX-scrollDivWidth/2,scrollBarVertSpace,scrollDivWidth,scrollBarThickness,scrollColor);
            }
            else if (i==dateOnScroll-1){
                drawLine(g,scrollBarX-scrollDivWidth/2,CONST_SCREENBOTTOM-scrollBarVertSpace-scrollBarThickness,scrollDivWidth,scrollBarThickness,scrollColor);
            }
            else {
                drawLine(g,scrollBarX-(scrollDivWidth)/2+scrollDivWidth*(1-scrollDivRatio)/2,(CONST_SCREENBOTTOM-scrollBarVertSpace-scrollBarThickness)/(dateOnScroll-1)*i+scrollBarVertSpace,scrollDivWidth*scrollDivRatio,scrollBarThickness,scrollColor);
            }
        }

        //Graph: Temperature
        Color tempColor = new Color(150, 108, 97);
        drawRec(g,80,300,CONST_SCREENRIGHT-110, 110,tempColor,true);

        //Graph: WindSpeed
        Color windColor = new Color(105, 150, 106);
        drawRec(g,80,450,CONST_SCREENRIGHT-110, 110,windColor,true);

        //Graph: Rainfall
        Color rainColor = new Color(100, 105, 150);
        drawRec(g,80,600,CONST_SCREENRIGHT-110, 110,rainColor,true);

        //Location
        Color locColor = new Color(148, 150, 94);
        drawRec(g,80,30,CONST_SCREENRIGHT-110, 30,locColor,false);

        //Location
        Color weaColor = new Color(140, 88, 150);
        drawCircle(g,150,120,140, 140,weaColor);
    }
}
