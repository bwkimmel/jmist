/**
 *
 */
package ca.eandb.jmist.framework.color.luminance;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
public final class LuminanceContinuousSpectrum implements Spectrum {

	/** Serialization version ID. */
	private static final long serialVersionUID = 1202132371356039167L;

	private final Function1 spectrum;

	public LuminanceContinuousSpectrum(Function1 spectrum) {
		this.spectrum = spectrum;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.Spectrum#sample(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color sample(WavelengthPacket lambda) {
		return sample((LuminanceWavelengthPacket) lambda);
	}

	public LuminanceColor sample(LuminanceWavelengthPacket lambda) {
		double value = spectrum.evaluate(lambda.getWavelength());
		return new LuminanceColor(value, lambda);
	}

}
