/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

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

	void illuminate(Vector3 from, Spectrum irradiance);

}
