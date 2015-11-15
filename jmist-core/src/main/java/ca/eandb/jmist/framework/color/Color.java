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
package ca.eandb.jmist.framework.color;

import java.io.Serializable;



/**
 * An immutable sample from a <code>Spectrum</code>, which supports various
 * mathematical operations.
 * @author Brad Kimmel
 */
public interface Color extends Serializable {

  /**
   * The <code>WavelengthPacket</code> used to generate the sample from a
   * <code>Spectrum</code> (may be <code>null</code>).
   * @return The <code>WavelengthPacket</code> used to generate the sample from
   *     a <code>Spectrum</code> (may be <code>null</code>).
   */
  WavelengthPacket getWavelengthPacket();

  /**
   * The <code>ColorModel</code> that this <code>Color</code> came from.
   * @return The <code>ColorModel</code> that this <code>Color</code> came from.
   */
  ColorModel getColorModel();

  /**
   * Multiplies this color by another.
   * @param other The <code>Color</code> to multiply by.
   * @return The product of <code>this</code> and <code>other</code>.
   */
  Color times(Color other);

  /**
   * Multiplies this color by a constant.
   * @param c The factor to multiply by.
   * @return A <code>Color</code> equal to this <code>Color</code> times
   *     <code>c</code>.
   */
  Color times(double c);

  /**
   * Divides this color by another.
   * @param other The <code>Color</code> to divide by.
   * @return The quotient of <code>this</code> and <code>other</code>.
   */
  Color divide(Color other);

  /**
   * Divides this color by a constant.
   * @param c The factor to divide by.
   * @return A <code>Color</code> equal to this <code>Color</code> divided by
   *     <code>c</code>.
   */
  Color divide(double c);

  /**
   * Adds this color to another.
   * @param other The <code>Color</code> to add.
   * @return The sum of <code>this</code> and <code>other</code>.
   */
  Color plus(Color other);

  /**
   * Subtracts another color from this color.
   * @param other The <code>Color</code> to subtract.
   * @return The value of <code>this</code> minus <code>other</code>.
   */
  Color minus(Color other);

  /**
   * A color of the same quality whose luminance is the square root of the
   * luminance of this color.
   * @return A <code>Color</code> of the same quality whose luminance is the
   *     square root of the luminance of this color.
   */
  Color sqrt();

  /**
   * A color of the same quality whose luminance is <em>e</em> raised to the
   * luminance of this color.
   * @return A <code>Color</code> of the same quality whose luminance is
   *     <em>e</em> raised to the luminance of this color.
   */
  Color exp();

  /**
   * A color whose luminance is <code>1 - x</code>, where <code>x</code> is
   * the luminance of this color.
   * @return A <code>Color</code> whose luminance is <code>1 - x</code>, where
   *     <code>x</code> is the luminance of this color.
   */
  Color invert();

  /**
   * A color whose luminance the negation of the luminance of this color.
   * @return A <code>Color</code> whose luminance the negation of the luminance
   *     of this color.
   */
  Color negative();

  /**
   * A color whose luminance is the absolute value of the luminance of this
   * color.
   * @return A <code>Color</code> whose luminance is the absolute value of the
   *     luminance of this color.
   */
  Color abs();

  /**
   * Raises this color to the power of another, componentwise.
   * @param other The <code>Color</code> to raise this color to.
   * @return A <code>Color</code> representing this color raised to the power of
   *     <code>other</code>, componentwise.
   */
  Color pow(Color other);

  /**
   * A color whose luminance is that of this color raised to a constant.
   * @param e The exponent.
   * @return A <code>Color</code> whose luminance is that of this color raised
   *     to a constant.
   */
  Color pow(double e);

  /**
   * A color whose components are those of this color, bounded above by the
   * specified value.
   * @param max The maximum component value.
   * @return A <code>Color</code> whose components do not exceed
   *     <code>max</code>.
   */
  Color clamp(double max);

  /**
   * A color whose components are those of this color, constrained to fall
   * within the given range.
   * @param min The minimum component value.
   * @param max The maximum component value.
   * @return A <code>Color</code> whose components satisfy
   *     <code>min &lt;= x &lt;= max</code>.
   */
  Color clamp(double min, double max);

  /**
   * Gets the value of a component of this color.
   * @param channel The index of the component to get.
   * @return The component value.
   */
  double getValue(int channel);

  /**
   * A color for which all components but one are non-zero.  The value of the
   * non-zero channel is equal to the value of the corresponding channel of
   * this color.
   * @param channel The index of the channel to keep.
   * @return A <code>Color</code> with only one non-zero component.
   */
  Color disperse(int channel);

  /**
   * The intensity of this color.
   * @return The intensity of this color.
   */
  double luminance();

  /**
   * The components of this <code>Color</code>.  The returned array shall not
   * be the backing array (if there is one) for this <code>Color</code> (i.e.,
   * modifying the contents of the array will not affect this
   * <code>Color</code>).
   * @return The components of this <code>Color</code>.
   */
  double[] toArray();

  /**
   * The CIE XYZ tristimulus values indicating how this color appears to a
   * human observer.
   * @return The <code>CIEXYZ</code> tristimulus values indicating how this
   *     color appears to a human observer.
   */
  CIEXYZ toXYZ();

  /**
   * An RGB representation of this color.
   * @return An <code>RGB</code> representation of this color.
   */
  RGB toRGB();

}
