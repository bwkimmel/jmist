/**
 *
 */
package ca.eandb.jmist.framework.tone;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.CIEXYZ;

/**
 * Applies tone mapping to a <code>CIEXYZ</code> color.
 * @author Brad Kimmel
 */
public interface ToneMapper extends Serializable {

	/**
	 * Applies this <code>ToneMapper</code> to the specified color.
	 * @param hdr The <code>CIEXYZ</code> color to apply the tone map to.
	 * @return The tone mapped <code>CIEXYZ</code> color.
	 */
	CIEXYZ apply(CIEXYZ hdr);

	/**
	 * A <code>ToneMapper</code> that does not change the input.
	 */
	public static final ToneMapper IDENTITY = new ToneMapper() {
		private static final long serialVersionUID = -1553891189627013915L;
		public CIEXYZ apply(CIEXYZ hdr) {
			return hdr;
		}
	};

}
