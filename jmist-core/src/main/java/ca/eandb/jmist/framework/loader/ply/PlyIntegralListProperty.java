/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.loader.ply;

import java.util.List;

/**
 * A <code>PlyProperty</code> consisting of a list of integral values.
 */
final class PlyIntegralListProperty extends PlyListProperty {

  /** The list of values. */
  private final List<Integer> values;

  /**
   * Creates a new <code>PlyIntegralListProperty</code>.
   * @param values The list of values.
   * @param descriptor The <code>PropertyDescriptor</code> describing this
   *     property.
   * @throws IllegalArgumentException if <code>descriptor</code> describes a
   *     singleton (non-list) or floating point (non-integral) property.
   */
  public PlyIntegralListProperty(List<Integer> values, PropertyDescriptor descriptor) {
    super(descriptor);

    if (!descriptor.getDataType().isIntegral()) {
      throw new IllegalArgumentException("Integral data type required");
    }
    this.values = values;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getNormalizedDoubleValue(int)
   */
  @Override
  public double getNormalizedDoubleValue(int index) {
    return values.get(index).doubleValue() / getPropertyDescriptor().getDataType().getMaxValue();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getDoubleValue(int)
   */
  @Override
  public double getDoubleValue(int index) {
    return values.get(index).doubleValue();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getIntValue(int)
   */
  @Override
  public int getIntValue(int index) {
    return values.get(index);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getCount()
   */
  @Override
  public int getCount() {
    return values.size();
  }

}
