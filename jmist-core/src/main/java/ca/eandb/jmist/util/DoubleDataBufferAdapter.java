/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.util;

import java.awt.image.DataBuffer;
import java.nio.DoubleBuffer;

/**
 * A <code>DataBuffer</code> that adapts a <code>DoubleBuffer</code>.
 * @author Brad Kimmel
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

  @Override
  public int getElem(int bank, int i) {
    return (int) getElemDouble(bank, i);
  }

  @Override
  public void setElem(int bank, int i, int val) {
    setElemDouble(bank, i, (double) val);
  }

  @Override
  public double getElemDouble(int bank, int i) {
    return (bank == 0) ? getElemDouble(i) : 0.0;
  }

  @Override
  public double getElemDouble(int i) {
    return buffer.get(i);
  }

  @Override
  public void setElemDouble(int i, double val) {
    buffer.put(i, val);
  }

  @Override
  public void setElemDouble(int bank, int i, double val) {
    if (bank == 0) {
      setElemDouble(i, val);
    }
  }

  /** The <code>DoubleBuffer</code> to adapt. */
  private final DoubleBuffer buffer;

}
