/**
 *
 */
package ca.eandb.jmist.framework.model;

import ca.eandb.jmist.framework.Bounded3;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.toolkit.Point3;

/**
 * @author bkimmel
 *
 */
public interface ModelElement extends VisibilityFunction3, Bounded3 {

	boolean contains(Point3 p);

	SurfacePoint generateRandomSurfacePoint();



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
	void addChild(ModelElement child) throws UnsupportedOperationException;

	/**
	 * Removes a child model element.  This method has no effect if {@code child}
	 * is not a child of this element.
	 * @param child The child to remove.
	 */
	void removeChild(ModelElement child);

	/**
	 * Gets the collection of children of this model element.
	 * @return The collection of children of this model element.
	 */
	Iterable<ModelElement> children();

}
