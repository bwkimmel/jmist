/*
 * Copyright (c) 2008 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.eandb.jmist.framework.color.xyz;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.pdf.PiecewiseLinearProbabilityDensityFunction;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * @author brad
 *
 */
public final class XYZColorModel extends ColorModel {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 1316752091233635632L;

	private static final LinearMatrix3 RGBLin_TO_XYZ = new LinearMatrix3(
			0.4124, 0.3576, 0.1805,
			0.2126, 0.7152, 0.0722,
			0.0193, 0.1192, 0.9505);

	private static final double[] X_BAR = new double[]{
		0.001368, 0.002236, 0.004243, 0.007650, 0.014310, // 380-400
		0.023190, 0.043510, 0.077630, 0.134380, 0.214770, // 405-425
		0.283900, 0.328500, 0.348280, 0.348060, 0.336200, // 430-450
		0.318700, 0.290800, 0.251100, 0.195360, 0.142100, // 455-475
		0.095640, 0.057950, 0.032010, 0.014700, 0.004900, // 480-500
		0.002400, 0.009300, 0.029100, 0.063270, 0.109600, // 505-525
		0.165500, 0.225750, 0.290400, 0.359700, 0.433450, // 530-550
		0.512050, 0.594500, 0.678400, 0.762100, 0.842500, // 555-575
		0.916300, 0.978600, 1.026300, 1.056700, 1.062200, // 580-600
		1.045600, 1.002600, 0.938400, 0.854450, 0.751400, // 605-625
		0.642400, 0.541900, 0.447900, 0.360800, 0.283500, // 630-650
		0.218700, 0.164900, 0.121200, 0.087400, 0.063600, // 655-675
		0.046770, 0.032900, 0.022700, 0.015840, 0.011359, // 680-700
		0.008111, 0.005790, 0.004109, 0.002899, 0.002049, // 705-725
		0.001440, 0.001000, 0.000690, 0.000476, 0.000332, // 730-750
		0.000235, 0.000166, 0.000117, 0.000083, 0.000059, // 755-775
		0.000042                                          // 780-780
	};

	private static final double[] Y_BAR = new double[]{
		0.000039, 0.000064, 0.000120, 0.000217, 0.000396, // 380-400
		0.000640, 0.001210, 0.002180, 0.004000, 0.007300, // 405-425
		0.011600, 0.016840, 0.023000, 0.029800, 0.038000, // 430-450
		0.048000, 0.060000, 0.073900, 0.090980, 0.112600, // 455-475
		0.139020, 0.169300, 0.208020, 0.258600, 0.323000, // 480-500
		0.407300, 0.503000, 0.608200, 0.710000, 0.793200, // 505-525
		0.862000, 0.914850, 0.954000, 0.980300, 0.994950, // 530-550
		1.000000, 0.995000, 0.978600, 0.952000, 0.915400, // 555-575
		0.870000, 0.816300, 0.757000, 0.694900, 0.631000, // 580-600
		0.566800, 0.503000, 0.441200, 0.381000, 0.321000, // 605-625
		0.265000, 0.217000, 0.175000, 0.138200, 0.107000, // 630-650
		0.081600, 0.061000, 0.044580, 0.032000, 0.023200, // 655-675
		0.017000, 0.011920, 0.008210, 0.005723, 0.004102, // 680-700
		0.002929, 0.002091, 0.001484, 0.001047, 0.000740, // 705-725
		0.000520, 0.000361, 0.000249, 0.000172, 0.000120, // 730-750
		0.000085, 0.000060, 0.000042, 0.000030, 0.000021, // 755-775
		0.000015                                          // 780-780
	};

	private static final double[] Z_BAR = new double[]{
		0.006450, 0.010550, 0.020050, 0.036210, 0.067850, // 380-400
		0.110200, 0.207400, 0.371300, 0.645600, 1.039050, // 405-425
		1.385600, 1.622960, 1.747060, 1.782600, 1.772110, // 430-450
		1.744100, 1.669200, 1.528100, 1.287640, 1.041900, // 455-475
		0.812950, 0.616200, 0.465180, 0.353300, 0.272000, // 480-500
		0.212300, 0.158200, 0.111700, 0.078250, 0.057250, // 505-525
		0.042160, 0.029840, 0.020300, 0.013400, 0.008750, // 530-550
		0.005750, 0.003900, 0.002750, 0.002100, 0.001800, // 555-575
		0.001650, 0.001400, 0.001100, 0.001000, 0.000800, // 580-600
		0.000600, 0.000340, 0.000240, 0.000190, 0.000100, // 605-625
		0.000050, 0.000030, 0.000020, 0.000010, 0.000000, // 630-650
		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, // 655-675
		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, // 680-700
		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, // 705-725
		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, // 730-750
		0.000000, 0.000000, 0.000000, 0.000000, 0.000000, // 755-775
		0.000000                                          // 780-780
	};

	private static final double[] WAVELENGTHS = ArrayUtil.range(380e-9, 780e-9, X_BAR.length);

	private static final ProbabilityDensityFunction X_PDF = new PiecewiseLinearProbabilityDensityFunction(WAVELENGTHS, X_BAR);
	private static final ProbabilityDensityFunction Y_PDF = new PiecewiseLinearProbabilityDensityFunction(WAVELENGTHS, Y_BAR);
	private static final ProbabilityDensityFunction Z_PDF = new PiecewiseLinearProbabilityDensityFunction(WAVELENGTHS, Z_BAR);

	private static XYZColorModel instance;

	public static XYZColorModel getInstance() {
		if (instance == null) {
			instance = new XYZColorModel();
		}
		return instance;
	}

	/**
	 * This constructor is private because this class is a singleton.
	 */
	private XYZColorModel() {
		/* nothing to do. */
	}

	private double linearize(double c) {
		if (c <= 0.04045) {
			return c / 12.92;
		} else {
			return Math.pow((c + 0.055) / 1.055, 2.4);
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromRGB(double, double, double)
	 */
	@Override
	public Spectrum fromRGB(double r, double g, double b) {
		Vector3 rgb = new Vector3(linearize(r), linearize(g), linearize(b));
		Vector3 xyz = RGBLin_TO_XYZ.times(rgb);
		return new XYZColor(xyz.x(), xyz.y(), xyz.z(), null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#fromXYZ(double, double, double)
	 */
	@Override
	public Spectrum fromXYZ(double x, double y, double z) {
		return new XYZColor(x, y, z, null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack()
	 */
	@Override
	public Spectrum getBlack() {
		return XYZColor.BLACK;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getBlack(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getBlack(WavelengthPacket lambda) {
		return getBlack((XYZWavelengthPacket) lambda);
	}

	public Color getBlack(XYZWavelengthPacket lambda) {
		return new XYZColor(0, 0, 0, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getContinuous(ca.eandb.jmist.framework.Function1)
	 */
	@Override
	public Spectrum getContinuous(Function1 spectrum) {
		return new XYZContinuousSpectrum(spectrum);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double)
	 */
	@Override
	public Spectrum getGray(double value) {
		return new XYZColor(value, value, value, null);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getGray(double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getGray(double value, WavelengthPacket lambda) {
		return getGray(value, (XYZWavelengthPacket) lambda);
	}

	public Color getGray(double value, XYZWavelengthPacket lambda) {
		return new XYZColor(value, value, value, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getNumChannels()
	 */
	@Override
	public int getNumChannels() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getWhite()
	 */
	@Override
	public Spectrum getWhite() {
		return XYZColor.WHITE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#getWhite(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color getWhite(WavelengthPacket lambda) {
		return getWhite((XYZWavelengthPacket) lambda);
	}

	public Color getWhite(XYZWavelengthPacket lambda) {
		return new XYZColor(1, 1, 1, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#sample(ca.eandb.jmist.framework.Random)
	 */
	@Override
	public Color sample(Random random) {
		XYZWavelengthPacket lambda = new XYZWavelengthPacket(X_PDF.sample(random), Y_PDF.sample(random), Z_PDF.sample(random));
		return new XYZColor(1, 1, 1, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.color.ColorModel#createRaster(int, int)
	 */
	@Override
	public Raster createRaster(int width, int height) {
		return new DoubleRaster(width, height, 3) {
			private static final long serialVersionUID = 538645330465767339L;
			protected Color getPixel(double[] raster, int index) {
				return new XYZColor(raster[index], raster[index + 1], raster[index + 2], null);
			}
		};
	}

}
