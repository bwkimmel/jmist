/**
 *
 */
package ca.eandb.jmist.framework.color.luminance;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * A <code>WavelengthPacket</code> for a <code>Color</code> associated with the
 * <code>LuminanceColorModel</code>.
 * @author Brad Kimmel
 */
public final class LuminanceWavelengthPacket implements WavelengthPacket {

	/** The wavelength for this packet (in meters). */
	private final double wavelength;

	/**
	 * Creates a new <code>LuminanceWavelengthPacket</code>.
	 * @param wavelength The wavelength for this packet (in meters).
	 */
	public LuminanceWavelengthPacket(double wavelength) {
		this.wavelength = wavelength;
	}

	/**
	 * Gets the wavelength for this packet (in meters)
	 * @return The wavelength for this packet (in m).
	 */
	public double getWavelength() {
		return wavelength;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.WavelengthPacket#getColorModel()
	 */
	public ColorModel getColorModel() {
		return LuminanceColorModel.getInstance();
	}

}
