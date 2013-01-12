/**
 * 
 */
package ca.eandb.jmist.framework.color;

import java.io.Serializable;

/**
 * A function mapping <code>Color</code>s to non-negative real numbers.
 * @author Brad Kimmel
 */
public interface ColorMeasure extends Serializable {

	/**
	 * Evaluates the measure.
	 * @param c The <code>Color</code> to measure.
	 * @return The measure of the color.
	 */
	double evaluate(Color c);
	
}
