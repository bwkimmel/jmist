/**
 *
 */
package ca.eandb.jmist.framework.color.rgb;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * A three channel <code>ColorModel</code>.
 * @author Brad
 */
public final class RGBColorModel extends ColorModel {

	/** Serialization version ID. */
	private static final long serialVersionUID = 5180023685340681958L;

	/** The single <code>RGBColorModel</code> instance. */
	private static RGBColorModel instance;

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
		RGB rgb = ColorUtil.convertXYZ2RGB(x, y, z);
		return fromRGB(rgb.r(), rgb.g(), rgb.b());
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
	public Raster createRaster(int width, int height) {
		return new DoubleRaster(width, height, 3) {
			private static final long serialVersionUID = -2721143490321662390L;
			protected Color getPixel(double[] raster, int index) {
				return new RGBColor(raster[index], raster[index + 1], raster[index + 2]);
			}
		};
	}

}
