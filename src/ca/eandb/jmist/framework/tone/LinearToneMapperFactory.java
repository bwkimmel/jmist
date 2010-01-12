/**
 *
 */
package ca.eandb.jmist.framework.tone;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.CIExyY;

/**
 * A <code>ToneMapperFactory</code> that builds linear tone maps.
 * @author Brad Kimmel
 */
public final class LinearToneMapperFactory implements ToneMapperFactory {

	/** Serialization version ID. */
	private static final long serialVersionUID = -246622078552676822L;

	private static final double DEFAULT_DELTA = 1.0;

	private final double delta;

	public LinearToneMapperFactory(double delta) {
		this.delta = delta;
	}

	public LinearToneMapperFactory() {
		this(DEFAULT_DELTA);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.tone.ToneMapperFactory#createToneMapper(java.lang.Iterable)
	 */
	public ToneMapper createToneMapper(Iterable<CIEXYZ> samples) {
		double Yavg = 0.0;
		double Ymax = 0.0;
		int n = 0;
		for (CIEXYZ sample : samples) {
			if (sample != null) {
				double Y = Math.abs(sample.Y());
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
		CIExyY white = new CIExyY(1.0 / 3.0, 1.0 / 3.0, Yavg / Ymid);
		return new LinearToneMapper(white.toXYZ());
	}

}
