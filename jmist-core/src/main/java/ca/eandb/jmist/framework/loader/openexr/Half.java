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
package ca.eandb.jmist.framework.loader.openexr;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author brad
 *
 */
public final class Half extends Number implements Comparable<Half>, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -8133679410568154468L;

  public static final int MAX_EXPONENT = 15;

  public static final Half MAX_VALUE = new Half((short) 0x7bff);

  public static final int MIN_EXPONENT = -14;

  public static final Half MIN_NORMAL = new Half((short) 0x0400);

  public static final Half MIN_VALUE = new Half((short) 0x0001);

  public static final Half NaN = new Half((short) 0x7e00);

  public static final Half NEGATIVE_INFINITY = new Half((short) 0xfc00);

  public static final Half POSITIVE_INFINITY = new Half((short) 0x7c00);

  public static final Half ZERO = new Half((short) 0x0000);

  public static final Half NEGATIVE_ZERO = new Half((short) 0x8000);

  public static final Half ONE = new Half((short) 0x3c00);

  public static final Half NEGATIVE_ONE = new Half((short) 0xbc00);

  public static final int SIZE = 16;

  private static final short SIGN_MASK = (short) 0x8000;

  private static final short EXPONENT_MASK = (short) 0x7c00;

  private static final short FRACTION_MASK = (short) 0x03ff;

  private static final int EXPONENT_SHIFT = 10;

  private static final int EXPONENT_BIAS = -15;

  private final short bits;

  private Half(short bits) {
    this.bits = bits;
  }

  public Half() {
    this((short) 0);
  }

  public Half(double value) {
    this.bits = valueOf(value).bits;
  }

  public Half(float value) {
    this.bits = valueOf(value).bits;
  }

  public Half(String s) throws NumberFormatException {
    this.bits = parseHalf(s).bits;
  }

  public static Half parseHalf(String s) {
    return valueOf(Float.parseFloat(s));
  }

  public static int compare(Half h1, Half h2) {
    if (h1.isNaN() || h2.isNaN()) {
      return 0;
    }
    if ((h1.bits & ~SIGN_MASK) == 0 && (h2.bits & ~SIGN_MASK) == 0) {
      return 0;
    }
    if (h1.bits == h2.bits) {
      return 0;
    } else {
      int a = 0xffff & (h1.bits ^ SIGN_MASK);
      int b = 0xffff & (h2.bits ^ SIGN_MASK);
      return a < b ? -1 : 1;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Half other) {
    return compare(this, other);
  }

  public double doubleValue() {
    long exponentBits = ((long) (bits & EXPONENT_MASK)) >> EXPONENT_SHIFT;
    long fractionBits = bits & FRACTION_MASK;
    long signBit = bits & SIGN_MASK;
    if (exponentBits == 0) { // subnormal
      signBit <<= 48;
      if (fractionBits == 0) { // +/- zero
        return Double.longBitsToDouble(signBit);
      }

      exponentBits = 1009;
      while ((fractionBits & EXPONENT_MASK) == 0) {
        fractionBits <<= 1;
        exponentBits--;
      }
      exponentBits <<= 52;
      fractionBits &= FRACTION_MASK;
      fractionBits <<= 42;

      return Double.longBitsToDouble(signBit | exponentBits | fractionBits);
    } else if (exponentBits == 0x1f) { // infinity/nan
      if (fractionBits == 0) { // infinity
        return signBit == 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
      } else { // nan
        return Double.NaN;
      }
    } else { // normal
      signBit <<= 48;
      fractionBits <<= 42;
      exponentBits = (exponentBits + 1008) << 52;
      return Double.longBitsToDouble(signBit | exponentBits | fractionBits);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Half && equals((Half) obj);
  }

  public boolean equals(Half other) {
    if (isNaN() || other.isNaN()) {
      return false;
    }
    if ((bits & ~SIGN_MASK) == 0 && (other.bits & ~SIGN_MASK) == 0) {
      return true;
    }
    return bits == other.bits;
  }

  public float floatValue() {
    int exponentBits = (bits & EXPONENT_MASK) >> EXPONENT_SHIFT;
    int fractionBits = bits & FRACTION_MASK;
    int signBit = bits & SIGN_MASK;
    if (exponentBits == 0) { // subnormal
      signBit <<= 16;
      if (fractionBits == 0) { // +/- zero
        return Float.intBitsToFloat(signBit);
      }

      exponentBits = 113;
      while ((fractionBits & EXPONENT_MASK) == 0) {
        fractionBits <<= 1;
        exponentBits--;
      }
      exponentBits <<= 23;
      fractionBits &= FRACTION_MASK;
      fractionBits <<= 13;

      return Float.intBitsToFloat(signBit | exponentBits | fractionBits);
    } else if (exponentBits == 0x1f) { // infinity/nan
      if (fractionBits == 0) { // infinity
        return signBit == 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
      } else { // nan
        return Float.NaN;
      }
    } else { // normal
      signBit <<= 16;
      fractionBits <<= 13;
      exponentBits = (exponentBits + 112) << 23;
      return Float.intBitsToFloat(signBit | exponentBits | fractionBits);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return isNaN() ? super.hashCode() : (new Short(bits)).hashCode();
  }

  /* (non-Javadoc)
   * @see java.lang.Number#intValue()
   */
  @Override
  public int intValue() {
    int exponentBits = (bits & EXPONENT_MASK) >> EXPONENT_SHIFT;
    int fractionBits = (bits & FRACTION_MASK);
    int signBit = (bits & SIGN_MASK);
    if (exponentBits == 0) { // subnormal
      return 0;
    } else if (exponentBits == 0x1fL) { // infinity/nan
      if (fractionBits == 0) { // infinity
        return signBit == 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
      } else { // nan
        return 0;
      }
    } else { // normal
      int value = (((0x0400 | fractionBits) << 5) >> (30 - exponentBits));
      return signBit == 0 ? value : -value;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Number#shortValue()
   */
  @Override
  public short shortValue() {
    int value = intValue();
    if (value < Short.MIN_VALUE) {
      return Short.MIN_VALUE;
    } else if (value > Short.MAX_VALUE) {
      return Short.MAX_VALUE;
    } else {
      return (short) value;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Number#byteValue()
   */
  @Override
  public byte byteValue() {
    int value = intValue();
    if (value < Byte.MIN_VALUE) {
      return Byte.MIN_VALUE;
    } else if (value > Byte.MAX_VALUE) {
      return Byte.MAX_VALUE;
    } else {
      return (byte) value;
    }
  }

  public boolean isInfinite() {
    return (bits & EXPONENT_MASK) == EXPONENT_MASK && (bits & FRACTION_MASK) == 0;
  }

  public boolean isNaN() {
    return (bits & EXPONENT_MASK) == EXPONENT_MASK && (bits & FRACTION_MASK) != 0;
  }

  public long longValue() {
    long exponentBits = (bits & EXPONENT_MASK) >> EXPONENT_SHIFT;
    long fractionBits = (bits & FRACTION_MASK);
    long signBit = (bits & SIGN_MASK);
    if (exponentBits == 0) { // subnormal
      return 0L;
    } else if (exponentBits == 0x1fL) { // infinity/nan
      if (fractionBits == 0) { // infinity
        return signBit == 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
      } else { // nan
        return 0L;
      }
    } else { // normal
      long value = (((0x0400L | fractionBits) << 5) >> (30L - exponentBits));
      return signBit == 0 ? value : -value;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return Float.toString(floatValue());
  }

  public short toShortBits() {
    return bits;
  }

  public static Half fromShortBits(short bits) {
    return new Half(bits);
  }

  public static Half add(Half a, Half b) {
    return valueOf(a.floatValue() + b.floatValue());
  }

  public static Half sub(Half a, Half b) {
    return valueOf(a.floatValue() - b.floatValue());
  }

  public static Half mul(Half a, Half b) {
    return valueOf(a.floatValue() * b.floatValue());
  }

  public static Half div(Half a, Half b) {
    return valueOf(a.floatValue() / b.floatValue());
  }

  public static Half sqrt(Half a) {
    return valueOf(Math.sqrt(a.doubleValue()));
  }

  public static Half pow(Half a, Half b) {
    return valueOf(Math.pow(a.doubleValue(), b.doubleValue()));
  }

  public static Half abs(Half a) {
    return (a.bits & SIGN_MASK) != 0 ? new Half(a.bits & ~SIGN_MASK) : a;
  }

  public static Half negate(Half a) {
    return new Half(a.bits ^ SIGN_MASK);
  }

  public static Half exp(Half a) {
    return valueOf(Math.exp(a.doubleValue()));
  }

  public static Half floor(Half a) {
    return valueOf(Math.floor(a.doubleValue()));
  }

  public static Half ceil(Half a) {
    return valueOf(Math.ceil(a.doubleValue()));
  }

  public static Half cbrt(Half a) {
    return valueOf(Math.cbrt(a.doubleValue()));
  }

  public static Half acos(Half a) {
    return valueOf(Math.acos(a.doubleValue()));
  }

  public static Half asin(Half a) {
    return valueOf(Math.asin(a.doubleValue()));
  }

  public static Half atan(Half a) {
    return valueOf(Math.atan(a.doubleValue()));
  }

  public static Half atan2(Half y, Half x) {
    return valueOf(Math.atan2(y.doubleValue(), x.doubleValue()));
  }

  public static Half copySign(Half magnitude, Half sign) {
    return ((sign.bits ^ magnitude.bits) & SIGN_MASK) == 0 ? magnitude : negate(magnitude);
  }

  public static Half cos(Half a) {
    return valueOf(Math.cos(a.doubleValue()));
  }

  public static Half cosh(Half x) {
    return valueOf(Math.cosh(x.doubleValue()));
  }

  public static Half expm1(Half x) {
    return valueOf(Math.expm1(x.doubleValue()));
  }

  public static int getExponent(Half x) {
    return ((x.bits & EXPONENT_MASK) >> EXPONENT_SHIFT) + EXPONENT_BIAS;
  }

  public static Half hypot(Half x, Half y) {
    return valueOf(Math.hypot(x.doubleValue(), y.doubleValue()));
  }

  public static Half log(Half a) {
    return valueOf(Math.log(a.doubleValue()));
  }

  public static Half log10(Half a) {
    return valueOf(Math.log10(a.doubleValue()));
  }

  public static Half log1p(Half x) {
    return valueOf(Math.log1p(x.doubleValue()));
  }

  public static Half max(Half a, Half b) {
    return compare(a, b) >= 0 ? a : b;
  }

  public static Half min(Half a, Half b) {
    return compare(a, b) <= 0 ? a : b;
  }

  public static int round(Half a) {
    return Math.round(a.floatValue());
  }

  public static Half signum(Half a) {
    if ((a.bits & ~SIGN_MASK) == 0) {
      return Half.ZERO;
    } else if ((a.bits & SIGN_MASK) == 0) {
      return Half.ONE;
    } else {
      return Half.NEGATIVE_ONE;
    }
  }

  public static Half sin(Half a) {
    return valueOf(Math.sin(a.doubleValue()));
  }

  public static Half sinh(Half x) {
    return valueOf(Math.sinh(x.doubleValue()));
  }

  public static Half tan(Half a) {
    return valueOf(Math.tan(a.doubleValue()));
  }

  public static Half tanh(Half x) {
    return valueOf(Math.tanh(x.doubleValue()));
  }

  public static Half toDegrees(Half angrad) {
    return valueOf(angrad.doubleValue() * (180.0 / Math.PI));
  }

  public static Half toRadians(Half angdeg) {
    return valueOf(angdeg.doubleValue() * (Math.PI / 180.0));
  }

  public static Half valueOf(double a) {
    long bits = Double.doubleToLongBits(a);
    long fractionBits = (bits & 0x000fffffffffffffL);
    long exponentBits = (bits & 0x7ff0000000000000L) >> 52;
    long signBit = (bits & 0x8000000000000000L);
    if (exponentBits == 0L) { // subnormal
      return signBit == 0L ? Half.ZERO : Half.NEGATIVE_ZERO;
    } else if (exponentBits == 0x7ffL) { // infinity/nan
      if (fractionBits == 0L) { // infinity
        return signBit == 0L ? Half.POSITIVE_INFINITY : Half.NEGATIVE_INFINITY;
      } else { // nan
        return Half.NaN;
      }
    } else { // normal
      exponentBits -= 1023L;
      if (-14L <= exponentBits && exponentBits <= 15L) { // result normal
        exponentBits = (exponentBits + 15L) << EXPONENT_SHIFT;
        fractionBits >>= 42;
        signBit >>>= 48;
        return Half.fromShortBits((short) (signBit | exponentBits | fractionBits));
      } else if (exponentBits > 15L) { // result infinity
        return signBit == 0L ? Half.POSITIVE_INFINITY : Half.NEGATIVE_INFINITY;
      } else if (exponentBits >= -24L) { // result subnormal
        fractionBits = (0x0010000000000000L | fractionBits) >> (28 - exponentBits);
        signBit >>>= 48;
        return Half.fromShortBits((short) (signBit | fractionBits));
      } else { // result zero
        return Half.ZERO;
      }
    }
  }

  public static Half valueOf(float a) {
    int bits = Float.floatToIntBits(a);
    int fractionBits = (bits & 0x007fffff);
    int exponentBits = (bits & 0x7fc00000) >> 23;
    int signBit = (bits & 0x80000000);
    if (exponentBits == 0) { // subnormal
      return signBit == 0 ? Half.ZERO : Half.NEGATIVE_ZERO;
    } else if (exponentBits == 0xff) { // infinity/nan
      if (fractionBits == 0) { // infinity
        return signBit == 0 ? Half.POSITIVE_INFINITY : Half.NEGATIVE_INFINITY;
      } else { // nan
        return Half.NaN;
      }
    } else { // normal
      exponentBits -= 127;
      if (-14 <= exponentBits && exponentBits <= 15) { // result normal
        exponentBits = (exponentBits + 15) << EXPONENT_SHIFT;
        fractionBits >>= 13;
        signBit >>>= 16;
        return Half.fromShortBits((short) (signBit | exponentBits | fractionBits));
      } else if (exponentBits > 15) { // result infinity
        return signBit == 0 ? Half.POSITIVE_INFINITY : Half.NEGATIVE_INFINITY;
      } else if (exponentBits >= -24) { // result subnormal
        fractionBits = (0x00800000 | fractionBits) >> -(1 + exponentBits);
        signBit >>>= 16;
        return Half.fromShortBits((short) (signBit | fractionBits));
      } else { // result zero
        return Half.ZERO;
      }
    }
  }

  public static void main(String[] args) {
    byte[] x = new byte[2];
    ByteBuffer buf = ByteBuffer.wrap(x);
    buf.order(ByteOrder.LITTLE_ENDIAN);
    buf.asShortBuffer().put(Half.valueOf(3.283691e-2f).toShortBits());
    System.out.printf("%02x %02x", x[0], x[1]);
    System.out.println();


    System.out.println(Half.valueOf(3.283691e-2f).floatValue());
    System.out.println(Half.valueOf(1.0f).floatValue());
    System.out.println(Half.valueOf(-1.0f).floatValue());
    System.out.println(Half.valueOf(-2.0f).floatValue());
    System.out.println(Half.valueOf(-1e20f).floatValue());
    System.out.println(Half.valueOf(1e20f).floatValue());
    System.out.println(Half.valueOf(Float.POSITIVE_INFINITY).floatValue());
    System.out.println(Half.valueOf(Float.NEGATIVE_INFINITY).floatValue());
    System.out.println(Half.valueOf(1.000001f).floatValue());
    { float f = (float) Math.pow(2, -5); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -8); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -13); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -14); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -15); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -16); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -20); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -24); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    { float f = (float) Math.pow(2, -25); System.out.printf("%e, %e", f, Half.valueOf(f).floatValue()); System.out.println(); }
    System.out.println(Half.valueOf(0.0).doubleValue());
    System.out.println(Half.valueOf(1.0).doubleValue());
    System.out.println(Half.valueOf(-1.0).doubleValue());
    System.out.println(Half.valueOf(-2.0).doubleValue());
    System.out.println(Half.valueOf(-1e20).doubleValue());
    System.out.println(Half.valueOf(1e20).doubleValue());
    System.out.println(Half.valueOf(Double.POSITIVE_INFINITY).doubleValue());
    System.out.println(Half.valueOf(Double.NEGATIVE_INFINITY).doubleValue());
    System.out.println(Half.valueOf(1.000001).doubleValue());
    { double f = Math.pow(2, -5); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -8); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -13); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -14); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -15); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -16); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -20); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -24); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
    { double f = Math.pow(2, -25); System.out.printf("%e, %e", f, Half.valueOf(f).doubleValue()); System.out.println(); }
//        3c00   = 1
//        c000   = -2
//
//        7bff   = 6.5504 x 104  (max half precision)
//
//        0400   = 2-14 ~ 6.10352 x 10-5 (minimum positive normal)
//
//        0001   = 2-24 ~ 5.96046 x 10-8 (minimum strictly positive subnormal)
//
//        0000   = 0
//        8000   = -0
//
//        7c00   = infinity
//        fc00   = -infinity
//
//        3555   ~ 0.33325... ~ 1/3

    System.out.println(Half.fromShortBits((short) 0x3c00).floatValue());
    System.out.println(Half.fromShortBits((short) 0xc000).floatValue());
    System.out.println(Half.fromShortBits((short) 0x7bff).floatValue());
    System.out.println(Half.fromShortBits((short) 0x0400).floatValue());
    System.out.println(Half.fromShortBits((short) 0x0001).floatValue());
    System.out.println(Half.fromShortBits((short) 0x0000).floatValue());
    System.out.println(Half.fromShortBits((short) 0x8000).floatValue());
    System.out.println(Half.fromShortBits((short) 0x7c00).floatValue());
    System.out.println(Half.fromShortBits((short) 0xfc00).floatValue());
    System.out.println(Half.fromShortBits((short) 0x3555).floatValue());
    System.out.println(Half.fromShortBits((short) 0x3c00).doubleValue());
    System.out.println(Half.fromShortBits((short) 0xc000).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x7bff).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x0400).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x0001).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x0000).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x8000).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x7c00).doubleValue());
    System.out.println(Half.fromShortBits((short) 0xfc00).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x3555).doubleValue());
    System.out.println(Half.fromShortBits((short) 0x3c00).longValue());
    System.out.println(Half.fromShortBits((short) 0xc000).longValue());
    System.out.println(Half.fromShortBits((short) 0x7bff).longValue());
    System.out.println(Half.fromShortBits((short) 0x0400).longValue());
    System.out.println(Half.fromShortBits((short) 0x0001).longValue());
    System.out.println(Half.fromShortBits((short) 0x0000).longValue());
    System.out.println(Half.fromShortBits((short) 0x8000).longValue());
    System.out.println(Half.fromShortBits((short) 0x7c00).longValue());
    System.out.println(Half.fromShortBits((short) 0xfc00).longValue());
    System.out.println(Half.fromShortBits((short) 0x3555).longValue());
    System.out.println(Half.fromShortBits((short) 0x3c00).intValue());
    System.out.println(Half.fromShortBits((short) 0xc000).intValue());
    System.out.println(Half.fromShortBits((short) 0x7bff).intValue());
    System.out.println(Half.fromShortBits((short) 0x0400).intValue());
    System.out.println(Half.fromShortBits((short) 0x0001).intValue());
    System.out.println(Half.fromShortBits((short) 0x0000).intValue());
    System.out.println(Half.fromShortBits((short) 0x8000).intValue());
    System.out.println(Half.fromShortBits((short) 0x7c00).intValue());
    System.out.println(Half.fromShortBits((short) 0xfc00).intValue());
    System.out.println(Half.fromShortBits((short) 0x3555).intValue());
    System.out.println(Half.fromShortBits((short) 0x3c00).shortValue());
    System.out.println(Half.fromShortBits((short) 0xc000).shortValue());
    System.out.println(Half.fromShortBits((short) 0x7bff).shortValue());
    System.out.println(Half.fromShortBits((short) 0x0400).shortValue());
    System.out.println(Half.fromShortBits((short) 0x0001).shortValue());
    System.out.println(Half.fromShortBits((short) 0x0000).shortValue());
    System.out.println(Half.fromShortBits((short) 0x8000).shortValue());
    System.out.println(Half.fromShortBits((short) 0x7c00).shortValue());
    System.out.println(Half.fromShortBits((short) 0xfc00).shortValue());
    System.out.println(Half.fromShortBits((short) 0x3555).shortValue());
    System.out.println(Half.fromShortBits((short) 0x3c00).byteValue());
    System.out.println(Half.fromShortBits((short) 0xc000).byteValue());
    System.out.println(Half.fromShortBits((short) 0x7bff).byteValue());
    System.out.println(Half.fromShortBits((short) 0x0400).byteValue());
    System.out.println(Half.fromShortBits((short) 0x0001).byteValue());
    System.out.println(Half.fromShortBits((short) 0x0000).byteValue());
    System.out.println(Half.fromShortBits((short) 0x8000).byteValue());
    System.out.println(Half.fromShortBits((short) 0x7c00).byteValue());
    System.out.println(Half.fromShortBits((short) 0xfc00).byteValue());
    System.out.println(Half.fromShortBits((short) 0x3555).byteValue());

  }

}
