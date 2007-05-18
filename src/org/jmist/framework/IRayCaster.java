/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IRayCaster {

	IIntersection castRay(Ray3 ray, Interval I);

}
