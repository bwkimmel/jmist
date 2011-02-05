/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public final class RibException extends Exception {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -2901115182844555955L;
	
	private final RtErrorType type;
	private final RtErrorSeverity severity;

	/**
	 * @param message
	 */
	public RibException(RtErrorType type, RtErrorSeverity severity, String message) {
		super(message);
		this.type = type;
		this.severity = severity;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RibException(RtErrorType type, RtErrorSeverity severity, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
		this.severity = severity;
	}
	
	public RtErrorType type() {
		return type;
	}
	
	public RtErrorSeverity severity() {
		return severity;
	}

}
