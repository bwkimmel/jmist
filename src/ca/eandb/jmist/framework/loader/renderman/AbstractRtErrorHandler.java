/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public abstract class AbstractRtErrorHandler implements RtErrorHandler {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RtErrorHandler#apply(ca.eandb.jmist.framework.loader.renderman.RtErrorType, ca.eandb.jmist.framework.loader.renderman.RtErrorSeverity, java.lang.String)
	 */
	@Override
	public void apply(RtErrorType type, RtErrorSeverity severity, String message) {
		apply(type, severity, message, null);
	}

}
