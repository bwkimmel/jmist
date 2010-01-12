/**
 * 
 */
package ca.eandb.jmist.framework.color;

import java.io.Serializable;

/**
 * @author Brad
 *
 */
public interface ColorMeasure extends Serializable {

	double evaluate(Color c);
	
}
