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
package ca.eandb.jmist.framework.color.luminance;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
/* package */ final class LuminanceColor implements Color, Spectrum {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7982563437549789288L;

  public static final LuminanceColor BLACK = new LuminanceColor(0.0);

  public static final LuminanceColor WHITE = new LuminanceColor(1.0);

  private final double value;

  private final LuminanceWavelengthPacket lambda;

  public LuminanceColor(double value) {
    this(value, null);
  }

  public LuminanceColor(double value, LuminanceWavelengthPacket lambda) {
    this.value = value;
    this.lambda = lambda;
  }

  public LuminanceColor(double value, double wavelength) {
    this(value, new LuminanceWavelengthPacket(wavelength));
  }

  private LuminanceColor create(double value, Color compat) {
    return new LuminanceColor(value,
        (lambda == compat.getWavelengthPacket()) ? lambda : null);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#abs()
   */
  public Color abs() {
    return new LuminanceColor(Math.abs(value), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#clamp(double)
   */
  public Color clamp(double max) {
    return new LuminanceColor(Math.min(value, max), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#clamp(double, double)
   */
  public Color clamp(double min, double max) {
    return new LuminanceColor(MathUtil.clamp(value, min, max), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#disperse(int)
   */
  public Color disperse(int channel) {
    return this;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
   */
  public Color divide(Color other) {
    return divide((LuminanceColor) other);
  }

  public LuminanceColor divide(LuminanceColor other) {
    return create(value / other.value, other);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#divide(double)
   */
  public Color divide(double c) {
    return new LuminanceColor(value / c, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#exp()
   */
  public Color exp() {
    return new LuminanceColor(Math.exp(value), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#getColorModel()
   */
  public ColorModel getColorModel() {
    return LuminanceColorModel.getInstance();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#getValue(int)
   */
  public double getValue(int channel) {
    if (channel != 0) {
      throw new IndexOutOfBoundsException();
    }
    return value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#getWavelengthPacket()
   */
  public WavelengthPacket getWavelengthPacket() {
    return lambda;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#invert()
   */
  public Color invert() {
    return new LuminanceColor(1.0 / value, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#luminance()
   */
  public double luminance() {
    return value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
   */
  public Color minus(Color other) {
    return minus((LuminanceColor) other);
  }

  public LuminanceColor minus(LuminanceColor other) {
    return create(value - other.value, other);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#negative()
   */
  public Color negative() {
    return new LuminanceColor(-value, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
   */
  public Color plus(Color other) {
    return plus((LuminanceColor) other);
  }

  public LuminanceColor plus(LuminanceColor other) {
    return create(value + other.value, other);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
   */
  public Color pow(Color other) {
    return pow((LuminanceColor) other);
  }

  public LuminanceColor pow(LuminanceColor other) {
    return create(Math.pow(value, other.value), other);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#pow(double)
   */
  public Color pow(double e) {
    return new LuminanceColor(Math.pow(value, e), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#sqrt()
   */
  public Color sqrt() {
    return new LuminanceColor(Math.sqrt(value), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
   */
  public Color times(Color other) {
    return times((LuminanceColor) other);
  }

  public LuminanceColor times(LuminanceColor other) {
    return create(value * other.value, other);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#times(double)
   */
  public Color times(double c) {
    return new LuminanceColor(value * c, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#toArray()
   */
  public double[] toArray() {
    return new double[]{ value };
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#toRGB()
   */
  public RGB toRGB() {
    return new RGB(value, value, value);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#toXYZ()
   */
  public CIEXYZ toXYZ() {
    return new CIEXYZ(value, value, value);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Spectrum#sample(ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  public Color sample(WavelengthPacket lambda) {
    return sample((LuminanceWavelengthPacket) lambda);
  }

  public LuminanceColor sample(LuminanceWavelengthPacket lambda) {
    return new LuminanceColor(value, lambda);
  }

}
