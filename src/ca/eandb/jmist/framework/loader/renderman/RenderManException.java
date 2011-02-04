/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
public final class RenderManException extends RuntimeException {

	/** Serialization version ID. */
	private static final long serialVersionUID = 1833533771357708034L;

	private final RtErrorType type;
	
	private final RtErrorSeverity severity;
	
	public RenderManException(RtErrorType type, RtErrorSeverity severity, String message) {
		super(String.format("(%d,%d): %s", type.code(), severity.level(), message));
		this.type = type;
		this.severity = severity;
	}
	
	public RenderManException(RtErrorType type, RtErrorSeverity severity, String message, Throwable cause) {
		super(String.format("(%d,%d): %s", type.code(), severity.level(), message), cause);
		this.type = type;
		this.severity = severity;
	}
	
	public RtErrorType getType() {
		return type;
	}
	
	public RtErrorSeverity getSeverity() {
		return severity;
	}
	
}
