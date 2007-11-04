/**
 *
 */
package org.jmist.framework;

/**
 * @author bkimmel
 *
 */
public interface Light {

	void illuminate(SurfacePoint x, VisibilityFunction3 vf);

}
