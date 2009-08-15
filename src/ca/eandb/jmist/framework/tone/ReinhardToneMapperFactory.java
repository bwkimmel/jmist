/**
 *
 */
package ca.eandb.jmist.framework.tone;

import ca.eandb.jmist.framework.color.CIEXYZ;

/**
 * @author Brad
 *
 */
public final class ReinhardToneMapperFactory implements ToneMapperFactory {

	/** Serialization version ID. */
	private static final long serialVersionUID = -8014074363504189066L;

	private static final double DEFAULT_DELTA = 1.0;

	private final double delta;

	public ReinhardToneMapperFactory(double delta) {
		this.delta = delta;
	}

	public ReinhardToneMapperFactory() {
		this(DEFAULT_DELTA);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ToneMapperFactory#createToneMapper(java.lang.Iterable)
	 */
	public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
		double Yavg = 0.0;
		double Ymax = 0.0;
		int n = 0;
		for (CIEXYZ sample : samples) {
			if (sample != null) {
				double Y = sample.Y();
				if (Y > Ymax) {
					Ymax = Y;
				}
				Yavg += Math.log(delta + Y);
				n++;
			}
		}
		Yavg /= (double) n;
		Yavg = Math.exp(Yavg) - delta;

		double Ymid = 1.03 - 2.0 / (2.0 + Math.log10(Yavg + 1.0));
		return new ReinhardToneMapper(Ymid / Yavg, Ymax);
	}

}
