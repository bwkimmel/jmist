/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.function.PiecewiseLinearFunction1;
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
	private static final double[] LAMBDA_4 = ArrayUtil.range(400e-9, 780e-9, 77);

	/** Refractive index of water. */
	private static final Function1 N_WATER = new PiecewiseLinearFunction1(
			LAMBDA_4,
			new double[]{
					1.346, 1.345, 1.344, 1.343, 1.342, 1.341, 1.340, 1.339, 1.338, 1.338,
					1.337, 1.336, 1.336, 1.335, 1.335, 1.334, 1.334, 1.334, 1.334, 1.333,
					1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333,
					1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333,
					1.333, 1.333, 1.333, 1.333, 1.333, 1.332, 1.332, 1.332, 1.332, 1.332,
					1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332,
					1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332,
					1.332, 1.332, 1.332, 1.331, 1.331, 1.331, 1.331});

	/** Refractive index of fused silica. */
	private static final Function1 N_FUSED_SILICA = new SellmeierFunction1(
			new double[]{ 6.96166300e-01, 4.07942600e-01, 8.97479400e-01 },
			new double[]{ 4.67914826e-15, 1.35120631e-14, 9.79340025e-13 },
			new Interval(0.365e-6, 2.3e-6));

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
	 * @return A <code>Material</code> representing water.
	 */
	public static Material water(ColorModel c) {
		return new DielectricMaterial(c.getContinuous(N_WATER));
	}

	/**
	 * Creates a <code>Material</code> representing fused silica.
	 * @param c The <code>ColorModel</code> to use.
	 * @return A <code>Material</code> representing fused silica.
	 */
	public static Material fusedSilica(ColorModel c) {
		return new DielectricMaterial(c.getContinuous(N_FUSED_SILICA));
	}

	/** Private constructor to prevent instances from being created. */
	private Materials() {}

}
