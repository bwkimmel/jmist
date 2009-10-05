/**
 *
 */
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Animator;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad
 *
 */
public final class EmptyScene implements Scene {

	/** Serialization version ID. */
	private static final long serialVersionUID = -2855797477543563321L;

	public static final Scene INSTANCE = new EmptyScene();

	private EmptyScene() {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getLens()
	 */
	public Lens getLens() {
		return Lens.NULL;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getLight()
	 */
	public Light getLight() {
		return Light.NULL;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getRoot()
	 */
	public SceneElement getRoot() {
		return NullSceneElement.INSTANCE;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return Box3.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return Sphere.EMPTY;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Scene#getAnimator()
	 */
	public Animator getAnimator() {
		return Animator.STATIC;
	}

}
