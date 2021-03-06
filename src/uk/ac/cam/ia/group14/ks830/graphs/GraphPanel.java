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
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

import static uk.ac.cam.ia.group14.util.WeatherSlice.Parameter.RAIN;
import static uk.ac.cam.ia.group14.util.WeatherSlice.Parameter.TEMPERATURE;
import static uk.ac.cam.ia.group14.util.WeatherSlice.Parameter.WIND;

/**
 * This is a GraphPanel implementation from {@see <a href="https://gist.github.com/roooodcastro/6325153"></a>}
 * (see also {@see <a href="http://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java"></a>}).
 *
 * Some of the parameters have been changed to match the style of our weather application,
 * and methods have been added to facilitate display of different graphs based on their weather parameter.
 *
 * @author {@see <a href="https://gist.github.com/roooodcastro">Rodrigo</a>}
 * @author {@see <a href="https://stackoverflow.com/users/522444/hovercraft-full-of-eels">Hovercraft Full Of Eels</a>}
 *
 *
 */
public class GraphPanel extends JPanel {

	// dimensions of the graph so they are consistent and the data is displayed clearly
    private final int preferredWidth = 3000;
    private final int preferredHeight = 120;

    // padding for labels
    private int padding = 20;
    private int labelPadding = 10;

    // declare colour variables
    private Color lineColor;
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
	private Color backgroundColor = new Color(215, 225, 255);


    // declare variables for appearance of the graph
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;

    // the graph instance values
    private int startTime = 0; // starting time in the graph
    private double[] values; // declare the array which will store graph values
	private WeatherSlice.Parameter parameter; // the metric displayed by this instance of the GraphPanel

	/**
	 * Constructor creating a GraphPanel for a certain metric.
	 *
	 * @param values the array of the values to be displayed indexed by time
	 * @param parameter the metrix that is to be displayed, one of
	 * {@link uk.ac.cam.ia.group14.util.WeatherSlice.Parameter#TEMPERATURE},
	 * {@link uk.ac.cam.ia.group14.util.WeatherSlice.Parameter#RAIN},
	 * {@link uk.ac.cam.ia.group14.util.WeatherSlice.Parameter#WIND}.
	 * @param startTime indicates the hour at the first timestep in the array
	 */
    private GraphPanel(double[] values, WeatherSlice.Parameter parameter, int startTime) {

    	// the graph will display the values given in the argument
    	this.values = values;
    	this.startTime = startTime;
    	this.parameter = parameter;

    	// set the size of the graph
    	this.setSize(new Dimension(preferredWidth, preferredHeight));
    	this.setBackground(backgroundColor);

    	// set curve colours based on the parameter
    	switch (parameter) {
		    case TEMPERATURE:
			    lineColor = new Color(150, 105, 105, 180);
		    	break;
		    case RAIN:
			    lineColor = new Color(100, 105, 150, 180);
		    	break;

		    case WIND:
			    lineColor = new Color(105, 150, 105, 180);
		    	break;

		    default:
			    lineColor = new Color(44, 102, 230, 180);
	    }
    }

	/**
	 * Paints the graph for the values and the metric of this instance of GraphPanel.
	 */
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // scale x and y axes to fit the graph in nicely and adjust to the range of values displayed
        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (values.length - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        // get the list of points to plot
        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - values[i]) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // set background colour
		this.setBackground(backgroundColor);
		g2.setColor(backgroundColor);
		g2.fillRect(padding + labelPadding, padding,
				getWidth() - (2 * padding) - labelPadding,
				getHeight() - 2 * padding - labelPadding);

		g2.setColor(Color.BLACK);


		// create hatch marks and grid lines for x axis
		for (int i = 0; i < values.length; i++) {
			if (values.length > 1) {
				int x0 = i * (getWidth() - padding * 2 - labelPadding) / (values.length - 1) + padding + labelPadding;
				int x1 = x0;
				int y0 = getHeight() - padding - labelPadding;
				int y1 = y0 - pointWidth;

				// display a grid line every fourth hatch mark as well as the time at that mark
				if (i % 4 == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth,
							x1, graphPoints.get(i).y);
					g2.setColor(Color.BLACK);
					String xLabel = ((((i + startTime) % 24) / 10 == 0) ?
							("0" + (i + startTime) % 24) : (i + startTime) % 24) + ":00";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
				}
				g2.drawLine(x0, y0, x1, y1);
			}
		}

		// create x axis
		g2.drawLine(padding + labelPadding,
		        getHeight() - padding - labelPadding,
		        getWidth() - padding,
		        getHeight() - padding - labelPadding);


		// paint the lines that will connect the points
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

        // paint the points and related metric labels
        for (int i = 0; i < graphPoints.size(); i++) {
        	g2.setColor(pointColor);
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);

            // display the metric value every third value or at extremes of graph
            if (i == 0 || i == values.length - 1
		            || i % 3 == 0) {
	            // display the value above the point
	            g2.setColor(pointColor);
	            String metricLabel = "";
	            switch (parameter) {
		            case RAIN:
		            	// additional formatting as with these parameters decimal digits are important
			            metricLabel += (new DecimalFormat("#.#")).format(values[i]);
			            break;

		            case WIND:
			            metricLabel += (new DecimalFormat("#.#")).format(values[i]);
			            break;

		            default:
		            	// for the temperature case
			            metricLabel += ((int) values[i]);
	            };

	            // draw the label
	            FontMetrics metrics = g2.getFontMetrics();
	            int labelWidth = metrics.stringWidth(metricLabel);
	            g2.drawString(metricLabel, x - labelWidth / 2, y - (metrics.getHeight() / 2) + 1);
            }
        }
    }

	/**
	 * Finds the minimum metric value (used to scale the y-axis)
 	 */
    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (double score : values) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

	/**
	 * Finds the maximum metric value (used to scale the y-axis)
	 */
    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (double score : values) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

	/**
	 * Returns an image of the graph that is used for display in the main frame.
	 *
	 * @param values the values of the weather metric
	 * @param parameter the weather metric that the graph should display
	 * @param startTime the time at the first value in the array
	 * @return a {@link BufferedImage} containing the image of the required graph.
	 */
	public static BufferedImage getImage(double[] values, WeatherSlice.Parameter parameter, int startTime) {
		GraphPanel panel = new GraphPanel(values, parameter, startTime);

		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.print(g);
		g.dispose();
		return bi;
	}
}
