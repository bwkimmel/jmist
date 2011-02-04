/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.eandb.util.UnexpectedException;
import ca.eandb.util.UnimplementedException;


/**
 * @author Brad
 *
 */
public final class RibReader {
	
	public void read(Reader reader, RenderManContext context) throws IOException {

		RibTokenizer tokenizer = new RibTokenizer(reader);
		
		while (true) {
			
			RibToken token = tokenizer.nextToken();
			
			switch (token.type) {
			case RibToken.RT_EOF:
				return;
				
			case RibToken.RT_IDENTIFIER:
				processCommand(token.stringValue, tokenizer);
				break;
				
			default:
				break;
				
			}
			
		}
		
	}
	
	private Method findMethod(String command) {
		return null;
	}

	private void processCommand(String command, RibTokenizer tokenizer, RenderManContext context) {
		
		Method method = findMethod(command);
		
		Class<?>[] paramTypes = method.getParameterTypes();
		Object[] params = new Object[paramTypes.length];

		for (int i = 0; i < params.length; i++) {
			params[i] = parseArgument(paramTypes[i], tokenizer);
		}
		
		try {
			method.invoke(context, params);
		} catch (Exception e) {
			throw new UnexpectedException(e);
		}
		
	}
	
	private Object parseArray(int elementType, RibTokenizer tokenizer) throws IOException {
		RibToken token = tokenizer.nextToken();
		if (token.type != RibToken.RT_OPEN_ARRAY) {
			throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Expected '['");
		}
		List<RibToken> elements = new ArrayList<RibToken>();
		while (true) {
			token = tokenizer.nextToken();
			elements.add(token);

			switch (elementType) {
			
			case RibToken.RT_REAL:
				if (token.type != RibToken.RT_REAL && token.type != RibToken.RT_INTEGER) {
					throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Floating point value or ']' expected");
				}
				break;
				
			case RibToken.RT_INTEGER:
				if (token.type != RibToken.RT_INTEGER) {
					throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Integer value or ']' expected");					
				}
				break;
				
			case RibToken.RT_STRING:
				if (token.type != RibToken.RT_STRING) {
					throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "String value or ']' expected");					
				}
				break;
				
			case RibToken.RT_CLOSE_ARRAY:
				switch (elementType) {
				case RibToken.RT_REAL: {
					double[] result = new double[elements.size()];
					for (int i = 0, n = elements.size(); i < n; i++) {
						result[i] = elements.get(i).realValue;
					}
					return result;
				}
				case RibToken.RT_INTEGER: {
					int[] result = new int[elements.size()];
					for (int i = 0, n = elements.size(); i < n; i++) {
						result[i] = elements.get(i).intValue;
					}
					return result;
				}
				case RibToken.RT_STRING: {
					String[] result = new String[elements.size()];
					for (int i = 0, n = elements.size(); i < n; i++) {
						result[i] = elements.get(i).stringValue;
					}
					return result;
				}	
				}
				
			default:
				throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Expected ']'");
				
			}
		}
	}
	
	private String parseString(RibTokenizer tokenizer) throws IOException {
		RibToken token = tokenizer.nextToken();
		if (token.type != RibToken.RT_STRING) {
			throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "String value expected");
		}
		return token.stringValue;
	}
	
	private RtToken parseToken(RibTokenizer tokenizer) throws IOException {
		RibToken token = tokenizer.nextToken();
		if (token.type != RibToken.RT_STRING) {
			throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Token expected");
		}
		return TokenFactory.BUILTIN.create(token.stringValue);
	}

	private double parseDouble(RibTokenizer tokenizer) throws IOException {
		RibToken token = tokenizer.nextToken();
		if (token.type != RibToken.RT_REAL && token.type != RibToken.RT_INTEGER) {
			throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Floating point value expected");
		}
		return token.realValue;
	}

	private int parseInteger(RibTokenizer tokenizer) throws IOException {
		RibToken token = tokenizer.nextToken();
		if (token.type != RibToken.RT_INTEGER) {
			throw new RenderManException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Integer value expected");
		}
		return token.intValue;
	}
	
	private RtFilterFunc parseFilterFunc(RibTokenizer tokenizer) throws IOException {
		String name = parseString(tokenizer);
		if (name.equals("box")) {
			return RenderManContext.RiBoxFilter;
		} else if (name.equals("triangle")) {
			return RenderManContext.RiTriangleFilter;
		} else if (name.equals("catmull-rom")) {
			return RenderManContext.RiCatmullRomFilter;
		} else if (name.equals("gaussian")) {
			return RenderManContext.RiGaussianFilter;
		} else if (name.equals("b-spline")) {
			throw new UnimplementedException("RiBSplineFilter");
		} else if (name.equals("sinc")) {
			return RenderManContext.RiSincFilter;
		} else {
			throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Invalid filter function");
		}
	}
	
	private RtErrorHandler parseErrorHandler(RibTokenizer tokenizer) throws IOException {
		String name = parseString(tokenizer);
		if (name.equals("ignore")) {
			return RenderManContext.RiErrorIgnore;
		} else if (name.equals("print")) {
			return RenderManContext.RiErrorPrint;
		} else if (name.equals("abort")) {
			return RenderManContext.RiErrorAbort;
		} else {
			throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Invalid filter function");
		}
	}
	
	private Map<RtToken, Object> parseParameterList(RibTokenizer tokenizer) throws IOException {
		
	}

	private Object parseArgument(Class<?> type, RibTokenizer tokenizer) throws IOException {
		
		if (type == RtToken.class) {
			return parseToken(tokenizer);
		} else if (type == Double.TYPE) {
			return parseDouble(tokenizer);
		} else if (type == Integer.TYPE) {
			return parseInteger(tokenizer);
		} else if (type == String.class) {
			return parseString(tokenizer);
		} else if (type == double[].class) {
			return parseArray(RibToken.RT_REAL, tokenizer);
		} else if (type == int[].class) {
			return parseArray(RibToken.RT_INTEGER, tokenizer);
		} else if (type == String[].class) {
			return parseArray(RibToken.RT_STRING, tokenizer);
		} else if (type == RtBound.class) {
			
		} else if (type == RtColor.class) {
			
		} else if (type == RtMatrix.class) {
		} else if (type == RtPoint.class) {
		} else if (type == RtPoint[].class) {
		} else if (type == RtFilterFunc.class) {
			return parseFilterFunc(tokenizer);
		} else if (type == RtErrorHandler.class) {
			return parseErrorHandler(tokenizer);
		} else if (type == Map.class) {
			return parseParameterList(tokenizer);
		} 
		
	}
	
}
