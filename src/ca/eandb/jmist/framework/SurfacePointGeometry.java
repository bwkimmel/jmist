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
public interface SurfacePointGeometry {

	Point3 getPosition();
	Vector3 getNormal();
	Basis3 getBasis();
	Vector3 getShadingNormal();
	Basis3 getShadingBasis();

	Vector3 getTangent();

	Point2 getUV();

	boolean isSurfaceClosed();

}
