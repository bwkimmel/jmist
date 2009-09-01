/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public interface ScatteringNode extends PathNode {

	Color getSourceRadiance();

	double getSourcePDF();

	double getSourcePDF(Vector3 v);

}
