/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
public final class TestPathInfo implements PathInfo {

	private final RayCaster rayCaster;

	private final WavelengthPacket wavelengthPacket;

	public TestPathInfo(RayCaster rayCaster, WavelengthPacket wavelengthPacket) {
		this.rayCaster = rayCaster;
		this.wavelengthPacket = wavelengthPacket;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathInfo#getColorModel()
	 */
	public ColorModel getColorModel() {
		return getWavelengthPacket().getColorModel();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathInfo#getRayCaster()
	 */
	public RayCaster getRayCaster() {
		return rayCaster;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.gi2.PathInfo#getWavelengthPacket()
	 */
	public WavelengthPacket getWavelengthPacket() {
		return wavelengthPacket;
	}

}
