package uk.ac.cam.ia.group14.ks830.graphs;

import uk.ac.cam.ia.group14.util.WeatherSlice;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@link AltitudePanel} is very similar to the {@link GraphPanel} but processes the data based on altitude
 * and not the time, and only returns the temperature metric.
 */
public class AltitudePanel extends JPanel{
	// dimensions of the graph so they are consistent with the frame and the data is displayed clearly
	private final int preferredWidth = 300;
	private final int preferredHeight = 120;

	// padding for labels
	private int padding = 20;
	private int labelPadding = 10;

	// declare colour variables
	private Color lineColor = new Color(150, 75, 0, 180);
	private Color pointColor = new Color(100, 100, 100, 180);
	private Color gridColor = new Color(200, 200, 200, 200);
	private Color backgroundColor = new Color(215, 225, 255);


	// declare variables for appearance of the graph (i.e. the curve)
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private int pointWidth = 4;

	// declare the array which will store temperature values (indexed by altitude)
	private double[] values;

	/**
	 * The constructor uses an array of given values to display an altitude-temperature graph.
	 * @param values: array of size 30 that contains temperatures for successive altitudes
	 */
	private AltitudePanel(double[] values) {

		// the graph will display the values given in the argument
		this.values = values;

		// set the size of the graph
		this.setSize(new Dimension(preferredWidth, preferredHeight));
		this.setBackground(backgroundColor);
	}

	/**
	 * The method that paints the graph for it to be later used in the application. Very similar to
	 * the paintComponent method in {@link GraphPanel}.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// scaling of axes so that the metric range fits nicely in space available
		double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (values.length - 1);
		double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

		// get the list of points to plot
		List<Point> graphPoints = new ArrayList<>();
		for (int i = 0; i < values.length; i+=2) {
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
		for (int i = 0; i < graphPoints.size(); i++) {
			if (graphPoints.size() > 1) {
				int x0 = i * (getWidth() - padding * 2 - labelPadding) / (graphPoints.size() - 1) + padding + labelPadding;
				int x1 = x0;
				int y0 = getHeight() - padding - labelPadding;
				int y1 = y0 - pointWidth;

				// render the grid line on every fifth hatch mark and display the label
				if (i % 5 == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth,
							x1, graphPoints.get(i).y);
					g2.setColor(Color.BLACK);
					String xLabel = i * 100 + "m";
					FontMetrics metrics = g2.getFontMetrics();
					int labelWidth = metrics.stringWidth(xLabel);
					g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
				}

				// draw hatch mark
				g2.drawLine(x0, y0, x1, y1);
			}
		}

		// create x axis
		g2.drawLine(padding + labelPadding,
				getHeight() - padding - labelPadding,
				getWidth() - padding,
				getHeight() - padding - labelPadding);


		// paint the curve lines
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

			// display the label every third value, or if it is an extreme point in the graph
			int j = i * 2;
			if (i % 3 == 0 || i == 0 ||
					i == graphPoints.size() - 1) {

				// display the value above the point
				g2.setColor(pointColor);
				String metricLabel = "" + ((int) values[j]);

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
	 * Generates an image of the graph.
	 *
	 * @param values an array of temperature values indexed by altitude
	 * @return a {@link BufferedImage} containing the temperature-altitude graph.
	 */
	public static BufferedImage getImage(double[] values) {
		AltitudePanel panel = new AltitudePanel(values);

		int w = panel.getWidth();
		int h = panel.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		panel.print(g);
		g.dispose();
		return bi;
	}

}
