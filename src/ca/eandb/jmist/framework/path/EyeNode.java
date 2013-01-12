/**
 *
 */
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;

/**
 * The terminal node at the viewer end of a path.
 * @author Brad Kimmel
 */
public interface EyeNode extends PathNode {

	Point2 project(HPoint3 x);

}
