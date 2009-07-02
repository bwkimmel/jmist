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

	public abstract Spectrum getBlack();

	public abstract Spectrum getWhite();

	public abstract Spectrum fromRGB(double r, double g, double b);

	public abstract Spectrum fromXYZ(double x, double y, double z);

	public abstract Spectrum getGray(double value);

	public abstract Spectrum getContinuous(Function1 spectrum);

	public abstract Color getBlack(WavelengthPacket lambda);

	public abstract Color getWhite(WavelengthPacket lambda);

	public abstract Color getGray(double value, WavelengthPacket lambda);

	public abstract Color sample();

	public abstract int getNumChannels();

}
