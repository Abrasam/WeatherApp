package uk.ac.cam.ia.group14.ks830.graphs;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 * This is a GraphPanel implementation from {@see <a href="https://gist.github.com/roooodcastro/6325153"></a>}
 * (see also {@see <a href="http://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java"></a>}).
 *
 * Some of the parameters have been changed to match the style of our weather application,
 * and methods have been added to facilitate display of different graph styles.
 *
 * @author {@see <a href="https://gist.github.com/roooodcastro">Rodrigo</a>}
 * @author {@see <a href="https://stackoverflow.com/users/522444/hovercraft-full-of-eels">Hovercraft Full Of Eels</a>}
 *
 *
 */
public class GraphPanel extends JPanel {

	// dimensions to match the main frame
    private final int preferredWidth = 3000;
    private final int preferredHeight = 120;

    // padding for labels
    private int padding = 20;
    private int labelPadding = 10;

    // declare colour variables
    private Color lineColor;
    private Color pointColor;
    private Color gridColor = new Color(200, 200, 200, 200);

    // declare variables for appearance of the graph
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;

    // number of marks along y-axis
    // private int numberYDivisions = 3;
    private double[] values;

    public GraphPanel(double[] values, WeatherSlice.Parameter parameter) {

    	// the graph will display the values given in the argument
    	this.values = values;

    	// set the size of the graph
    	this.setPreferredSize(new Dimension(preferredWidth, preferredHeight));

    	switch (parameter) {
		    case TEMPERATURE:
		    	System.out.println("temperature");
			    lineColor = new Color(44, 102, 230, 180);
			    pointColor = new Color(100, 100, 100, 180);
		    	break;
		    case RAIN:
		    	System.out.println("rain");
			    lineColor = new Color(44, 102, 230, 180);
			    pointColor = new Color(100, 100, 100, 180);
		    	break;

		    case WIND:
		    	System.out.println("wind");
			    lineColor = new Color(44, 102, 230, 180);
			    pointColor = new Color(100, 100, 100, 180);
		    	break;

		    default:
			    lineColor = new Color(44, 102, 230, 180);
			    pointColor = new Color(100, 100, 100, 180);
	    }

    }

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (values.length - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - values[i]) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        /*g2.setColor(Color.WHITE);
        g2.fillRect(padding
		        + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
		        getHeight() - 2 * padding - labelPadding);*/
        g2.setColor(Color.BLACK);


		// create hatch marks and grid lines for y axis.
		/*for (int i = 0; i < numberYDivisions + 1; i++) {
			int x0 = padding + labelPadding;
			int x1 = pointWidth + padding + labelPadding;
			int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
			int y1 = y0;
			if (values.length > 0) {
				// do NOT draw the horizontal grid lines
				// g2.setColor(gridColor);
				// g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);

				g2.setColor(Color.BLACK);
				String yLabel = (int) ((((getMinScore() + (getMaxScore() - getMinScore()) *
						((i * 1.0) / numberYDivisions)) * 100)) / 100.0) + "";
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}*/

		// and for x axis
		for (int i = 0; i < values.length; i++) {
			if (values.length > 1) {
				int x0 = i * (getWidth() - padding * 2 - labelPadding) / (values.length - 1) + padding + labelPadding;
				int x1 = x0;
				int y0 = getHeight() - padding - labelPadding;
				int y1 = y0 - pointWidth;
				if (i % 4 == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth,
							x1, graphPoints.get(i).y);
					g2.setColor(Color.BLACK);
					String xLabel = (((i%24) / 10 == 0) ? ("0" + i%24) : i%24) + ":00";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
				}
				g2.drawLine(x0, y0, x1, y1);
			}
		}


		// create y axis
        /*g2.drawLine(padding + labelPadding,
		        getHeight() - padding - labelPadding,
		        padding + labelPadding,
		        padding);*/

		// create x axis
		g2.drawLine(padding + labelPadding,
		        getHeight() - padding - labelPadding,
		        getWidth() - padding,
		        getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);

        for (int i = 0; i < graphPoints.size(); i++) {

        	g2.setColor(pointColor);
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);

            // display the temperature if it is a local minimum or a local maximum
            if (i > 0 && i < values.length - 1 &&
		            ((values[i] >= values[i+1] && values[i] >= values[i-1]) ||
				            values[i] <= values[i+1] && values[i] <= values[i-1])) {
	            // display the value above the point
	            g2.setColor(pointColor);
	            String metricLabel = ((int) values[i]) + "";
	            FontMetrics metrics = g2.getFontMetrics();
	            int labelWidth = metrics.stringWidth(metricLabel);
	            g2.drawString(metricLabel, x - labelWidth / 2, y - (metrics.getHeight() / 2) + 1);
            }
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, heigth);
//    }

    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (double score : values) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (double score : values) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setValues(double[] values) {
        this.values = values;
        invalidate();
        this.repaint();
    }

    public double[] getValues() {
        return values;
    }

    public GraphPanel getPanel() {


	    return this;
    }
    
    private static void createAndShowGui() {
	    double[] values = new double[120];
	    Random random = new Random();
	    int maxDataPoints = values.length;
	    int maxScore = 20;
	    for (int i = 0; i < maxDataPoints; i++) {
		    values[i] = ((10 + random.nextDouble() * maxScore));
	    }

	    GraphPanel mainPanel = new GraphPanel(values, WeatherSlice.Parameter.TEMPERATURE);

	    JScrollPane scrollPane = new JScrollPane(mainPanel);

	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));


	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setVisible(false);

	    scrollPane.setBounds(0, 0, 300, 120);
	    scrollPane.setWheelScrollingEnabled(true);
	    scrollPane.setPreferredSize(new Dimension(300, 120));

	    scrollPane.setBackground(new Color(1, 0, 0, 0));

	    scrollPane.getViewport().setBorder(null);
	    scrollPane.setViewportBorder(null);
	    scrollPane.setBorder(null);


	    JPanel contentPane = new JPanel(null);
	    contentPane.setPreferredSize(new Dimension(300, 120));
	    contentPane.add(scrollPane);


	    JFrame frame = new JFrame("DrawGraph");


	    frame.setContentPane(contentPane);
	    //frame.getContentPane().add(mainPanel);

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    frame.pack();
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
    }
    
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}
