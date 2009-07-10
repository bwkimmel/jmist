/**
 *
 */
package ca.eandb.jmist.framework.color.rgb;

import java.io.Serializable;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Vector3;

/**
 * A three channel <code>ColorModel</code>.
 * @author Brad
 */
public final class RGBColorModel extends ColorModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5180023685340681958L;

	/** The single <code>RGBColorModel</code> instance. */
	private static RGBColorModel instance;

	private static final LinearMatrix3 XYZ_TO_sRGBLin = new LinearMatrix3(
			 3.2410, -1.5374, -0.4986,
			-0.9692,  1.8760,  0.0416,
			 0.0556, -0.2040,  1.0570);

//	/**
//	 * Gets the single <code>RGBColorModel</code> instance.
//	 * @return The <code>RGBColorModel</code>.
//	 */
//	public static RGBColorModel getInstance() {
//		if (instance == null) {
//			instance = new RGBColorModel();
//		}
//		return instance;
//	}

	/**
	 * Creates a new <code>RGBColorModel</code>.
	 * This constructor is private because this class is a singleton.
	 */
	private RGBColorModel() {
		/* nothing to do. */
	}

	/**
	 * Gets the single <code>RGBColorModel</code> instance.
	 * @return The single <code>RGBColorModel</code> instance.
	 */
	public static RGBColorModel getInstance() {
		if (instance == null) {
			instance = new RGBColorModel();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromRGB(double, double, double)
	 */
	@Override
	public Spectrum fromRGB(double r, double g, double b) {
		return new RGBColor(r, g, b);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromXYZ(double, double, double)
	 */
	@Override
	public Spectrum fromXYZ(double x, double y, double z) {
		Vector3 xyz = new Vector3(x, y, z);
		Vector3 rgb = XYZ_TO_sRGBLin.times(xyz);
		return fromRGB(
				delinearize(rgb.x()),
				delinearize(rgb.y()),
				delinearize(rgb.z()));
	}

	private double delinearize(double c) {
		if (c <= 0.0031308) {
			return 12.92 * c;
		} else {
			return 1.055 * Math.pow(c, 1.0 / 2.4) - 0.055;
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack()
	 */
	@Override
	public Spectrum getBlack() {
		return RGBColor.BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double)
	 */
	@Override
	public Spectrum getGray(double value) {
		return new RGBColor(value, value, value);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getUnit()
	 */
	@Override
	public Spectrum getWhite() {
		return RGBColor.WHITE;
	}

	@Override
	public Spectrum getContinuous(Function1 spectrum) {
		return new RGBColor(spectrum.evaluate(650e-9), spectrum.evaluate(550e-9), spectrum.evaluate(450e-9));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getBlack(WavelengthPacket lambda) {
		return RGBColor.BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getGray(double value, WavelengthPacket lambda) {
		return new RGBColor(value, value, value);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getWhite(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getWhite(WavelengthPacket lambda) {
		return RGBColor.WHITE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#sample(ca.eandb.jmist.framework.Random)
	 */
	@Override
	public Color sample(Random random) {
		return RGBColor.WHITE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumBands()
	 */
	@Override
	public int getNumChannels() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#createRaster(int, int)
	 */
	@Override
	public Raster createRaster(final int width, final int height) {
		return new Raster() {

			final double[] raster = new double[width * height * 3];

			@Override
			public Color getPixel(int x, int y) {
				int index = (y * width + x) * 3;
				double r = raster[index];
				double g = raster[index + 1];
				double b = raster[index + 2];
				return new RGBColor(r, g, b);
			}

			@Override
			public int getHeight() {
				return height;
			}

			@Override
			public int getWidth() {
				return width;
			}

			@Override
			public void setPixel(int x, int y, Color color) {
				int index = (y * width + x) * 3;
				raster[index] = color.getValue(0);
				raster[index + 1] = color.getValue(1);
				raster[index + 2] = color.getValue(2);
			}

		};
	}

}
