/**
 *
 */
package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.framework.Function1;

/**
 * @author Brad
 *
 */
public abstract class ColorModel {

	public static ColorModel getInstance() {
		return RGBColorModel.getInstance();
	}

	public abstract Spectrum getBlack();

	public abstract Spectrum getWhite();

	public abstract Spectrum fromRGB(double r, double g, double b);

	public abstract Spectrum getGray(double value);

	public abstract Spectrum getContinuous(Function1 spectrum);
	
	public abstract Color sample();

	public abstract int getNumChannels();

}
