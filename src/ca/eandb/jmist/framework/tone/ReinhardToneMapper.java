/**
 *
 */
package ca.eandb.jmist.framework.tone;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.CIExyY;
import ca.eandb.jmist.framework.color.ColorUtil;

/**
 * @author Brad
 *
 */
public final class ReinhardToneMapper implements ToneMapper {

	/** Serialization version ID. */
	private static final long serialVersionUID = 260645449581170296L;

	private final double yWhiteSquared;

	private final double yScale;

	public ReinhardToneMapper(double yScale, double yWhite) {
		this.yScale = yScale;
		this.yWhiteSquared = yWhite * yWhite;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ToneMapper#apply(ca.eandb.jmist.framework.color.CIEXYZ)
	 */
	public CIEXYZ apply(CIEXYZ hdr) {
		CIExyY xyY = CIExyY.fromXYZ(hdr);
		double Y = xyY.Y() * yScale;
		if (yWhiteSquared < Double.POSITIVE_INFINITY) {
			Y = Y * (1.0 + Y / yWhiteSquared) / (1.0 + Y);
		} else {
			Y = Y / (1.0 + Y);
		}
		return ColorUtil.convertxyY2XYZ(xyY.x(), xyY.y(), Y);
	}

}
