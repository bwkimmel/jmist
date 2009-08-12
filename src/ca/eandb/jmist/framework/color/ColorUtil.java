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

package ca.eandb.jmist.framework.color;

import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Tuple;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * @author brad
 *
 */
public final class ColorUtil {

	public static final double LUMENS_PER_WATT = 683.0;

	public static final Tuple X_BAR = new Tuple(
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
	);

	public static final Tuple Y_BAR = new Tuple(
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
	);

	public static final Tuple Z_BAR = new Tuple(
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
	);

	public static final Tuple XYZ_WAVELENGTHS = new Tuple(ArrayUtil.range(380e-9, 780e-9, ColorUtil.X_BAR.size()));

	private static final LinearMatrix3 XYZ_TO_sRGBLin = new LinearMatrix3(
			 3.2410, -1.5374, -0.4986,
			-0.9692,  1.8760,  0.0416,
			 0.0556, -0.2040,  1.0570);

	private static final LinearMatrix3 sRGBLin_TO_XYZ = new LinearMatrix3(
			0.4124, 0.3576, 0.1805,
			0.2126, 0.7152, 0.0722,
			0.0193, 0.1192, 0.9505);

	private static final double EPSILON = 0.008856;
	private static final double CBRT_EPSILON = Math.cbrt(EPSILON);

	private static final double KAPPA = 903.3;

	public static CIEXYZ convertLab2XYZ(double L, double a, double b, double Xr, double Yr, double Zr) {
		double fy = (L + 16.0) / 116.0;
		double fx = (a / 500.0) + fy;
		double fz = fy - (b / 200.0);
		double xr = fx > CBRT_EPSILON ? fx * fx * fx : (116.0 * fx - 16.0) / KAPPA;
		double zr = fz > CBRT_EPSILON ? fz * fz * fz : (116.0 * fz - 16.0) / KAPPA;
		double yr;
		if (L > KAPPA * EPSILON) {
			yr = (L + 16.0) / 116.0;
			yr = yr * yr * yr;
		} else {
			yr = L / KAPPA;
		}
		double X = xr * Xr;
		double Y = yr * Yr;
		double Z = zr * Zr;
		return new CIEXYZ(X, Y, Z);
	}

	public static CIEXYZ convertLab2XYZ(double L, double a, double b, CIEXYZ ref) {
		return convertLab2XYZ(L, a, b, ref.X(), ref.Y(), ref.Z());
	}

	public static CIEXYZ convertLab2XYZ(CIELab lab, CIEXYZ ref) {
		return convertLab2XYZ(lab.L(), lab.a(), lab.b(), ref);
	}

	public static CIELab convertXYZ2Lab(double X, double Y, double Z, double Xr, double Yr, double Zr) {
		double xr = X / Xr;
		double yr = Y / Yr;
		double zr = Z / Zr;
		double fx = xr > EPSILON ? Math.cbrt(xr) : (KAPPA * xr + 16.0) / 116.0;
		double fy = yr > EPSILON ? Math.cbrt(yr) : (KAPPA * yr + 16.0) / 116.0;
		double fz = zr > EPSILON ? Math.cbrt(zr) : (KAPPA * zr + 16.0) / 116.0;
		double L = 116.0 * fy - 16.0;
		double a = 500.0 * (fx - fy);
		double b = 200.0 * (fy - fz);
		return new CIELab(L, a, b);
	}

	public static CIELab convertXYZ2Lab(double X, double Y, double Z, CIEXYZ ref) {
		return convertXYZ2Lab(X, Y, Z, ref.X(), ref.Y(), ref.Z());
	}

	public static CIELab convertXYZ2Lab(CIEXYZ xyz, CIEXYZ ref) {
		return convertXYZ2Lab(xyz.X(), xyz.Y(), xyz.Z(), ref);
	}

	public static CIEYuv convertXYZ2Yuv(double X, double Y, double Z) {
		double w = X + 15.0 * Y + 3.0 * Z;
		return new CIEYuv(Y, 4.0 * X / w, 6.0 * Y / w);
	}

	public static CIEYuv convertXYZ2Yuv(CIEXYZ xyz) {
		return convertXYZ2Yuv(xyz.X(), xyz.Y(), xyz.Z());
	}

	public static CIEXYZ convertYuv2XYZ(double Y, double u, double v) {
		double w = 2.0 * u - 8.0 * v + 4.0;
		double x = 3.0 * u / w;
		double y = 2.0 * v / w;
		return convertxyY2XYZ(x, y, Y);
	}

	public static CIEXYZ convertYuv2XYZ(CIEYuv Yuv) {
		return convertYuv2XYZ(Yuv.Y(), Yuv.u(), Yuv.v());
	}

	public static CIExyY convertXYZ2xyY(double X, double Y, double Z) {
		double w = X + Y + Z;
		return new CIExyY(X / w, Y / w, Y);
	}

	public static CIExyY convertXYZ2xyY(CIEXYZ xyz) {
		return convertXYZ2xyY(xyz.X(), xyz.Y(), xyz.Z());
	}

	public static CIEXYZ convertxyY2XYZ(double x, double y, double Y) {
		double w = Y / y;
		return new CIEXYZ(x * w, Y, (1.0 - x - y) * w);
	}

	public static CIEXYZ convertxyY2XYZ(CIExyY xyY) {
		return convertxyY2XYZ(xyY.x(), xyY.y(), xyY.Y());
	}

	public static CIEXYZ convertRGB2XYZ(double r, double g, double b) {
		Vector3 rgb = new Vector3(linearize(r), linearize(g), linearize(b));
		Vector3 xyz = sRGBLin_TO_XYZ.times(rgb);
		return new CIEXYZ(xyz.x(), xyz.y(), xyz.z());
	}

	public static CIEXYZ convertRGB2XYZ(RGB rgb) {
		return convertRGB2XYZ(rgb.r(), rgb.g(), rgb.b());
	}

	private static double linearize(double c) {
		if (c <= 0.04045) {
			return c / 12.92;
		} else {
			return Math.pow((c + 0.055) / 1.055, 2.4);
		}
	}

	public static RGB convertXYZ2RGB(double x, double y, double z) {
		Vector3 xyz = new Vector3(x, y, z);
		Vector3 rgb = XYZ_TO_sRGBLin.times(xyz);
		return new RGB(
				delinearize(rgb.x()),
				delinearize(rgb.y()),
				delinearize(rgb.z()));
	}

	public static RGB convertXYZ2RGB(CIEXYZ xyz) {
		return convertXYZ2RGB(xyz.X(), xyz.Y(), xyz.Z());
	}

	private static double delinearize(double c) {
		if (c <= 0.0031308) {
			return 12.92 * c;
		} else {
			return 1.055 * Math.pow(c, 1.0 / 2.4) - 0.055;
		}
	}

	public static CIEXYZ convertSample2XYZ(double wavelength, double value) {
		return new CIEXYZ(
				value * MathUtil.interpolate(XYZ_WAVELENGTHS, X_BAR, wavelength),
				value * MathUtil.interpolate(XYZ_WAVELENGTHS, Y_BAR, wavelength),
				value * MathUtil.interpolate(XYZ_WAVELENGTHS, Z_BAR, wavelength));
	}

	public static RGB convertSample2RGB(double wavelength, double value) {
		return convertXYZ2RGB(convertSample2XYZ(wavelength, value));
	}

	public static CIEXYZ convertSpectrum2XYZ(double[] wavelengths, double[] values) {
		CIEXYZ xyz = CIEXYZ.ZERO;
		for (int i = 0, n = wavelengths.length; i < n; i++) {
			xyz = xyz.plus(convertSample2XYZ(wavelengths[i], values[i]
					/ (double) n));
		}
		return xyz;
	}

	public static CIEXYZ convertSpectrum2XYZ(Tuple wavelengths, double[] values) {
		CIEXYZ xyz = CIEXYZ.ZERO;
		for (int i = 0, n = wavelengths.size(); i < n; i++) {
			xyz = xyz.plus(convertSample2XYZ(wavelengths.at(i), values[i]
					/ (double) n));
		}
		return xyz;
	}

	public static RGB convertSpectrum2RGB(double[] wavelengths, double[] values) {
		return convertXYZ2RGB(convertSpectrum2XYZ(wavelengths, values));
	}

	public static RGB convertSpectrum2RGB(Tuple wavelengths, double[] values) {
		return convertXYZ2RGB(convertSpectrum2XYZ(wavelengths, values));
	}

	public static double getMeanChannelValue(Color color) {
		int channels = color.getColorModel().getNumChannels();
		double sum = 0.0;
		for (int i = 0; i < channels; i++) {
			sum += color.getValue(i);
		}
		return sum / (double) channels;
	}

	public static double getTotalChannelValue(Color color) {
		int channels = color.getColorModel().getNumChannels();
		double sum = 0.0;
		for (int i = 0; i < channels; i++) {
			sum += color.getValue(i);
		}
		return sum;
	}

	/** Instances of <code>ColorUtil</code>	cannot be created. */
	private ColorUtil() {}

}
