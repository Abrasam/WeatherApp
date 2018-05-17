package uk.ac.cam.ia.group14.ks830.graphs;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import javax.swing.*;
import java.util.Random;


/**
 * {@link Graph} class receives the data from arrays of {@link WeatherSlice}s
 * and processes it to return the {@link javax.swing.JPanel}s of graphs for weather attributes
 * {@link WeatherSlice#temp}, {@link WeatherSlice#wind}, and {@link WeatherSlice#rain}
 * against {@link WeatherSlice#time}, adjusting the designs of each graph accordingly.
 *
 */

public class Graph {

	private JPanel temperatureGraph;
	private JPanel rainGraph;
	private JPanel windGraph;


	/**
	 * The constructor uses the {@link WeatherSlice[]} array to extract the metrics into arrays,
	 * which are then converted into three {@link GraphPanel}s.
	 *
	 * @param weatherSliceData the array of Weather Slices for a 120-hour period, starting at midnight.
	 */
	private Graph(WeatherSlice[] weatherSliceData) {
		// extract weatherSliceData into the separate temperature, rain, wind data
		double[] temperatureData = new double[120];
		double[] rainData = new double[120];
		double[] windData = new double[120];
		int i = 0;

		for(WeatherSlice slice: weatherSliceData) {
			temperatureData[i] = slice.getTemp();
			rainData[i] = slice.getRain();
			windData[i++] = slice.getWind();
		}

		temperatureGraph = GraphPanel.getPanel(temperatureData, WeatherSlice.Parameter.TEMPERATURE);
		rainGraph = GraphPanel.getPanel(rainData, WeatherSlice.Parameter.RAIN);
		windGraph = GraphPanel.getPanel(windData, WeatherSlice.Parameter.WIND);
	}

	/**
	 * The Graph to generate random values for data. Used for testing.
	 */
	private Graph() {
		// extract weatherSliceData into the separate temperature, rain, wind data
		double[] temperatureData = generateRandomValues();
		double[] rainData = generateRandomValues();
		double[] windData = generateRandomValues();

		temperatureGraph = GraphPanel.getPanel(temperatureData, WeatherSlice.Parameter.TEMPERATURE);
		rainGraph = GraphPanel.getPanel(rainData, WeatherSlice.Parameter.RAIN);
		windGraph = GraphPanel.getPanel(windData, WeatherSlice.Parameter.WIND);
	}
	/**
	 * Returns the GraphPanel displaying the rain data
	 */
	public JPanel getRainGraph() {
		return rainGraph;
	}

	/**
	 * Returns the GraphPanel displaying the random temperature data. Use for testing.
	 */
	public JPanel getTemperatureGraph() {
		return temperatureGraph;
	}

	/**
	 * Returns the GraphPanel displaying the random wind data. Use for testing.
	 */
	public JPanel getRandomWindGraph() {
		return windGraph;
	}

	/**
	 * Returns the GraphPanel displaying the random rain data. Use for testing.
	 */
	public static JPanel getRandomRainGraph() {
		Graph graph = new Graph();
		return graph.rainGraph;
	}

	/**
	 * Returns the GraphPanel displaying the random temperature data. Use for testing.
	 */
	public static JPanel getRandomTemperatureGraph() {
		Graph graph = new Graph();
		return graph.temperatureGraph;
	}

	/**
	 * Returns the GraphPanel displaying the wind data
	 */
	public static JPanel getWindGraph() {
		Graph graph = new Graph();
		return graph.windGraph;
	}

	/**
	 * private method for random value generation, to be used for testing
	 */
	private double[] generateRandomValues() {
		double[] values = new double[120];
		Random random = new Random();
		int maxDataPoints = values.length;
		int maxScore = 50;
		for (int i = 0; i < maxDataPoints; i++) {
			values[i] = ((-10 + random.nextDouble() * maxScore));
		}

		return values;
	}
}

