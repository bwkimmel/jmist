/**
 *
 */
package ca.eandb.jmist.toolkit.matlab;

/**
 * The types of the MATLAB elements in a MAT-file.
 * @author Brad Kimmel
 */
public enum MatlabDataType {

	/** Signed 8-bit integer. */
	INT8(1,1),

	/** Unsigned 8-bit integer. */
	UINT8(2,1),

	/** Signed 16-bit integer. */
	INT16(3,2),

	/** Unsigned 16-bit integer. */
	UINT16(4,2),

	/** Signed 32-bit integer. */
	INT32(5,4),

	/** Unsigned 32-bit integer. */
	UINT32(6,4),

	/** IEEE 754 single precision floating point. */
	SINGLE(7,4),

	/** IEEE 754 double precision floating point. */
	DOUBLE(9,8),

	/** Signed 64-bit integer. */
	INT64(12,8),

	/** Unsigned 64-bit integer. */
	UINT64(13,8),

	/** An array, structure, cell, or object. */
	MATRIX(14),

	/** GZIP-compressed data. */
	COMPRESSED(15),

	/** UTF-8 String. */
	UTF8(16),

	/** UTF-16 String. */
	UTF16(17),

	/** UTF-32 String. */
	UTF32(18);

	/** The value that signifies this type in the MAT-file. */
	public final int value;

	/**
	 * The size (in bytes) of items of this type (or zero if not applicable).
	 */
	public final int size;

	/**
	 * Creates a new <code>MatlabDataType</code> with no applicable size.
	 * @param value The value that signifies this type in the MAT-file.
	 */
	MatlabDataType(int value) {
		this.value = value;
		this.size = 0;
	}

	/**
	 * Creates a new <code>MatlabDataType</code>.
	 * @param value The value that signifies this type in the MAT-file.
	 * @param size The size (in bytes) of a single item of this type.
	 */
	MatlabDataType(int value, int size) {
		this.value = value;
		this.size = size;
	}

}
