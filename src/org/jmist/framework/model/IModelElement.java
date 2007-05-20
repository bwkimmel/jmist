/**
 *
 */
package org.jmist.framework.model;

import org.jmist.framework.IBounded3;
import org.jmist.framework.IIntersection;
import org.jmist.framework.ISurfacePoint;
import org.jmist.framework.IVisibilityFunction3;
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

	void addChild(IModelElement child) throws UnsupportedOperationException;
	void removeChild(String name);
	IModelElement findChild(String name);

	Iterable<IModelElement> children();

}
