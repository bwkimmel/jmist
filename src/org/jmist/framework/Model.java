/**
 *
 */
package org.jmist.framework;

/**
 * A complete model that can be rendered.
 * @author bkimmel
 */
public interface Model {

	/**
	 * Gets the model's <code>Geometry</code>.
	 * @return The model's <code>Geometry</code>.
	 */
	Geometry getGeometry();

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
