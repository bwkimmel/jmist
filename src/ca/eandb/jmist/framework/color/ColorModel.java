/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Spectrum;

/**
 * @author Brad
 *
 */
public abstract class ColorModel {

	private static ColorModel instance;

	public static ColorModel getInstance() {
		if (instance == null) {
			instance = new RGBColorModel();
		}
		return instance;
	}

	public abstract Color getBlack();

	public abstract Color getUnit();

	public abstract Color fromRGB(double r, double g, double b);

	public abstract Color getGray(double value);

	public abstract Color fromSpectrum(Spectrum spectrum);

	public abstract int getNumBands();

}
