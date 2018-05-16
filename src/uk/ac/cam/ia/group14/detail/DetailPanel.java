package uk.ac.cam.ia.group14.detail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Calendar;
import java.util.Date;

public class DetailPanel extends JPanel implements MouseListener,MouseMotionListener {

    public static String cardName = "DetailPanel";
    private static boolean init = true;

    //Actual screen size: x: 0-393, y: 0-770
    private int CONST_SCREENRIGHT = 393;
    private int CONST_SCREENBOTTOM = 770;

    //Scroll param
    private Color scrollColor = new Color(150,150,150);
    private int scrollBarX = 50;
    private int scrollBarY = CONST_SCREENBOTTOM/2;
    private int scrollBarHeight = 700;
    private int scrollBarThickness = 3;
    private int scrollDivWidth = 16;
    private double scrollDivRatio = 0.8;
    private int dateOnScroll = 6;

    //ScrollButton param
    private Color scrollButtonColor = new Color(64, 64, 64);
    private double buttonX;
    private double buttonY;
    private int scrollButtonSize = 18;
    private boolean holdButton = false;
    private boolean sigMove = false;
    private int sigMoveConst = 15;

    //Date param
    private String[] weekday = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private String[] weekdayShort = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private Date startDate;
    private Date endDate;
    private Date range;
    public Date curDate;

    //Mouse param
    private Double pressX=-999.0;
    private Double pressY=-999.0;
    private double convergeRate = 0.2;

    private void drawLine(Graphics g,double x, double y, double w, double h, Color c){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(x,y,w,h,Math.min(w,h)/1.25,Math.min(w,h));
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

    private void drawCircle(Graphics g,double x, double y, double w, double h, Color c, boolean fill){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        Ellipse2D.Double cir = new Ellipse2D.Double(x, y, w, h);
        g2d.draw(cir);
        if (fill) g2d.fill(cir);
    }

    private void drawString(Graphics g,double x, double y, String str, int size) {
        g.setFont(new Font("Courier New", Font.BOLD, size));
        g.drawString(str, (int)x, (int)y);
    }

    private Date roundDateHour(Date cur, boolean roundDown){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.add(Calendar.MINUTE,roundDown?0:30);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    private Date addDate(Date cur, int incr){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.add(Calendar.DATE,incr);
        return cal.getTime();
    }

    private int getMinute(Date cur){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        return cal.get(Calendar.MINUTE);
    }

    private int getHour(Date cur){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    private int getDay(Date cur){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        return cal.get(Calendar.DATE);
    }

    private int getMonth(Date cur){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        return cal.get(Calendar.MONTH);
    }

    private int getWeeday(Date cur){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    private Date addMillisecond(Date cur, int incr){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.add(Calendar.MILLISECOND,incr);
        return cal.getTime();
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if (init) initialise();

        //Scroll - impl
        drawLine(g,scrollBarX-scrollBarThickness/2,scrollBarY-scrollBarHeight/2,scrollBarThickness,scrollBarHeight,scrollColor);
        for (int i=0;i<dateOnScroll;i++){
            drawLine(g,scrollBarX-scrollDivWidth/2 + ((i==0 || i==dateOnScroll-1)?0:scrollDivWidth*(1-scrollDivRatio)/2),scrollBarY-scrollBarHeight/2-scrollBarThickness/2+scrollBarHeight/(dateOnScroll-1)*i,scrollDivWidth*((i==0 || i==dateOnScroll-1)?1:scrollDivRatio),scrollBarThickness,scrollColor);
        }
        //ScrollButton - impl
        drawCircle(g,buttonX-scrollButtonSize/2,buttonY-scrollButtonSize/2,scrollButtonSize,scrollButtonSize,scrollButtonColor,true);

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
        drawRec(g,80,20,CONST_SCREENRIGHT-110, 30,locColor,false);

        //WeatherLogo
        Color weaColor = new Color(140, 88, 150);
        drawCircle(g,150,120,140, 140,weaColor,false);

        //date
        String day = Integer.toString(getDay(curDate));
        if (day.length()==1) day = "0" + day;
        String month = Integer.toString(getMonth(curDate)+1);
        if (month.length()==1) month = "0" + month;
        String hour = Integer.toString(getHour(curDate));
        if (hour.length()==1) hour = "0" + hour;
        String minute = Integer.toString(getMinute(curDate));
        if (minute.length()==1) minute = "0" + minute;
        drawString(g,210-13*6,85,day+"/"+month+"   "+hour+":"+minute,24);

        //Weekdays
        drawString(g,210-weekday[getWeeday(curDate)-1].length()*6,110,weekday[getWeeday(curDate)-1],24);
    }

    private void initialise() {
        addMouseListener(this);
        addMouseMotionListener(this);
        startDate = roundDateHour(new Date (),true);
        curDate = startDate;
        endDate = addDate(startDate,dateOnScroll);
        range = addDate(startDate,dateOnScroll-1);
        buttonX = scrollBarX;
        buttonY = scrollBarY-scrollBarHeight/2+((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime()))*(scrollBarHeight);
        init = false;
    }

    private boolean inRange(double x1,double y1, double x2, double y2, double range){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))<=(range*1.05+3);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Clicked");
        //too slow and repeat with press
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Press");
        if (inRange(e.getX(),e.getY(),buttonX,buttonY,scrollButtonSize/2)){
            holdButton = true;
            pressX = (double)e.getX();
            pressY = (double)e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sigMove = false;
        sigMoveConst = 15;
        holdButton = false;
        double incr;
        Date targetDate = roundDateHour(curDate,false);
        while (curDate!=targetDate){
            incr = targetDate.getTime()-curDate.getTime();
            if (Math.abs(incr)>30000) {
                incr*=convergeRate;
                curDate = addMillisecond(curDate,(int)incr);
            } else {
                curDate = targetDate;
            }
            buttonY = scrollBarY-scrollBarHeight/2+((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime()))*(scrollBarHeight);
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (holdButton){
            if (sigMove){
                buttonY = e.getY();
                if (buttonY<scrollBarY-scrollBarHeight/2) buttonY = scrollBarY-scrollBarHeight/2;
                else if (buttonY>scrollBarY+scrollBarHeight/2) buttonY = scrollBarY+scrollBarHeight/2;
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.MILLISECOND,(int)((buttonY-scrollBarY+scrollBarHeight/2)/scrollBarHeight*(range.getTime()-startDate.getTime())));
                curDate = cal.getTime();
            } else if (!(inRange(pressX,pressY,e.getX(),e.getY(),sigMoveConst/6))){
                sigMove = true;
            } else {
                sigMoveConst--;
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
