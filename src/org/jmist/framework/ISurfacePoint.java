/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface ISurfacePoint {

	Point3 location();
	Vector3 normal();
	Vector3 microfacetNormal();

	Vector3 tangent();

	IMaterial material();

	IMedium mediumAbove();
	IMedium mediumBelow();

	Point2 textureCoordinates();

}
