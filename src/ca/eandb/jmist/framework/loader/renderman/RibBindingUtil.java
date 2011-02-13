/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.eandb.util.DoubleArray;
import ca.eandb.util.IntegerArray;

/**
 * @author Brad
 *
 */
final class RibBindingUtil {
	
	private RibBindingUtil() {}
	
	public static double[] getRealArrayParameter(Map<RtToken, RtValue> params, RtToken key, double[] def) {
		RtValue value = params.get(key);
		return value != null ? value.realArrayValue() : def;
	}
	
	public static int[] getIntArrayParameter(Map<RtToken, RtValue> params, RtToken key, int[] def) {
		RtValue value = params.get(key);
		return value != null ? value.intArrayValue() : def;
	}
	
	public static String[] getStringArrayParameter(Map<RtToken, RtValue> params, RtToken key, String[] def) {
		RtValue value = params.get(key);
		return value != null ? value.stringArrayValue() : def;
	}
	
	public static double getRealParameter(Map<RtToken, RtValue> params, RtToken key, double def) {
		RtValue value = params.get(key);
		return value != null ? value.realValue() : def;
	}
	
	public static int getIntParameter(Map<RtToken, RtValue> params, RtToken key, int def) {
		RtValue value = params.get(key);
		return value != null ? value.intValue() : def;
	}
	
	public static String getStringParameter(Map<RtToken, RtValue> params, RtToken key, String def) {
		RtValue value = params.get(key);
		return value != null ? value.stringValue() : def;
	}

	public static Object parseArgument(Class<?> type, RibTokenizer tokenizer, RenderManContext context) throws RibException {
		if (type == Double.TYPE || type == Double.class) {
			return parseReal(tokenizer);
		} else if (type == Integer.TYPE || type == Integer.class) {
			return parseInteger(tokenizer);
		} else if (type == Boolean.TYPE || type == Boolean.class) {
			return parseBoolean(tokenizer);
		} else if (type == String.class) {
			return parseString(tokenizer);
		} else if (type == RtToken.class) {
			return parseToken(tokenizer, context);
		} else if (type == double[].class) {
			return parseRealArray(tokenizer);
		} else if (type == int[].class) {
			return parseIntegerArray(tokenizer);
		} else if (type == String[].class) {
			return parseStringArray(tokenizer);
		} else if (type == RtBound.class) {
			return parseBound(tokenizer);
		} else if (type == RtMatrix.class) {
			return parseMatrix(tokenizer);
		} else if (type == RtPoint.class) {
			return parsePoint(tokenizer);
		} else if (type == Map.class) {
			return parseParameterList(tokenizer, context);
		}
		throw new IllegalArgumentException("Unrecognized argument type");
	}
	
	private static RtValue parseArray(RibTokenizer tokenizer) throws RibException {
		List<RtValue> values = new ArrayList<RtValue>();
		RibToken token = tokenizer.getCurrentToken();
		if (token.type != RibToken.Type.OPEN_ARRAY) {
			throw new RibException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Expected '['");
		}
		tokenizer.advance();
		while (true) {			
			token = tokenizer.getCurrentToken();
			switch (token.type) {
			case OPEN_ARRAY:
				throw new RibException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Nested arrays not allowed");
				
			case CLOSE_ARRAY:
				tokenizer.advance();
				return new ArrayRtValue(values);
				
			default:
				values.add(parseValue(tokenizer));
			}
		}
	}
	
	public static RtValue parseValue(RibTokenizer tokenizer) throws RibException {
		RibToken token = tokenizer.getCurrentToken();
		switch (token.type) {
		case STRING:
			tokenizer.advance();
			return new StringRtValue(token.stringValue);
			
		case INTEGER:
			tokenizer.advance();
			return new IntegerRtValue(token.intValue);
			
		case REAL:
			tokenizer.advance();
			return new RealRtValue(token.realValue);
			
		case OPEN_ARRAY:
			return parseArray(tokenizer);
			
		case EOF:
			throw new RibException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Unexpected end of file");
			
		default:
			throw new RibException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Value expected");
			
		}
	}
	
	public static Map<RtToken, RtValue> parseParameterList(RibTokenizer tokenizer,
			RenderManContext context) throws RibException {

		Map<RtToken, RtValue> result = new HashMap<RtToken, RtValue>();
		RibToken token;
		
		while (true) {
			token = tokenizer.getCurrentToken();
			
			switch (token.type) {
			case STRING:
				tokenizer.advance();
				RtToken key = TokenFactory.BUILTIN.create(token.stringValue);
				RtValue value = parseValue(tokenizer);
				result.put(key, value);
				break;
				
			case IDENTIFIER:
			case EOF:
				return result;
				
			default:
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Token, identifier, or EOF expected");
				
			}
		}
		
	}

	public static double parseReal(RibTokenizer tokenizer) throws RibException {
		RibToken token = tokenizer.getCurrentToken();
		if (token.type != RibToken.Type.REAL && token.type != RibToken.Type.INTEGER) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected real value");
		}
		tokenizer.advance();
		return token.realValue;
	}
	
	public static boolean parseBoolean(RibTokenizer tokenizer) throws RibException {
		return parseInteger(tokenizer) != 0;
	}
	
	public static int parseInteger(RibTokenizer tokenizer) throws RibException {
		RibToken token = tokenizer.getCurrentToken();
		if (token.type != RibToken.Type.INTEGER) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected integer value");			
		}
		tokenizer.advance();
		return token.intValue;
	}
	
	public static String parseString(RibTokenizer tokenizer) throws RibException {
		RibToken token = tokenizer.getCurrentToken();
		if (token.type != RibToken.Type.STRING) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected string value");			
		}
		tokenizer.advance();
		return token.stringValue;
	}
	
	public static RtToken parseToken(RibTokenizer tokenizer, RenderManContext context) throws RibException {
		RibToken token = tokenizer.getCurrentToken();
		if (token.type != RibToken.Type.STRING) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected token");			
		}
		tokenizer.advance();
		return TokenFactory.BUILTIN.create(token.stringValue);
	}
	
	public static double[] parseRealArray(RibTokenizer tokenizer) throws RibException {
		DoubleArray result = new DoubleArray();
		RibToken token = tokenizer.getCurrentToken();
		
		if (token.type != RibToken.Type.OPEN_ARRAY) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected '['");
		}
		tokenizer.advance();

		while (true) {
			token = tokenizer.getCurrentToken();
			switch (token.type) {
			case CLOSE_ARRAY:
				tokenizer.advance();
				return result.toDoubleArray();
				
			case REAL:
			case INTEGER:
				tokenizer.advance();
				result.add(token.realValue);
				break;
				
			default:
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected real value or ']'");
				
			}
		}
	}
	
	public static double[] parseRealArray(int size, RibTokenizer tokenizer) throws RibException {
		double[] result = new double[size];
		RibToken token = tokenizer.getCurrentToken();
		boolean delimited = (token.type == RibToken.Type.OPEN_ARRAY);
		if (delimited) {
			tokenizer.advance();
		}
		
		for (int i = 0; i < size; i++) {
			token = tokenizer.getCurrentToken();
			if (token.type != RibToken.Type.REAL && token.type != RibToken.Type.INTEGER) {
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected real value");
			}
			result[i] = token.realValue;
			tokenizer.advance();
		}
		
		if (delimited) {
			token = tokenizer.getCurrentToken();
			if (token.type != RibToken.Type.CLOSE_ARRAY) {
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected ']'");
			}
			tokenizer.advance();
		}
		
		return result;
	}
	
	public static int[] parseIntegerArray(RibTokenizer tokenizer) throws RibException {
		IntegerArray result = new IntegerArray();
		RibToken token = tokenizer.getCurrentToken();
		
		if (token.type != RibToken.Type.OPEN_ARRAY) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected '['");
		}
		tokenizer.advance();

		while (true) {
			token = tokenizer.getCurrentToken();
			switch (token.type) {
			case CLOSE_ARRAY:
				tokenizer.advance();
				return result.toIntegerArray();
				
			case INTEGER:
				tokenizer.advance();
				result.add(token.intValue);
				break;
				
			default:
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected integer value or ']'");
				
			}
		}		
	}
	
	public static int[] parseIntegerArray(int size, RibTokenizer tokenizer) throws RibException {
		int[] result = new int[size];
		RibToken token = tokenizer.getCurrentToken();
		boolean delimited = (token.type == RibToken.Type.OPEN_ARRAY);
		if (delimited) {
			tokenizer.advance();
		}
		
		for (int i = 0; i < size; i++) {
			token = tokenizer.getCurrentToken();
			if (token.type != RibToken.Type.INTEGER) {
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected integer value");
			}
			result[i] = token.intValue;
			tokenizer.advance();
		}
		
		if (delimited) {
			token = tokenizer.getCurrentToken();
			if (token.type != RibToken.Type.CLOSE_ARRAY) {
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected ']'");
			}
			tokenizer.advance();
		}
		
		return result;		
	}
	
	public static String[] parseStringArray(RibTokenizer tokenizer) throws RibException {
		List<String> result = new ArrayList<String>();
		RibToken token = tokenizer.getCurrentToken();
		
		if (token.type != RibToken.Type.OPEN_ARRAY) {
			throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected '['");
		}
		tokenizer.advance();

		while (true) {
			token = tokenizer.getCurrentToken();
			switch (token.type) {
			case CLOSE_ARRAY:
				tokenizer.advance();
				return result.toArray(new String[result.size()]);
				
			case STRING:
				tokenizer.advance();
				result.add(token.stringValue);
				break;
				
			default:
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected string value or ']'");
				
			}
		}
		
	}
	
	public static String[] parseStringArray(int size, RibTokenizer tokenizer) throws RibException {
		String[] result = new String[size];
		RibToken token = tokenizer.getCurrentToken();
		boolean delimited = (token.type == RibToken.Type.OPEN_ARRAY);
		if (delimited) {
			tokenizer.advance();
		}
		
		for (int i = 0; i < size; i++) {
			token = tokenizer.getCurrentToken();
			if (token.type != RibToken.Type.STRING) {
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected string value");
			}
			result[i] = token.stringValue;
			tokenizer.advance();
		}
		
		if (delimited) {
			token = tokenizer.getCurrentToken();
			if (token.type != RibToken.Type.CLOSE_ARRAY) {
				throw new RibException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Expected ']'");
			}
			tokenizer.advance();
		}
		
		return result;		
	}
	
	public static RtBound parseBound(RibTokenizer tokenizer) throws RibException {
		double[] box = parseRealArray(6, tokenizer);
		return new RtBound(box[0], box[1], box[2], box[3], box[4], box[5]);
	}
	
	public static RtMatrix parseMatrix(RibTokenizer tokenizer) throws RibException {
		double[] m = parseRealArray(16, tokenizer);
		return new RtMatrix(
				m[0] , m[1] , m[2] , m[3] ,
				m[4] , m[5] , m[6] , m[7] ,
				m[8] , m[9] , m[10], m[11],
				m[12], m[13], m[14], m[15]);
	}
	
	public static RtPoint parsePoint(RibTokenizer tokenizer) throws RibException {
		double[] p = parseRealArray(3, tokenizer);
		return new RtPoint(p[0], p[1], p[2]);		
	}
	

}
