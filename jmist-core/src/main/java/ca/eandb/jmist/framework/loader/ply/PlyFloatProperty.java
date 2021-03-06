/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
 * A <code>PlyProperty</code> consisting of a single floating point value.
 */
final class PlyFloatProperty extends PlySingletonProperty {

  /** The value of this property. */
  private final double value;

  /**
   * Creates a new <code>PlyFloatProperty</code>.
   * @param value The value of the property.
   * @param descriptor The <code>PropertyDescriptor</code> describing this
   *     property.
   * @throws IllegalArgumentException if <code>descriptor</code> describes a
   *     list or integral (not-float) property.
   */
  public PlyFloatProperty(double value, PropertyDescriptor descriptor) {
    super(descriptor);

    if (!descriptor.getDataType().isFloatingPoint()) {
      throw new IllegalArgumentException("Floating point data type required");
    }
    this.value = value;
  }

  @Override
  public double getNormalizedDoubleValue() {
    return value;
  }

  @Override
  public double getDoubleValue() {
    return value;
  }

  @Override
  public int getIntValue() {
    return (int) value;
  }

}
