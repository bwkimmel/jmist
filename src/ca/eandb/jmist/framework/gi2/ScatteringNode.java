/**
 *
 */
package ca.eandb.jmist.framework.gi2;

import ca.eandb.jmist.framework.color.Color;

/**
 * @author Brad
 *
 */
public interface ScatteringNode extends PathNode {

	Color getSourceRadiance();

	double getSourcePDF();

}
