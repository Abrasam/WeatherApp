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
 *
 *
 */
public class AltitudePanel extends JPanel{
	// dimensions of the graph so they are consistent and the data is displayed clearly
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


	// declare variables for appearance of the graph
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
	private int pointWidth = 4;

	// the graph instance values
	private double[] values; // declare the array which will store graph values

	/**
	 * The constructor uses an array of given values to display a altitude-temperature graph.
	 * @param values: array of size 30 that contains temperatures for successive altitudes
	 */
	private AltitudePanel(double[] values) {

		// the graph will display the values given in the argument
		this.values = values;

		// set the size of the graph
		this.setSize(new Dimension(preferredWidth, preferredHeight));
		this.setBackground(backgroundColor);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
				if (i % 5 == 0) {
					g2.setColor(gridColor);
					g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth,
							x1, graphPoints.get(i).y);
					g2.setColor(Color.BLACK);
					String xLabel = i * 50 + " m";
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

			// display the parameter if it is a local minimum or a local maximum
			if (i > 0 && i < values.length - 1 &&
					((values[i] >= values[i+1] && values[i] >= values[i-1]) ||
							values[i] <= values[i+1] && values[i] <= values[i-1])) {
				// display the value above the point
				g2.setColor(pointColor);
				String metricLabel = "" + ((int) values[i]);;
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(metricLabel);
				g2.drawString(metricLabel, x - labelWidth / 2, y - (metrics.getHeight() / 2) + 1);
			}
		}
	}

	// find the minimum metric value (used to scale the y-axis)
	private double getMinScore() {
		double minScore = Double.MAX_VALUE;
		for (double score : values) {
			minScore = Math.min(minScore, score);
		}
		return minScore;
	}

	// find the maximum metric value (used to scale the y-axis)
	private double getMaxScore() {
		double maxScore = Double.MIN_VALUE;
		for (double score : values) {
			maxScore = Math.max(maxScore, score);
		}
		return maxScore;
	}

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
