/**
 * 
 */
package ca.eandb.jmist.framework.loader.radiance;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.RGB;

/**
 * Represents the RGBE pixel format for a <code>RadiancePicture</code>.
 * This class is a singleton.
 * @see #INSTANCE
 * @author Brad Kimmel
 */
final class RGBEPixelFormat implements PixelFormat {

	/** Serialization version ID. */
	private static final long serialVersionUID = 8026681085259479612L;
	
	/** The single <code>RGBEPixelFormat</code> instance. */
	public static final RGBEPixelFormat INSTANCE = new RGBEPixelFormat();
	
	/**
	 * Creates a new <code>RGBEPixelFormat</code>.
	 * This constructor is private because this class is a singleton.
	 */
	private RGBEPixelFormat() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toRGB(int)
	 */
	public RGB toRGB(int raw) {
		return RGB.fromRGBE(raw);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toRaw(ca.eandb.jmist.framework.color.RGB)
	 */
	public int toRaw(RGB rgb) {
		return rgb.toRGBE();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toRaw(ca.eandb.jmist.framework.color.CIEXYZ)
	 */
	public int toRaw(CIEXYZ xyz) {
		return xyz.toRGB().toRGBE();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toXYZ(int)
	 */
	public CIEXYZ toXYZ(int raw) {
		return toRGB(raw).toXYZ();
	}

}
