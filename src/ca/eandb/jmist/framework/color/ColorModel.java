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

	public static ColorModel getInstance() {
		return RGBColorModel.getInstance();
	}

	public abstract Color getBlack();

	public abstract Color getWhite();

	public abstract Color fromRGB(double r, double g, double b);

	public abstract Color getGray(double value);

	public abstract Color fromSpectrum(Spectrum spectrum);

	public abstract int getNumChannels();

}
