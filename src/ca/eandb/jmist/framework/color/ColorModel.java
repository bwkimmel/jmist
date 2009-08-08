/**
 *
 */
package ca.eandb.jmist.framework.color;

import java.io.Serializable;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.math.Tuple3;

/**
 * @author Brad
 *
 */
public abstract class ColorModel implements Serializable {

	public abstract Spectrum getBlack();

	public abstract Spectrum getWhite();

	public abstract Spectrum fromRGB(double r, double g, double b);

	public Spectrum fromRGB(Tuple3 rgb) {
		return fromRGB(rgb.x(), rgb.y(), rgb.z());
	}

	public abstract Spectrum fromXYZ(double x, double y, double z);

	public Spectrum fromXYZ(Tuple3 xyz) {
		return fromXYZ(xyz.x(), xyz.y(), xyz.z());
	}

	public abstract Spectrum getGray(double value);

	public abstract Spectrum getContinuous(Function1 spectrum);

	public abstract Color getBlack(WavelengthPacket lambda);

	public abstract Color getWhite(WavelengthPacket lambda);

	public abstract Color getGray(double value, WavelengthPacket lambda);

	public abstract Color sample(Random random);

	public abstract Raster createRaster(int width, int height);

	public abstract int getNumChannels();

}
