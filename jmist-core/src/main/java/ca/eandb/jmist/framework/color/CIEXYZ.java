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

import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Tuple3;

/**
 * CIE XYZ Tristimulus values.
 * @see <a href="http://en.wikipedia.org/wiki/CIE_1931_color_space">CIE 1931 color space</a>
 * @author Brad Kimmel
 */
public class CIEXYZ extends Tuple3 implements Spectrum {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3369371416389668782L;

  public static final CIEXYZ ZERO = new CIEXYZ(0.0, 0.0, 0.0);
  public static final CIEXYZ A = new CIEXYZ(1.09850, 1.00000, 0.35585);
  public static final CIEXYZ B = new CIEXYZ(0.99072, 1.00000, 0.85223);
  public static final CIEXYZ C = new CIEXYZ(0.98074, 1.00000, 1.18232);
  public static final CIEXYZ D50 = new CIEXYZ(0.96422, 1.00000, 0.82521);
  public static final CIEXYZ D55 = new CIEXYZ(0.95682, 1.00000, 0.92149);
  public static final CIEXYZ D65 = new CIEXYZ(0.95047, 1.00000, 1.08883);
  public static final CIEXYZ D75 = new CIEXYZ(0.94972, 1.00000, 1.22638);
  public static final CIEXYZ E = new CIEXYZ(1.00000, 1.00000, 1.00000);
  public static final CIEXYZ F2 = new CIEXYZ(0.99186, 1.00000, 0.67393);
  public static final CIEXYZ F7 = new CIEXYZ(0.95041, 1.00000, 1.08747);
  public static final CIEXYZ F11 = new CIEXYZ(1.00962, 1.00000, 0.64350);

  public CIEXYZ(double X, double Y, double Z) {
    super(X, Y, Z);
  }

  public final double X() {
    return x;
  }

  public final double Y() {
    return y;
  }

  public final double Z() {
    return z;
  }

  public final CIEXYZ abs() {
    return new CIEXYZ(Math.abs(x), Math.abs(y), Math.abs(z));
  }

  public final CIEXYZ plus(CIEXYZ other) {
    return new CIEXYZ(x + other.x, y + other.y, z + other.z);
  }

  public final CIEXYZ minus(CIEXYZ other) {
    return new CIEXYZ(x - other.x, y - other.y, z - other.z);
  }

  public final CIEXYZ divide(CIEXYZ other) {
    return new CIEXYZ(x / other.x, y / other.y, z / other.z);
  }

  public final CIEXYZ divide(double c) {
    return new CIEXYZ(x / c, y / c, z / c);
  }

  public final CIEXYZ times(CIEXYZ other) {
    return new CIEXYZ(x * other.x, y * other.y, z * other.z);
  }

  public final CIEXYZ times(double c) {
    return new CIEXYZ(x * c, y * c, z * c);
  }

  public final CIEXYZ clamp(double max) {
    return clamp(0.0, max);
  }

  public final CIEXYZ clamp(double min, double max) {
    return new CIEXYZ(
        MathUtil.clamp(x, min, max),
        MathUtil.clamp(y, min, max),
        MathUtil.clamp(z, min, max));
  }

  public final CIEXYZ adapt(CIEXYZ fromRef, CIEXYZ toRef) {
    return CIELab.fromXYZ(this, fromRef).toXYZ(toRef);
  }

  public final double luminance() {
    return ColorUtil.convertXYZ2Luminance(this);
  }

  public final int toXYZE() {
    double v = (x > y && x > z) ? x : (y > z ? y : z);
    if (v < 1e-32) {
      return 0;
    }
    long bits = Double.doubleToRawLongBits(v);
    int e = (int) (((bits & 0x7ff0000000000000L)) >>> 52L) - 0x3fe;
    v = Double.longBitsToDouble((bits & 0x800fffffffffffffL) | 0x3fe0000000000000L) * 256.0 / v;

    return
      (MathUtil.clamp((int) Math.floor(x * v), 0, 255) << 24) |
      (MathUtil.clamp((int) Math.floor(y * v), 0, 255) << 16) |
      (MathUtil.clamp((int) Math.floor(z * v), 0, 255) <<  8) |
      MathUtil.clamp(e + 128, 0, 255);
  }

  public static CIEXYZ fromXYZE(int xyze) {
    int e = (xyze & 0x000000ff);
    if (e > 0) {
      int x = (xyze & 0xff000000) >>> 24;
      int y = (xyze & 0x00ff0000) >>> 16;
      int z = (xyze & 0x0000ff00) >>>  8;
      long bits = ((long) (e - (128 + 8) + 0x3ff)) << 52;
      double f = Double.longBitsToDouble(bits);
      return new CIEXYZ(x * f, y * f, z * f);
    } else {
      return CIEXYZ.ZERO;
    }
  }

  public final RGB toRGB() {
    return ColorUtil.convertXYZ2RGB(this);
  }

  public static final CIEXYZ fromRGB(double r, double g, double b) {
    return ColorUtil.convertRGB2XYZ(r, g, b);
  }

  public static final CIEXYZ fromRGB(RGB rgb) {
    return ColorUtil.convertRGB2XYZ(rgb);
  }

  @Override
  public Color sample(WavelengthPacket lambda) {
    return lambda.getColorModel().fromXYZ(this).sample(lambda);
  }

}
