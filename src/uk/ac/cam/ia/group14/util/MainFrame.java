package uk.ac.cam.ia.group14.util;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(){
        super("Mountain Weather");
        setSize(400,800);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main (String[] args){
        MainFrame d = new MainFrame();
        d.setVisible(true);
    }
}
