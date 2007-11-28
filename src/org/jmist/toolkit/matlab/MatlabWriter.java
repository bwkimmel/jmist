/**
 *
 */
package org.jmist.toolkit.matlab;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.jmist.toolkit.Complex;

/**
 * @author bkimmel
 *
 */
public final class MatlabWriter {

	/**
	 * @param stream
	 * @throws IOException
	 */
	public MatlabWriter(OutputStream stream) throws IOException {
		this.stream = new DataOutputStream(stream);
		this.writeHeader();
	}

	public void flush() throws IOException {
		this.stream.flush();
	}

	public void close() throws IOException {
		this.stream.close();
	}

	public void write(boolean[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT8, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeBoolean(array[i]);
		}
	}

	public void write(double[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.DOUBLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeDouble(array[i]);
		}
	}

	public void write(float[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.SINGLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeFloat(array[i]);
		}
	}

	public void write(byte[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT8, array.length);
		this.stream.write(array);
	}

	public void write(short[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT16, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeShort(array[i]);
		}
	}

	public void write(int[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT32, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeInt(array[i]);
		}
	}

	public void write(long[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT64, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeLong(array[i]);
		}
	}

	public void writeUnsigned(byte[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT8, array.length);
		this.stream.write(array);
	}

	public void writeUnsigned(short[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT16, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeShort(array[i]);
		}
	}

	public void writeUnsigned(int[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT32, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeInt(array[i]);
		}
	}

	public void writeUnsigned(long[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT64, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeLong(array[i]);
		}
	}

	public void write(String name, double[] pr, double[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.DOUBLE,
				MatlabDataType.DOUBLE, pi != null, false, false, dimensions,
				pr.length);

		this.write(pr);
		if (pi != null) {
			this.write(pi);
		}

	}

	public void write(String name, double[] array, int[] dimensions) throws IOException {
		this.write(name, array, null, dimensions);
	}

	public void write(String name, float[] pr, float[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.SINGLE,
				MatlabDataType.SINGLE, pi != null, false, false, dimensions,
				pr.length);

		this.write(pr);
		if (pi != null) {
			this.write(pi);
		}

	}

	public void write(String name, float[] array, int[] dimensions) throws IOException {
		this.write(name, array, null, dimensions);
	}

	public void write(String name, int[] pr, int[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.INT32,
				MatlabDataType.INT32, pi != null, false, false, dimensions,
				pr.length);

		this.write(pr);
		if (pi != null) {
			this.write(pi);
		}

	}

	public void write(String name, int[] array, int[] dimensions) throws IOException {
		this.write(name, array, null, dimensions);
	}

	public void write(String name, short[] pr, short[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.INT16,
				MatlabDataType.INT16, pi != null, false, false, dimensions,
				pr.length);

		this.write(pr);
		if (pi != null) {
			this.write(pi);
		}

	}

	public void write(String name, short[] array, int[] dimensions) throws IOException {
		this.write(name, array, null, dimensions);
	}

	public void write(String name, byte[] pr, byte[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.INT8,
				MatlabDataType.INT8, pi != null, false, false, dimensions,
				pr.length);

		this.write(pr);
		if (pi != null) {
			this.write(pi);
		}

	}

	public void write(String name, byte[] array, int[] dimensions) throws IOException {
		this.write(name, array, null, dimensions);
	}

	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.UINT32,
				MatlabDataType.UINT32, pi != null, false, false, dimensions,
				pr.length);

		this.writeUnsigned(pr);
		if (pi != null) {
			this.writeUnsigned(pi);
		}

	}

	public void writeUnsigned(String name, int[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, null, dimensions);
	}

	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.UINT16,
				MatlabDataType.UINT16, pi != null, false, false, dimensions,
				pr.length);

		this.writeUnsigned(pr);
		if (pi != null) {
			this.writeUnsigned(pi);
		}

	}

	public void writeUnsigned(String name, short[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, null, dimensions);
	}

	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.UINT8,
				MatlabDataType.UINT8, pi != null, false, false, dimensions,
				pr.length);

		this.writeUnsigned(pr);
		if (pi != null) {
			this.writeUnsigned(pi);
		}

	}

	public void writeUnsigned(String name, byte[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, null, dimensions);
	}

	public void write(String name, Complex[] array, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.DOUBLE,
				MatlabDataType.DOUBLE, true, false, false, dimensions,
				array.length);

		this.writePrimitiveElementTag(MatlabDataType.DOUBLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeDouble(array[i].re());
		}

		this.writePrimitiveElementTag(MatlabDataType.DOUBLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream.writeDouble(array[i].im());
		}

	}

	public void write(String name, boolean[] array, int[] dimensions) throws IOException {

		this.writeArrayHeader(name, MatlabArrayType.UINT8,
				MatlabDataType.INT8, false, false, false, dimensions,
				array.length);

		this.write(array);

	}

	public void write(String name, String value) throws IOException {

		int length = value.length();

		this.writeArrayHeader(name, MatlabArrayType.CHAR,
				MatlabDataType.UINT16, false, false, false, new int[]{ 1, length },
				length);

		this.writePrimitiveElementTag(MatlabDataType.UINT16, length);
		this.stream.writeChars(value);

	}

	private static int roundToBoundary(int size) {
		int n = size % MATLAB_BYTE_ALIGNMENT;
		return n != 0 ? size + (MATLAB_BYTE_ALIGNMENT - n) : size;
	}

	private static void checkDimensions(int[] dimensions, int elements) {
		if (dimensions.length < 2) {
			throw new IllegalArgumentException("must have at least two dimensions");
		}
		int total = 1;
		for (int i = 0; i < dimensions.length; i++) {
			total *= dimensions[i];
		}
		if (total != elements) {
			throw new IllegalArgumentException("incorrect number of elements.");
		}
	}

	private void writeArrayHeader(String name, MatlabArrayType arrayType,
			MatlabDataType dataType, boolean complex, boolean global,
			boolean logical, int[] dimensions, int elements) throws IOException {

		assert(dataType.size > 0);

		checkDimensions(dimensions, elements);

		int bytes = 0;

		/* size of array flags */
		bytes += MATLAB_TAG_SIZE + MATLAB_ARRAY_FLAGS_SIZE;

		/* size of dimensions */
		bytes += MATLAB_TAG_SIZE + roundToBoundary(dimensions.length * MatlabDataType.INT32.size);

		/* size of array name */
		bytes += MATLAB_TAG_SIZE + roundToBoundary(name.length());

		/* size of real data */
		bytes += MATLAB_TAG_SIZE + roundToBoundary(elements * dataType.size);

		/* size of imaginary data (optional) */
		if (complex) {
			bytes += MATLAB_TAG_SIZE + roundToBoundary(elements * dataType.size);
		}

		this.writeElementTag(MatlabDataType.MATRIX, bytes);
		this.writeArrayFlags(arrayType, complex, global, logical);
		this.writeDimensionsArray(dimensions);
		this.writeArrayName(name);

	}

	private void writeArrayFlags(MatlabArrayType type, boolean complex, boolean global, boolean logical) throws IOException {

		byte[] flags = new byte[MATLAB_ARRAY_FLAGS_SIZE];

		if (complex) {
			flags[MATLAB_ARRAY_FLAGS_INDEX] |= MATLAB_ARRAY_COMPLEX;
		}

		if (global) {
			flags[MATLAB_ARRAY_FLAGS_INDEX] |= MATLAB_ARRAY_GLOBAL;
		}

		if (logical) {
			flags[MATLAB_ARRAY_FLAGS_INDEX] |= MATLAB_ARRAY_LOGICAL;
		}

		flags[MATLAB_ARRAY_CLASS_INDEX] = type.value;

		this.writeElementTag(MatlabDataType.UINT32, MATLAB_ARRAY_FLAGS_SIZE);
		this.stream.write(flags);

	}

	private void writeDimensionsArray(int[] dimensions) throws IOException {
		this.write(dimensions);
	}

	private void writeArrayName(String name) throws IOException {
		this.writeElementTag(MatlabDataType.INT8, name.length());
		this.stream.writeBytes(name);
	}

	private void writeHeader() throws IOException {

		assert(this.stream.size() == 0);

		String description = String.format(HEADER_FORMAT_STRING, new Date().toString());
		this.stream.writeBytes(description);

		assert(this.stream.size() < MATLAB_DESCRIPTION_SIZE);
		this.writePaddingTo(MATLAB_DESCRIPTION_SIZE);

		this.stream.writeShort(MATLAB_FILE_VERSION);
		this.stream.writeShort(MATLAB_ENDIAN_INDICATOR);

		assert(this.stream.size() == MATLAB_HEADER_SIZE);

	}

	private void writePaddingTo(int position) throws IOException {
		this.writePadding(position - this.stream.size());
		assert(this.stream.size() == position);
	}

	private void writePadding(int amount) throws IOException {
		assert(amount >= 0);

		if (amount > 0) {
			this.stream.write(new byte[amount]);
		}
	}

	private void align() throws IOException {
		int n = this.stream.size() % MATLAB_BYTE_ALIGNMENT;
		if (n > 0) {
			this.writePadding(MATLAB_BYTE_ALIGNMENT - n);
		}
	}

	private final void writePrimitiveElementTag(MatlabDataType type, int elements) throws IOException {
		assert(type.size > 0);
		this.writeElementTag(type, elements * type.size);
	}

	private final void writeElementTag(MatlabDataType type, int bytes) throws IOException {
		this.align();
		this.stream.writeInt(type.value);
		this.stream.writeInt(bytes);
	}

	private final DataOutputStream stream;

	private static final String HEADER_FORMAT_STRING = "MATLAB 5.0 MAT-file, Platform: JAVA, Created on: %s, Created by: jMIST.";

	private static final int MATLAB_FILE_VERSION = 0x0100;
	private static final int MATLAB_ENDIAN_INDICATOR = 0x4D49;

	private static final int MATLAB_DESCRIPTION_SIZE = 124;
	private static final int MATLAB_HEADER_SIZE = 128;

	private static final int MATLAB_BYTE_ALIGNMENT = 8;
	private static final int MATLAB_TAG_SIZE = 8;

	private static enum MatlabDataType {

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

	};

	private static enum MatlabArrayType {

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

	private static final byte MATLAB_ARRAY_COMPLEX = 0x08;
	private static final byte MATLAB_ARRAY_GLOBAL = 0x04;
	private static final byte MATLAB_ARRAY_LOGICAL = 0x02;
	private static final int MATLAB_ARRAY_FLAGS_INDEX = 2;
	private static final int MATLAB_ARRAY_CLASS_INDEX = 3;
	private static final int MATLAB_ARRAY_FLAGS_SIZE = 8;

}
