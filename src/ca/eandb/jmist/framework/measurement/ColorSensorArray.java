/**
 * 
 */
package ca.eandb.jmist.framework.measurement;

import java.util.Arrays;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
public final class ColorSensorArray {

	private final ColorModel colorModel;
	
	private final double[] weight;
	
	private final int numSensors;
	
	private final int numChannels;
	
	private class Callback implements CollectorSphere.Callback {

		private final Color color;
		
		public Callback(Color color) {
			this.color = color;
		}
		
		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.measurement.CollectorSphere.Callback#record(int)
		 */
		public void record(int sensor) {
			ColorSensorArray.this.record(sensor, color);
		}
		
	}
	
	public ColorSensorArray(int numSensors, ColorModel colorModel) {
		this.colorModel = colorModel;
		this.numSensors = numSensors;
		this.numChannels = colorModel.getNumChannels();
		this.weight = new double[numSensors * numChannels];
	}
	
	public Callback createCallback(Color color) {
		return new Callback(color);
	}
	
	public void record(int sensor, Color color) {
		if (color != null) {
			double[] channels = color.toArray();
			for (int i = 0, j = sensor * numChannels; i < numChannels; i++, j++) {
				weight[j] += channels[i];
			}
		}
	}
	
	public Color getTotalWeight(int sensor) {
		int from = sensor * numChannels;
		int to = from + numChannels;
		return colorModel.fromArray(Arrays.copyOfRange(weight, from, to), null);
	}
	
	public void reset() {
		Arrays.fill(weight, 0);
	}
	
	public void merge(ColorSensorArray other) {
		MathUtil.add(weight, other.weight);
	}
	
	public int sensors() {
		return numSensors;
	}

}
