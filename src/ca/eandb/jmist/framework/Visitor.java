/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * Visits <code>Object</code>s in a collection.
 * @author Brad Kimmel
 */
public interface Visitor {

	/**
	 * Visits an item stored in the collection.
	 * @param item The <code>Object</code> that was visited.
	 * @return A value indicating whether the traversal should continue.
	 */
	boolean visit(Object object);

}
