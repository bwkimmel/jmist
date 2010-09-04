/**
 *
 */
package ca.eandb.jmist.framework.color;

import java.io.Serializable;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;

/**
 * @author Brad
 *
 */
public abstract class ColorModel implements Serializable {

	/** Serialization version ID. */
	private static final long serialVersionUID = -102213996206421899L;

	public abstract Spectrum getBlack();

	public abstract Spectrum getWhite();

	public abstract Spectrum fromRGB(double r, double g, double b);

	public Spectrum fromRGB(RGB rgb) {
		return fromRGB(rgb.r(), rgb.g(), rgb.b());
	}

	public abstract Spectrum fromXYZ(double x, double y, double z);

	public Spectrum fromXYZ(CIEXYZ xyz) {
		return fromXYZ(xyz.X(), xyz.Y(), xyz.Z());
	}

	public abstract Spectrum getGray(double value);

	public abstract Spectrum getContinuous(Function1 spectrum);

	public abstract Color fromArray(double[] values, WavelengthPacket lambda);

	public abstract Color getBlack(WavelengthPacket lambda);

	public abstract Color getWhite(WavelengthPacket lambda);

	public abstract Color getGray(double value, WavelengthPacket lambda);

	public abstract Color sample(Random random);

	public abstract Raster createRaster(int width, int height);

	public abstract int getNumChannels();

}
