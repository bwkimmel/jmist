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
 * Represents a polynomial.
 * @author Brad Kimmel
 */
public final class Polynomial implements Serializable {

  /**
   * Initializes the polynomial with the specified coefficients, provided in
   * ascending order by exponent.
   * @param coeff The coefficients of the polynomial in ascending order by
   *     exponent.
   */
  public Polynomial(double... coeff) {

    for (int exponent = coeff.length - 1; exponent >= 0; exponent--) {
      this.initializeCoefficient(exponent, coeff[exponent]);
    }

    this.initializeZero();
    this.checkInvariant();

  }

  /**
   * Gets the coefficient for the specified exponent.
   * @param exponent The exponent for which to get the associated exponent
   *     (must be non-negative).
   * @return The coefficient for the specified exponent.
   */
  public double coefficient(int exponent) {
    return exponent < this.coeff.length ? this.coeff[exponent] : 0.0;
  }

  /**
   * Gets the degree of this polynomial (the highest exponent with a non-zero
   * coefficient).
   */
  public int degree() {
    this.checkInvariant();
    return this.coeff.length - 1;
  }

  /**
   * Evaluates this <code>Polynomial</code> at the specified domain value.
   * @param x The domain value at which to evaluate this
   *     <code>Polynomial</code>.
   * @return The value of this <code>Polynomial</code> at <code>x</code>.
   */
  public double at(double x) {

    double value = 0.0;
    double power = 1.0;

    for (int i = 0; i < this.coeff.length; i++) {
      value += this.coeff[i] * power;
      power *= x;
    }

    return value;

  }

  /**
   * Evaluates this <code>Polynomial</code> at the specified domain value.
   * @param z The domain value at which to evaluate this
   *     <code>Polynomial</code>.
   * @return The value of this <code>Polynomial</code> at <code>z</code>.
   */
  public Complex at(Complex z) {

    Complex value = Complex.ZERO;
    Complex power = Complex.ONE;

    for (int i = 0; i < this.coeff.length; i++) {
      value = value.plus(power.times(this.coeff[i]));
      power = power.times(z);
    }

    return value;

  }

  /**
   * Adds this polynomial to another.
   * @param other The polynomial to add this polynomial to.
   * @return The sum of the two polynomials.
   */
  public Polynomial plus(Polynomial other) {

    int      maxDegree  = Math.max(this.degree(), other.degree());
    Polynomial  sum      = new Polynomial();
    double    coeff;

    for (int exponent = maxDegree; exponent >= 0; exponent--) {
      coeff = this.coefficient(exponent) + other.coefficient(exponent);
      sum.initializeCoefficient(exponent, coeff);
    }

    sum.initializeZero();
    sum.checkInvariant();

    return sum;

  }

  /**
   * Subtracts another polynomial from this polynomial.
   * @param other The polynomial to subtract from this one.
   * @return The difference between the two polynomials.
   */
  public Polynomial minus(Polynomial other) {

    int      maxDegree  = Math.max(this.degree(), other.degree());
    Polynomial  difference  = new Polynomial();
    double    coeff;

    for (int exponent = maxDegree; exponent >= 0; exponent--) {
      coeff = this.coefficient(exponent) - other.coefficient(exponent);
      difference.initializeCoefficient(exponent, coeff);
    }

    difference.initializeZero();
    difference.checkInvariant();

    return difference;

  }

  /**
   * Multiplies this polynomial with another polynomial.
   * @param other The polynomial to multiply with this one.
   * @return The product of the two polynomials.
   */
  public Polynomial times(Polynomial other) {

    int      maxDegree  = this.degree() + other.degree();
    Polynomial  product    = new Polynomial();
    double    coeff;
    int      i, j;

    for (int exponent = maxDegree; exponent >= 0; exponent--) {

      coeff = 0.0;

      for (i = 0, j = exponent; j >= 0; i++, j--) {
        coeff += this.coefficient(i) * other.coefficient(j);
      }

      product.initializeCoefficient(exponent, coeff);

    }

    product.initializeZero();
    product.checkInvariant();

    return product;

  }

  /**
   * Computes the real roots of this polynomial.
   * @return An array containing the real roots of this polynomial.
   */
  public double[] roots() {
    this.checkInvariant();
    return Solver.roots(this.coeff);
  }

  /**
   * Computes the complex roots of this polynomial.
   * @return An array containing the complex roots of this polynomial.
   */
  public Complex[] complexRoots() {
    this.checkInvariant();
    return Solver.complexRoots(this.coeff);
  }

  /**
   * Returns the zero polynomial.
   * @return The zero polynomial.
   */
  public static Polynomial zero() {
    return new Polynomial(new double[0]);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.toString("x");
  }

  /**
   * Generates a string representation of this <code>Polynomial</code>, with
   * the specified variable name as the unknown quantity.  It is the
   * responsibility of the caller to place parentheses around the variable if
   * necessary (for example <code>this.toString("xy")</code> would generate
   * something like "1.0xy^0 - 2.0xy^1 + 3.0xy^2", when "1.0(xy)^0
   * - 2.0(xy)^1 + 3.0(xy)^2" is what was intended;
   * <code>this.toString("(xy)")</code> would achieve this).
   * @param variableName The name of the unknown quantity.
   * @return The <code>String</code> representation of this
   *     <code>Polynomial</code> with <code>variableName</code> for the
   *     unknown quantity.
   */
  public String toString(String variableName) {

    int        degree  = this.degree();
    StringBuilder  result  = new StringBuilder();
    double      coeff;

    if (degree >= 0) {

      for (int exponent = 0; exponent <= degree; exponent++) {

        coeff = this.coefficient(exponent);

        if (exponent > 0) {
          result.append(coeff >= 0.0 ? " + " : " - ");
          result.append(Math.abs(coeff));
        } else {
          result.append(coeff);
        }

        result.append(variableName);
        result.append("^");
        result.append(exponent);

      }

    } else { // degree < 0

      result.append(0);

    }

    return result.toString();

  }

  /**
   * Default constructor.  The class invariant is not satisfied by this
   * constructor, so additional work is needed before the result of this
   * constructor can be returned by a public method.
   */
  private Polynomial() {
    // do nothing
  }

  /**
   * Initializes a coefficient of a new Polynomial.  This method must be called
   * in descending order by exponent.  {@link #initializeZero()} must be called
   * after all calls to this method before returning the new Polynomial.
   * @param exponent The exponent associated with the coefficient to initialize.
   * @param coeff The value of the coefficient to initialize.
   */
  private void initializeCoefficient(int exponent, double coeff) {

    if (this.coeff == null) {
      if (coeff != 0.0) {
        this.coeff = new double[exponent + 1];
      }
    }

    if (this.coeff != null) {
      this.coeff[exponent] = coeff;
    }

  }

  /**
   * Initializes this polynomial to the zero polynomial if no non-zero
   * coefficients have been set.  This method should be called after all calls
   * to {@link #initializeCoefficient(int, double)} before returning the new
   * Polynomial.
   */
  private void initializeZero() {
    if (this.coeff == null) {
      this.coeff = new double[0];
    }
  }

  /**
   * Ensures that the class invariant is satisfied.  The {@code this.coeff}
   * must be non-null and the last coefficient (if any) must be non-zero.
   */
  private void checkInvariant() {
    assert(this.coeff != null);
    assert(this.coeff.length == 0 || this.coeff[this.coeff.length - 1] != 0.0);
  }

  /**
   * The coefficients of this polynomial.
   */
  private double[] coeff;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -9117582867339363081L;

}
