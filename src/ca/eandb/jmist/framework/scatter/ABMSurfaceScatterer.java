/**
 *
 */
package ca.eandb.jmist.framework.scatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.function.AXpBFunction1;
import ca.eandb.jmist.framework.function.PiecewiseLinearFunction1;
import ca.eandb.jmist.framework.function.ScaledFunction1;
import ca.eandb.jmist.framework.function.SumFunction1;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * @author brad
 *
 */
public final class ABMSurfaceScatterer implements SurfaceScatterer {

	private static final double[] WAVELENGTHS = ArrayUtil.range(400e-9, 700e-9, 61); // m

	private static final double[] SAC_CHLOROPHYLL_AB_VALUES = { // cm^2/g
		73400, 67700, 62300, 58000, 55600, 53400, 52700, 52200,
		51000, 49400, 46700, 44400, 43500, 43500, 43000, 42600,
		42100, 41300, 40000, 37900, 34800, 30700, 26500, 22100,
		18300, 15400, 13600, 12700, 12200, 12000, 11800, 11800,
		12200, 13000, 14200, 15600, 16800, 17800, 18500, 18900,
		19200, 19800, 20700, 21900, 22900, 23400, 23600, 24000,
		25300, 27400, 29400, 30600, 32700, 36100, 38900, 40500,
		40300, 36900, 28200, 19100, 12600
	};

	private static final double[] SAC_CAROTENOIDS_VALUES = { // cm^2/g
		 6535.506923,  7207.118815,  8017.169001,  8559.258518,
		 9328.970969,  9942.106999, 10669.577847, 11562.119164,
		11975.755432, 12370.904604, 12843.907698, 13162.723765,
		13594.276407, 14057.034526, 14381.579285, 14550.187782,
		14490.659508, 14248.260257, 13773.149034, 13667.762322,
		13620.968781, 13177.895459, 12539.340804, 10989.320278,
		 8895.380246,  6936.325813,  4544.066850,  3170.884351,
		 2333.883282,  1824.555971,  1519.712737,  1309.073662,
		 1164.526320,  1068.505480,  1011.292774,   958.240154,
		  958.240154,   958.240154,   947.933115,   921.314601,
		  901.556158,   883.250517,   875.085062,   875.085062,
		  873.419624,   867.527208,   861.634792,   855.742376,
		  849.849960,   843.957544,   838.065128,   834.592308,
		  841.777197,   848.962085,   856.146973,   863.331861,
		  870.516749,   881.011273,   897.284130,   913.556987,
		  894.049970
	};

	private static final double[] SAC_WATER_VALUES = { // cm^-1
		0.000066, 0.000053, 0.000047, 0.000044,
		0.000045, 0.000048, 0.000049, 0.000053,
		0.000063, 0.000075, 0.000092, 0.000096,
		0.000098, 0.000101, 0.000106, 0.000114,
		0.000127, 0.000136, 0.000150, 0.000173,
		0.000204, 0.000256, 0.000325, 0.000396,
		0.000409, 0.000417, 0.000434, 0.000452,
		0.000474, 0.000511, 0.000565, 0.000596,
		0.000619, 0.000642, 0.000695, 0.000772,
		0.000896, 0.001100, 0.001351, 0.001672,
		0.002224, 0.002577, 0.002644, 0.002678,
		0.002755, 0.002834, 0.002916, 0.003012,
		0.003108, 0.003250, 0.003400, 0.003710,
		0.004100, 0.004290, 0.004390, 0.004480,
		0.004650, 0.004860, 0.005160, 0.005590,
		0.006240
	};

	public static final double[] IOR_WATER_VALUES = {
		1.346, 1.345, 1.344, 1.343, 1.342, 1.341, 1.340, 1.339,
		1.338, 1.338, 1.337, 1.336, 1.336, 1.335, 1.335, 1.334,
		1.334, 1.334, 1.334, 1.333, 1.333, 1.333, 1.333, 1.333,
		1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333,
		1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333, 1.333,
		1.333, 1.333, 1.333, 1.333, 1.333, 1.332, 1.332, 1.332,
		1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332, 1.332,
		1.332, 1.332, 1.332, 1.332, 1.332
	};

	public static final double[] IOR_CUTICLE_VALUES = {
		1.539712, 1.539678, 1.537372, 1.536209,
		1.534937, 1.533561, 1.532009, 1.530511,
		1.529224, 1.528078, 1.527108, 1.526012,
		1.524844, 1.523591, 1.522421, 1.521544,
		1.520067, 1.519138, 1.518554, 1.517828,
		1.517050, 1.516272, 1.515421, 1.514547,
		1.513748, 1.513030, 1.512445, 1.511623,
		1.510911, 1.510230, 1.509549, 1.508952,
		1.508368, 1.507785, 1.507203, 1.506620,
		1.505908, 1.505078, 1.504297, 1.503139,
		1.502896, 1.502662, 1.502248, 1.501666,
		1.501087, 1.500568, 1.500049, 1.499530,
		1.499011, 1.498409, 1.497752, 1.497096,
		1.496439, 1.495974, 1.495609, 1.495244,
		1.494879, 1.494510, 1.494139, 1.493768,
		1.493398
	};

	static {

		// convert everything to base SI units
		for (int i = 0; i < WAVELENGTHS.length; i++) {

			// convert cm^2/g to m^2/kg  (divide by 10)
			SAC_CHLOROPHYLL_AB_VALUES[i] /= 10.0;
			SAC_CAROTENOIDS_VALUES[i] /= 10.0;

			// convert cm^-1 to m^-1  (multiply by 100)
			SAC_WATER_VALUES[i] *= 100.0;

		}

	}

	private static final Function1 SAC_CHLOROPHYLL_AB = new PiecewiseLinearFunction1(
			WAVELENGTHS, SAC_CHLOROPHYLL_AB_VALUES);

	private static final Function1 SAC_CAROTENOIDS = new PiecewiseLinearFunction1(
			WAVELENGTHS, SAC_CAROTENOIDS_VALUES);

	private static final Function1 SAC_WATER = new PiecewiseLinearFunction1(
			WAVELENGTHS, SAC_WATER_VALUES);

	private static final Function1 IOR_WATER = new PiecewiseLinearFunction1(
			WAVELENGTHS, IOR_WATER_VALUES);

	private static final Function1 IOR_CUTICLE = new PiecewiseLinearFunction1(
			WAVELENGTHS, IOR_CUTICLE_VALUES);

	private static final Function1 IOR_AIR = Function1.ONE;

	private static final double SAC_PROTEIN = 1.992; // m^2/kg

	private static final double SAC_CELLULOSE_LIGNIN = 0.876; // m^2/kg

	private double cuticleUndulationsAspectRatio = 5.0;
	private double epidermisCellCapsAspectRatio = 5.0;
	private double palisadeCellCapsAspectRatio = 1.0;
	private double spongyCellCapsAspectRatio = 5.0;


	private double mesophyllThickness;

	private double concentrationScatterersAntidermalWall;
	private double concentrationScatterersMesophyll;

	private final LayeredSurfaceScatterer subsurface = new LayeredSurfaceScatterer();

	private void build() {
		subsurface.clear();

		Function1 iorMesophyll = new AXpBFunction1(
				(1.0 - concentrationScatterersMesophyll),
				1.5608 * concentrationScatterersMesophyll,
				IOR_WATER);
		Function1 iorAntidermalWall = new AXpBFunction1(
				(1.0 - concentrationScatterersAntidermalWall),
				1.535 * concentrationScatterersAntidermalWall,
				IOR_WATER);

		double concChlorophyllInMesophyll = 0.0;
		double concCarotenoidsInMesophyll = 0.0;
		double concProteinInMesophyll = 0.0;
		double concCelluloseLigninInMesophyll = 0.0;

		subsurface
			.addLayerToBottom(new ABMInterfaceSurfaceScatterer(
					IOR_CUTICLE, IOR_AIR,
					cuticleUndulationsAspectRatio,
					epidermisCellCapsAspectRatio,
					Double.POSITIVE_INFINITY,
					epidermisCellCapsAspectRatio
					))
			.addLayerToBottom(new ABMInterfaceSurfaceScatterer(
					iorMesophyll, IOR_CUTICLE,
					epidermisCellCapsAspectRatio,
					palisadeCellCapsAspectRatio,
					epidermisCellCapsAspectRatio,
					palisadeCellCapsAspectRatio))
			.addLayerToBottom(new AbsorbingSurfaceScatterer(
					mesophyllAbsorptionCoefficient,
					mesophyllThickness))
			.addLayerToBottom(new ABMInterfaceSurfaceScatterer(
					IOR_AIR, iorMesophyll,
					palisadeCellCapsAspectRatio,
					spongyCellCapsAspectRatio,
					palisadeCellCapsAspectRatio,
					spongyCellCapsAspectRatio))
			.addLayerToBottom(new ABMInterfaceSurfaceScatterer(
					iorAntidermalWall, IOR_AIR,
					Double.POSITIVE_INFINITY,
					Double.POSITIVE_INFINITY,
					Double.POSITIVE_INFINITY,
					Double.POSITIVE_INFINITY))
			.addLayerToBottom(new ABMInterfaceSurfaceScatterer(
					IOR_CUTICLE, iorAntidermalWall,
					Double.POSITIVE_INFINITY,
					epidermisCellCapsAspectRatio,
					Double.POSITIVE_INFINITY,
					epidermisCellCapsAspectRatio))
			.addLayerToBottom(new ABMInterfaceSurfaceScatterer(
					IOR_AIR, IOR_CUTICLE,
					epidermisCellCapsAspectRatio,
					Double.POSITIVE_INFINITY,
					epidermisCellCapsAspectRatio,
					cuticleUndulationsAspectRatio));
	}


	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double lambda, Random rnd) {

		if (subsurface.getNumLayers() == 0) {
			build();
		}

		return subsurface.scatter(x, v, adjoint, lambda, rnd);
	}


}
