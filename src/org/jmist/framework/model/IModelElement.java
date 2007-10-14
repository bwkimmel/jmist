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

	boolean contains(Point3 p);

	ISurfacePoint generateRandomSurfacePoint();

	

	/*
	 * The following methods maintain the list of children of this
	 * model element.
	 */

	/**
	 * Adds a child model element.
	 * @param child The child to add.
	 * @throws UnsupportedOperationException If this model element is not
	 * 		a composite model element.
	 */
	void addChild(IModelElement child) throws UnsupportedOperationException;

	/**
	 * Removes a child model element.  This method has no effect if {@code child}
	 * is not a child of this element.
	 * @param child The child to remove.
	 */
	void removeChild(IModelElement child);

	/**
	 * Gets the collection of children of this model element.
	 * @return The collection of children of this model element.
	 */
	Iterable<IModelElement> children();

}
