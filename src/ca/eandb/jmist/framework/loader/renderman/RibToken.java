/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class RibToken {
	
	public static final int RT_EOF = -1;
	public static final int RT_IDENTIFIER = -2;
	public static final int RT_STRING = -3;
	public static final int RT_REAL = -4;
	public static final int RT_INTEGER = -5;
	public static final int RT_OPEN_ARRAY = '[';
	public static final int RT_CLOSE_ARRAY = ']';
	
	public final int type;
	public final String stringValue;
	public final double realValue;
	public final int intValue;
		
	private RibToken(int type, String stringValue, double realValue, int intValue) {
		this.type = type;
		this.stringValue = stringValue;
		this.realValue = realValue;
		this.intValue = intValue;
	}
	
	private static final RibToken TOKEN_EOF = new RibToken(RT_EOF, null, 0.0, 0);
	public static final RibToken eof() {
		return TOKEN_EOF;
	}
	
	private static final RibToken TOKEN_OPEN_ARRAY = new RibToken(RT_OPEN_ARRAY, null, 0.0, 0);
	public static final RibToken openArray() {
		return TOKEN_OPEN_ARRAY;
	}

	private static final RibToken TOKEN_CLOSE_ARRAY = new RibToken(RT_CLOSE_ARRAY, null, 0.0, 0);
	public static final RibToken closeArray() {
		return TOKEN_CLOSE_ARRAY;
	}
	
	public static final RibToken identifier(String name) {
		return new RibToken(RT_IDENTIFIER, name, 0.0, 0);
	}
	
	public static final RibToken string(String value) {
		return new RibToken(RT_STRING, value, 0.0, 0);
	}
	
	public static final RibToken real(double value) {
		return new RibToken(RT_REAL, null, value, 0);
	}
	
	public static final RibToken integer(int value) {
		return new RibToken(RT_INTEGER, null, value, value);
	}

}
