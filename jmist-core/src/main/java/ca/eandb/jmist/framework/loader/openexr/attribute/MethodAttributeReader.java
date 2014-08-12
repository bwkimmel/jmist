/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
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
