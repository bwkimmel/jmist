/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Spectrum;

/**
 * @author Brad
 *
 */
public final class RGBColorModel extends ColorModel {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromRGB(double, double, double)
	 */
	@Override
	public Color fromRGB(double r, double g, double b) {
		return new RGBColor(r, g, b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack()
	 */
	@Override
	public Color getBlack() {
		return RGBColor.BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double)
	 */
	@Override
	public Color getGray(double value) {
		return new RGBColor(value, value, value);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getUnit()
	 */
	@Override
	public Color getUnit() {
		return RGBColor.UNIT;
	}

	@Override
	public Color fromSpectrum(Spectrum spectrum) {
		return new RGBColor(spectrum.sample(650e-9), spectrum.sample(550e-9), spectrum.sample(450e-9));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumBands()
	 */
	@Override
	public int getNumBands() {
		return 3;
	}

}
