/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Brad
 *
 */
final class ReflectionRibBinding implements RibBinding {

	private final Method method;
	
	public ReflectionRibBinding(Method method) {
		if (method.getDeclaringClass() != RenderManContext.class) {
			throw new IllegalArgumentException("method must be declared by RenderManContext");
		}
		this.method = method;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.RibBinding#processBinding(ca.eandb.jmist.framework.loader.renderman.RibTokenizer, ca.eandb.jmist.framework.loader.renderman.RenderManContext)
	 */
	@Override
	public void processBinding(RibTokenizer tokenizer,
			RenderManContext context) throws RibException {

		tokenizer.advance();
		
		Class<?>[] argTypes = method.getParameterTypes();
		Object[] args = parseArguments(argTypes, tokenizer, context);
		
		try {
			method.invoke(context, args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Object[] parseArguments(Class<?>[] argTypes, RibTokenizer tokenizer, RenderManContext context) throws RibException {
		
		Object[] args = new Object[argTypes.length];
		
		for (int i = 0; i < args.length; i++) {
			args[i] = parseArgument(argTypes[i], tokenizer, context);
		}
		
		return args;
		
	}

	private Object parseArgument(Class<?> argType, RibTokenizer tokenizer, RenderManContext context) throws RibException {
		try {
			return RibBindingUtil.parseArgument(argType, tokenizer, context);
		} catch (IllegalArgumentException e) {
			throw new RenderManException(
					RtErrorType.BUG,
					RtErrorSeverity.SEVERE,
					String.format(
							"Reflection binding can't interpret parameter type `%s'",
							argType.getName()), e);
		}
	}

}
