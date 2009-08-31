/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public interface EyeNode extends PathNode {

	Point2 project(HPoint3 x);

}
