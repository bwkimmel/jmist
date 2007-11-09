/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface RayCaster extends VisibilityFunction3 {

	Intersection castRay(Ray3 ray, Interval I);

}
