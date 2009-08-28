/**
 *
 */
package ca.eandb.jmist.framework.gi2;

/**
 * @author Brad
 *
 */
public interface LightNode extends PathNode {

	double getSourcePDF();

	boolean isSourceSingular();

}
