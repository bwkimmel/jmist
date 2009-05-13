/**
 *
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
