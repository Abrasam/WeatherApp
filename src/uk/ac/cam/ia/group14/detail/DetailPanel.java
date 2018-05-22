package uk.ac.cam.ia.group14.detail;

import uk.ac.cam.ia.group14.ks830.graphs.Graph;
import uk.ac.cam.ia.group14.rjt80.display.MapPanel;
import uk.ac.cam.ia.group14.sjs252.WeatherFetcher;
import uk.ac.cam.ia.group14.summary.SummaryPanel;
import uk.ac.cam.ia.group14.util.*;

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
    private static boolean renew = true;

    //Actual screen size: x: 0-393, y: 0-770
    //private int CONST_SCREENRIGHT = 393;  //not used
    //private int CONST_SCREENBOTTOM = 770;  //not used

    //Scroll param
    private Color scrollColor = new Color(150,150,150);
    private int scrollBarX = 50;
    private int scrollBarY = 385; // ( CONST_SCREENBOTTOM / 2 );
    private int scrollBarHeight = 700;
    private int scrollBarThickness = 3;
    private int scrollDivWidth = 16;
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

    //Location param
    private Color locColor = new Color(75, 50, 0);
    private double locationX = 200;
    private double locationY = 85;
    private double locationFontSize = 40;

    //locationButton param
    private double locButtonX=90;
    private double locButtonY=80;
    private int locButtonSize = 30;

    //summaryButton param
    private double summaryX=360;
    private double summaryY=80;
    private int summarySize = 30;

    //Current Date Display param
    private double dateX = 215;
    private double dateY = 110;
    private int dateFontSize = 24;

    //Date param
    private String[] weekday = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private String[] weekdayShort = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private Date startDate;
    private Date range;
    private Date curDate;

    //Mouse param
    private Double pressX=-999.0;
    private Double pressY=-999.0;
    private double convergeRate = 0.2;

    //Stored graph
    private BufferedImage tempGraph;
    private BufferedImage windGraph;
    private BufferedImage rainGraph;
    private BufferedImage[] altiGraph = new BufferedImage[120];

    //Graph param
    private double graphX = 225;
    private double graphY = 310;
    private double graphW = 300;
    private double graphH = 120;
    private double graphSpacing = 5;
    private double graphSourceW = 3000;

    //Graph move
    private boolean holdGraph = false;

    //Current Data param
    private double curTempX = 345;
    private double curTempY = 165;
    private double curTempSize = 32;
    private double curWindX = 330;
    private double curWindY = 215;
    private double curWindSize = 26;
    private double curVisX = 145;
    private double curVisY = 150;
    private double curVisSize = 15;
    private double curCloudX = 137;
    private double curCloudY = 190;
    private double curCloudSize = 15;
    private double curFreezeX = 137;
    private double curFreezeY = 230;
    private double curFreezeSize = 15;
    private double curHumidX = 145;
    private double curHumidY = 230;
    private double curHumidSize = 15;

    //FPS
    private Date lastPaintTime;
    private int FPS = 9; //Best range: 8-10

    // Weather data
    private RegionID stateRegion = null;
    private Region stateData = null;
    private WeatherSlice[] ws;
    private double[][] altTemperature = new double[120][];

    //Other Class
    private MainFrame mf;
    private Graph gph = null;

    public DetailPanel(MainFrame mf){
        this.mf = mf;
        Color bgColor = new Color(215, 225, 255);
        this.setBackground(bgColor);
    }

    private void drawLine(Graphics g,double x, double y, double w, double h, Color c){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        RoundRectangle2D rec = new RoundRectangle2D.Double(x,y,w,h,Math.min(w,h)/1.25,Math.min(w,h));
        g2d.draw(rec);
        g2d.fill(rec);
    }

    private void drawCircle(Graphics g,double x, double y, double w, double h, Color c, boolean fill){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(5));
        Ellipse2D.Double cir = new Ellipse2D.Double(x, y, w, h);
        g2d.draw(cir);
        if (fill) g2d.fill(cir);
    }

    private void drawString(Graphics g,double x, double y, String str, int size, Color c, boolean underlined) {
        if (c!=null){
            g.setColor(c);
        } else {
            g.setColor(Color.BLACK);
        }
        g.setFont(new Font("Courier New", Font.BOLD, size));
        g.drawString(str, (int)x, (int)y);
        if (underlined){
            drawLine(g,x-1,y+3,str.length()*size*0.6,2,g.getColor());
        }
    }

    private void drawImg(Graphics g, BufferedImage bi,double x, double y, double w, double h, double sourceW,double portion) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(bi,(int)x,(int)y,(int)(x+w), (int)(y+h),(int)(sourceW*portion),0,(int)(w+sourceW*portion),(int)(h),this);
    }

    private Date roundDateHour(Date cur, boolean roundDown){
        //Round date to hour
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.add(Calendar.MINUTE,roundDown?0:30);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    private Date roundUpDate(Date cur){
        //Round date to next date
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.add(Calendar.DATE,1);
        cal.add(Calendar.SECOND,-1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        return cal.getTime();
    }

    private Date adjDate(Date cur, int detail, int incr){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        cal.add(detail,incr);
        return cal.getTime();
    }

    private int getDateDetail(Date cur, int detail){
        Calendar cal = Calendar.getInstance();
        cal.setTime(cur);
        return cal.get(detail);
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

    private int mapCurDateToWs(){
        //Map the current time to weather information index
        int ret = (int)(roundDateHour(curDate,false).getTime()-roundDateHour(startDate,false).getTime())/3600000;
        if (ret==120) ret--;
        return ret;
    }

    ////////////////////////////////////////////////////////////
    //This function is used as data prediction and calculation//
    ////////////////////////////////////////////////////////////
    private void refineData(){
        for (int i=0;i<ws.length;i++) {
            //Estimate data which is null
            if (ws[i]==null && i>0 && ws[i-1]!=null){
                ws[i] = new WeatherSlice(adjDate(ws[i-1].getTime(),Calendar.HOUR_OF_DAY,1),ws[i-1].getTemp(),ws[i-1].getWind(),ws[i-1].getRain(),ws[i-1].getVisibility(),ws[i-1].getCloudLevel(),ws[i-1].getFreezingAltitude(),ws[i-1].getStatus());
            }
        }
        if (ws[0]==null) return;
        for (int i=0;i<ws.length;i++){
            //(Date time, double temp, double wind, double rain, double visibility, double cloudLevel, double freezingAltitude, Status status)
            if (i%3==2) continue;
            Date time = adjDate(ws[i].getTime(),Calendar.HOUR_OF_DAY,i%3-2);
            //Smooth the data
            double temp = ws[i].getTemp() + ((i+3<ws.length)?(ws[i+3].getTemp()-ws[i].getTemp())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getTemp()-ws[i].getTemp())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getTemp()-ws[i].getTemp())*0.02:0) + ((i>=3)?(ws[i-3].getTemp()-ws[i].getTemp())*0.12:0) + ((i>=5)?(ws[i-5].getTemp()-ws[i].getTemp())*0.06:0) + ((i>=7)?(ws[i-7].getTemp()-ws[i].getTemp())*0.03:0);
            if (i%3==0 && i>0) temp = (temp + ws[i-1].getTemp())/2;
            if (i<ws.length-1) temp = (temp + ws[i+1].getTemp())/2;
            temp = Double.parseDouble(String.format("%.2f", temp));
            double wind = ws[i].getWind() + ((i+3<ws.length)?(ws[i+3].getWind()-ws[i].getWind())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getWind()-ws[i].getWind())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getWind()-ws[i].getWind())*0.02:0) + ((i>=3)?(ws[i-3].getWind()-ws[i].getWind())*0.12:0) + ((i>=5)?(ws[i-5].getWind()-ws[i].getWind())*0.06:0) + ((i>=7)?(ws[i-7].getWind()-ws[i].getWind())*0.03:0);
            if (i%3==0 && i>0) wind = (wind + ws[i-1].getWind())/2;
            if (i<ws.length-1) wind = (wind + ws[i+1].getWind())/2;
            wind = Double.parseDouble(String.format("%.2f", wind));
            double rain = ws[i].getRain() + ((i+3<ws.length)?(ws[i+3].getRain()-ws[i].getRain())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getRain()-ws[i].getRain())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getRain()-ws[i].getRain())*0.02:0) + ((i>=3)?(ws[i-3].getRain()-ws[i].getRain())*0.12:0) + ((i>=5)?(ws[i-5].getRain()-ws[i].getRain())*0.06:0) + ((i>=7)?(ws[i-7].getRain()-ws[i].getRain())*0.03:0);
            if (i%3==0 && i>0) rain = (rain + ws[i-1].getRain())/2;
            if (i<ws.length-1) rain = (rain + ws[i+1].getRain())/2;
            rain = Double.parseDouble(String.format("%.2f", rain));
            double visibility = ws[i].getVisibility() + ((i+3<ws.length)?(ws[i+3].getVisibility()-ws[i].getVisibility())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getVisibility()-ws[i].getVisibility())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getVisibility()-ws[i].getVisibility())*0.02:0) + ((i>=3)?(ws[i-3].getVisibility()-ws[i].getVisibility())*0.12:0) + ((i>=5)?(ws[i-5].getVisibility()-ws[i].getVisibility())*0.06:0) + ((i>=7)?(ws[i-7].getVisibility()-ws[i].getVisibility())*0.03:0);
            if (i%3==0 && i>0) visibility = (visibility + ws[i-1].getVisibility())/2;
            if (i<ws.length-1) visibility = (visibility + ws[i+1].getVisibility())/2;
            visibility = Double.parseDouble(String.format("%.2f", visibility));
            double cloudLevel = ws[i].getCloudLevel() + ((i+3<ws.length)?(ws[i+3].getCloudLevel()-ws[i].getCloudLevel())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getCloudLevel()-ws[i].getCloudLevel())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getCloudLevel()-ws[i].getCloudLevel())*0.02:0) + ((i>=3)?(ws[i-3].getCloudLevel()-ws[i].getCloudLevel())*0.12:0) + ((i>=5)?(ws[i-5].getCloudLevel()-ws[i].getCloudLevel())*0.06:0) + ((i>=7)?(ws[i-7].getCloudLevel()-ws[i].getCloudLevel())*0.03:0);
            if (i%3==0 && i>0) cloudLevel = (cloudLevel + ws[i-1].getCloudLevel())/2;
            if (i<ws.length-1) cloudLevel = (cloudLevel + ws[i+1].getCloudLevel())/2;
            cloudLevel = (double)Math.round(cloudLevel);
            double freezingAltitude = ws[i].getFreezingAltitude() + ((i+3<ws.length)?(ws[i+3].getFreezingAltitude()-ws[i].getFreezingAltitude())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getFreezingAltitude()-ws[i].getFreezingAltitude())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getFreezingAltitude()-ws[i].getFreezingAltitude())*0.02:0) + ((i>=3)?(ws[i-3].getFreezingAltitude()-ws[i].getFreezingAltitude())*0.12:0) + ((i>=5)?(ws[i-5].getFreezingAltitude()-ws[i].getFreezingAltitude())*0.06:0) + ((i>=7)?(ws[i-7].getFreezingAltitude()-ws[i].getFreezingAltitude())*0.03:0);
            if (i%3==0 && i>0) freezingAltitude = (freezingAltitude + ws[i-1].getFreezingAltitude())/2;
            if (i<ws.length-1) freezingAltitude = (freezingAltitude + ws[i+1].getFreezingAltitude())/2;
            freezingAltitude = (double)Math.round(freezingAltitude);
            double humidity = ws[i].getHumidity() + ((i+3<ws.length)?(ws[i+3].getHumidity()-ws[i].getHumidity())*0.08:0) + ((i+5<ws.length)?(ws[i+5].getHumidity()-ws[i].getHumidity())*0.04:0) + ((i+7<ws.length)?(ws[i+7].getHumidity()-ws[i].getHumidity())*0.02:0) + ((i>=3)?(ws[i-3].getHumidity()-ws[i].getHumidity())*0.12:0) + ((i>=5)?(ws[i-5].getHumidity()-ws[i].getHumidity())*0.06:0) + ((i>=7)?(ws[i-7].getHumidity()-ws[i].getHumidity())*0.03:0);
            if (i%3==0 && i>0) humidity = (freezingAltitude + ws[i-1].getHumidity())/2;
            if (i<ws.length-1) humidity = (freezingAltitude + ws[i+1].getHumidity())/2;
            if (humidity>100) humidity = 100;
            humidity = (double)Math.round(humidity);
            WeatherSlice.Status status = ws[i].getStatus();
            ws[i]=new WeatherSlice(time,temp,wind,rain,visibility,cloudLevel,humidity,status);
        }
        for (int i=0;i<120;i++){
            double[] tmp = new double[30];
            for (int j=0;j<30;j++){
                if (j==0) tmp[j] = ws[i].getTemp();
                else {
                    tmp[j] = tmp[j-1]-Math.random()/1.25+0.1;
                    if (tmp[j]>tmp[0]) tmp[j] = tmp[j]-Math.random()/4-0.25;
                    if (j>1 && tmp[j]>tmp[j-2] && tmp[j]>tmp[j-1]) tmp[j] = tmp[j]-Math.random()/3-0.1;
                    if (j>2 && tmp[j]>tmp[j-3] && tmp[j]>tmp[j-2]) tmp[j] = tmp[j]-Math.random()/4-0.5;
                }
                if (i>0) tmp[j] = (altTemperature[i-1][j] - (altTemperature[i-1][0]-tmp[0])/2+tmp[j])/2;
            }
            altTemperature[i] = tmp;
        }
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        if (stateRegion==null || stateRegion != mf.getDatum().getCurrentMountainRegion()){
            //Update region
            stateRegion = mf.getDatum().getCurrentMountainRegion();
            stateData = WeatherFetcher.getInstance().getRegion(stateRegion);
            ws = stateData.getHours();
            refineData();
            startDate = ws[0]==null ? new Date() : ws[0].getTime();
            renew = true;
        }
        if (init) initialise();
        if (renew) renewData();
        lastPaintTime = new Date();
        //Scroll - impl
        drawLine(g,scrollBarX-scrollBarThickness/2,scrollBarY-scrollBarHeight/2,scrollBarThickness,scrollBarHeight,scrollColor);
        for (int i=0;i<dateOnScroll;i++){
            int vertAdjust = 0;
            if ((startDate.getTime())-roundUpDate(startDate).getTime()<0) {
                if (i==0) continue;
                    // when day doesn't start at midnight, adjust the scroll
                else vertAdjust = (int)(scrollBarHeight/(dateOnScroll-1)*(-1-((double)(startDate.getTime()-roundUpDate(startDate).getTime())/86400000)));
            }
            drawLine(g,scrollBarX-scrollDivWidth/2,scrollBarY+vertAdjust-scrollBarHeight/2-scrollBarThickness/2+scrollBarHeight/(dateOnScroll-1)*i,scrollDivWidth,scrollBarThickness,scrollColor);
            drawString(g,scrollBarX-scrollDivWidth/2 - 35,scrollBarY+vertAdjust + 7 -scrollBarHeight/2-scrollBarThickness/2+scrollBarHeight/(dateOnScroll-1)*i,weekdayShort[(getDateDetail(adjDate(startDate,Calendar.DATE,i),Calendar.DAY_OF_WEEK)+5)%7],18,null,false);
        }
        //ScrollButton - impl
        drawCircle(g,buttonX-scrollButtonSize/2,buttonY-scrollButtonSize/2,scrollButtonSize,scrollButtonSize,scrollButtonColor,true);

        //WeatherLogo
        g.drawImage(IconBasket.getImage(curDate,ws[mapCurDateToWs()].getStatus()),(int)(weatherX-weatherSize/2),(int)(weatherY-weatherSize/2),weatherSize,weatherSize,this);

        //LocationButton
        g.drawImage(getImg("images/general/location.png"),(int)(locButtonX-locButtonSize/2),(int)(locButtonY-locButtonSize/2),locButtonSize,locButtonSize,this);

        //SummaryButton
        g.drawImage(getImg("images/general/summary.png"),(int)(summaryX-summarySize/2),(int)(summaryY-summarySize/2),summarySize,summarySize,this);

        //Graph Section Ratio Calculation -- to center the current time of the graph
        double graphSection = ((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime()))-graphW/(graphSourceW*2);
        graphSection += 0.008;//Make the section more center
        if (graphSection<0){
            graphSection=0;
        } else if (graphSection>(1-graphW/graphSourceW)){
            graphSection = 1-graphW/graphSourceW;
        }

        //Graph: Temperature
        drawImg(g,tempGraph,graphX-graphW/2,graphY-graphH/2,graphW,graphH,graphSourceW,graphSection);
        g.drawImage(getImg("images/general/temperature.png"),(int)(graphX-graphW/2)-20,(int)(graphY-graphH/2),40,40,this);

        //Graph: WindSpeed
        drawImg(g,windGraph,graphX-graphW/2,graphY-graphH/2+(graphH+graphSpacing)*1,graphW,graphH,graphSourceW,graphSection);
        g.drawImage(getImg("images/general/wind.png"),(int)(graphX-graphW/2)-20,(int)(graphY-graphH/2+(graphH+graphSpacing)*1),40,40,this);

        //Graph: Rainfall
        drawImg(g,rainGraph,graphX-graphW/2,graphY-graphH/2+(graphH+graphSpacing)*2,graphW,graphH,graphSourceW,graphSection);
        g.drawImage(getImg("images/general/rain.png"),(int)(graphX-graphW/2)-20,(int)(graphY-graphH/2+(graphH+graphSpacing)*2),40,40,this);

        //Graph: Altitude
        drawImg(g,altiGraph[mapCurDateToWs()],graphX-graphW/2,graphY-graphH/2+(graphH+graphSpacing)*3,graphW,graphH,300,0);
        g.drawImage(getImg("images/general/altitude.png"),(int)(graphX-graphW/2)-16,(int)(graphY-graphH/2+(graphH+graphSpacing)*3),48,48,this);

        //Location
        String regionName = mf.getDatum().getCurrentMountainRegion().toString();
        drawString(g,locationX-regionName.length()*locationFontSize/4,locationY-locationFontSize-1,regionName,(int)locationFontSize,locColor,true);

        //date
        String day = Integer.toString(getDateDetail(curDate,Calendar.DATE));
        if (day.length()==1) day = "0" + day;
        String month = Integer.toString(getDateDetail(curDate,Calendar.MONTH)+1);
        if (month.length()==1) month = "0" + month;
        String hour = Integer.toString(getDateDetail(curDate,Calendar.HOUR_OF_DAY));
        if (hour.length()==1) hour = "0" + hour;
        String minute = Integer.toString(getDateDetail(curDate,Calendar.MINUTE));
        if (minute.length()==1) minute = "0" + minute;
        drawString(g,dateX-13*dateFontSize/4,dateY-dateFontSize-1,day+"/"+month+"   "+hour+":"+minute,dateFontSize,null,false);

        //Weekdays
        drawString(g,dateX-weekday[(getDateDetail(curDate,Calendar.DAY_OF_WEEK)+5)%7].length()*(dateFontSize/4),dateY,weekday[(getDateDetail(curDate,Calendar.DAY_OF_WEEK)+5)%7],dateFontSize,null,false);

        //Current Data

        //--Current temp
        g.drawImage(getImg("images/general/temperature.png"),(int)(curTempX-curTempSize-25),(int)(curTempY-curTempSize-18),30,30,this);
        int curTempLen = String.format("%.0f", ws[mapCurDateToWs()].getTemp()-(ws[mapCurDateToWs()].getTemp()>0?0.5:-0.5)).length();
        drawString(g,curTempX-curTempLen*(curTempSize*1.2)/2,curTempY,String.format("%.0f", ws[mapCurDateToWs()].getTemp()-(ws[mapCurDateToWs()].getTemp()>0?0.5:-0.5))+" °",(int)curTempSize,null,false);
        drawString(g,curTempX,curTempY,String.format("%.1f", ws[mapCurDateToWs()].getTemp()-(int)ws[mapCurDateToWs()].getTemp()).substring(1),(int)(curTempSize/2.2),Color.GRAY,false);

        //--Current wind
        g.drawImage(getImg("images/general/wind.png"),(int)(curWindX-curWindSize-15),(int)(curWindY-curWindSize-15),35,35,this);
        int curWindLen = String.format("%.0f", ws[mapCurDateToWs()].getWind()-(ws[mapCurDateToWs()].getWind()>0?0.5:-0.5)).length();
        drawString(g,curWindX-curWindLen*(curWindSize*1.2)/2,curWindY,String.format("%.0f", ws[mapCurDateToWs()].getWind()-(ws[mapCurDateToWs()].getWind()>0?0.5:-0.5)),(int)curWindSize,null,false);
        drawString(g,curWindX,curWindY,String.format("%.1f", ws[mapCurDateToWs()].getWind()-(int)ws[mapCurDateToWs()].getWind()).substring(1)+"km/h",(int)(curWindSize/1.8),Color.DARK_GRAY,false);

        //--Current visibility
        g.drawImage(getImg("images/general/vis.png"),(int)(curVisX-curVisSize-55),(int)(curVisY-curVisSize-5),25,25,this);
        int curVisLen = String.format("%.0f", ws[mapCurDateToWs()].getVisibility()).length();
        drawString(g,curVisX-curVisLen*(curVisSize*1.2)/2,curVisY,String.format("%.0f", ws[mapCurDateToWs()].getVisibility()) + "%",(int)curVisSize,null,false);

        //--Current Cloudlevel
        g.drawImage(getImg("images/general/cloud.png"),(int)(curCloudX-curCloudSize-55),(int)(curCloudY-curCloudSize-5),25,25,this);
        int curCloudLen = String.format("%.0f", ws[mapCurDateToWs()].getCloudLevel()).length();
        drawString(g,curCloudX-curCloudLen*(curCloudSize*1.2)/2,curCloudY,String.format("%.0f", ws[mapCurDateToWs()].getCloudLevel()),(int)curCloudSize,null,false);
        drawString(g,140,curCloudY,"ft",(int)(curCloudSize*0.75),null,false);

        //--Current Freeze
//        g.drawImage(getImg("images/general/freeze.png"),(int)(curFreezeX-curFreezeSize-55),(int)(curFreezeY-curFreezeSize-5),25,25,this);
//        int curFreezeLen = String.format("%.0f", ws[mapCurDateToWs()].getFreezingAltitude()).length();
//        drawString(g,curFreezeX-curFreezeLen*(curFreezeSize*1.2)/2,curFreezeY,String.format("%.0f", ws[mapCurDateToWs()].getFreezingAltitude()),(int)curFreezeSize,null,false);
//        drawString(g,140,curFreezeY,"ft",(int)(curFreezeSize*0.75),null,false);

        //--Current Humidity
        g.drawImage(getImg("images/general/humidity.png"),(int)(curHumidX-curHumidSize-55),(int)(curHumidY-curHumidSize-5),25,25,this);
        int curHumidLen = String.format("%.0f", ws[mapCurDateToWs()].getHumidity()).length();
        drawString(g,curHumidX-curHumidLen*(curHumidSize*1.2)/2,curHumidY,String.format("%.0f", ws[mapCurDateToWs()].getHumidity()) + "%",(int)curHumidSize,null,false);
        //drawString(g,140,curHumidY,"ft",(int)(curHumidSize*0.75),null,false);
    }

    private void trimGraph(){
        tempGraph = tempGraph.getSubimage(15,0,2973,120);
        windGraph = windGraph.getSubimage(15,0,2973,120);
        rainGraph = rainGraph.getSubimage(15,0,2973,120);
        graphSourceW = 2973;
    }

    private void initialise() {
        addMouseListener(this);
        addMouseMotionListener(this);
        curDate = new Date();
        init = false;
    }

    private void renewData(){
        gph = new Graph(ws);
        range = adjDate(startDate, Calendar.DATE,dateOnScroll-1);
        //Prevent new start date is bigger than the current date;
        if (curDate.getTime()<startDate.getTime()) curDate = new Date();
        buttonX = scrollBarX;
        buttonY = scrollBarY-scrollBarHeight/2+((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime()))*(scrollBarHeight);
        tempGraph = gph.getTemperatureGraph();
        windGraph = gph.getWindGraph();
        rainGraph = gph.getRainGraph();
        for (int i=0;i<120;i++){
            altiGraph[i] = gph.getAltitudeGraph(altTemperature[i]);
        }
        renew = false;
        trimGraph();
    }

    private boolean inRange(double x1,double y1, double x2, double y2, double range){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))<=(range*1.05+3);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //double click to return to current time
        if (inRange(e.getX(),e.getY(),buttonX,buttonY,scrollButtonSize/2)){
            long newClickTime = new Date().getTime();
            if ((newClickTime-lastClickTime)<500){
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
        int mouseX = e.getX();
        int mouseY = e.getY();
        //Check for press on button
        if (inRange(mouseX,mouseY,buttonX,buttonY,scrollButtonSize/2)){
            holdButton = true;
            pressX = (double)mouseX;
            pressY = (double)mouseY;
            repaint();
        }
        //Check for press on location button
        else if (inRange(mouseX,mouseY,locButtonX,locButtonY,locButtonSize/2)){
            //System.out.println("Click on location, redirect to Location panel");
            mf.changeCard(MapPanel.cardName);
        }
        //Check for press on summary button
        else if (inRange(mouseX,mouseY,summaryX,summaryY,summarySize/2)){
            //System.out.println("Click on summary, redirect to Summary panel");
            repaint();
            mf.changeCard(SummaryPanel.cardName);
        }
        //Check for press on graphs
        else if (((mouseX>=(graphX-graphW/2)) && (mouseX<=(graphX+graphW/2))) &&
                ((mouseY>=(graphY-graphH/2)) && (mouseY<=(graphY+(graphH+graphSpacing)*3+graphH/2)))) {
            holdGraph = true;
            sigMove = true;
            pressX = (double)mouseX;
            pressY = (double)mouseY;
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        holdButton = false;
        holdGraph = false;
        if (sigMove) {
            //Repaint and calculate the ratio and convert to current time
            sigMove = false;
            sigMoveConst = 15;
            double incr;
            //Round the current time to nearest hour
            Date targetDate = roundDateHour(curDate, false);
            while (curDate != targetDate) {
                //Create a decaying time change until it reaches the desire time
                incr = targetDate.getTime() - curDate.getTime();
                if (Math.abs(incr) > 30000) {
                    incr *= convergeRate;
                    curDate = adjDate(curDate, Calendar.MILLISECOND, (int) incr);
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
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (holdButton){
            if (sigMove){ //Prevent unwanted touch to the button and change the view instantly
                //Change the current time according to the ratio
                buttonY = mouseY;
                if (buttonY<scrollBarY-scrollBarHeight/2) buttonY = scrollBarY-scrollBarHeight/2;
                else if (buttonY>scrollBarY+scrollBarHeight/2) buttonY = scrollBarY+scrollBarHeight/2;
                curDate = adjDate(startDate,Calendar.MILLISECOND,(int)((buttonY-scrollBarY+scrollBarHeight/2)/scrollBarHeight*(range.getTime()-startDate.getTime())));
            } else if (!(inRange(pressX,pressY,mouseX,mouseY,sigMoveConst/6))){
                sigMove = true;
            } else {
                sigMoveConst--;
            }
            if ((new Date().getTime()-lastPaintTime.getTime())>=1000/FPS) {
                repaint();
            }
        } else if (holdGraph){
            //Horizontal move of the graph
            curDate = adjDate(curDate, Calendar.MILLISECOND,(int)(pressX-mouseX)*144000); //144000 = 60*60*1000 / 25 -- (25px is the graph representation of 1 hour)
            if (curDate.getTime()<startDate.getTime()){
                curDate = startDate;
            } else if (curDate.getTime()>range.getTime()){
                curDate = range;
            }
            pressX = (double)mouseX;
            buttonY = scrollBarY-scrollBarHeight/2+((double)(curDate.getTime()-startDate.getTime())/(range.getTime()-startDate.getTime()))*(scrollBarHeight);
            if ((new Date().getTime()-lastPaintTime.getTime())>=1000/FPS) {
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void update() {
    }
}
