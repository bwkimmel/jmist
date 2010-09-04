/**
 * 
 */
package ca.eandb.jmist.framework.loader.radiance;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.RGB;

/**
 * Represents the XYZE pixel format for a <code>RadiancePicture</code>.
 * This class is a singleton.
 * @see #INSTANCE
 * @author Brad Kimmel
 */
final class XYZEPixelFormat implements PixelFormat {

	/** Serialization version ID. */
	private static final long serialVersionUID = 8026681085259479612L;
	
	/** The single <code>XYZEPixelFormat</code> instance. */
	public static final XYZEPixelFormat INSTANCE = new XYZEPixelFormat();
	
	/**
	 * Creates a new <code>XYZEPixelFormat</code>.
	 * This constructor is private because this class is a singleton.
	 */
	private XYZEPixelFormat() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toRGB(int)
	 */
	public RGB toRGB(int raw) {
		return toXYZ(raw).toRGB();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toRaw(ca.eandb.jmist.framework.color.RGB)
	 */
	public int toRaw(RGB rgb) {
		return rgb.toXYZ().toXYZE();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toRaw(ca.eandb.jmist.framework.color.CIEXYZ)
	 */
	public int toRaw(CIEXYZ xyz) {
		return xyz.toXYZE();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.radiance.PixelFormat#toXYZ(int)
	 */
	public CIEXYZ toXYZ(int raw) {
		return CIEXYZ.fromXYZE(raw);
	}

}
