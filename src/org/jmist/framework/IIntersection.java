/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IIntersection extends ISurfacePoint {

	Ray3 getRay();
	double getRayParameter();

}
