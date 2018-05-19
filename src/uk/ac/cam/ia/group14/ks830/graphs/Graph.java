package uk.ac.cam.ia.group14.ks830.graphs;
import uk.ac.cam.ia.group14.sjs252.WeatherFetcher;
import uk.ac.cam.ia.group14.util.RegionID;
import uk.ac.cam.ia.group14.util.WeatherSlice;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * {@link Graph} class receives the data from arrays of {@link WeatherSlice}s
 * and processes it to return the {@link javax.swing.JPanel}s of graphs for weather attributes
 * {@link WeatherSlice#temp}, {@link WeatherSlice#wind}, and {@link WeatherSlice#rain}
 * against {@link WeatherSlice#time}, adjusting the designs of each graph accordingly.
 *
 */

public class Graph {

	private BufferedImage temperatureGraph;
	private BufferedImage rainGraph;
	private BufferedImage windGraph;


	/**
	 * The constructor takes in the {@link RegionID} to get the corresponding {@link WeatherSlice}[] array
	 * from a {@link WeatherFetcher}.
	 * This is then converted into three {@link GraphPanel}s.
	 *
	 * @param regionID: the region for which the weather data graphs should be displayed.
	 */
	private Graph(RegionID regionID) {
		// retrieve a WeatherSlice array containing the hourly data for the next five days
		WeatherSlice[] weatherSliceData = WeatherFetcher.getInstance().getRegion(regionID).getHours();

		// extract weatherSliceData into the separate temperature, rain, wind data
		double[] temperatureData = new double[120];
		double[] rainData = new double[120];
		double[] windData = new double[120];
		int i = 0;

		int startTime = weatherSliceData[0].getTime().getHours();
		for(WeatherSlice slice: weatherSliceData) {
			temperatureData[i] = slice.getTemp();
			rainData[i] = slice.getRain();
			windData[i++] = slice.getWind();
		}

		temperatureGraph = GraphPanel.getImage(temperatureData, WeatherSlice.Parameter.TEMPERATURE, startTime);
		rainGraph = GraphPanel.getImage(rainData, WeatherSlice.Parameter.RAIN, startTime);
		windGraph = GraphPanel.getImage(windData, WeatherSlice.Parameter.WIND, startTime);
	}

	/**
	 * The constructor uses the {@link WeatherSlice[]} array directly to extract the metrics into arrays,
	 * which are then converted into three {@link GraphPanel}s.
	 *
	 * @param weatherSlices: the region for which the weather data graphs should be displayed.
	 */
	public Graph(WeatherSlice[] weatherSlices) {
		// retrieve a WeatherSlice array containing the hourly data for the next five days

		// extract weatherSliceData into the separate temperature, rain, wind data
		double[] temperatureData = new double[120];
		double[] rainData = new double[120];
		double[] windData = new double[120];
		int i = 0;

		int startTime = weatherSlices[0].getTime().getHours();
		for(WeatherSlice slice: weatherSlices) {
			temperatureData[i] = slice.getTemp();
			rainData[i] = slice.getRain();
			windData[i++] = slice.getWind();
		}

		temperatureGraph = GraphPanel.getImage(temperatureData, WeatherSlice.Parameter.TEMPERATURE, startTime);
		rainGraph = GraphPanel.getImage(rainData, WeatherSlice.Parameter.RAIN, startTime);
		windGraph = GraphPanel.getImage(windData, WeatherSlice.Parameter.WIND, startTime);
	}




	/**
	 * Returns a {@link BufferedImage} containing the rain data.
	 */
	public BufferedImage getRainGraph() {
		return rainGraph;
	}

	/**
	 * Returns a {@link BufferedImage} containing the temperature data.
	 */
	public BufferedImage getTemperatureGraph() {
		return temperatureGraph;
	}

	/**
	 * Returns a {@link BufferedImage} containing the wind data.
	 */
	public BufferedImage getWindGraph() {
		return windGraph;
	}


	/**
	 * Returns a {@link BufferedImage} of an altitude-temperature graph at a certain time.
	 *
	 * @param values: an array of 30 temperatures for different heights.
	 */
	public BufferedImage getAltitudeGraph(double[] values) {
		return AltitudePanel.getImage(values);
	}


}

