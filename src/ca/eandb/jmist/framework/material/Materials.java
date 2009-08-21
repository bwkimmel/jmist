/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.function.AbsorptionFunction1;
import ca.eandb.jmist.framework.function.PiecewiseLinearFunction1;
import ca.eandb.jmist.framework.function.ScaledFunction1;
import ca.eandb.jmist.framework.function.SellmeierFunction1;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * Static utility methods for creating various materials.
 * @author Brad Kimmel
 */
public final class Materials {

	/** Wavelengths for silver and gold. */
	private static final double[] LAMBDA_1 = new double[]{
		0.3757e-6, 0.3875e-6, 0.4000e-6, 0.4133e-6, 0.4275e-6,
		0.4428e-6, 0.4592e-6, 0.4769e-6, 0.4959e-6, 0.5166e-6,
		0.5391e-6, 0.5636e-6, 0.5904e-6, 0.6199e-6, 0.6526e-6,
		0.6888e-6, 0.7293e-6, 0.7749e-6, 0.8266e-6
	};

	/** Real part of the refractive index of silver. */
	private static final Function1 N_SILVER = new PiecewiseLinearFunction1(
			LAMBDA_1,
			new double[]{
					0.200, 0.192, 0.173, 0.173, 0.160,
					0.157, 0.144, 0.132, 0.130, 0.130,
					0.129, 0.120, 0.121, 0.131, 0.140,
					0.140, 0.148, 0.143, 0.145});

	/** Extinction index of silver. */
	private static final Function1 K_SILVER = new PiecewiseLinearFunction1(
			LAMBDA_1,
			new double[]{
					1.67, 1.81, 1.95, 2.11, 2.26,
					2.40, 2.56, 2.72, 2.88, 3.07,
					3.25, 3.45, 3.66, 3.88, 4.15,
					4.44, 4.74, 5.09, 5.50});

	/** Real part of the refractive index of gold. */
	private static final Function1 N_GOLD = new PiecewiseLinearFunction1(
			LAMBDA_1,
			new double[]{
					1.696, 1.674, 1.658, 1.636, 1.616,
					1.562, 1.426, 1.242, 0.916, 0.608,
					0.402, 0.306, 0.236, 0.194, 0.166,
					0.160, 0.164, 0.174, 0.188});

	/** Extinction index of gold. */
	private static final Function1 K_GOLD = new PiecewiseLinearFunction1(
			LAMBDA_1,
			new double[]{
					1.906, 1.936, 1.956, 1.958, 1.940,
					1.904, 1.846, 1.796, 1.840, 2.120,
					2.540, 2.88 , 2.97 , 3.06 , 3.15 ,
					3.80 , 4.35 , 4.86 , 5.39 });

	/** Wavelengths for refractive index of copper. */
	private static final double[] LAMBDA_2 = new double[]{
		0.3646e-6, 0.3874e-6, 0.4133e-6, 0.4428e-6, 0.4768e-6,
		0.5166e-6, 0.5390e-6, 0.5635e-6, 0.5904e-6, 0.6199e-6,
		0.6525e-6, 0.6702e-6, 0.6880e-6, 0.7084e-6, 0.7293e-6,
		0.8265e-6
	};

	/** Real part of the refractive index of copper. */
	private static final Function1 N_COPPER = new PiecewiseLinearFunction1(
			LAMBDA_2,
			new double[]{
					1.27 , 1.18 , 1.18 , 1.17 , 1.15 ,
					1.12 , 1.04 , 0.826, 0.468, 0.272,
					0.214, 0.215, 0.213, 0.214, 0.223,
					0.260});

	/** Extinction index of copper. */
	private static final Function1 K_COPPER = new PiecewiseLinearFunction1(
			LAMBDA_2,
			new double[]{
					1.95, 2.21, 2.21, 2.36, 2.50,
					2.60, 2.59, 2.60, 2.81, 3.24,
					3.67, 3.86, 4.05, 4.24, 4.43,
					5.26});

	/** Wavelengths for refractive index of platinum. */
	private static final double[] LAMBDA_3 = new double[]{
		0.3757e-6, 0.3874e-6, 0.3999e-6, 0.4133e-6, 0.4275e-6,
		0.4428e-6, 0.4592e-6, 0.4769e-6, 0.4959e-6, 0.5166e-6,
		0.5390e-6, 0.5636e-6, 0.5904e-6, 0.6199e-6, 0.6525e-6,
		0.6888e-6, 0.7293e-6, 0.7749e-6, 0.8265e-6
	};

	/** Real part of the refractive index of platinum. */
	private static final Function1 N_PLATINUM = new PiecewiseLinearFunction1(
			LAMBDA_3,
			new double[]{
					1.65, 1.68, 1.72, 1.75, 1.79,
					1.83, 1.87, 1.91, 1.96, 2.03,
					2.10, 2.17, 2.23, 2.30, 2.38,
					2.51, 2.63, 2.76, 2.92});

	/** Extinction index of platinum. */
	private static final Function1 K_PLATINUM = new PiecewiseLinearFunction1(
			LAMBDA_3,
			new double[]{
					2.69, 2.76, 2.84, 2.92, 3.01,
					3.10, 3.20, 3.30, 3.42, 3.54,
					3.67, 3.77, 3.92, 4.07, 4.26,
					4.43, 4.63, 4.84, 5.07});

	/** Wavelengths for refractive index of water. */
	private static final double[] LAMBDA_4 = ArrayUtil.range(200e-9, 1000e-9, 33);

	/** Real part of the refractive index of water. */
	private static final Function1 N_WATER = new PiecewiseLinearFunction1(
			LAMBDA_4,
			new double[]{
					1.396, 1.373, 1.362, 1.354, 1.349,
					1.146, 1.343, 1.341, 1.339, 1.338,
					1.337, 1.336, 1.335, 1.334, 1.333,
					1.333, 1.332, 1.332, 1.331, 1.331,
					1.331, 1.330, 1.330, 1.330, 1.329,
					1.329, 1.329, 1.328, 1.328, 1.328,
					1.327, 1.327, 1.327});

	/** Extinction index of water. */
	private static final Function1 K_WATER = new PiecewiseLinearFunction1(
			LAMBDA_4,
			new double[]{
		            1.10e-07, 4.90e-08, 3.35e-08, 2.35e-08, 1.60e-08,
		            1.08e-08, 6.50e-09, 3.50e-09, 1.86e-09, 1.30e-09,
		            1.02e-09, 9.35e-10, 1.00e-09, 1.32e-09, 1.96e-09,
		            3.60e-09, 1.09e-08, 1.39e-08, 1.64e-08, 2.23e-08,
		            3.35e-08, 9.15e-08, 1.56e-07, 1.48e-07, 1.25e-07,
		            1.82e-07, 2.93e-07, 3.91e-07, 4.86e-07, 1.06e-06,
		            2.93e-06, 3.48e-06, 2.89e-06});

	/** Absorption spectrum of water. */
	private static final Function1 ALPHA_WATER = new ScaledFunction1(2e-2, new AbsorptionFunction1(K_WATER));

	/** Refractive index of fused silica. */
	private static final Function1 N_FUSED_SILICA = new SellmeierFunction1(
			new double[]{ 6.96166300e-01, 4.07942600e-01, 8.97479400e-01 },
			new double[]{ 4.67914826e-15, 1.35120631e-14, 9.79340025e-13 },
			new Interval(0.365e-6, 2.3e-6));

	/** Wavelengths for refractive index of diamond. */
	private static final double[] LAMBDA_5 = new double[]{
		371.54e-9, 382.39e-9, 393.90e-9, 406.12e-9, 419.13e-9,
		433.00e-9, 447.81e-9, 463.68e-9, 480.71e-9, 499.04e-9,
		518.82e-9, 540.24e-9, 563.50e-9, 588.85e-9, 616.60e-9,
		647.08e-9, 680.74e-9, 718.09e-9, 759.78e-9, 806.61e-9
	};

	/** Refractive index of diamond. */
	private static final Function1 N_DIAMOND = new PiecewiseLinearFunction1(
			LAMBDA_5,
			new double[]{
					2.477, 2.471, 2.465, 2.459, 2.454,
					2.449, 2.444, 2.439, 2.434, 2.430,
					2.426, 2.422, 2.418, 2.414, 2.411,
					2.408, 2.405, 2.402, 2.399, 2.397});

	/** Wavelengths for refractive index of nickel. */
	private static final double[] LAMBDA_6 = new double[] {
		375.70e-9, 387.44e-9, 399.94e-9, 413.27e-9, 427.52e-9,
		442.79e-9, 459.19e-9, 476.85e-9, 495.92e-9, 516.58e-9,
		539.04e-9, 563.55e-9, 590.38e-9, 619.90e-9, 652.53e-9,
		688.78e-9, 729.29e-9, 774.87e-9, 826.53e-9
	};

	/** Real part of refractive index of nickel. */
	private static final Function1 N_NICKEL = new PiecewiseLinearFunction1(
			LAMBDA_6,
			new double[] {
					1.61, 1.61, 1.61, 1.61, 1.62,
					1.63, 1.64, 1.65, 1.67, 1.71,
					1.75, 1.80, 1.85, 1.92, 2.02,
					2.14, 2.28, 2.43, 2.53});

	/* Extinction index of nickel. */
	private static final Function1 K_NICKEL = new PiecewiseLinearFunction1(
			LAMBDA_6,
			new double[]{
					2.23, 2.30, 2.36, 2.44, 2.52,
					2.61, 2.71, 2.81, 2.93, 3.06,
					3.19, 3.33, 3.48, 3.65, 3.82,
					4.01, 4.18, 4.31, 4.47});

	/**
	 * Creates a <code>Material</code> representing silver.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing silver.
	 */
	public static Material silver(ColorModel c) {
		return new ConductiveMaterial(
				c.getContinuous(N_SILVER),
				c.getContinuous(K_SILVER),
				null);
	}

	/**
	 * Creates a <code>Material</code> representing gold.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing gold.
	 */
	public static Material gold(ColorModel c) {
		return new ConductiveMaterial(
				c.getContinuous(N_GOLD),
				c.getContinuous(K_GOLD),
				null);
	}

	/**
	 * Creates a <code>Material</code> representing copper.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing copper.
	 */
	public static Material copper(ColorModel c) {
		return new ConductiveMaterial(
				c.getContinuous(N_COPPER),
				c.getContinuous(K_COPPER),
				null);
	}

	/**
	 * Creates a <code>Material</code> representing platinum.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing platinum.
	 */
	public static Material platinum(ColorModel c) {
		return new ConductiveMaterial(
				c.getContinuous(N_PLATINUM),
				c.getContinuous(K_PLATINUM),
				null);
	}

	/**
	 * Creates a <code>Material</code> representing water.
	 * @param c The <code>ColorModel</code> to use.
	 * @param includeAbsorption A value indicating whether the material should
	 * 		be absorptive.
	 * @return A <code>Material</code> representing water.
	 */
	public static Material water(ColorModel c, boolean includeAbsorption) {
		return includeAbsorption
			? new ConductiveMaterial(
					c.getContinuous(N_WATER),
					c.getContinuous(K_WATER),
					c.getContinuous(ALPHA_WATER))
			: new DielectricMaterial(c.getContinuous(N_WATER));
	}

	/**
	 * Creates a <code>Material</code> representing (non-absorptive) water.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing (non-absorptive) water.
	 */
	public static Material water(ColorModel c) {
		return water(c, false);
	}

	/**
	 * Creates a <code>Material</code> representing fused silica.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing fused silica.
	 */
	public static Material fusedSilica(ColorModel c) {
		return new DielectricMaterial(c.getContinuous(N_FUSED_SILICA));
	}

	/**
	 * Creates a <code>Material</code> representing diamond.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing diamond.
	 */
	public static Material diamond(ColorModel c) {
		return new DielectricMaterial(c.getContinuous(N_DIAMOND));
	}

	/**
	 * Creates a <code>Material</code> representing nickel.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing nickel.
	 */
	public static Material nickel(ColorModel c) {
		return new ConductiveMaterial(
				c.getContinuous(N_NICKEL),
				c.getContinuous(K_NICKEL),
				null);
	}

	/** Private constructor to prevent instances from being created. */
	private Materials() {}

}
