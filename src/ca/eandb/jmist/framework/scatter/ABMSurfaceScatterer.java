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

	private double cuticleUndulationsAspectRatio = 5.0;
	private double epidermisCellCapsAspectRatio = 5.0;
	private double palisadeCellCapsAspectRatio = 1.0;
	private double spongyCellCapsAspectRatio = 5.0;
	
	
	private double collagenFiberRadius = 2.5e-8;
	private double thicknessStratumCorneum = 1e-3; // cm
	private double thicknessEpidermis = 1e-2; // cm
	private double thicknessPapillaryDermis = 2e-2; // cm
	private double thicknessReticularDermis = 1.8e-1; // cm
	private double concentrationEumelanin = 80;
	private double concentrationPheomelanin = 12;
	private double concentrationBetaCaroteneInStratumCorneum = 2.1e-4;
	private double concentrationBetaCaroteneInEpidermis = 2.1e-4;
	private double concentrationBetaCaroteneInBlood = 7e-5;
	private double concentrationHemoglobinInBlood = 150;
	private double concentrationBilirubinInBlood = 5e-2;
	private double pctMelanosomesInEpidermis = 0.052;
	private double pctWholeBloodInPapillaryDermis = 0.012;
	private double pctWholeBloodInReticularDermis = 0.0091;
	private double ratioOxyDeoxyHemoglobin = 0.75;
	private double foldsAspectRatio = 0.75;

	private final LayeredSurfaceScatterer subsurface = new LayeredSurfaceScatterer();
	
	private void build() {
		subsurface.clear();
		subsurface
			.addLayerToBottom( // air / stratum corneum interface
				new TrowbridgeReitzSurfaceScatterer(foldsAspectRatio, IOR_STRATUM_CORNEUM, 1.0))
			.addLayerToBottom( // stratum corneum
				new SequentialSurfaceScatterer()
					.addScatterer(new TabularPerturbingSurfaceScatterer(stratum_corneum_wavelengths, exitant_angles, stratum_corneum_perturb, 1000))
					.addScatterer(new AbsorbingSurfaceScatterer(
						new ScaledFunction1(
							concentrationBetaCaroteneInStratumCorneum / 537.0,
							OMLC_PRAHL_BETACAROTENE),
						thicknessStratumCorneum)))
			.addLayerToBottom( // stratum corneum / epidermis interface
				new FresnelSurfaceScatterer(IOR_EPIDERMIS, IOR_STRATUM_CORNEUM))
			.addLayerToBottom( // epidermis
				new SequentialSurfaceScatterer()
					.addScatterer(new TabularPerturbingSurfaceScatterer(epidermis_wavelengths, exitant_angles, epidermis_perturb, 1000))
					.addScatterer(new AbsorbingSurfaceScatterer(
						new SumFunction1()
							.addChild(new ScaledFunction1(
								pctMelanosomesInEpidermis * concentrationEumelanin,
								OMLC_EUMELANIN_EXT_MGML))
							.addChild(new ScaledFunction1(
								pctMelanosomesInEpidermis * concentrationPheomelanin,
								OMLC_PHEOMELANIN_EXT_MGML))
							.addChild(new ScaledFunction1(
								(1.0 - pctMelanosomesInEpidermis) * concentrationBetaCaroteneInEpidermis / 537.0,
								OMLC_PRAHL_BETACAROTENE)),
						thicknessEpidermis)))
			.addLayerToBottom( // epidermis / papillary dermis interface
				new FresnelSurfaceScatterer(IOR_PAPILLARY_DERMIS, IOR_EPIDERMIS))
			.addLayerToBottom( // papillary dermis
				new SequentialSurfaceScatterer()
					.addScatterer(new DiffusingSurfaceScatterer())
					.addScatterer(new AbsorbingSurfaceScatterer(
						new SumFunction1()
							.addChild(new ScaledFunction1(
								pctWholeBloodInPapillaryDermis * concentrationHemoglobinInBlood * ratioOxyDeoxyHemoglobin / 66500.0,
								OMLC_PRAHL_OXYHEMOGLOBIN))
							.addChild(new ScaledFunction1(
								pctWholeBloodInPapillaryDermis * concentrationHemoglobinInBlood * (1.0 - ratioOxyDeoxyHemoglobin) / 66500.0,
								OMLC_PRAHL_DEOXYHEMOGLOBIN))
							.addChild(new ScaledFunction1(
								pctWholeBloodInPapillaryDermis * concentrationBetaCaroteneInBlood / 537.0,
								OMLC_PRAHL_BETACAROTENE))
							.addChild(new ScaledFunction1(
								pctWholeBloodInPapillaryDermis * concentrationBilirubinInBlood / 585.0,
								OMLC_PRAHL_BILIRUBIN)),
						thicknessPapillaryDermis)))
			.addLayerToBottom( // papillary dermis / reticular dermis interface
				new FresnelSurfaceScatterer(IOR_RETICULAR_DERMIS, IOR_PAPILLARY_DERMIS))
			.addLayerToBottom( // reticular dermis
				new SequentialSurfaceScatterer()
					.addScatterer(new DiffusingSurfaceScatterer())
					.addScatterer(new AbsorbingSurfaceScatterer(
						new SumFunction1()
							.addChild(new ScaledFunction1(
								pctWholeBloodInReticularDermis * concentrationHemoglobinInBlood * ratioOxyDeoxyHemoglobin / 66500.0,
								OMLC_PRAHL_OXYHEMOGLOBIN))
							.addChild(new ScaledFunction1(
								pctWholeBloodInReticularDermis * concentrationHemoglobinInBlood * (1.0 - ratioOxyDeoxyHemoglobin) / 66500.0,
								OMLC_PRAHL_DEOXYHEMOGLOBIN))
							.addChild(new ScaledFunction1(
								pctWholeBloodInReticularDermis * concentrationBetaCaroteneInBlood / 537.0,
								OMLC_PRAHL_BETACAROTENE))
							.addChild(new ScaledFunction1(
								pctWholeBloodInReticularDermis * concentrationBilirubinInBlood / 585.0,
								OMLC_PRAHL_BILIRUBIN)),
						thicknessReticularDermis)))
			.addLayerToBottom( // reticular dermis / hypodermis interface
				new LambertianSurfaceScatterer());	
	}
	
	
	/**
	 * @return the collagenFiberRadius
	 */
	public double getCollagenFiberRadius() {
		return collagenFiberRadius;
	}


	/**
	 * @param collagenFiberRadius the collagenFiberRadius to set
	 */
	public void setCollagenFiberRadius(double collagenFiberRadius) {
		this.collagenFiberRadius = collagenFiberRadius;
		subsurface.clear();
	}


	/**
	 * @return the thicknessStratumCorneum
	 */
	public double getThicknessStratumCorneum() {
		return thicknessStratumCorneum;
	}


	/**
	 * @param thicknessStratumCorneum the thicknessStratumCorneum to set
	 */
	public void setThicknessStratumCorneum(double thicknessStratumCorneum) {
		this.thicknessStratumCorneum = thicknessStratumCorneum;
		subsurface.clear();
	}


	/**
	 * @return the thicknessEpidermis
	 */
	public double getThicknessEpidermis() {
		return thicknessEpidermis;
	}


	/**
	 * @param thicknessEpidermis the thicknessEpidermis to set
	 */
	public void setThicknessEpidermis(double thicknessEpidermis) {
		this.thicknessEpidermis = thicknessEpidermis;
		subsurface.clear();
	}


	/**
	 * @return the thicknessPapillaryDermis
	 */
	public double getThicknessPapillaryDermis() {
		return thicknessPapillaryDermis;
	}


	/**
	 * @param thicknessPapillaryDermis the thicknessPapillaryDermis to set
	 */
	public void setThicknessPapillaryDermis(double thicknessPapillaryDermis) {
		this.thicknessPapillaryDermis = thicknessPapillaryDermis;
		subsurface.clear();
	}


	/**
	 * @return the thicknessReticularDermis
	 */
	public double getThicknessReticularDermis() {
		return thicknessReticularDermis;
	}


	/**
	 * @param thicknessReticularDermis the thicknessReticularDermis to set
	 */
	public void setThicknessReticularDermis(double thicknessReticularDermis) {
		this.thicknessReticularDermis = thicknessReticularDermis;
		subsurface.clear();
	}


	/**
	 * @return the concentrationEumelanin
	 */
	public double getConcentrationEumelanin() {
		return concentrationEumelanin;
	}


	/**
	 * @param concentrationEumelanin the concentrationEumelanin to set
	 */
	public void setConcentrationEumelanin(double concentrationEumelanin) {
		this.concentrationEumelanin = concentrationEumelanin;
		subsurface.clear();
	}


	/**
	 * @return the concentrationPheomelanin
	 */
	public double getConcentrationPheomelanin() {
		return concentrationPheomelanin;
	}


	/**
	 * @param concentrationPheomelanin the concentrationPheomelanin to set
	 */
	public void setConcentrationPheomelanin(double concentrationPheomelanin) {
		this.concentrationPheomelanin = concentrationPheomelanin;
		subsurface.clear();
	}


	/**
	 * @return the concentrationBetaCaroteneInStratumCorneum
	 */
	public double getConcentrationBetaCaroteneInStratumCorneum() {
		return concentrationBetaCaroteneInStratumCorneum;
	}


	/**
	 * @param concentrationBetaCaroteneInStratumCorneum the concentrationBetaCaroteneInStratumCorneum to set
	 */
	public void setConcentrationBetaCaroteneInStratumCorneum(
			double concentrationBetaCaroteneInStratumCorneum) {
		this.concentrationBetaCaroteneInStratumCorneum = concentrationBetaCaroteneInStratumCorneum;
		subsurface.clear();
	}


	/**
	 * @return the concentrationBetaCaroteneInEpidermis
	 */
	public double getConcentrationBetaCaroteneInEpidermis() {
		return concentrationBetaCaroteneInEpidermis;
	}


	/**
	 * @param concentrationBetaCaroteneInEpidermis the concentrationBetaCaroteneInEpidermis to set
	 */
	public void setConcentrationBetaCaroteneInEpidermis(
			double concentrationBetaCaroteneInEpidermis) {
		this.concentrationBetaCaroteneInEpidermis = concentrationBetaCaroteneInEpidermis;
		subsurface.clear();
	}


	/**
	 * @return the concentrationBetaCaroteneInBlood
	 */
	public double getConcentrationBetaCaroteneInBlood() {
		return concentrationBetaCaroteneInBlood;
	}


	/**
	 * @param concentrationBetaCaroteneInBlood the concentrationBetaCaroteneInBlood to set
	 */
	public void setConcentrationBetaCaroteneInBlood(
			double concentrationBetaCaroteneInBlood) {
		this.concentrationBetaCaroteneInBlood = concentrationBetaCaroteneInBlood;
		subsurface.clear();
	}


	/**
	 * @return the concentrationHemoglobinInBlood
	 */
	public double getConcentrationHemoglobinInBlood() {
		return concentrationHemoglobinInBlood;
	}


	/**
	 * @param concentrationHemoglobinInBlood the concentrationHemoglobinInBlood to set
	 */
	public void setConcentrationHemoglobinInBlood(
			double concentrationHemoglobinInBlood) {
		this.concentrationHemoglobinInBlood = concentrationHemoglobinInBlood;
		subsurface.clear();
	}


	/**
	 * @return the concentrationBilirubinInBlood
	 */
	public double getConcentrationBilirubinInBlood() {
		return concentrationBilirubinInBlood;
	}


	/**
	 * @param concentrationBilirubinInBlood the concentrationBilirubinInBlood to set
	 */
	public void setConcentrationBilirubinInBlood(
			double concentrationBilirubinInBlood) {
		this.concentrationBilirubinInBlood = concentrationBilirubinInBlood;
		subsurface.clear();
	}


	/**
	 * @return the pctMelanosomesInEpidermis
	 */
	public double getPctMelanosomesInEpidermis() {
		return pctMelanosomesInEpidermis;
	}


	/**
	 * @param pctMelanosomesInEpidermis the pctMelanosomesInEpidermis to set
	 */
	public void setPctMelanosomesInEpidermis(double pctMelanosomesInEpidermis) {
		this.pctMelanosomesInEpidermis = pctMelanosomesInEpidermis;
		subsurface.clear();
	}


	/**
	 * @return the pctWholeBloodInPapillaryDermis
	 */
	public double getPctWholeBloodInPapillaryDermis() {
		return pctWholeBloodInPapillaryDermis;
	}


	/**
	 * @param pctWholeBloodInPapillaryDermis the pctWholeBloodInPapillaryDermis to set
	 */
	public void setPctWholeBloodInPapillaryDermis(
			double pctWholeBloodInPapillaryDermis) {
		this.pctWholeBloodInPapillaryDermis = pctWholeBloodInPapillaryDermis;
		subsurface.clear();
	}


	/**
	 * @return the pctWholeBloodInReticularDermis
	 */
	public double getPctWholeBloodInReticularDermis() {
		return pctWholeBloodInReticularDermis;
	}


	/**
	 * @param pctWholeBloodInReticularDermis the pctWholeBloodInReticularDermis to set
	 */
	public void setPctWholeBloodInReticularDermis(
			double pctWholeBloodInReticularDermis) {
		this.pctWholeBloodInReticularDermis = pctWholeBloodInReticularDermis;
		subsurface.clear();
	}


	/**
	 * @return the ratioOxyDeoxyHemoglobin
	 */
	public double getRatioOxyDeoxyHemoglobin() {
		return ratioOxyDeoxyHemoglobin;
	}


	/**
	 * @param ratioOxyDeoxyHemoglobin the ratioOxyDeoxyHemoglobin to set
	 */
	public void setRatioOxyDeoxyHemoglobin(double ratioOxyDeoxyHemoglobin) {
		this.ratioOxyDeoxyHemoglobin = ratioOxyDeoxyHemoglobin;
		subsurface.clear();
	}


	/**
	 * @return the foldsAspectRatio
	 */
	public double getFoldsAspectRatio() {
		return foldsAspectRatio;
	}


	/**
	 * @param foldsAspectRatio the foldsAspectRatio to set
	 */
	public void setFoldsAspectRatio(double foldsAspectRatio) {
		this.foldsAspectRatio = foldsAspectRatio;
		subsurface.clear();
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
