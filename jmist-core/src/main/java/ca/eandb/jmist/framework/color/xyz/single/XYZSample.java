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
package ca.eandb.jmist.framework.color.xyz.single;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
public final class XYZSample implements SingleXYZColor {
  
  /** Serialization version ID. */
  private static final long serialVersionUID = 7597437809118584565L;

  private final double value;
  
  private final SingleXYZWavelengthPacket lambda;
  
  public XYZSample(double value, SingleXYZWavelengthPacket lambda) {
    this.value = value;
    this.lambda = lambda;
  }
  
  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.xyz.single.SingleXYZColor#toXYZColor()
   */
  @Override
  public XYZColor toXYZColor() {
    CIEXYZ c = ColorUtil.convertSample2XYZ(lambda.getWavelength(), value);
    return new XYZColor(c.X(), c.Y(), c.Z());
  }
  
  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.xyz.single.SingleXYZColor#asSample()
   */
  @Override
  public XYZSample asSample() {
    return this;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#getWavelengthPacket()
   */
  @Override
  public WavelengthPacket getWavelengthPacket() {
    return lambda;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#getColorModel()
   */
  @Override
  public ColorModel getColorModel() {
    return SingleXYZColorModel.getInstance();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#times(ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public Color times(Color other) {
    SingleXYZColor c = (SingleXYZColor) other;
    if (c.getWavelengthPacket() == lambda) {
      return new XYZSample(value * c.asSample().value, lambda);
    } else {
      return toXYZColor().times(c.toXYZColor());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#times(double)
   */
  @Override
  public Color times(double c) {
    return new XYZSample(value * c, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#divide(ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public Color divide(Color other) {
    SingleXYZColor c = (SingleXYZColor) other;
    if (c.getWavelengthPacket() == lambda) {
      return new XYZSample(value / c.asSample().value, lambda);
    } else {
      return toXYZColor().divide(c.toXYZColor());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#divide(double)
   */
  @Override
  public Color divide(double c) {
    return new XYZSample(value / c, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#plus(ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public Color plus(Color other) {
    SingleXYZColor c = (SingleXYZColor) other;
    if (c.getWavelengthPacket() == lambda) {
      return new XYZSample(value + c.asSample().value, lambda);
    } else {
      return toXYZColor().plus(c.toXYZColor());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#minus(ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public Color minus(Color other) {
    SingleXYZColor c = (SingleXYZColor) other;
    if (c.getWavelengthPacket() == lambda) {
      return new XYZSample(value - c.asSample().value, lambda);
    } else {
      return toXYZColor().minus(c.toXYZColor());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#sqrt()
   */
  @Override
  public Color sqrt() {
    return new XYZSample(Math.sqrt(value), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#exp()
   */
  @Override
  public Color exp() {
    return new XYZSample(Math.exp(value), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#invert()
   */
  @Override
  public Color invert() {
    return new XYZSample(1.0 / value, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#negative()
   */
  @Override
  public Color negative() {
    return new XYZSample(-value, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#abs()
   */
  @Override
  public Color abs() {
    return value >= 0 ? this : negative();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#pow(ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public Color pow(Color other) {
    SingleXYZColor c = (SingleXYZColor) other;
    if (c.getWavelengthPacket() == lambda) {
      return new XYZSample(Math.pow(value, c.asSample().value), lambda);
    } else {
      return toXYZColor().pow(c.toXYZColor());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#pow(double)
   */
  @Override
  public Color pow(double e) {
    return new XYZSample(Math.pow(value, e), lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#clamp(double)
   */
  @Override
  public Color clamp(double max) {
    return (value <= max) ? this : new XYZSample(max, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#clamp(double, double)
   */
  @Override
  public Color clamp(double min, double max) {
    if (value < min) {
      return new XYZSample(min, lambda);
    } else if (value > max) {
      return new XYZSample(max, lambda);
    } else {
      return this;
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#getValue(int)
   */
  @Override
  public double getValue(int channel) {
    return value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#disperse(int)
   */
  @Override
  public Color disperse(int channel) {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#luminance()
   */
  @Override
  public double luminance() {
    return ColorUtil.convertSample2Luminance(lambda.getWavelength(), value);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#toArray()
   */
  @Override
  public double[] toArray() {
    return new double[] { value, value, value };
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#toXYZ()
   */
  @Override
  public CIEXYZ toXYZ() {
    return ColorUtil.convertSample2XYZ(lambda.getWavelength(), value);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.Color#toRGB()
   */
  @Override
  public RGB toRGB() {
    return ColorUtil.convertSample2RGB(lambda.getWavelength(), value);
  }

}
