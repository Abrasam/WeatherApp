package uk.ac.cam.ia.group14.ks830.graphs;
import uk.ac.cam.ia.group14.util.WeatherSlice;


/**
 * {@link Graph} class receives the data from arrays of {@link WeatherSlice}s
 * and processes it to return the {@link javax.swing.JPanel}s of graphs for weather attributes
 * {@link WeatherSlice#temp}, {@link WeatherSlice#wind}, and {@link WeatherSlice#rain}
 * against {@link WeatherSlice#time}, adjusting the designs of each graph accordingly.
 *
 */

public class Graph {

	private GraphPanel temperatureGraph;
	private GraphPanel rainGraph;
	private GraphPanel windGraph;


	/**
	 * The constructor uses the {@link WeatherSlice[]} array to extract the metrics into arrays,
	 * which are then converted into three {@link GraphPanel}s.
	 *
	 * @param weatherSliceData the array of Weather Slices for a 120-hour period, starting at midnight.
	 */
	public Graph(WeatherSlice[] weatherSliceData) {
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

		temperatureGraph = new GraphPanel(temperatureData, WeatherSlice.Parameter.TEMPERATURE);
		rainGraph = new GraphPanel(rainData, WeatherSlice.Parameter.RAIN);
		windGraph = new GraphPanel(windData, WeatherSlice.Parameter.WIND);
	}

	/**
	 * Returns the GraphPanel displaying the rain data
	 */
	public GraphPanel getRainGraph() {
		return rainGraph;
	}

	/**
	 * Returns the GraphPanel displaying the temperature data
	 */
	public GraphPanel getTemperatureGraph() {
		return temperatureGraph;
	}

	/**
	 * Returns the GraphPanel displaying the wind data
	 */
	public GraphPanel getWindGraph() {
		return windGraph;
	}
}
