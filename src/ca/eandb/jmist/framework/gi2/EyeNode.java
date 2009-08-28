/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public interface EyeNode extends PathNode {

	void write(Color c, Raster raster);

	double getAperturePDF();

	boolean isApertureSingular();

}
