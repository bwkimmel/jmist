/**
 *
 */
package org.jdcp.server.classmanager;

/**
 * @author brad
 *
 */
public interface ParentClassManager extends ClassManager {

	ClassManager createChildClassManager();

	void releaseChildClassManager(ClassManager child)
			throws IllegalArgumentException;

}
