/**
 *
 */
package ca.eandb.jmist.framework.tone;

import ca.eandb.jmist.framework.color.CIEXYZ;

/**
 * @author Brad
 *
 */
public final class ConstantToneMapperFactory implements ToneMapperFactory {

	/** Serialization version ID. */
	private static final long serialVersionUID = 5180806374052522488L;

	private final ToneMapper toneMapper;

	public ConstantToneMapperFactory(ToneMapper toneMapper) {
		this.toneMapper = toneMapper;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ToneMapperFactory#createToneMapper(java.lang.Iterable)
	 */
	public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
		return toneMapper;
	}

}
