package uk.ac.cam.ia.group14.detail;

import sun.applet.Main;
import uk.ac.cam.ia.group14.ks830.graphs.Graph;
import uk.ac.cam.ia.group14.rjt80.display.MapPanel;
import uk.ac.cam.ia.group14.summary.SummaryPanel;
import uk.ac.cam.ia.group14.util.IconBasket;
import uk.ac.cam.ia.group14.util.MainFrame;
import uk.ac.cam.ia.group14.util.UpdateableJPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class DetailPanel extends UpdateableJPanel implements MouseListener,MouseMotionListener {

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
    private long lastClickTime=0;

    //Weather param
    private double weatherX=220;
    private double weatherY=180;
    private int weatherSize = 125;

    //locationButton param
    private double locButtonX=90;
    private double locButtonY=80;
    private int locButtonSize = 30;

    //summaryButton param
    private double summaryX=360;
    private double summaryY=80;
    private int summarySize = 30;

    //Current Date Display param
    private double dateX = 220;
    private double dateY = 110;
    private int dateFont = 24;

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

    //Stored graph
    private BufferedImage tempGraph;
    private BufferedImage windGraph;
    private BufferedImage rainGraph;
    private BufferedImage attiGraph;

    private MainFrame mf;

    public DetailPanel(MainFrame mf){
        this.mf = mf;
    }

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

    private void drawImg(Graphics g, BufferedImage bi,double x, double y, double w, double h,double portion) {
        Graphics2D g2d = (Graphics2D) g.create();
        //g.drawImage(bi,75,250,3000, 120,this);
        g2d.drawImage(bi,(int)x,(int)y,(int)(x+w), (int)(y+h),(int)(3000*portion),(int)0,(int)(w+3000*portion),(int)(h),this);
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

    private BufferedImage getImg(String s){
        BufferedImage img=null;
        try
        {
            img = ImageIO.read(new File(s));
        }
        catch (IOException e) {
            System.out.println("Please check the file!");
        }
        return img;
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


        //WeatherLogo
        //g.drawImage(getImg("images/weather/cloud-rain-bolt.png"),(int)(weatherX-weatherSize/2),(int)(weatherY-weatherSize/2),weatherSize,weatherSize,this);
        g.drawImage(IconBasket.getImage(true, true, true, false, false, false),(int)(weatherX-weatherSize/2),(int)(weatherY-weatherSize/2),weatherSize,weatherSize,this);

        //LocationButton
        g.drawImage(getImg("images/general/location.png"),(int)(locButtonX-locButtonSize/2),(int)(locButtonY-locButtonSize/2),locButtonSize,locButtonSize,this);

        //SummaryButton
        g.drawImage(getImg("images/general/summary.png"),(int)(summaryX-summarySize/2),(int)(summaryY-summarySize/2),summarySize,summarySize,this);

        //Graph: Temperature
        Color tempColor = new Color(150, 105, 105);
        drawImg(g,tempGraph,75,250,300,120,((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime())));

        //Graph: WindSpeed
        Color windColor = new Color(105, 150, 105);
        drawImg(g,windGraph,75,375,300,120,((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime())));

        //Graph: Rainfall
        Color rainColor = new Color(100, 105, 150);
        drawImg(g,rainGraph,75,500,300,120,((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime())));

        //Graph: Temperature
        Color tempAttiColor = new Color(150, 105, 105);
        drawImg(g,attiGraph,75,625,300,120,((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime())));

        //Location
        Color locColor = new Color(150, 150, 105);
        drawRec(g,80,20,CONST_SCREENRIGHT-110, 30,locColor,false);

        //date
        String day = Integer.toString(getDay(curDate));
        if (day.length()==1) day = "0" + day;
        String month = Integer.toString(getMonth(curDate)+1);
        if (month.length()==1) month = "0" + month;
        String hour = Integer.toString(getHour(curDate));
        if (hour.length()==1) hour = "0" + hour;
        String minute = Integer.toString(getMinute(curDate));
        if (minute.length()==1) minute = "0" + minute;
        drawString(g,dateX-13*dateFont/4,dateY-dateFont-1,day+"/"+month+"   "+hour+":"+minute,dateFont);

        //Weekdays
        drawString(g,dateX-weekday[getWeeday(curDate)-1].length()*(dateFont/4),dateY,weekday[getWeeday(curDate)-1],dateFont);
    }

    private void initialise() {
        addMouseListener(this);
        addMouseMotionListener(this);
        startDate = new Date ();
        curDate = startDate;
        startDate = roundDateHour(startDate,true);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY,0);
        startDate = cal.getTime();
        endDate = addDate(startDate,dateOnScroll);
        range = addDate(startDate,dateOnScroll-1);
        buttonX = scrollBarX;
        buttonY = scrollBarY-scrollBarHeight/2+((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime()))*(scrollBarHeight);
        init = false;
        tempGraph = Graph.getRandomTemperatureGraph();
        windGraph = Graph.getRandomWindGraph();
        rainGraph = Graph.getRandomRainGraph();
        attiGraph = Graph.getRandomTemperatureGraph();
    }

    private boolean inRange(double x1,double y1, double x2, double y2, double range){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))<=(range*1.05+3);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //double click to return to current time
        if (inRange(e.getX(),e.getY(),buttonX,buttonY,scrollButtonSize/2)){
            long newClickTime = new Date().getTime();
            if ((newClickTime-lastClickTime)<350){
                curDate = new Date();
                buttonY = scrollBarY - scrollBarHeight / 2 + ((double) (curDate.getTime() - startDate.getTime()) / (range.getTime() - startDate.getTime())) * (scrollBarHeight);
                lastClickTime = 0;
                repaint();
            } else {
                lastClickTime = newClickTime;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Check for press on button
        if (inRange(e.getX(),e.getY(),buttonX,buttonY,scrollButtonSize/2)){
            holdButton = true;
            pressX = (double)e.getX();
            pressY = (double)e.getY();
            repaint();
        }
        //Check for press on location button
        else if (inRange(e.getX(),e.getY(),locButtonX,locButtonY,locButtonSize/2)){
            System.out.println("Click on location, redirect to Location panel");
            mf.changeCard(MapPanel.cardName);
        }
        //Check for press on summary button
        else if (inRange(e.getX(),e.getY(),summaryX,summaryY,summarySize/2)){
            System.out.println("Click on summary, redirect to Summary panel");
            mf.changeCard(SummaryPanel.cardName);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        holdButton = false;
        if (sigMove) {
            sigMove = false;
            sigMoveConst = 15;
            double incr;
            Date targetDate = roundDateHour(curDate, false);
            while (curDate != targetDate) {
                incr = targetDate.getTime() - curDate.getTime();
                if (Math.abs(incr) > 30000) {
                    incr *= convergeRate;
                    curDate = addMillisecond(curDate, (int) incr);
                } else {
                    curDate = targetDate;
                }
                buttonY = scrollBarY - scrollBarHeight / 2 + ((double) (curDate.getTime() - startDate.getTime()) / (range.getTime() - startDate.getTime())) * (scrollBarHeight);
                repaint();
            }
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

    @Override
    public void update() {

    }
}
