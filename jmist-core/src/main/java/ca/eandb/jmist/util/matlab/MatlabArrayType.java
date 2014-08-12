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
package ca.eandb.jmist.util.matlab;

/**
 * Types of MATLAB arrays.
 * @author Brad Kimmel
 */
public enum MatlabArrayType {

	/** Cell array. */
	CELL(1),

	/** Structure. */
	STRUCT(2),

	/** Object. */
	OBJECT(3),

	/** Character array. */
	CHAR(4),

	/** Sparse array. */
	SPARSE(5),

	/** IEEE 754 double precision floating point. */
	DOUBLE(6),

	/** IEEE 754 single precision floating point. */
	SINGLE(7),

	/** Signed 8-bit integer. */
	INT8(8),

	/** Unsigned 8-bit integer. */
	UINT8(9),

	/** Signed 16-bit integer. */
	INT16(10),

	/** Unsigned 16-bit integer. */
	UINT16(11),

	/** Signed 32-bit integer. */
	INT32(12),

	/** Unsigned 32-bit integer. */
	UINT32(13);

	/** The value that signifies this array type in the MAT-file. */
	public final byte value;

	/**
	 * Creates a new <code>MatlabArrayType</code>.
	 * @param value The value that signifies this array type in the MAT-file.
	 */
	MatlabArrayType(int value) {
		this.value = (byte) value;
	}

}
