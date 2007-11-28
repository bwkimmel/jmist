/**
 * 
 */
package org.jmist.toolkit.matlab;

/**
 * @author bkimmel
 *
 */
enum MatlabDataType {

	INT8(1,1),
	UINT8(2,1),
	INT16(3,2),
	UINT16(4,2),
	INT32(5,4),
	UINT32(6,4),
	SINGLE(7,4),
	DOUBLE(9,8),
	INT64(12,8),
	UINT64(13,8),
	MATRIX(14),
	COMPRESSED(15),
	UTF8(16),
	UTF16(17),
	UTF32(18);

	public final int value;
	public final int size;

	MatlabDataType(int value) {
		this.value = value;
		this.size = 0;
	}

	MatlabDataType(int value, int size) {
		this.value = value;
		this.size = size;
	}

}
