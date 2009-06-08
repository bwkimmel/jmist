/**
 *
 */
package ca.eandb.jmist.framework.model;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Model;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.geometry.BoundingBoxHierarchyGeometry;
import ca.eandb.jmist.framework.geometry.CompositeGeometry;
import ca.eandb.jmist.framework.geometry.primitive.BoxGeometry;
import ca.eandb.jmist.framework.geometry.primitive.RectangleGeometry;
import ca.eandb.jmist.framework.lens.PinholeLens;
import ca.eandb.jmist.framework.lens.TransformableLens;
import ca.eandb.jmist.framework.light.CompositeLight;
import ca.eandb.jmist.framework.light.RandomCompositeLight;
import ca.eandb.jmist.framework.light.SimpleCompositeLight;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.framework.random.StratifiedRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public final class EmissionTestModel implements Model {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getGeometry()
	 */
	public Geometry getGeometry() {
		if (geometry == null) {
			this.initialize();
		}

		return geometry;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getLens()
	 */
	public Lens getLens() {
		if (lens == null) {
			this.initialize();
		}

		return lens;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getLight()
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

		lens = PinholeLens.fromHfovAndAspect(0.785398, 1.0);

		lens.rotateX(-Math.PI / 2.0);
		lens.translate(new Vector3(0.0, 550.0, 50.0));

	}

	private static Model instance = null;

	private final ColorModel cm = ColorModel.getInstance();
	private final Material diffuse50 = new LambertianMaterial(new UniformPainter(cm.getGray(0.5)));
	private final Material diffuseLuminaire1 = new LambertianMaterial(null, new UniformPainter(cm.getWhite()));

	private CompositeGeometry geometry = null;
	private CompositeLight light = null;
	private TransformableLens lens = null;

}
