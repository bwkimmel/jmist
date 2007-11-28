/**
 * 
 */
package org.jmist.toolkit.matlab;

/**
 * @author bkimmel
 *
 */
enum MatlabArrayType {

	CELL(1),
	STRUCT(2),
	OBJECT(3),
	CHAR(4),
	SPARSE(5),
	DOUBLE(6),
	SINGLE(7),
	INT8(8),
	UINT8(9),
	INT16(10),
	UINT16(11),
	INT32(12),
	UINT32(13);

	public final byte value;

	MatlabArrayType(int value) {
		this.value = (byte) value;
	}

}
