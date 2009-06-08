/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Spectrum;

/**
 * A three channel <code>ColorModel</code>.
 * @author Brad
 */
public final class RGBColorModel extends ColorModel {

	/** The single <code>RGBColorModel</code> instance. */
	private static RGBColorModel instance;

	/**
	 * Gets the single <code>RGBColorModel</code> instance.
	 * @return The <code>RGBColorModel</code>.
	 */
	public static RGBColorModel getInstance() {
		if (instance == null) {
			instance = new RGBColorModel();
		}
		return instance;
	}

	/**
	 * Creates a new <code>RGBColorModel</code>.
	 * This constructor is private because this class is a singleton.
	 */
	private RGBColorModel() {
		/* nothing to do. */
	}

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
	public Color getWhite() {
		return RGBColor.WHITE;
	}

	@Override
	public Color fromSpectrum(Spectrum spectrum) {
		return new RGBColor(spectrum.sample(650e-9), spectrum.sample(550e-9), spectrum.sample(450e-9));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumBands()
	 */
	@Override
	public int getNumChannels() {
		return 3;
	}

}
