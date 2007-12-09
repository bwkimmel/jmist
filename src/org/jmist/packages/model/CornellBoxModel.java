/**
 *
 */
package org.jmist.packages.model;

import org.jmist.framework.ConstantSpectrum;
import org.jmist.framework.Geometry;
import org.jmist.framework.Lens;
import org.jmist.framework.Light;
import org.jmist.framework.Material;
import org.jmist.framework.Model;
import org.jmist.framework.Spectrum;
import org.jmist.packages.BoundingBoxHierarchyGeometry;
import org.jmist.packages.TransformableLens;
import org.jmist.packages.geometry.primitive.PolygonGeometry;
import org.jmist.packages.geometry.primitive.PolyhedronGeometry;
import org.jmist.packages.geometry.primitive.RectangleGeometry;
import org.jmist.packages.lens.PinholeLens;
import org.jmist.packages.material.LambertianMaterial;
import org.jmist.packages.spectrum.PiecewiseLinearSpectrum;
import org.jmist.packages.spectrum.ScaledSpectrum;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Vector3;
import org.jmist.util.ArrayUtil;

/**
 * A <code>Model</code> of the Cornell Box.
 * @author bkimmel
 */
public final class CornellBoxModel implements Model {

	/**
	 * Creates a new <code>CornellBoxModel</code>.  This constructor is private
	 * because this class is a singleton.
	 */
	private CornellBoxModel() {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Model#getGeometry()
	 */
	@Override
	public Geometry getGeometry() {
		return this.cornellBox;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Model#getLight()
	 */
	@Override
	public Light getLight() {
		return (Light) lightBox;
		//return new PointLight(new Point3(278.0, 540.8, 279.5), emission, true);
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Model#getLens()
	 */
	@Override
	public Lens getLens() {
		return this.lens;
	}

	/**
	 * Creates the <code>Lens</code> used in the Cornell Box.
	 * @return The <code>Lens</code> used in the Cornell Box.
	 */
	private static Lens createLens() {

		TransformableLens lens = new PinholeLens(2.0 * Math.atan2(0.25 / 2.0, 0.35), 1.0);

		lens.rotateY(Math.PI);
		lens.translate(new Vector3(278.0, 273.0, -800.0));

		return lens;

	}

	/**
	 * Gets the single instance of <code>CornellBoxModel</code>.
	 * @return The single instance of <code>CornellBoxModel</code>.
	 */
	public static Model getInstance() {
		if (instance == null) {
			instance = new CornellBoxModel();
		}
		return instance;
	}

	/** The single <code>CornellBoxModel</code> instance. */
	private static CornellBoxModel instance = null;

	/** The wavelengths at which the reflectance spectra are given. */
	private static double[] WAVELENGTHS = ArrayUtil.range(400.0e-9, 700.0e-9, 76);

	/** The reflectance <code>Spectrum</code> for the white walls. */
	private Spectrum white = new PiecewiseLinearSpectrum(WAVELENGTHS, new double[]{

			0.343,0.445,0.551,0.624,0.665,0.687,0.708,0.723,0.715,0.710, /* 400 - 436 */
			0.745,0.758,0.739,0.767,0.777,0.765,0.751,0.745,0.748,0.729, /* 440 - 476 */
			0.745,0.757,0.753,0.750,0.746,0.747,0.735,0.732,0.739,0.734, /* 480 - 516 */
			0.725,0.721,0.733,0.725,0.732,0.743,0.744,0.748,0.728,0.716, /* 520 - 556 */
			0.733,0.726,0.713,0.740,0.754,0.764,0.752,0.736,0.734,0.741, /* 560 - 596 */
			0.740,0.732,0.745,0.755,0.751,0.744,0.731,0.733,0.744,0.731, /* 600 - 636 */
			0.712,0.708,0.729,0.730,0.727,0.707,0.703,0.729,0.750,0.760, /* 640 - 676 */
			0.751,0.739,0.724,0.730,0.740,0.737                          /* 680 - 700 */

	});

	/** The reflectance <code>Spectrum</code> for the green wall. */
	private Spectrum green = new PiecewiseLinearSpectrum(WAVELENGTHS, new double[]{

			0.092,0.096,0.098,0.097,0.098,0.095,0.095,0.097,0.095,0.094, /* 400 - 436 */
			0.097,0.098,0.096,0.101,0.103,0.104,0.107,0.109,0.112,0.115, /* 440 - 476 */
			0.125,0.140,0.160,0.187,0.229,0.285,0.343,0.390,0.435,0.464, /* 480 - 516 */
			0.472,0.476,0.481,0.462,0.447,0.441,0.426,0.406,0.373,0.347, /* 520 - 556 */
			0.337,0.314,0.285,0.277,0.266,0.250,0.230,0.207,0.186,0.171, /* 560 - 596 */
			0.160,0.148,0.141,0.136,0.130,0.126,0.123,0.121,0.122,0.119, /* 600 - 636 */
			0.114,0.115,0.117,0.117,0.118,0.120,0.122,0.128,0.132,0.139, /* 640 - 676 */
			0.144,0.146,0.150,0.152,0.157,0.159                          /* 680 - 700 */

	});

	/** The reflectance <code>Spectrum</code> for the red wall. */
	private Spectrum red = new PiecewiseLinearSpectrum(WAVELENGTHS, new double[]{

			0.040,0.046,0.048,0.053,0.049,0.050,0.053,0.055,0.057,0.056, /* 400 - 436 */
			0.059,0.057,0.061,0.061,0.060,0.062,0.062,0.062,0.061,0.062, /* 440 - 476 */
			0.060,0.059,0.057,0.058,0.058,0.058,0.056,0.055,0.056,0.059, /* 480 - 516 */
			0.057,0.055,0.059,0.059,0.058,0.059,0.061,0.061,0.063,0.063, /* 520 - 556 */
			0.067,0.068,0.072,0.080,0.090,0.099,0.124,0.154,0.192,0.255, /* 560 - 596 */
			0.287,0.349,0.402,0.443,0.487,0.513,0.558,0.584,0.620,0.606, /* 600 - 636 */
			0.609,0.651,0.612,0.610,0.650,0.638,0.627,0.620,0.630,0.628, /* 640 - 676 */
			0.642,0.639,0.657,0.639,0.635,0.642                          /* 680 - 700 */

	});

	/** The emission <code>Spectrum</code> for the light box. */
	private Spectrum emission = new ScaledSpectrum(5e16, new PiecewiseLinearSpectrum(
			new double[]{ 400.0e-9, 500.0e-9, 600.0e-9, 700.0e-9 },
			new double[]{   0.0   ,   8.0   ,  15.6   ,  18.4    }
	));

	/** The <code>Material</code> for the white walls. */
	private Material matteWhite = new LambertianMaterial(white);

	/** The <code>Material</code> for the green wall. */
	private Material matteGreen = new LambertianMaterial(green);

	/** The <code>Material</code> for the red wall. */
	private Material matteRed = new LambertianMaterial(red);

	/** The <code>Material</code> for the light box. */
	private Material matteEmissive = new LambertianMaterial(new ConstantSpectrum(0.78), emission);

	/** The <code>Lens</code> to use to view the box. */
	private Lens lens = createLens();

	/** The <code>Geometry</code> for the floor. */
	private Geometry floor = new PolygonGeometry(
			new Point3[]{
					new Point3(552.8, 0.0,   0.0),
					new Point3(  0.0, 0.0,   0.0),
					new Point3(  0.0, 0.0, 559.2),
					new Point3(549.6, 0.0, 559.2),

					new Point3(130.0, 0.0,  65.0),
					new Point3( 82.0, 0.0, 225.0),
					new Point3(240.0, 0.0, 272.0),
					new Point3(290.0, 0.0, 114.0),

					new Point3(423.0, 0.0, 247.0),
					new Point3(265.0, 0.0, 296.0),
					new Point3(314.0, 0.0, 456.0),
					new Point3(472.0, 0.0, 406.0)
			},
			new int[][]{
					new int[]{  0,  1,  2,  3 },
					new int[]{  7,  6,  5,  4 },
					new int[]{ 11, 10,  9,  8 }
			},
			matteWhite
	);

	/** The <code>Geometry</code> for the light box. */
	private Geometry lightBox = new RectangleGeometry(
			new Point3(278.0, 548.8, 279.5),		// center
			Basis3.fromUV(Vector3.I, Vector3.K),	// basis
			130.0, 105.0,							// su, sv
			false,									// twoSided
			matteEmissive							// material
	);

	/** The <code>Geometry</code> for the ceiling. */
	private Geometry ceiling = new PolygonGeometry(
			new Point3[]{
					new Point3(556.0, 548.8,   0.0),
					new Point3(556.0, 548.8, 559.2),
					new Point3(  0.0, 548.8, 559.2),
					new Point3(  0.0, 548.8,   0.0),
					new Point3(343.0, 548.8, 227.0),
					new Point3(343.0, 548.8, 332.0),
					new Point3(213.0, 548.8, 332.0),
					new Point3(213.0, 548.8, 227.0)
			},
			new int[][]{
					new int[]{ 0, 1, 2, 3 },
					new int[]{ 7, 6, 5, 4 }
			},
			matteWhite
	);

	/** The <code>Geometry</code> for the back wall. */
	private Geometry backWall = new PolygonGeometry(
			new Point3[]{
					new Point3(549.6,   0.0, 559.2),
					new Point3(  0.0,   0.0, 559.2),
					new Point3(  0.0, 548.8, 559.2),
					new Point3(556.0, 548.8, 559.2)
			},
			matteWhite
	);

	/** The <code>Geometry</code> for the right wall. */
	private Geometry rightWall = new PolygonGeometry(
			new Point3[]{
					new Point3(0.0,   0.0, 559.2),
					new Point3(0.0,   0.0,   0.0),
					new Point3(0.0, 548.8,   0.0),
					new Point3(0.0, 548.8, 559.2)
			},
			matteGreen
	);

	/** The <code>Geometry</code> for the left wall. */
	private Geometry leftWall = new PolygonGeometry(
			new Point3[]{
					new Point3(552.8,   0.0,   0.0),
					new Point3(549.6,   0.0, 559.2),
					new Point3(556.0, 548.8, 559.2),
					new Point3(556.0, 548.8,   0.0)
			},
			matteRed
	);

	/** The <code>Geometry</code> for the short block. */
	private Geometry shortBlock = new PolyhedronGeometry(
			new Point3[]{
					new Point3(130.0, 165.0,  65.0),
					new Point3( 82.0, 165.0, 225.0),
					new Point3(240.0, 165.0, 272.0),
					new Point3(290.0, 165.0, 114.0),
					new Point3(290.0,   0.0, 114.0),
					new Point3(240.0,   0.0, 272.0),
					new Point3(130.0,   0.0,  65.0),
					new Point3( 82.0,   0.0, 225.0)
			},
			new int[][]{
					new int[]{ 0, 1, 2, 3 },
					new int[]{ 4, 3, 2, 5 },
					new int[]{ 6, 0, 3, 4 },
					new int[]{ 7, 1, 0, 6 },
					new int[]{ 5, 2, 1, 7 }
			},
			matteWhite
	);

	/** The <code>Geometry</code> for the tall block. */
	private Geometry tallBlock = new PolyhedronGeometry(
			new Point3[]{
					new Point3(423.0, 330.0, 247.0),
					new Point3(265.0, 330.0, 296.0),
					new Point3(314.0, 330.0, 456.0),
					new Point3(472.0, 330.0, 406.0),
					new Point3(423.0,   0.0, 247.0),
					new Point3(472.0,   0.0, 406.0),
					new Point3(314.0,   0.0, 456.0),
					new Point3(265.0,   0.0, 296.0)
			},
			new int[][]{
					new int[]{ 0, 1, 2, 3 },
					new int[]{ 4, 0, 3, 5 },
					new int[]{ 5, 3, 2, 6 },
					new int[]{ 6, 2, 1, 7 },
					new int[]{ 7, 1, 0, 4 }
			},
			matteWhite
	);

	/** The <code>Geometry</code> for the entire Cornell Box. */
	private Geometry cornellBox = new BoundingBoxHierarchyGeometry()
		.addChild(floor)
		.addChild(lightBox)
		.addChild(ceiling)
		.addChild(backWall)
		.addChild(rightWall)
		.addChild(leftWall)
		.addChild(shortBlock)
		.addChild(tallBlock);

}
