/**
 *
 */
package ca.eandb.jmist.framework.tone;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.CIEXYZ;

/**
 * A factory for creating a <code>ToneMapper</code> from a sequence of
 * <code>CIEXYZ</code> color samples.
 * @author Brad Kimmel
 */
public interface ToneMapperFactory extends Serializable {

	/**
	 * Creates a <code>ToneMapper</code> from a sequence of <code>CIEXYZ</code>
	 * samples.
	 * @param samples A sequence of <code>CIEXYZ</code> samples (null entries
	 * 		will be ignored).
	 * @return A <code>ToneMapper</code>.
	 */
	ToneMapper createToneMapper(Iterable<CIEXYZ> samples);

	/**
	 * A <code>ToneMapperFactory</code> that always returns
	 * <code>ToneMapper.IDENTITY</code>
	 * @see ToneMapper#IDENTITY
	 */
	public static final ToneMapperFactory IDENTITY_FACTORY = new ToneMapperFactory() {
		private static final long serialVersionUID = 827095046468759801L;
		public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
			return ToneMapper.IDENTITY;
		}
	};

}
