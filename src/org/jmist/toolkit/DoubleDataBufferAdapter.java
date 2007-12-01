/**
 *
 */
package org.jmist.toolkit;

import java.awt.image.DataBuffer;
import java.nio.DoubleBuffer;

/**
 * A <code>DataBuffer</code> that adapts a <code>DoubleBuffer</code>.
 * @author bkimmel
 */
public final class DoubleDataBufferAdapter extends DataBuffer {

	/**
	 * Creates a new <code>DoubleDataBufferAdapter</code>.
	 * @param buffer The <code>DoubleBuffer</code> to adapt.
	 */
	public DoubleDataBufferAdapter(DoubleBuffer buffer) {
		super(DataBuffer.TYPE_DOUBLE, buffer.capacity());
		this.buffer = buffer;
	}

	/* (non-Javadoc)
	 * @see java.awt.image.DataBuffer#getElem(int, int)
	 */
	@Override
	public int getElem(int bank, int i) {
		return (int) getElemDouble(bank, i);
	}

	/* (non-Javadoc)
	 * @see java.awt.image.DataBuffer#setElem(int, int, int)
	 */
	@Override
	public void setElem(int bank, int i, int val) {
		setElemDouble(bank, i, (double) val);
	}

	/* (non-Javadoc)
	 * @see java.awt.image.DataBuffer#getElemDouble(int, int)
	 */
	@Override
	public double getElemDouble(int bank, int i) {
		return (bank == 0) ? getElemDouble(i) : 0.0;
	}

	/* (non-Javadoc)
	 * @see java.awt.image.DataBuffer#getElemDouble(int)
	 */
	@Override
	public double getElemDouble(int i) {
		return buffer.get(i);
	}

	/* (non-Javadoc)
	 * @see java.awt.image.DataBuffer#setElemDouble(int, double)
	 */
	@Override
	public void setElemDouble(int i, double val) {
		buffer.put(i, val);
	}

	/* (non-Javadoc)
	 * @see java.awt.image.DataBuffer#setElemDouble(int, int, double)
	 */
	@Override
	public void setElemDouble(int bank, int i, double val) {
		if (bank == 0) {
			setElemDouble(i, val);
		}
	}

	/** The <code>DoubleBuffer</code> to adapt. */
	private final DoubleBuffer buffer;

}
