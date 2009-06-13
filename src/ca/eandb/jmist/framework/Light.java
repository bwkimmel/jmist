/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad Kimmel
 *
 */
public interface Light {

	Color illuminate(Intersection x, VisibilityFunction3 vf);

}
