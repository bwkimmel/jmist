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
package ca.eandb.jmist.math;

import java.io.Serializable;

/**
 * A complex number.
 * This class is immutable.
 * @author Brad Kimmel
 */
public final class Complex implements Serializable {

  /**
   * Initializes a number on the real line.
   * @param re The real part.
   */
  public Complex(double re) {
    this.re = re;
    this.im = 0.0;
  }

  /**
   * Initializes the complex number from its components.
   * @param re The real part.
   * @param im The imaginary part.
   */
  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
  }

  /**
   * Creates a complex number, r*e^(I*theta), from its polar form.
   * @param theta The angle from the positive real axis (in radians).
   * @param r The distance from the origin.
   * @return The complex number r*e^(I*theta).
   */
  public static Complex polar(double theta, double r) {
    return new Complex(r * Math.cos(theta), r * Math.sin(theta));
  }

  /**
   * Creates an array of <code>Complex</code> numbers with the specified real
   * and imaginary parts.
   * @param re The real parts.
   * @param im The imaginary parts (must be the same length as
   *     <code>re</code>.
   * @return An array of the <code>Complex</code> numbers
   *     <code>(re[i], im[i])</code>, for each <code>i</code>.
   * @throws IllegalArgumentException if <code>re.length != im.length</code>.
   */
  public static Complex[] array(double[] re, double[] im) {

    if (re.length != im.length) {
      throw new IllegalArgumentException("re.length != im.length");
    }

    Complex[] result = new Complex[re.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = new Complex(re[i], im[i]);
    }

    return result;

  }

  /**
   * Gets the real part of the complex number.
   * @return The real part.
   */
  public double re() {
    return re;
  }

  /**
   * Gets the imaginary part of the complex number.
   * @return The imaginary part.
   */
  public double im() {
    return im;
  }

  /**
   * Gets the modulus of the complex number.
   * @return The modulus of the complex number.
   */
  public double abs() {
    return Math.hypot(re, im);
  }

  /**
   * Gets the argument of the complex number.
   * @return The argument of the complex number.
   */
  public double arg() {
    return Math.atan2(im, re);
  }

  /**
   * Adds a real number to this complex number.
   * @param x The real number to add.
   * @return The sum of this complex number and x.
   */
  public Complex plus(double x) {
    return new Complex(re + x, im);
  }

  /**
   * Adds two complex numbers.
   * @param z The complex number to add.
   * @return The sum of this complex number and z.
   */
  public Complex plus(Complex z) {
    return new Complex(re + z.re, im + z.im);
  }

  /**
   * Finds the difference between a complex number
   * and a real number.
   * @param x The real number to subtract.
   * @return The difference between this complex number
   *         and x.
   */
  public Complex minus(double x) {
    return new Complex(re - x, im);
  }

  /**
   * Finds the difference between two complex numbers.
   * @param z The complex number to subtract.
   * @return The difference between this complex number
   *         and z.
   */
  public Complex minus(Complex z) {
    return new Complex(re - z.re, im - z.im);
  }

  /**
   * Multiples a complex number and a real number.
   * @param x The real number by which to multiply.
   * @return The product of this complex number and x.
   */
  public Complex times(double x) {
    return new Complex(re * x, im * x);
  }

  /**
   * Multiples two complex numbers.
   * @param z The complex number by which to multiply.
   * @return The product of this complex number and z.
   */
  public Complex times(Complex z) {
    return new Complex(re * z.re - im * z.im, re * z.im + im * z.re);
  }

  /**
   * Divides a complex number by a real number.
   * @param x The real number by which to divide (the denominator).
   * @return The quotient of this complex number and x.
   */
  public Complex divide(double x) {
    return new Complex(re / x, im / x);
  }

  /**
   * Divides a complex number by another complex number.
   * @param z The complex number by which to divide (the denominator).
   * @return The quotient of this complex number and z.
   */
  public Complex divide(Complex z) {
    double c = z.re * z.re + z.im * z.im;
    return new Complex((re * z.re + im * z.im) / c, (im * z.re - re * z.im) / c);
  }

  /**
   * Computes the reciprocal of this complex number.
   * @return The reciprocal of this complex number.
   */
  public Complex reciprocal() {
    double c = re * re + im * im;
    return new Complex(re / c, -im / c);
  }

  /**
   * The additive inverse of this complex number.
   * @return The additive inverse of this complex number.
   */
  public Complex negative() {
    return new Complex(-re, -im);
  }

  /**
   * Computes the complex conjugate of this number (a - b*I),
   * where this = (a + b*I).
   * @return The complex conjugate of this number.
   */
  public Complex conjugate() {
    return new Complex(re, -im);
  }

  /**
   * Computes the square root of a complex number.
   * @return The square root.
   */
  public Complex sqrt() {
    return polar(this.arg() / 2.0, Math.sqrt(this.abs()));
  }

  /**
   * Computes the square root of a real number.
   * @param x The value of which to compute the square root.
   * @return The square root.
   */
  public static Complex sqrt(double x) {
    return x >= 0.0 ? new Complex(Math.sqrt(x))
            : new Complex(0.0, Math.sqrt(-x));
  }

  /**
   * Computes the cube root of a complex number.
   * @return The cube root.
   */
  public Complex cbrt() {
    return polar(this.arg() / 3.0, Math.cbrt(this.abs()));
  }

  /**
   * Computes e^z, where z is this complex number.
   * @return Euclids number, E, raised to the power of this
   *         complex number.
   */
  public Complex exp() {
    return polar(Math.exp(re), im);
  }

  /**
   * Raises this complex number to a real exponent.
   * @param exponent The exponent to raise this complex number to.
   * @return This complex number raised to the given exponent.
   */
  public Complex pow(double exponent) {
    return polar(this.arg() * exponent, Math.pow(this.abs(), exponent));
  }

  /**
   * Raises this complex number to a complex exponent.
   * @param exponent The exponent to raise this complex number to.
   * @return The complex number raised to the given exponent.
   */
  public Complex pow(Complex exponent) {
    double theta  = this.arg();
    double r    = this.abs();

    return polar(
        Math.pow(r, exponent.re) * Math.exp(-exponent.im * theta),
        exponent.re * theta + exponent.im * Math.log(r)
    );
  }

  /**
   * Computes the sin of this complex number.
   * @return sin(z), where z is this complex number.
   */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    /**
     * Computes the cosine of this complex number.
     * @return cos(z), where z is this complex number.
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    /**
     * Computes the tangent of this complex number.
     * @return tan(z), where z is this complex number.
     */
    public Complex tan() {
        return sin().divide(cos());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
  public String toString() {
      return Double.toString(re) + " + " + Double.toString(im) + "*I";
    }

  /**
   * The complex number 0 + 0i.
   */
  public static final Complex ZERO = new Complex(0.0);

  /**
   * The real unit (1).
   */
  public static final Complex ONE = new Complex(1.0, 0.0);

  /**
   * The imaginary unit, sqrt(-1).
   */
  public static final Complex I = new Complex(0.0, 1.0);

  /** The real part of this complex number. */
  private final double re;

  /** The imaginary part of this complex number. */
  private final double im;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -5588907469158256485L;

}
