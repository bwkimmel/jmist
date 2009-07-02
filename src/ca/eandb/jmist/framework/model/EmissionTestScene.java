/**
 *
 */
package ca.eandb.jmist.framework.model;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.geometry.PrimitiveListGeometry;
import ca.eandb.jmist.framework.geometry.primitive.BoxGeometry;
import ca.eandb.jmist.framework.geometry.primitive.RectangleGeometry;
import ca.eandb.jmist.framework.lens.PinholeLens;
import ca.eandb.jmist.framework.lens.TransformableLens;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.framework.scene.MaterialMapSceneElement;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public final class EmissionTestScene implements Scene {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getGeometry()
	 */
	public SceneElement getRoot() {
		initialize();
		return root;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getLens()
	 */
	public Lens getLens() {
		initialize();
		return lens;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getLight()
	 */
	public Light getLight() {
		initialize();
		return root.createLight();
	}

	public static Scene getInstance() {
		if (instance == null) {
			instance = new EmissionTestScene();
		}

		return instance;
	}

	private synchronized void initialize() {

		if (root != null) {
			return;
		}

		PrimitiveListGeometry geometry = new PrimitiveListGeometry()
			.addPrimitive(
				new RectangleGeometry(
					new Point3(-200.0, 50.0, 0.0),
					Basis3.fromW(Vector3.I),
					100.0, 100.0,
					false))
			.addPrimitive(
				new BoxGeometry(
					new Box3(-200.0, -10.0, -200.0, 200.0, 0.0, 200.0)));

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				geometry.addPrimitive(new RectangleGeometry(
						new Point3(200.0, (double) (5 + (10 * i)), (double) (-45 + (10 * j))),
						Basis3.fromW(Vector3.NEGATIVE_I),
						10.0, 10.0,
						false));
			}
		}

		MaterialMapSceneElement materials = new MaterialMapSceneElement(geometry)
			.addMaterial("diffuse50", diffuse50)
			.addMaterial("diffuseLuminaire1", diffuseLuminaire1)
			.setMaterial(0, "diffuseLuminaire1")
			.setMaterial(1, "diffuse50")
			.setMaterialRange(2, 100, "diffuseLuminaire1");

		root = materials;

		lens = PinholeLens.fromHfovAndAspect(0.785398, 1.0);

		lens.rotateX(-Math.PI / 2.0);
		lens.translate(new Vector3(0.0, 550.0, 50.0));

	}

	@Override
	public Box3 boundingBox() {
		initialize();
		return root.boundingBox();
	}

	@Override
	public Sphere boundingSphere() {
		initialize();
		return root.boundingSphere();
	}

	private static Scene instance = null;

	private final ColorModel cm = ColorModel.getInstance();
	private final Material diffuse50 = new LambertianMaterial(new UniformPainter(cm.getGray(0.5)));
	private final Material diffuseLuminaire1 = new LambertianMaterial(null, new UniformPainter(cm.getGray(2e5)));

	private SceneElement root = null;
	private TransformableLens lens = null;

}
