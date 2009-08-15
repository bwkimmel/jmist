/**
 *
 */
package ca.eandb.jmist.framework.tone;

import ca.eandb.jmist.framework.color.CIEXYZ;

/**
 * A <code>ToneMapper</code> that simply divides by a given white point.
 * @author Brad Kimmel
 */
public final class LinearToneMapper implements ToneMapper {

	/** Serialization version ID. */
	private static final long serialVersionUID = -881104257251336238L;

	/** The white point to divide by. */
	private final CIEXYZ white;

	/**
	 * Creates a <code>LinearToneMapper</code>.
	 * @param white The <code>CIEXYZ</code> white point to divide by.
	 */
	public LinearToneMapper(CIEXYZ white) {
		this.white = white;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.tone.ToneMapper#apply(ca.eandb.jmist.framework.color.CIEXYZ)
	 */
	public CIEXYZ apply(CIEXYZ hdr) {
		return hdr.divide(white);
	}

}
