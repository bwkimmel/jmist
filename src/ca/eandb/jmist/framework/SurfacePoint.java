/**
 *
 */
package ca.eandb.jmist.framework;


/**
 * @author Brad Kimmel
 *
 */
public interface SurfacePoint extends SurfacePointGeometry {

	Material getMaterial();

	Medium getAmbientMedium();

}
