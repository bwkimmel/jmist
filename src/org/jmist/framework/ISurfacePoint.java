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

	Point3 getLocation();
	Vector3 getNormal();
	Vector3 getMicroNormal();

	Vector3 getTangent();

	IMaterial getMaterial();

	IMedium getMediumAbove();
	IMedium getMediumBelow();

	Point2 getTextureCoordinates();

}
