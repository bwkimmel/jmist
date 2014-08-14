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

/**
 * A <code>PlyProperty</code> representing a list of values.
 */
abstract class PlyListProperty implements PlyProperty {

  /** The <code>PropertyDescriptor</code> describing this property. */
  private final PropertyDescriptor descriptor;

  /**
   * Initializes this <code>PlyListProperty</code>.
   * @param descriptor The <code>PropertyDescriptor</code> describing this
   *     property.
   * @throws IllegalArgumentException If <code>descriptor</code> describes a
   *     singleton (non-list) property.
   */
  public PlyListProperty(PropertyDescriptor descriptor) {
    if (!descriptor.isList()) {
      throw new IllegalArgumentException("Property type must be a list");
    }
    this.descriptor = descriptor;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getPropertyDescriptor()
   */
  @Override
  public final PropertyDescriptor getPropertyDescriptor() {
    return descriptor;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getNormalizedDoubleValue()
   */
  @Override
  public double getNormalizedDoubleValue() {
    return getNormalizedDoubleValue(0);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getDoubleValue()
   */
  @Override
  public double getDoubleValue() {
    return getDoubleValue(0);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.PlyProperty#getIntValue()
   */
  @Override
  public int getIntValue() {
    return getIntValue(0);
  }

}
