/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * @author Brad Kimmel
 *
 */
public interface SurfacePoint extends SurfacePointGeometry {

	Material material();

	Medium ambientMedium();

	Shader shader();

	SceneObject sceneObject();

}
