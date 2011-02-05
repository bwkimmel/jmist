/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.util.UnexpectedException;


/**
 * @author Brad
 *
 */
public final class RibReader {
	
	private Map<Integer, RtObjectHandle> objects = new HashMap<Integer, RtObjectHandle>();
	private Map<Integer, RtLightHandle> lights = new HashMap<Integer, RtLightHandle>();
	private RtErrorHandler errorHandler = RenderManContext.RiErrorAbort;
	private int colorSamples = 3;
	
	final class BasisRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			tokenizer.advance();
			
			RtBasis[] basis = { null, null };
			int[] step = { 0, 0 };
			
			for (int i = 0; i < 2; i++) {
				RibToken token = tokenizer.getCurrentToken();
				switch (token.type) {
				case STRING:
					if (token.stringValue.equals("bezier")) {
						basis[i] = RenderManContext.RiBezierBasis;
					} else if (token.stringValue.equals("b-spline")) {
						basis[i] = RenderManContext.RiBSplineBasis;
					} else if (token.stringValue.equals("catmull-rom")) {
						basis[i] = RenderManContext.RiCatmullRomBasis;
					} else if (token.stringValue.equals("hermite")) {
						basis[i] = RenderManContext.RiHermiteBasis;
					} else if (token.stringValue.equals("power")) {
						basis[i] = RenderManContext.RiPowerBasis;
					} else {
						throw new RibException(RtErrorType.BADTOKEN,
								RtErrorSeverity.ERROR,
								String.format("Invalid basis `%s'",
										token.stringValue));
					}
					tokenizer.advance();
					break;
					
				case OPEN_ARRAY:
					double[] b = RibBindingUtil.parseRealArray(16, tokenizer);
					basis[i] = new RtBasis(
							b[0] , b[1] , b[2] , b[3] ,
							b[4] , b[5] , b[6] , b[7] ,
							b[8] , b[9] , b[10], b[11],
							b[12], b[13], b[14], b[15]);
					break;
					
				default:
					throw new RenderManException(RtErrorType.BADTOKEN,
							RtErrorSeverity.ERROR, "Basis name or '[' expected");
					
				}
				
				step[i] = RibBindingUtil.parseInteger(tokenizer);
			}
			
			context.basis(basis[0], step[0], basis[1], step[1]);
			
		}
		
	}
	
	final class ColorSamplesRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			int n = RibBindingUtil.parseInteger(tokenizer);
			double[] nRGB = RibBindingUtil.parseRealArray(3 * n, tokenizer);
			double[] RGBn = RibBindingUtil.parseRealArray(3 * n, tokenizer);
			
			context.colorSamples(n, nRGB, RGBn);
			colorSamples = n;
			
		}
		
	}
	
	final class ColorRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			tokenizer.advance();
			double[] channels = RibBindingUtil.parseRealArray(colorSamples, tokenizer);
			RtColor color = new RtColor(channels);
			
			context.color(color);
			
		}
		
	}
	
	final class OpacityRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			tokenizer.advance();
			double[] channels = RibBindingUtil.parseRealArray(colorSamples, tokenizer);
			RtColor color = new RtColor(channels);
			
			context.opacity(color);
			
		}
		
	}
	
	final class ErrorHandlerRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			tokenizer.advance();
			RtToken token = RibBindingUtil.parseToken(tokenizer, context);

			if (token == RenderManContext.RI_ABORT) {
				errorHandler = RenderManContext.RiErrorAbort;
			} else if (token == RenderManContext.RI_IGNORE) {
				errorHandler = RenderManContext.RiErrorIgnore;				
			} else if (token == RenderManContext.RI_PRINT) {
				errorHandler = RenderManContext.RiErrorPrint;
			} else {
				throw new RenderManException(RtErrorType.BADTOKEN, RtErrorSeverity.ERROR, "Invalid error handler");
			}
			
			context.errorHandler(errorHandler);
			
		}
		
	}
	
	final class ObjectBeginRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			tokenizer.advance();
			int sequenceNumber = RibBindingUtil.parseInteger(tokenizer);
			RtObjectHandle h = context.objectBegin();
			
			if (h != null) {
				objects.put(sequenceNumber, h);
			}
			
		}
		
	}
	
	final class ObjectInstanceRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {
			
			tokenizer.advance();
			
			int sequenceNumber = RibBindingUtil.parseInteger(tokenizer);
			RtObjectHandle h = objects.get(sequenceNumber);
			if (h == null) {
				throw new RenderManException(RtErrorType.BADHANDLE,
						RtErrorSeverity.ERROR, String.format(
								"Invalid object sequence number (%d)",
								sequenceNumber));
			}
			
			context.objectInstance(h);

		}
		
	}
	
	final class LightSourceRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {

			tokenizer.advance();
			
			RtToken shader = RibBindingUtil.parseToken(tokenizer, context);
			int sequenceNumber = RibBindingUtil.parseInteger(tokenizer);
			Map<RtToken, RtValue> params = RibBindingUtil.parseParameterList(tokenizer, context);
			
			RtLightHandle h = context.lightSource(shader, params);
			if (h != null) {
				lights.put(sequenceNumber, h);
			}
			
		}
		
	}
	
	final class AreaLightSourceRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {

			tokenizer.advance();
			
			RtToken shader = RibBindingUtil.parseToken(tokenizer, context);
			int sequenceNumber = RibBindingUtil.parseInteger(tokenizer);
			Map<RtToken, RtValue> params = RibBindingUtil.parseParameterList(tokenizer, context);
			
			RtLightHandle h = context.areaLightSource(shader, params);
			if (h != null) {
				lights.put(sequenceNumber, h);
			}
			
		}
		
	}
	
	final class IlluminateRibBinding implements RibBinding {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
		 */
		@Override
		public void processBinding(RibTokenizer tokenizer,
				RenderManContext context) throws RibException {

			tokenizer.advance();
			
			int sequenceNumber = RibBindingUtil.parseInteger(tokenizer);
			boolean onoff = RibBindingUtil.parseBoolean(tokenizer);
			
			RtLightHandle h = lights.get(sequenceNumber);
			if (h == null) {
				throw new RenderManException(RtErrorType.BADHANDLE,
						RtErrorSeverity.ERROR, String.format(
								"Invalid light sequence number (%d)",
								sequenceNumber));
			}
			
			context.illuminate(h, onoff);
			
		}
		
	}
	
	private final Map<String, RibBinding> bindings = new HashMap<String, RibBinding>();
	
	public RibReader() {
		Class<?>[] declaredClasses = RibReader.class.getDeclaredClasses();
		for (int i = 0; i < declaredClasses.length; i++) {
			String name = declaredClasses[i].getSimpleName();
			if (name.endsWith("RibBinding") && RibBinding.class.isAssignableFrom(declaredClasses[i])) {
				String key = name.substring(0, name.length() - 10).toLowerCase();
				RibBinding binding;
				try {
					Constructor<?> constructor = declaredClasses[i].getDeclaredConstructor(RibReader.class);
					binding = (RibBinding) constructor.newInstance(RibReader.this);
				} catch (Exception e) {
					throw new UnexpectedException(e);
				}
				bindings.put(key, binding);
			}
		}
		
		Method[] methods = RenderManContext.class.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			String key = methods[i].getName().toLowerCase();
			if (!bindings.containsKey(key)) {
				RibBinding binding = new ReflectionRibBinding(methods[i]);
				bindings.put(key, binding);
			}
		}
	}
	
	private void reset() {
		objects.clear();
		lights.clear();
	}
	
	public void read(Reader reader, RenderManContext context) {
		
		reset();
		
		RibTokenizer tokenizer = new RibTokenizer(reader);
		boolean resync = false;
		
		while (true) {
			
			RibToken token = tokenizer.getCurrentToken();

			try {
				if (resync) {
					while (token.type != RibToken.Type.IDENTIFIER) {
						tokenizer.advance();
					}
					resync = false;
				}
				
				switch (token.type) {
				case EOF:
					return;
					
				case IDENTIFIER:
					processCommand(token.stringValue, tokenizer, context);
					break;
					
				default:
					throw new RibException(RtErrorType.SYNTAX, RtErrorSeverity.ERROR, "Identifier expected");
					
				}
				
			} catch (RibException e) {
				
				errorHandler.apply(e.type(), e.severity(), e.getMessage(), e);
				resync = true;
				
			}
			
		}
		
	}
	
	private void processCommand(String command, RibTokenizer tokenizer, RenderManContext context) throws RibException {
	
		RibBinding binding = bindings.get(command.toLowerCase());
		if (binding == null) {
			throw new RibException(RtErrorType.SYNTAX,
					RtErrorSeverity.ERROR, String.format(
							"Unrecognized command `%s'", command));
		}
		
		binding.processBinding(tokenizer, context);
		
	}
		
}
