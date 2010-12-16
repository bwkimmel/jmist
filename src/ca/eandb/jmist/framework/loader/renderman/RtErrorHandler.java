/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public interface RtErrorHandler {

	void apply(int code, int severity, String message);
	
}
