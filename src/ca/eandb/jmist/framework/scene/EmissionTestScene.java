/**
 *
 */
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.geometry.PrimitiveListGeometry;
import ca.eandb.jmist.framework.geometry.primitive.BoxGeometry;
import ca.eandb.jmist.framework.geometry.primitive.RectangleGeometry;
import ca.eandb.jmist.framework.lens.PinholeLens;
import ca.eandb.jmist.framework.lens.TransformableLens;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public final class EmissionTestScene extends AbstractScene {

	private static final PrimitiveListGeometry geometry;

	private static final TransformableLens lens;

	private final SceneElement root;

	public EmissionTestScene(ColorModel colorModel) {
		Material diffuse50 = new LambertianMaterial(colorModel.getGray(0.5));
		Material diffuseLuminaire1 = new LambertianMaterial(null, colorModel.getGray(2e5));

		this.root = new MaterialMapSceneElement(geometry)
				.addMaterial("diffuse50", diffuse50)
				.addMaterial("diffuseLuminaire1", diffuseLuminaire1)
				.setMaterial(0, "diffuseLuminaire1")				// large light source
				.setMaterial(1, "diffuse50")						// floor
				.setMaterialRange(2, 100, "diffuseLuminaire1");		// small light sources
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getGeometry()
	 */
	public SceneElement getRoot() {
		return root;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getLens()
	 */
	public Lens getLens() {
		return lens;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Model#getLight()
	 */
	public Light getLight() {
		return root.createLight();
	}

	static {

		geometry = new PrimitiveListGeometry()
			.addPrimitive( // large light source
				new RectangleGeometry(
					new Point3(-200.0, 50.0, 0.0),
					Basis3.fromW(Vector3.I),
					100.0, 100.0,
					false))
			.addPrimitive( // floor
				new BoxGeometry(
					new Box3(-200.0, -10.0, -200.0, 200.0, 0.0, 200.0)));

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				geometry.addPrimitive(new RectangleGeometry( // small light source
						new Point3(200.0, (double) (5 + (10 * i)), (double) (-45 + (10 * j))),
						Basis3.fromW(Vector3.NEGATIVE_I),
						10.0, 10.0,
						false));
			}
		}

		lens = PinholeLens.fromHfovAndAspect(0.785398, 1.0);

		lens.rotateX(-Math.PI / 2.0);
		lens.translate(new Vector3(0.0, 550.0, 50.0));

	}

}
