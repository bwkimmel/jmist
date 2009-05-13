/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
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
