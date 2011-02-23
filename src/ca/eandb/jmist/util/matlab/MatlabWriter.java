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
	 * Writes a one-dimensional array of <code>double</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void write(String name, double[] pr) throws IOException {
		write(name, pr, new int[]{ pr.length, 1 });
	}
	
	/**
	 * Writes a one-dimensional array of <code>float</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void write(String name, float[] pr) throws IOException {
		write(name, pr, new int[]{ pr.length, 1 });		
	}
	
	/**
	 * Writes a one-dimensional array of <code>boolean</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void write(String name, boolean[] pr) throws IOException {
		write(name, pr, new int[]{ pr.length, 1 });				
	}
	
	/**
	 * Writes a one-dimensional array of <code>int</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void write(String name, int[] pr) throws IOException {
		write(name, pr, new int[]{ pr.length, 1 });
	}
	
	/**
	 * Writes a one-dimensional array of <code>short</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void write(String name, short[] pr) throws IOException {
		write(name, pr, new int[]{ pr.length, 1 });
	}
	
	/**
	 * Writes a one-dimensional array of <code>byte</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void write(String name, byte[] pr) throws IOException {
		write(name, pr, new int[]{ pr.length, 1 });
	}
	
	/**
	 * Writes a one-dimensional array of <code>int</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, int[] pr) throws IOException {
		writeUnsigned(name, pr, new int[]{ pr.length, 1 });
	}
	
	/**
	 * Writes a one-dimensional array of <code>short</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, short[] pr) throws IOException {
		writeUnsigned(name, pr, new int[]{ pr.length, 1 });
	}
	
	/**
	 * Writes a one-dimensional array of <code>byte</code>s to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The array of elements to write.
	 * @throws IOException If writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, byte[] pr) throws IOException {
		writeUnsigned(name, pr, new int[]{ pr.length, 1 });
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>double</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, double pr, double pi) throws IOException {
		this.write(name, pr, pi, false);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, double[] pr, double[] pi, int[] dimensions, boolean global) throws IOException {
		write(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, double[] pr, double[] pi, int[] dimensions, int[] strides) throws IOException {
		write(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, double[] pr, double[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.DOUBLE,
				MatlabDataType.DOUBLE, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>double</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary parts of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, double pr, double pi, boolean global) throws IOException {
		this.write(name, new double[]{ pr }, new double[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>double</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, double value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a <code>double</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, double[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>double</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, double value, boolean global) throws IOException {
		this.write(name, new double[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>float</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, float pr, float pi) throws IOException {
		this.write(name, pr, pi, false);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, float[] pr, float[] pi, int[] dimensions, boolean global) throws IOException {
		write(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, float[] pr, float[] pi, int[] dimensions, int[] strides) throws IOException {
		write(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, float[] pr, float[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.SINGLE,
				MatlabDataType.SINGLE, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>float</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, float pr, float pi, boolean global) throws IOException {
		this.write(name, new float[]{ pr }, new float[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>float</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, float value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a <code>float</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, float[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>float</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, float value, boolean global) throws IOException {
		this.write(name, new float[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, int pr, int pi) throws IOException {
		this.write(name, pr, pi, false);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, int[] pr, int[] pi, int[] dimensions, boolean global) throws IOException {
		write(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, int[] pr, int[] pi, int[] dimensions, int[] strides) throws IOException {
		write(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, int[] pr, int[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.INT32,
				MatlabDataType.INT32, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, int pr, int pi, boolean global) throws IOException {
		this.write(name, new int[]{ pr }, new int[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, int value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes an <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, int[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes an <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, int value, boolean global) throws IOException {
		this.write(name, new int[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, short pr, short pi) throws IOException {
		this.write(name, pr, pi, false);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, short[] pr, short[] pi, int[] dimensions, boolean global) throws IOException {
		write(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, short[] pr, short[] pi, int[] dimensions, int[] strides) throws IOException {
		write(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, short[] pr, short[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.INT16,
				MatlabDataType.INT16, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, short pr, short pi, boolean global) throws IOException {
		this.write(name, new short[]{ pr }, new short[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, short value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, short[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, short value, boolean global) throws IOException {
		this.write(name, new short[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, byte pr, byte pi) throws IOException {
		this.write(name, pr, pi, false);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, byte[] pr, byte[] pi, int[] dimensions, boolean global) throws IOException {
		write(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, byte[] pr, byte[] pi, int[] dimensions, int[] strides) throws IOException {
		write(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void write(String name, byte[] pr, byte[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.INT8,
				MatlabDataType.INT8, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, byte pr, byte pi, boolean global) throws IOException {
		this.write(name, new byte[]{ pr }, new byte[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, byte value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, byte[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	/**
	 * Writes a <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, byte value, boolean global) throws IOException {
		this.write(name, new byte[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an unsigned <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, int pr, int pi) throws IOException {
		this.writeUnsigned(name, pr, pi, false);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions, boolean global) throws IOException {
		writeUnsigned(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions, int[] strides) throws IOException {
		writeUnsigned(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT32,
				MatlabDataType.UINT32, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeUnsignedElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeUnsignedElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an unsigned <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, int pr, int pi, boolean global) throws IOException {
		this.writeUnsigned(name, new int[]{ pr }, new int[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an unsigned <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, int value) throws IOException {
		this.writeUnsigned(name, value, false);
	}

	/**
	 * Writes an unsigned <code>int</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, int[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	/**
	 * Writes an unsigned <code>int</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, int value, boolean global) throws IOException {
		this.writeUnsigned(name, new int[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an unsigned <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, short pr, short pi) throws IOException {
		this.writeUnsigned(name, pr, pi, false);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions, boolean global) throws IOException {
		writeUnsigned(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions, int[] strides) throws IOException {
		writeUnsigned(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT16,
				MatlabDataType.UINT16, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeUnsignedElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeUnsignedElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an unsigned <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, short pr, short pi, boolean global) throws IOException {
		this.writeUnsigned(name, new short[]{ pr }, new short[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an unsigned <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, short value) throws IOException {
		this.writeUnsigned(name, value, false);
	}

	/**
	 * Writes an unsigned <code>short</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, short[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	/**
	 * Writes an unsigned <code>short</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, short value, boolean global) throws IOException {
		this.writeUnsigned(name, new short[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an unsigned <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, byte pr, byte pi) throws IOException {
		this.writeUnsigned(name, pr, pi, false);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions, boolean global) throws IOException {
		writeUnsigned(name, pr, pi, dimensions, null, global);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions, int[] strides) throws IOException {
		writeUnsigned(name, pr, pi, dimensions, strides, false);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real parts of the array elements.
	 * @param pi The imaginary parts of the array elements (optional, must
	 * 		be the same length as <code>pr</code> if provided).
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>pr.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if <code>pi != null</code> and
	 * 		<code>pi.length != pr.length</code>.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>pr.length</code>.
	 */
	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT8,
				MatlabDataType.UINT8, pi != null, global, false, dimensions,
				pr.length);

		this.out.writeUnsignedElement(pr, dimensions, strides);
		if (pi != null) {
			this.out.writeUnsignedElement(pi, dimensions, strides);
		}
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes an unsigned <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param pr The real part of the value.
	 * @param pi The imaginary part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, byte pr, byte pi, boolean global) throws IOException {
		this.writeUnsigned(name, new byte[]{ pr }, new byte[]{ pi }, SINGLETON, global);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes an unsigned <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, byte value) throws IOException {
		this.writeUnsigned(name, value, false);
	}

	/**
	 * Writes an unsigned <code>byte</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The real parts of the array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void writeUnsigned(String name, byte[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	/**
	 * Writes an unsigned <code>byte</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The real part of the value.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void writeUnsigned(String name, byte value, boolean global) throws IOException {
		this.writeUnsigned(name, new byte[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes a <code>Complex</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>Complex</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The value to write.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, Complex value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a <code>Complex</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
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
	 * Writes a <code>Complex</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The value to write.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, Complex value, boolean global) throws IOException {
		this.write(name, value.re(), value.im(), global);
	}

	/**
	 * Writes a <code>boolean</code> array variable to the MAT-file.
	 * @param name The name of the variable.
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
	 * Writes a <code>boolean</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The value to write.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, boolean value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a <code>boolean</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, boolean[] array, int[] dimensions, boolean global) throws IOException {
		write(name, array, dimensions, null, global);
	}

	/**
	 * Writes a <code>boolean</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, boolean[] array, int[] dimensions, int[] strides) throws IOException {
		write(name, array, dimensions, strides, false);
	}

	/**
	 * Writes a <code>boolean</code> array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param array The array elements.
	 * @param dimensions The array dimensions (there must be at least two
	 * 		dimensions and the product of the dimensions must be
	 * 		<code>array.length</code>).
	 * @param strides The differences between indices into <code>pr</code> of
	 * 		consecutive entries along each dimension (must be equal in length
	 * 		to <code>dimensions</code>).
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 * @throws IllegalArgumentException if the product of
	 * 		<code>dimensions</code> is not equal to <code>array.length</code>.
	 */
	public void write(String name, boolean[] array, int[] dimensions, int[] strides, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT8,
				MatlabDataType.INT8, false, global, true, dimensions,
				array.length);

		this.out.writeElement(array, dimensions, strides);
		this.out.endElement();
		this.out.endElement();

	}

	/**
	 * Writes a <code>boolean</code> variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The value to write.
	 * @param global A value indicating if the variable is to have global scope.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, boolean value, boolean global) throws IOException {
		this.write(name, new boolean[]{ value }, SINGLETON, global);
	}

	/**
	 * Writes a character array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The <code>String</code> to write.
	 * @throws IOException if writing to the underlying stream fails.
	 */
	public void write(String name, String value) throws IOException {
		this.write(name, value, false);
	}

	/**
	 * Writes a character array variable to the MAT-file.
	 * @param name The name of the variable.
	 * @param value The <code>String</code> to write.
	 * @param global A value indicating if the variable is to have global scope.
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

	/** The dimensions for a single element array. */
	private static final int[] SINGLETON = { 1, 1 };

}
