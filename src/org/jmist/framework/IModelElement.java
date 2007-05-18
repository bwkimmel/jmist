/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.*;

/**
 * @author bkimmel
 *
 */
public interface IModelElement extends IVisibilityFunction3, IBounded3 {

	String getName();

	boolean contains(Point3 p);

	IIntersection intersect(Ray3 ray);

	ISurfacePoint generateRandomSurfacePoint();

	void addChild(IModelElement child);
	void removeChild(String name);
	IModelElement findChild(String name);

	Iterable<IModelElement> children();

}
