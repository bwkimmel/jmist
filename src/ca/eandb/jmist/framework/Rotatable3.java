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
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Vector3;

/**
 * Something that may be rotated in three-dimensional space.
 * @author Brad Kimmel
 */
public interface Rotatable3 {

	/**
	 * Rotates the object about the x-axis.
	 * Equivalent to {@code this.rotate(Vector3.I, angle);}
	 * @param angle The angle (in radians) to rotate the object by.
	 * @see rotate
	 */
	void rotateX(double angle);

	/**
	 * Rotates the object about the y-axis.
	 * Equivalent to {@code this.rotate(Vector3.J, angle);}
	 * @param angle The angle (in radians) to rotate the object by.
	 * @see rotate
	 */
	void rotateY(double angle);

	/**
	 * Rotates the object about the z-axis.
	 * Equivalent to {@code this.rotate(Vector3.K, angle);}
	 * @param angle The angle (in radians) to rotate the object by.
	 * @see rotate
	 */
	void rotateZ(double angle);

	/**
	 * Rotates the object about an arbitrary axis.
	 * @param axis The axis to rotate the object around (must not be Vector3.ZERO).
	 * @param angle The angle (in radians) to rotate the object by.
	 */
	void rotate(Vector3 axis, double angle);

}
