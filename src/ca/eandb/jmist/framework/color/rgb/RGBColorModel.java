/**
 *
 */
package ca.eandb.jmist.framework.color.rgb;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Matrix;
import ca.eandb.jmist.math.Vector3;

/**
 * A three channel <code>ColorModel</code>.
 * @author Brad
 */
public final class RGBColorModel extends ColorModel {

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
	 * @see ca.eandb.jmist.framework.color.ColorModel#sample()
	 */
	@Override
	public Color sample() {
		return RGBColor.WHITE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumBands()
	 */
	@Override
	public int getNumChannels() {
		return 3;
	}

}
