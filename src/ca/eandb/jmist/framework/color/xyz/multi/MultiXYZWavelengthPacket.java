/**
 *
 */
package ca.eandb.jmist.framework.color.xyz.multi;

import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
/* package */ final class MultiXYZWavelengthPacket implements WavelengthPacket {

	private final MultiXYZColorModel owner;

	private final double[] wavelengths;

	public MultiXYZWavelengthPacket(double[] wavelengths, MultiXYZColorModel owner) {
		this.owner = owner;
		this.wavelengths = wavelengths;
	}

	public double getLambda(int channel) {
		return wavelengths[channel];
	}

	public double getLambdaX(int channel) {
		return wavelengths[channel + owner.getOffsetX()];
	}

	public double getLambdaY(int channel) {
		return wavelengths[channel + owner.getOffsetY()];
	}

	public double getLambdaZ(int channel) {
		return wavelengths[channel + owner.getOffsetZ()];
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.WavelengthPacket#getColorModel()
	 */
	public MultiXYZColorModel getColorModel() {
		return owner;
	}

}
