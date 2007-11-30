/**
 *
 */
package org.jmist.toolkit.matlab;
import java.io.IOException;
import java.io.OutputStream;

import org.jmist.toolkit.Complex;

/**
 * @author bkimmel
 *
 */
public final class MatlabWriter {

	/**
	 * @param out
	 * @throws IOException
	 */
	public MatlabWriter(OutputStream out) throws IOException {
		this.out = new MatlabOutputStream(out);
	}

	public void flush() throws IOException {
		this.out.flush();
	}

	public void close() throws IOException {
		this.out.close();
	}

	public void write(String name, double[] pr, double[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

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

	public void write(String name, double[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, double[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	public void write(String name, float[] pr, float[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

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

	public void write(String name, float[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, float[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	public void write(String name, int[] pr, int[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

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

	public void write(String name, int[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, int[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	public void write(String name, short[] pr, short[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

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

	public void write(String name, short[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, short[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	public void write(String name, byte[] pr, byte[] pi, int[] dimensions) throws IOException {
		this.write(name, pr, pi, dimensions, false);
	}

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

	public void write(String name, byte[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, byte[] array, int[] dimensions, boolean global) throws IOException {
		this.write(name, array, null, dimensions, global);
	}

	public void writeUnsigned(String name, int[] pr, int[] pi, int[] dimensions) throws IOException {
		this.writeUnsigned(name, pr, pi, dimensions, false);
	}

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

	public void writeUnsigned(String name, int[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, dimensions, false);
	}

	public void writeUnsigned(String name, int[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	public void writeUnsigned(String name, short[] pr, short[] pi, int[] dimensions) throws IOException {
		this.writeUnsigned(name, pr, pi, dimensions, false);
	}

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

	public void writeUnsigned(String name, short[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, dimensions, false);
	}

	public void writeUnsigned(String name, short[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	public void writeUnsigned(String name, byte[] pr, byte[] pi, int[] dimensions) throws IOException {
		this.writeUnsigned(name, pr, pi, dimensions, false);
	}

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

	public void writeUnsigned(String name, byte[] array, int[] dimensions) throws IOException {
		this.writeUnsigned(name, array, dimensions, false);
	}

	public void writeUnsigned(String name, byte[] array, int[] dimensions, boolean global) throws IOException {
		this.writeUnsigned(name, array, null, dimensions, global);
	}

	public void write(String name, Complex[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, Complex[] array, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.DOUBLE,
				MatlabDataType.DOUBLE, true, global, false, dimensions,
				array.length);

		this.out.writeElement(array);
		this.out.endElement();
		this.out.endElement();

	}

	public void write(String name, boolean[] array, int[] dimensions) throws IOException {
		this.write(name, array, dimensions, false);
	}

	public void write(String name, boolean[] array, int[] dimensions, boolean global) throws IOException {

		this.out.beginElement(MatlabDataType.COMPRESSED);
		this.out.beginArrayElement(name, MatlabArrayType.UINT8,
				MatlabDataType.INT8, false, global, true, dimensions,
				array.length);

		this.out.writeElement(array);
		this.out.endElement();
		this.out.endElement();

	}

	public void write(String name, String value) throws IOException {
		this.write(name, value, false);
	}

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

	private final MatlabOutputStream out;

}
