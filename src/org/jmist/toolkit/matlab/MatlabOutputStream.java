/**
 *
 */
package org.jmist.toolkit.matlab;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Stack;
import java.util.zip.Deflater;

import org.jmist.toolkit.Complex;

/**
 * @author bkimmel
 *
 */
public final class MatlabOutputStream extends OutputStream implements DataOutput {

	public MatlabOutputStream(OutputStream out) throws IOException {
		this.streams = new Stack<DataOutputStream>();
		this.streams.push(new DataOutputStream(out));
		this.writeHeader();
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutputStream#write(byte[], int, int)
	 */
	@Override
	public synchronized void write(byte[] b, int off, int len)
			throws IOException {
		this.stream().write(b, off, len);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutputStream#write(int)
	 */
	@Override
	public synchronized void write(int b) throws IOException {
		this.stream().write(b);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterOutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		this.stream().write(b);
	}

	public void beginElement(MatlabDataType type) throws IOException {
		this.push(type);
	}

	private void push(MatlabDataType type) throws IOException {
		this.streams.push(CompoundElementOutputStream.create(type));
	}

	public void beginElement(MatlabDataType type, int bytes) throws IOException {
		this.push(type, bytes);
	}

	private void push(MatlabDataType type, int bytes) throws IOException {
		this.streams.push(CompoundElementOutputStream.create(type, bytes, this.stream()));
	}

	public void endElement() throws IOException {
		CompoundElementOutputStream top = this.pop();
		top.writeTo(this.stream());
	}

	private CompoundElementOutputStream pop() {
		if (this.streams.size() > 1) {
			return (CompoundElementOutputStream) this.streams.pop();
		} else {
			throw new IllegalStateException("matlab element underflow");
		}
	}

	public void beginArrayElement(String name, MatlabArrayType arrayType,
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

		this.beginElement(MatlabDataType.MATRIX, bytes);
		this.writeArrayFlagsElement(arrayType, complex, global, logical);
		this.writeArrayDimensionsElement(dimensions);
		this.writeArrayNameElement(name);

	}

	private static int roundToBoundary(int position) {
		int n = position % MATLAB_BYTE_ALIGNMENT;
		return n > 0 ? position + (MATLAB_BYTE_ALIGNMENT - n) : position;
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

	public void writeElement(boolean[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT8, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeBoolean(array[i]);
		}
	}

	public void writeElement(double[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.DOUBLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeDouble(array[i]);
		}
	}

	public void writeElement(float[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.SINGLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeFloat(array[i]);
		}
	}

	public void writeElement(byte[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT8, array.length);
		this.stream().write(array);
	}

	public void writeElement(short[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT16, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeShort(array[i]);
		}
	}

	public void writeElement(int[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT32, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeInt(array[i]);
		}
	}

	public void writeElement(long[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.INT64, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeLong(array[i]);
		}
	}

	public void writeUnsignedElement(byte[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT8, array.length);
		this.stream().write(array);
	}

	public void writeUnsignedElement(short[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT16, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeShort(array[i]);
		}
	}

	public void writeUnsignedElement(int[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT32, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeInt(array[i]);
		}
	}

	public void writeUnsignedElement(long[] array) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT64, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeLong(array[i]);
		}
	}

	public void writeElement(String s) throws IOException {
		this.writePrimitiveElementTag(MatlabDataType.UINT16, s.length());
		this.stream().writeChars(s);
	}

	public void writeElement(Complex[] array) throws IOException {

		this.writePrimitiveElementTag(MatlabDataType.DOUBLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeDouble(array[i].re());
		}

		this.writePrimitiveElementTag(MatlabDataType.DOUBLE, array.length);
		for (int i = 0; i < array.length; i++) {
			this.stream().writeDouble(array[i].im());
		}

	}

	public void writeArrayFlagsElement(MatlabArrayType type, boolean complex, boolean global, boolean logical) throws IOException {

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
		this.stream().write(flags);

	}

	public void writeArrayDimensionsElement(int[] dimensions) throws IOException {
		this.writeElement(dimensions);
	}

	public void writeArrayNameElement(String name) throws IOException {
		this.writeElementTag(MatlabDataType.INT8, name.length());
		this.stream().writeBytes(name);
	}

	private final void writePrimitiveElementTag(MatlabDataType type, int elements) throws IOException {
		writePrimitiveElementTagTo(this.stream(), type, elements);
	}

	private static final void writePrimitiveElementTagTo(DataOutputStream out, MatlabDataType type, int elements) throws IOException {
		assert(type.size > 0);
		writeElementTagTo(out, type, elements * type.size);
	}

	private final void writeElementTag(MatlabDataType type, int bytes) throws IOException {
		writeElementTagTo(this.stream(), type, bytes);
	}

	private static final void writeElementTagTo(DataOutputStream out, MatlabDataType type, int bytes) throws IOException {
		if (type != MatlabDataType.COMPRESSED) {
			align(out);
		}
		out.writeInt(type.value);
		out.writeInt(bytes);
	}

	private static void writePaddingTo(DataOutputStream out, int amount) throws IOException {
		assert(amount >= 0);

		if (amount > 0) {
			out.write(new byte[amount]);
		}
	}

	private void writePaddingTo(int position) throws IOException {
		this.writePadding(position - this.stream().size());
		assert(this.stream().size() == position);
	}

	private void writePadding(int amount) throws IOException {
		assert(amount >= 0);

		if (amount > 0) {
			this.stream().write(new byte[amount]);
		}
	}

	private static void align(DataOutputStream out) throws IOException {
		int n = out.size() % MATLAB_BYTE_ALIGNMENT;
		if (n > 0) {
			writePaddingTo(out, MATLAB_BYTE_ALIGNMENT - n);
		}
	}

	private void writeHeader() throws IOException {

		assert(this.stream().size() == 0);

		String description = String.format(HEADER_FORMAT_STRING, new Date().toString());
		this.stream().writeBytes(description);

		assert(this.stream().size() < MATLAB_DESCRIPTION_SIZE);
		this.writePaddingTo(MATLAB_DESCRIPTION_SIZE);

		this.stream().writeShort(MATLAB_FILE_VERSION);
		this.stream().writeShort(MATLAB_ENDIAN_INDICATOR);

		assert(this.stream().size() == MATLAB_HEADER_SIZE);

	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeBoolean(boolean)
	 */
	@Override
	public void writeBoolean(boolean arg0) throws IOException {
		this.stream().writeBoolean(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeByte(int)
	 */
	@Override
	public void writeByte(int arg0) throws IOException {
		this.stream().writeByte(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeBytes(java.lang.String)
	 */
	@Override
	public void writeBytes(String arg0) throws IOException {
		this.stream().writeBytes(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeChar(int)
	 */
	@Override
	public void writeChar(int arg0) throws IOException {
		this.stream().writeChar(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeChars(java.lang.String)
	 */
	@Override
	public void writeChars(String arg0) throws IOException {
		this.stream().writeChars(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeDouble(double)
	 */
	@Override
	public void writeDouble(double arg0) throws IOException {
		this.stream().writeDouble(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeFloat(float)
	 */
	@Override
	public void writeFloat(float arg0) throws IOException {
		this.stream().writeFloat(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeInt(int)
	 */
	@Override
	public void writeInt(int arg0) throws IOException {
		this.stream().writeInt(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeLong(long)
	 */
	@Override
	public void writeLong(long arg0) throws IOException {
		this.stream().writeLong(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeShort(int)
	 */
	@Override
	public void writeShort(int arg0) throws IOException {
		this.stream().writeShort(arg0);
	}

	/* (non-Javadoc)
	 * @see java.io.DataOutput#writeUTF(java.lang.String)
	 */
	@Override
	public void writeUTF(String arg0) throws IOException {
		this.stream().writeUTF(arg0);
	}

	public int size() {
		int total = 0;
		for (DataOutputStream out : this.streams) {
			total += out.size();
		}
		return total;
	}

	private DataOutputStream stream() {
		return this.streams.peek();
	}

	private static final int MATLAB_BYTE_ALIGNMENT = 8;

	private static final String HEADER_FORMAT_STRING = "MATLAB 5.0 MAT-file, Platform: JAVA, Created on: %s, Created by: jMIST.";

	private static final int MATLAB_FILE_VERSION = 0x0100;
	private static final int MATLAB_ENDIAN_INDICATOR = 0x4D49;

	private static final int MATLAB_DESCRIPTION_SIZE = 124;
	private static final int MATLAB_HEADER_SIZE = 128;

	private static abstract class CompoundElementOutputStream
		extends DataOutputStream {

		protected CompoundElementOutputStream(OutputStream out) {
			super(out);
		}

		public static CompoundElementOutputStream create(MatlabDataType type) throws IOException {
			if (type == MatlabDataType.COMPRESSED) {
				return new CompressedCompoundElementOutputStream(new ByteArrayOutputStream());
			} else {
				return new BufferedCompoundElementOutputStream(type, new ByteArrayOutputStream());
			}
		}

		public static CompoundElementOutputStream create(MatlabDataType type, int bytes, DataOutputStream out) throws IOException {
			return new FixedLengthCompoundElementOutputStream(out, type, bytes);
		}

		public abstract void writeTo(DataOutputStream out) throws IOException;

	}

	private static final class BufferedCompoundElementOutputStream
		extends CompoundElementOutputStream {

		public BufferedCompoundElementOutputStream(MatlabDataType type, ByteArrayOutputStream bytes) throws IOException {
			super(bytes);
			this.type = type;
			this.bytes = bytes;
		}

		public void writeTo(DataOutputStream out) throws IOException {

			/* From "MATLAB 7 MAT-File Format", Page 1-10:
			 *
			 * "For data elements representing MATLAB arrays, (type miMATRIX),
			 * the value of the Number of Bytes field includes padding bytes in
			 * the total.  For all other MAT-file data types, the value of the
			 * Number of Bytes field does not include padding bytes."
			 */
			if (type == MatlabDataType.MATRIX) {
				align(this);
			}

			writeElementTagTo(out, this.type, bytes.size());
			this.bytes.writeTo(out);
			out.flush();

		}

		private final MatlabDataType type;
		private final ByteArrayOutputStream bytes;

	}

	private static final class CompressedCompoundElementOutputStream
		extends CompoundElementOutputStream {

		public CompressedCompoundElementOutputStream(ByteArrayOutputStream bytes) throws IOException {
			super(bytes);
			this.bytes = bytes;
			//this.gzip = (GZIPOutputStream) super.out;
		}

		public void writeTo(DataOutputStream out) throws IOException {

			this.flush();
			//this.gzip.finish();
			//this.gzip.flush();
			Deflater def = new Deflater();
			def.setInput(bytes.toByteArray());
			bytes.reset();
			def.finish();
			byte[] buffer = new byte[1024];
			while (!def.finished()) {
				int length = def.deflate(buffer, 0, buffer.length);
				if (length > 0) {
					bytes.write(buffer, 0, length);
				}
			}

			writeElementTagTo(out, MatlabDataType.COMPRESSED, bytes.size());
			bytes.writeTo(out);

			out.flush();

		}

		private final ByteArrayOutputStream bytes;
		//private final GZIPOutputStream gzip;

	}

	public static final class FixedLengthCompoundElementOutputStream
		extends CompoundElementOutputStream {

		public FixedLengthCompoundElementOutputStream(DataOutputStream out, MatlabDataType type, int bytes) throws IOException {
			super(out);
			writeElementTagTo(out, type, bytes);
			this.type = type;
			this.endPosition = out.size() + bytes;
		}

		@Override
		public void writeTo(DataOutputStream out) throws IOException {

			/* From "MATLAB 7 MAT-File Format", Page 1-10:
			 *
			 * "For data elements representing MATLAB arrays, (type miMATRIX),
			 * the value of the Number of Bytes field includes padding bytes in
			 * the total.  For all other MAT-file data types, the value of the
			 * Number of Bytes field does not include padding bytes."
			 */
			if (this.type == MatlabDataType.MATRIX) {
				align(out);
			}

			out.flush();

			if (out.size() != endPosition) {
				throw new IllegalStateException(String.format("incorrect array length, current position is %d (should be %d).", this.size(), this.endPosition));
			}

		}

		private final MatlabDataType type;
		private final int endPosition;

	}

	private static final byte MATLAB_ARRAY_COMPLEX = 0x08;
	private static final byte MATLAB_ARRAY_GLOBAL = 0x04;
	private static final byte MATLAB_ARRAY_LOGICAL = 0x02;
	private static final int MATLAB_ARRAY_FLAGS_INDEX = 2;
	private static final int MATLAB_ARRAY_CLASS_INDEX = 3;
	private static final int MATLAB_ARRAY_FLAGS_SIZE = 8;
	private static final int MATLAB_TAG_SIZE = 8;

	private final Stack<DataOutputStream> streams;

}
