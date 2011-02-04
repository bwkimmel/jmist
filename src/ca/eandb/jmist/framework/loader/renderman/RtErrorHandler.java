/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public interface RtErrorHandler {

	void apply(RtErrorType type, RtErrorSeverity severity, String message, Throwable cause);
	
	void apply(RtErrorType type, RtErrorSeverity severity, String message);
	
}
