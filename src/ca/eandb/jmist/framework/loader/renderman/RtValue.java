/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public interface RtValue {

	double realValue();
	
	int intValue();
	
	String stringValue();
	
	double[] realArrayValue();
	
	int[] intArrayValue();
	
	String[] stringArrayValue();
	
}
