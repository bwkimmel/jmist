/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface SurfacePoint {

	Point3 location();
	Vector3 normal();
	Basis3 basis();
	Vector3 microfacetNormal();
	Basis3 microfacetBasis();

	Vector3 tangent();

	Material material();

	Medium ambientMedium();

	Point2 textureCoordinates();

	boolean closed();

}
