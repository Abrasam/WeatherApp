package uk.ac.cam.ia.group14.summary;

import javax.swing.*;

public class RowForecast {

    private JPanel wholeThing;

    private JLabel dayOfWeekLbl;
    private JPanel leftStatsPnl;
    private JLabel forecastIconLbl;
    private JPanel rightStatsPnl;

    public RowForecast (JLabel dayOfWeekLbl, JPanel leftStatsPnl, JLabel forecastIconLbl, JPanel rightStatsPnl, JPanel wholeThing) {
        this.dayOfWeekLbl = dayOfWeekLbl;
        this.leftStatsPnl = leftStatsPnl;
        this.forecastIconLbl = forecastIconLbl;
        this.rightStatsPnl = rightStatsPnl;
        this.wholeThing = wholeThing;
    }

    public JLabel getDayOfWeekLbl() {
        return dayOfWeekLbl;
    }

    public JPanel getLeftStatsPnl() {
        return leftStatsPnl;
    }

    public JLabel getForecastIconLbl() {
        return forecastIconLbl;
    }

    public JPanel getRightStatsPnl() {
        return rightStatsPnl;
    }

    public JPanel getWholeThing() {
        return wholeThing;
    }

    public void setDayOfWeekLbl(JLabel dayOfWeekLbl) {
        this.dayOfWeekLbl = dayOfWeekLbl;
    }

    public void setLeftStatsPnl(JPanel leftStatsPnl) {
        this.leftStatsPnl = leftStatsPnl;
    }

    public void setForecastIconLbl(JLabel forecastIconLbl) {
        this.forecastIconLbl = forecastIconLbl;
    }

    public void setRightStatsPnl(JPanel rightStatsPnl) {
        this.rightStatsPnl = rightStatsPnl;
    }

    public void setWholeThing(JPanel wholeThing) {
        this.wholeThing = wholeThing;
    }
}
