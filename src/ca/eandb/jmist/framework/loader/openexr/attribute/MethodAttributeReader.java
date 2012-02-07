/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ca.eandb.util.UnexpectedException;

/**
 * @author brad
 *
 */
final class MethodAttributeReader implements AttributeReader {
	
	private final Method method;
	
	MethodAttributeReader(Method method) {
		Class<?>[] paramTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		if (paramTypes.length != 2 || paramTypes[0] != DataInput.class || paramTypes[1] != int.class || !Attribute.class.isAssignableFrom(returnType)) {
			throw new IllegalArgumentException("Incorrect signature");
		}
		this.method = method;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.AttributeReader#read(java.io.DataInput, int)
	 */
	@Override
	public Attribute read(DataInput in, int size) throws IOException {
		try {
			return (Attribute) method.invoke(null, in, size);
		} catch (IllegalArgumentException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			} else {
				throw new UnexpectedException(e);
			}
		}
	}

}
