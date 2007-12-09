/**
 *
 */
package org.jmist.packages.light;

import org.jmist.framework.AbstractSpectrum;
import org.jmist.framework.Illuminable;
import org.jmist.framework.Light;
import org.jmist.framework.Spectrum;
import org.jmist.framework.SurfacePoint;
import org.jmist.framework.VisibilityFunction3;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.RandomUtil;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;
import org.jmist.util.ArrayUtil;
import org.jmist.util.MathUtil;

/**
 * A hemispherical <code>Light</code> that simulates daylight conditions.
 * @author bkimmel
 */
public final class DayLight implements Light {

	/**
	 * Creates a new <code>DayLight</code> with the sun and zenith in the
	 * positive Y direction.
	 */
	public DayLight() {
		this(Vector3.J);
	}

	/**
	 * Creates a new <code>DayLight</code> with zenith in the positive Y
	 * direction.
	 * @param sun The direction toward the sun.
	 */
	public DayLight(Vector3 sun) {
		this(sun, Vector3.J);
	}

	/**
	 * Creates a new <code>DayLight</code>.
	 * @param sun The direction toward the sun.
	 * @param zenith The direction toward the center of the sky.
	 */
	public DayLight(Vector3 sun, Vector3 zenith) {
		this(sun, zenith, 2.0, true);
	}

	/**
	 * Creates a new <code>DayLight</code>.
	 * @param sun The direction toward the sun.
	 * @param zenith The direction toward the center of the sky.
	 * @param turbidity The turbidity (haziness) in the atmosphere.
	 * @param shadows A value indicating whether shadows should be simulated.
	 */
	public DayLight(Vector3 sun, Vector3 zenith, double turbidity, boolean shadows) {

		this.sun = sun.unit();
		this.zenith = zenith.unit();
		this.l = 0.0035;
		this.w = 0.02;
		this.daytime = (sun.dot(zenith) > 0.0);

		this.FY = new double[5];
		this.Fx = new double[5];
		this.Fy = new double[5];

		this.shadows = shadows;
		this.solarRadiance = new SunRadianceSpectrum();

		double	sdotz = sun.dot(zenith);
		double	theta_s = Math.acos(sdotz);
		int		i;

		airmass = 1.0 / (sdotz + 0.15 * Math.pow(93.885 - theta_s * (180.0 / Math.PI), -1.253));

		for (i = 0; i < 5; i++)
		{
			FY[i] = TY0[i] + turbidity * TY1[i];
			Fx[i] = Tx0[i] + turbidity * Tx1[i];
			Fy[i] = Ty0[i] + turbidity * Ty1[i];
		}

		double	chi = (4.0 / 9.0 - turbidity / 120.0) * (Math.PI - 2.0 * theta_s);
		double	Yz = (4.0453 * turbidity - 4.9710) * Math.tan(chi) - 0.2155 * turbidity + 2.4192;

		double	T2 = turbidity * turbidity;
		double	theta_s2 = theta_s * theta_s;
		double	theta_s3 = theta_s * theta_s2;

		double	xz = (T2 * Txz[0][0] + turbidity * Txz[1][0] + Txz[2][0]) * theta_s3 +
				     (T2 * Txz[0][1] + turbidity * Txz[1][1] + Txz[2][1]) * theta_s2 +
				     (T2 * Txz[0][2] + turbidity * Txz[1][2] + Txz[2][2]) * theta_s  +
				     (T2 * Txz[0][3] + turbidity * Txz[1][3] + Txz[2][3]);

		double	yz = (T2 * Tyz[0][0] + turbidity * Tyz[1][0] + Tyz[2][0]) * theta_s3 +
				     (T2 * Tyz[0][1] + turbidity * Tyz[1][1] + Tyz[2][1]) * theta_s2 +
				     (T2 * Tyz[0][2] + turbidity * Tyz[1][2] + Tyz[2][2]) * theta_s  +
				     (T2 * Tyz[0][3] + turbidity * Tyz[1][3] + Tyz[2][3]);


		Y0 = Yz / computeF(zenith, FY);
		x0 = xz / computeF(zenith, Fx);
		y0 = yz / computeF(zenith, Fy);

		alpha = 1.3;
		beta = 0.04608 * turbidity - 0.04586;

		tau_o = new double[DL_WAVELENGTHS.length];
		tau_wa = new double[DL_WAVELENGTHS.length];
		for (i = 0; i < DL_WAVELENGTHS.length; i++)
		{
			tau_o[i] = Math.exp(-ko[i] * l * airmass);
			tau_wa[i] = Math.exp(-0.2385 * kwa[i] * w * airmass / Math.pow(1.0 + 20.07 * kwa[i] * w * airmass, 0.45));
		}

	}

	private double computeF(Vector3 I, double[] F) {

		assert(F.length == 5);

		double	zdotI = zenith.dot(I);
		double	sdotI = sun.dot(I);
		double	gamma = Math.acos(sdotI);

		return (1.0 + F[0] * Math.exp(F[1] / zdotI)) * (1.0 + F[2] * Math.exp(F[3] * gamma) + F[4] * (sdotI * sdotI));

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Light#illuminate(org.jmist.framework.SurfacePoint, org.jmist.framework.VisibilityFunction3, org.jmist.framework.Illuminable)
	 */
	@Override
	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
			Illuminable target) {

		Vector3	source = RandomUtil.uniformOnUpperHemisphere().toCartesian(Basis3.fromW(zenith));
		Ray3	ray = new Ray3(x.location(), source);

		if (source.dot(x.normal()) > 0.0 && (!shadows || vf.visibility(ray, Interval.POSITIVE))) {
			target.illuminate(source, new SkyRadianceSpectrum(source));
		}

		if (daytime && sun.dot(x.normal()) > 0.0) {
			ray = new Ray3(x.location(), sun);

			if (!shadows || vf.visibility(ray, Interval.POSITIVE)) {
				target.illuminate(sun, this.solarRadiance);
			}
		}

	}

	/**
	 * A <code>Spectrum</code> representing the indirect radiance from a
	 * direction toward the sky.
	 * @author bkimmel
	 */
	private final class SkyRadianceSpectrum extends AbstractSpectrum {

		/**
		 * Creates a new <code>SkyRadianceSpectrum</code>.
		 * @param source The direction from which to compute the radiance.
		 */
		public SkyRadianceSpectrum(Vector3 source) {
			this.source = source;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.AbstractSpectrum#sample(double)
		 */
		@Override
		public double sample(double wavelength) {

			this.ensureReady();
			double	S0_lambda = MathUtil.interpolate(DL_WAVELENGTHS, S0, wavelength);
			double	S1_lambda = MathUtil.interpolate(DL_WAVELENGTHS, S1, wavelength);
			double	S2_lambda = MathUtil.interpolate(DL_WAVELENGTHS, S2, wavelength);

			return Yfactor * (S0_lambda + M1 * S1_lambda + M2 * S2_lambda);

		}

		/**
		 * Performs common computation prior to sampling this
		 * <code>Spectrum</code>.
		 */
		private void ensureReady() {
			if (!ready) {
				Y = Math.max(Y0 * computeF(source, FY), 0.0);
				x = x0 * computeF(source, Fx);
				y = y0 * computeF(source, Fy);
				M1 = (-1.3515 - 1.7703 * x + 5.9114 * y) / (0.0241 + 0.2562 * x - 0.7341 * y);
				M2 = (0.0300 - 31.4424 * x + 30.0717 * y) / (0.0241 + 0.2562 * x - 0.7341 * y);
				YS = YS0 + M1 * YS1 + M2 * YS2;
				Yfactor = Y / YS;
				ready = true;
			}
		}

		private boolean ready = false;
		private final Vector3 source;
		private double Y, x, y;
		private double M1, M2;
		private double YS;
		private double Yfactor;

	}

	/**
	 * A <code>Spectrum</code> representing the direct radiance from the sun.
	 * @author bkimmel
	 */
	private final class SunRadianceSpectrum extends AbstractSpectrum {

		/* (non-Javadoc)
		 * @see org.jmist.framework.AbstractSpectrum#sample(double)
		 */
		@Override
		public double sample(double wavelength) {

			double	H0 = MathUtil.interpolate(DL_WAVELENGTHS, SOLAR_RADIANCE, wavelength);
			double	tau_r = Math.exp(-0.008735 * Math.pow(wavelength, -4.08 * airmass));
			double	tau_a = Math.exp(-beta * Math.pow(wavelength, -alpha * airmass));
			double	tau_o = MathUtil.interpolate(DL_WAVELENGTHS, DayLight.this.tau_o, wavelength);
			double	tau_wa = MathUtil.interpolate(DL_WAVELENGTHS, DayLight.this.tau_wa, wavelength);

			return H0 * tau_r * tau_a * tau_o * tau_wa;

		}

	}

	private static final double DL_MIN_WAVELENGTH = 380.0;
	private static final double DL_MAX_WAVELENGTH = 750.0;

	private static final double[] DL_WAVELENGTHS = ArrayUtil.range(DL_MIN_WAVELENGTH, DL_MAX_WAVELENGTH, 38);

	private static final double[] SOLAR_RADIANCE = {
		                           16659.0, 16233.7,	// 380 - 390
		21127.5, 25888.2, 25829.1, 24232.3, 26760.5,	// 400 - 440
		29658.3, 30545.4, 30057.5, 30663.7, 28830.4,	// 450 - 490
		28712.1, 27825.0, 27100.6, 27233.6, 26361.3,	// 500 - 540
		25503.8, 25060.2, 25311.6, 25355.9, 25134.2,	// 550 - 590
		24631.5, 24173.2, 23685.3, 23212.1, 22827.7,	// 600 - 640
		22339.8, 21970.2, 21526.7, 21097.9, 20728.3,	// 650 - 690
		20240.4, 19870.8, 19427.2, 19072.4, 18628.9,	// 700 - 740
		18259.2                                     	// 750
	};

	private static final double[] S0 = {
		                                                         63.4,  65.8,	// 380 - 390
		 94.8, 104.8, 105.9,  96.8, 113.9, 125.6, 125.5, 121.3, 121.3, 113.5,	// 400 - 490
		113.1, 110.8, 106.5, 108.8, 105.3, 104.4, 100.0,  96.0,  95.1,  89.1,	// 500 - 590
		 90.5,  90.3,  88.4,  84.0,  85.1,  81.9,  82.6,  84.9,  81.3,  71.9,	// 600 - 690
		 74.3,  76.4,  63.3,  71.7,  77.0,  65.2                             	// 700 - 750
	};

	private static final double YS0 = 7.3378297738502e+6;

	private static final double[] S1 = {
		                                                         38.5,  35.0,	// 380 - 390
		 43.4,  46.3,  43.9,  37.1,  36.7,  35.9,  32.6,  27.9,  24.3,  20.1,	// 400 - 490
		 16.2,  13.2,   8.6,   6.1,   4.2,   1.9,   0.0,  -1.6,  -3.5,  -3.5,	// 500 - 590
		 -5.8,  -7.2,  -8.6,  -9.5, -10.9, -10.7, -12.0, -14.0, -13.6, -12.0,	// 600 - 690
		-13.3, -12.9, -10.6, -11.6, -12.2, -10.2                             	// 700 - 750
	};

	private static final double YS1 = 1.4739864150750e+5;

	private static final double[] S2 = {
		                                                          3.0,   1.2,	// 380 - 390
		 -1.1,  -0.5,  -0.7,  -1.2,  -2.6,  -2.9,  -2.8,  -2.6,  -2.6,  -1.8,	// 400 - 490
		 -1.5,  -1.3,  -1.2,  -1.0,  -0.5,  -0.3,  -0.0,   0.2,   0.5,   2.1,	// 500 - 590
		  3.2,   4.1,   4.7,   5.1,   6.7,   7.3,   8.6,   9.8,  10.2,   8.3,	// 600 - 690
		  9.6,   8.5,   7.0,   7.6,   8.0,   6.7                             	// 700 - 750
	};

	private static final double YS2 = 5.1160547134100e+4;

	private static final double[] ko = {
		                                                 0.0,  0.0,	// 380 - 390
		 0.0,  0.0,  0.0,  0.0,  0.0,  0.3,  0.6,  0.9,  1.4,  2.1,	// 400 - 490
		 3.0,  4.0,  4.8,  6.3,  7.5,  8.5, 10.3, 12.0, 12.0, 11.5,	// 500 - 590
		12.5, 12.0, 10.5,  9.0,  7.9,  6.7,  5.7,  4.8,  3.6,  2.8,	// 600 - 690
		 2.3,  1.8,  1.4,  1.1,  1.0,  0.9                         	// 700 - 750
	};

	private static final double[] kwa = {
		                                                                0.00,   0.00,	// 380 - 390
		0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,	// 400 - 490
		0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,	// 500 - 590
		0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   0.00,   1.60,	// 600 - 690
		2.40,   1.25, 100.00,  87.00,   6.10,   0.10                                 	// 700 - 750
	};

	private static final double[] TY0 = { -1.4630,  0.4275,  5.3251, -2.5771,  0.3703 };
	private static final double[] TY1 = {  0.1787, -0.3554, -0.0227,  0.1206, -0.0670 };

	private static final double[] Tx0 = { -0.2592,  0.0008,  0.2125, -0.8989,  0.0452 };
	private static final double[] Tx1 = { -0.0193, -0.0665, -0.0004, -0.0641, -0.0033 };

	private static final double[] Ty0 = { -0.2608,  0.0092,  0.2102, -1.6537,  0.0529 };
	private static final double[] Ty1 = { -0.0167, -0.0950, -0.0079, -0.0441, -0.0109 };

	private static final double[][] Txz = {
		{  0.0017, -0.0037,  0.0021,  0.0000 },
		{ -0.0290,  0.0638, -0.0320,  0.0039 },
		{  0.1169, -0.2120,  0.0605,  0.2589 }
	};

	private static final double[][] Tyz = {
		{  0.0028, -0.0061,  0.0032,  0.0000 },
		{ -0.0421,  0.0897, -0.0415,  0.0052 },
		{  0.1535, -0.2676,  0.0667,  0.2669 }
	};

	private final Vector3	zenith;
	private final Vector3	sun;
	private final boolean	daytime;
	private final double	airmass;
	private final double	Y0, x0, y0;
	private final double[]	FY, Fx, Fy;
	private final double[]	tau_o, tau_wa;
	private final double	alpha, beta;
	private final double	w, l;
	private final boolean	shadows;
	private final Spectrum	solarRadiance;

}