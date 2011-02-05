/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class RibToken {
	
	public static enum Type {
		EOF, IDENTIFIER, STRING, REAL, INTEGER, OPEN_ARRAY, CLOSE_ARRAY
	};
	
	public final Type type;
	public final String stringValue;
	public final double realValue;
	public final int intValue;
		
	private RibToken(Type type, String stringValue, double realValue, int intValue) {
		this.type = type;
		this.stringValue = stringValue;
		this.realValue = realValue;
		this.intValue = intValue;
	}
	
	private static final RibToken TOKEN_EOF = new RibToken(Type.EOF, null, 0.0, 0);
	public static final RibToken eof() {
		return TOKEN_EOF;
	}
	
	private static final RibToken TOKEN_OPEN_ARRAY = new RibToken(Type.OPEN_ARRAY, null, 0.0, 0);
	public static final RibToken openArray() {
		return TOKEN_OPEN_ARRAY;
	}

	private static final RibToken TOKEN_CLOSE_ARRAY = new RibToken(Type.CLOSE_ARRAY, null, 0.0, 0);
	public static final RibToken closeArray() {
		return TOKEN_CLOSE_ARRAY;
	}
	
	public static final RibToken identifier(String name) {
		return new RibToken(Type.IDENTIFIER, name, 0.0, 0);
	}
	
	public static final RibToken string(String value) {
		return new RibToken(Type.STRING, value, 0.0, 0);
	}
	
	public static final RibToken real(double value) {
		return new RibToken(Type.REAL, null, value, 0);
	}
	
	public static final RibToken integer(int value) {
		return new RibToken(Type.INTEGER, null, value, value);
	}

}
