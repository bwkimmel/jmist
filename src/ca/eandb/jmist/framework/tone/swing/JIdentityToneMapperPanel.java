/**
 * 
 */
package ca.eandb.jmist.framework.tone.swing;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.tone.ToneMapper;

/**
 * @author brad
 *
 */
public final class JIdentityToneMapperPanel extends JToneMapperPanel {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6721700070291533870L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.tone.ToneMapperFactory#createToneMapper(java.lang.Iterable)
	 */
	@Override
	public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
		return ToneMapper.IDENTITY;
	}

}
