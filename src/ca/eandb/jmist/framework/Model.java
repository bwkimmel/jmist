/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * A complete model that can be rendered.
 * @author Brad Kimmel
 */
public interface Model {

	/**
	 * Gets the model's <code>SceneElement</code>.
	 * @return The model's <code>SceneElement</code>.
	 */
	SceneElement getGeometry();

	/**
	 * Gets the <code>Light</code> that illuminates the scene.
	 * @return The <code>Light</code> that illuminates the scene.
	 */
	Light getLight();

	/**
	 * Gets the <code>Lens</code> from which to view the scene.
	 * @return The <code>Lens</code> from which to view the scene.
	 */
	Lens getLens();

}
