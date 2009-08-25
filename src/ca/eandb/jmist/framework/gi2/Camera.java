/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public interface Camera {

	EyeNode sample(Point2 pointOnImagePlane, Color weight);

	EyeNode sample(PathNode target, Color weight);

}
