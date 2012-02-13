/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public final class RtColor {
	
	private double[] channels;
	
	public RtColor(double... channels) {
		this.channels = channels.clone();
	}
	
	public double get(int index) {
		return channels[index];
	}
	
	public int size() {
		return channels.length;
	}

}
