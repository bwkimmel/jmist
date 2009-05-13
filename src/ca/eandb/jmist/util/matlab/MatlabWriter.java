/**
 *
 */
package ca.eandb.jmist.util.matlab;

import java.io.IOException;
import java.io.OutputStream;

import ca.eandb.jmist.math.Complex;

/**
 * Convenience class for writing array variables to a
 * <code>MatlabOutputStream</code>.
 * @author Brad Kimmel
 */
public final class MatlabWriter {

	/**
	 * Creates a new <code>MatlabWriter</code>.
	 * @param out The <code>OutputStream</code> to write to.
	 * @throws IOException if writing to <code>out</code> fails.
	 */
	public MatlabWriter(OutputStream out) throws IOException {
		this(new MatlabOutputStream(out));
	}

	/**
	 * Creates a new <code>MatlabWriter</code>.
	 * @param out The <code>MatlabOutputStream</code> to write to.
	 * @throws IOException if writing to <code>out</code> fails.
	 */
	public MatlabWriter(MatlabOutputStream out) throws IOException {
		this.out = out;
	}

	/**
	 * Flushes the underlying stream.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void flush() throws IOException {
		this.out.flush();
	}

	/**
	 * Closes the underlying stream.
	 * @throws IOException if closing the stream fails.
	 */
	public void close() throws IOException {
		this.out.close();
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, double[] pr, double[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, double[] pr, double[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.DOUBLE,
				MatlabDataType.DOUBLE, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr);
		if (pi != null) {
			this.out.writeElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, double[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, double[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, float[] pr, float[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, float[] pr, float[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.SINGLE,
				MatlabDataType.SINGLE, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr);
		if (pi != null) {
			this.out.writeElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, float[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, float[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, int[] pr, int[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, int[] pr, int[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.INT32,
				MatlabDataType.INT32, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr);
		if (pi != null) {
			this.out.writeElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, int[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, int[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, short[] pr, short[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, short[] pr, short[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.INT16,
				MatlabDataType.INT16, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr);
		if (pi != null) {
			this.out.writeElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, short[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, short[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, byte[] pr, byte[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, byte[] pr, byte[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.INT8,
				MatlabDataType.INT8, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr);
		if (pi != null) {
			this.out.writeElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, byte[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, byte[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions) throws IOException {
		this.writeUnsigned(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT32,
				MatlabDataType.UINT32, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeUnsignedElement(pr);
		if (pi != null) {
			this.out.writeUnsignedElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, int[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, dimensions, false);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, int[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions) throws IOException {
		this.writeUnsigned(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT16,
				MatlabDataType.UINT16, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeUnsignedElement(pr);
		if (pi != null) {
			this.out.writeUnsignedElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, short[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, dimensions, false);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, short[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions) throws IOException {
		this.writeUnsigned(name, pr, pi, dimensions, false);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT8,
				MatlabDataType.UINT8, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeUnsignedElement(pr);
		if (pi != null) {
			this.out.writeUnsignedElement(pi);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, byte[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, dimensions, false);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, byte[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>Complex</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, Complex[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes a <code>Complex</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, Complex[] array, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.DOUBLE,
				MatlabDataType.DOUBLE, true, global, false, dimensions,
				array.length);

		this.out.writeElement(array);
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>boolean</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, boolean[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	/**
	 * Writes a <code>boolean</code> array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, boolean[] array, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT8,
				MatlabDataType.INT8, false, global, true, dimensions,
				array.length);

		this.out.writeElement(array);
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a character array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param value The <code>String</code> to write.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, String value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a character array variable to the MAT-file.
	 * @param name The name of the array.
	 * @param value The <code>String</code> to write.
	 * @param global A value indicating if the array is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, String value, boolean global) throws IOException {

		int length = value.length();

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.CHAR,
				MatlabDataType.UINT16, false, global, false, new int[]{ 1, length },
				length);

		this.out.writeElement(value);
		this.out.endElement();
		this.out.endElement();

	}

	/** The underlying <code>MatlabOutputStream</code> to write to. */
	private final MatlabOutputStream out;

}
