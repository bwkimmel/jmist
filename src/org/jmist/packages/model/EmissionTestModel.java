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
import org.jmist.packages.CompositeGeometry;
import org.jmist.packages.StratifiedRandom;
import org.jmist.packages.ThreadLocalRandom;
import org.jmist.packages.TransformableLens;
import org.jmist.packages.geometry.primitive.BoxGeometry;
import org.jmist.packages.geometry.primitive.RectangleGeometry;
import org.jmist.packages.lens.PinholeLens;
import org.jmist.packages.light.CompositeLight;
import org.jmist.packages.light.RandomCompositeLight;
import org.jmist.packages.light.SimpleCompositeLight;
import org.jmist.packages.material.LambertianMaterial;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Box3;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public final class EmissionTestModel implements Model {

	/* (non-Javadoc)
	 * @see org.jmist.framework.Model#getGeometry()
	 */
	public Geometry getGeometry() {
		if (geometry == null) {
			this.initialize();
		}

		return geometry;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Model#getLens()
	 */
	public Lens getLens() {
		if (lens == null) {
			this.initialize();
		}

		return lens;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Model#getLight()
	 */
	public Light getLight() {
		if (light == null) {
			this.initialize();
		}

		return light;
	}

	public static Model getInstance() {
		if (instance == null) {
			instance = new EmissionTestModel();
		}

		return instance;
	}

	private void initialize() {

		RectangleGeometry lightCell = new RectangleGeometry(
				new Point3(-200.0, 50.0, 0.0),
				Basis3.fromW(Vector3.I),
				100.0, 100.0,
				false,
				diffuseLuminaire1
		);

		CompositeLight smallLights = new RandomCompositeLight(new ThreadLocalRandom(new StratifiedRandom(100)));

		light = new SimpleCompositeLight()
				.addChild(lightCell)
				.addChild(smallLights);

		geometry = new BoundingBoxHierarchyGeometry()
				.addChild(lightCell)
				.addChild(
						new BoxGeometry(
								new Box3(-200.0, -10.0, -200.0, 200.0, 0.0, 200.0),
								diffuse50
						)
				);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				lightCell = new RectangleGeometry(
						new Point3(200.0, (double) (5 + (10 * i)), (double) (-45 + (10 * j))),
						Basis3.fromW(Vector3.NEGATIVE_I),
						10.0, 10.0,
						false,
						diffuseLuminaire1
				);

				geometry.addChild(lightCell);
				smallLights.addChild(lightCell);
			}
		}

		lens = new PinholeLens(0.785398, 1.0);

		lens.rotateX(-Math.PI / 2.0);
		lens.translate(new Vector3(0.0, 550.0, 50.0));

	}

	private static Model instance = null;

	private final Material diffuse50 = new LambertianMaterial(new ConstantSpectrum(0.5));
	private final Material diffuseLuminaire1 = new LambertianMaterial(null, Spectrum.ONE);

	private CompositeGeometry geometry = null;
	private CompositeLight light = null;
	private TransformableLens lens = null;

}
