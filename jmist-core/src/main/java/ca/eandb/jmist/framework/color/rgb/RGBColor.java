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
package ca.eandb.jmist.framework.color.rgb;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;


/**
 * @author Brad
 *
 */
/* package */ final class RGBColor implements Color, Spectrum {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -1599907078097936372L;

  public static final RGBColor BLACK = new RGBColor(0, 0, 0);

  public static final RGBColor WHITE = new RGBColor(1, 1, 1);

  private static final WavelengthPacket WAVELENGTH_PACKET = new WavelengthPacket() {
    public ColorModel getColorModel() {
      return RGBColorModel.getInstance();
    }
  };

  private final double r;

  private final double g;

  private final double b;

  /**
   * @param r
   * @param g
   * @param b
   */
  public RGBColor(double r, double g, double b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public CIEXYZ toXYZ() {
    return ColorUtil.convertRGB2XYZ(r, g, b);
  }

  @Override
  public RGB toRGB() {
    return new RGB(r, g, b);
  }

  @Override
  public Color abs() {
    return new RGBColor(Math.abs(r), Math.abs(g), Math.abs(b));
  }

  @Override
  public Color divide(Color other) {
    return divide((RGBColor) other);
  }

  public RGBColor divide(RGBColor other) {
    return new RGBColor(r / other.r, g / other.g, b / other.b);
  }

  @Override
  public Color divide(double c) {
    return new RGBColor(r / c, g / c, b / c);
  }

  @Override
  public Color exp() {
    return new RGBColor(Math.exp(r), Math.exp(g), Math.exp(b));
  }

  @Override
  public Color invert() {
    return new RGBColor(1.0 / r, 1.0 / g, 1.0 / b);
  }

  @Override
  public double luminance() {
    return ColorUtil.convertRGB2Luminance(r, g, b);
  }

  @Override
  public Color minus(Color other) {
    return minus((RGBColor) other);
  }

  public RGBColor minus(RGBColor other) {
    return new RGBColor(r - other.r, g - other.g, b - other.b);
  }

  @Override
  public Color negative() {
    return new RGBColor(-r, -g, -b);
  }

  @Override
  public Color plus(Color other) {
    return plus((RGBColor) other);
  }

  public RGBColor plus(RGBColor other) {
    return new RGBColor(r + other.r, g + other.g, b + other.b);
  }

  @Override
  public Color pow(Color other) {
    return pow((RGBColor) other);
  }

  public RGBColor pow(RGBColor other) {
    return new RGBColor(Math.pow(r, other.r), Math.pow(g, other.g), Math.pow(b, other.b));
  }

  @Override
  public Color pow(double e) {
    return new RGBColor(Math.pow(r, e), Math.pow(g, e), Math.pow(b, e));
  }

  @Override
  public Color sqrt() {
    return new RGBColor(Math.sqrt(r), Math.sqrt(g), Math.sqrt(b));
  }

  @Override
  public Color times(Color other) {
    return times((RGBColor) other);
  }

  public RGBColor times(RGBColor other) {
    return new RGBColor(r * other.r, g * other.g, b * other.b);
  }

  @Override
  public Color times(double c) {
    return new RGBColor(r * c, g * c, b * c);
  }

  @Override
  public Color clamp(double min, double max) {
    return new RGBColor(
        MathUtil.clamp(r, min, max),
        MathUtil.clamp(g, min, max),
        MathUtil.clamp(b, min, max));
  }

  @Override
  public Color clamp(double max) {
    return new RGBColor(Math.min(r, max), Math.min(g, max), Math.min(b, max));
  }

  @Override
  public Color disperse(int channel) {
    switch (channel) {
    case 0:
      return new RGBColor(r, 0, 0);
    case 1:
      return new RGBColor(0, g, 0);
    case 2:
      return new RGBColor(0, 0, b);
    default:
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public ColorModel getColorModel() {
    return RGBColorModel.getInstance();
  }

  @Override
  public double getValue(int channel) {
    switch (channel) {
    case 0:
      return r;
    case 1:
      return g;
    case 2:
      return b;
    default:
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public double[] toArray() {
    return new double[]{ r, g, b };
  }

  @Override
  public WavelengthPacket getWavelengthPacket() {
    return WAVELENGTH_PACKET;
  }

  @Override
  public Color sample(WavelengthPacket lambda) {
    return this;
  }

}
