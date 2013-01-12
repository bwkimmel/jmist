/**
 *
 */
package ca.eandb.jmist.framework;


/**
 * Represents the properties of a point on the surface of some element in a
 * scene. 
 * @author Brad Kimmel
 */
public interface SurfacePoint extends SurfacePointGeometry {

	/** The <code>Material</code> properties at this surface point. */
	Material getMaterial();

	/** 
	 * The <code>Medium</code> on the upper side of the surface (i.e., in the
	 * direction which the normal points).
	 */
	Medium getAmbientMedium();

}
