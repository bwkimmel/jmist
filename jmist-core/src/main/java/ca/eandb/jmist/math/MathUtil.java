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
package ca.eandb.jmist.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.FastMath;

/**
 * Static mathematical utility methods.
 * @author Brad Kimmel
 */
public final class MathUtil {

  /**
   * Returns the cosecant of a value.
   * @param x The value whose cosecant is to be returned.
   * @return The cosecant of the value.
   */
  public static double csc(double x) {
    return 1.0 / Math.sin(x);
  }

  /**
   * Returns the secant of a value.
   * @param x The value whose secant is to be returned.
   * @return The secant of the value.
   */
  public static double sec(double x) {
    return 1.0 / Math.cos(x);
  }

  /**
   * Returns the cotangent of a value.
   * @param x The value whose cotangent is to be returned.
   * @return The cotangent of the value.
   */
  public static double cot(double x) {
    return 1.0 / Math.tan(x);
  }

  /**
   * Returns the arc cosecant of a value.
   * @param x The value whose arc cosecant is to be returned.
   * @return The arc cosecant of the value.
   */
  public static double acsc(double x) {
    return Math.asin(1.0 / x);
  }

  /**
   * Returns the arc secant of a value.
   * @param x The value whose arc secant is to be returned.
   * @return The arc secant of the value.
   */
  public static double asec(double x) {
    return FastMath.acos(1.0 / x);
  }

  /**
   * Returns the arc cotangent of a value.
   * @param x The value whose arc cotangent is to be returned.
   * @return The arc cotangent of the value.
   */
  public static double acot(double x) {
    return Math.atan(1.0 / x);
  }

  /**
   * Returns the hyperbolic sine of a value.
   * @param x The value whose hyperbolic sine is to be returned.
   * @return The hyperbolic sine of the value.
   */
  public static double sinh(double x) {
    return Math.sinh(x);
  }

  /**
   * Returns the hyperbolic cosine of a value.
   * @param x The value whose hyperbolic cosine is to be returned.
   * @return The hyperbolic cosine of the value.
   */
  public static double cosh(double x) {
    return Math.cosh(x);
  }

  /**
   * Returns the hyperbolic tangent of a value.
   * @param x The value whose hyperbolic tangent is to be returned.
   * @return The hyperbolic tangent of the value.
   */
  public static double tanh(double x) {
    return Math.tanh(x);
  }

  /**
   * Returns the hyperbolic cosecant of a value.
   * @param x The value whose hyperbolic cosecant is to be returned.
   * @return The hyperbolic cosecant of the value.
   */
  public static double csch(double x) {
    return 1.0 / Math.sinh(x);
  }

  /**
   * Returns the hyperbolic secant of a value.
   * @param x The value whose hyperbolic secant is to be returned.
   * @return The hyperbolic secant of the value.
   */
  public static double sech(double x) {
    return 1.0 / Math.cosh(x);
  }

  /**
   * Returns the hyperbolic cotangent of a value.
   * @param x The value whose hyperbolic cotangent is to be returned.
   * @return The hyperbolic cotangent of the value.
   */
  public static double coth(double x) {
    return 1.0 / Math.tanh(x);
  }

  /**
   * Returns the arc hyperbolic sine of a value.
   * @param x The value whose arc hyperbolic sine is to be returned.
   * @return The arc hyperbolic sine of the value.
   */
  public static double asinh(double x) {
    return Math.log(x + Math.sqrt(x * x + 1.0));
  }

  /**
   * Returns the arc hyperbolic cosine of a value.
   * @param x The value whose arc hyperbolic cosine is to be returned.
   * @return The arc hyperbolic cosine of the value.
   */
  public static double acosh(double x) {
    return Math.log(x + Math.sqrt(x * x - 1.0));
  }

  /**
   * Returns the arc hyperbolic tangent of a value.
   * @param x The value whose arc hyperbolic tangent is to be returned.
   * @return The arc hyperbolic tangent of the value.
   */
  public static double atanh(double x) {
    return 0.5 * Math.log((1.0 + x) / (1.0 - x));
  }

  /**
   * Returns the arc hyperbolic cosecant of a value.
   * @param x The value whose arc hyperbolic cosecant is to be returned.
   * @return The arc hyperbolic cosecant of the value.
   */
  public static double acsch(double x) {
    return x > 0.0 ? Math.log((1.0 + Math.sqrt(1.0 + x * x)) / x)
        : Math.log((1.0 - Math.sqrt(1.0 + x * x)) / x);
  }

  /**
   * Returns the arc hyperbolic cotangent of a value.
   * @param x The value whose arc hyperbolic cotangent is to be returned.
   * @return The arc hyperbolic cotangent of the value.
   */
  public static double acoth(double x) {
    return 0.5 * Math.log((x + 1.0) / (x - 1.0));
  }

  /**
   * Returns the arc hyperbolic secant of a value.
   * @param x The value whose arc hyperbolic secant is to be returned.
   * @return The arc hyperbolic secant of the value.
   */
  public static double asech(double x) {
    return x > 0.0 ? Math.log((1.0 + Math.sqrt(1.0 - x * x)) / x)
        : Math.log((1.0 - Math.sqrt(1.0 - x * x)) / x);
  }

  /**
   * Returns the square of a number, cheaper than Math.pow
   * @param x number to be squared.
   * @return x * x
   */
  public static double sqr(double x) {
    return x * x;
  }

  /**
   * Returns the smallest (closest to negative infinity) integer that is at
   * least as large as <code>a / b</code>.
   * @param a The numerator.
   * @param b The denominator.
   * @return <code>a / b</code>, if <code>a % b == 0</code>, or
   *         <code>a / b + 1</code> otherwise.
   */
  public static int divRoundUp(int a, int b) {
    int quotient = a / b;
    int remainder = a % b;
    return remainder == 0 ? quotient : quotient + 1;
  }

  /**
   * Returns the minimum value in an array of <code>double</code>s.
   * @param array The array of <code>double</code>s of which to find the
   *     minimum value.
   * @return The minimum value in <code>array</code>.
   */
  public static double min(double[] array) {
    double min = Double.POSITIVE_INFINITY;
    for (int i = 0; i < array.length; i++) {
      if (array[i] < min) {
        min = array[i];
      }
    }
    return min;
  }

  /**
   * Returns the minimum value in an collection of <code>double</code>s.
   * @param values The collection of <code>double</code>s of which to find
   *     the  minimum value.
   * @return The minimum value in <code>values</code>.
   */
  public static double min(Iterable<Double> values) {
    double min = Double.POSITIVE_INFINITY;
    for (double x : values) {
      if (x < min) {
        min = x;
      }
    }
    return min;
  }

  /**
   * Returns the maximum value in an array of <code>double</code>s.
   *
   * @param array
   *            The array of <code>double</code>s of which to find the
   *            maximum value.
   * @return The maximum value in <code>array</code>.
   */
  public static double max(double[] array) {
    double max = Double.NEGATIVE_INFINITY;
    for (int i = 0; i < array.length; i++) {
      if (array[i] > max) {
        max = array[i];
      }
    }
    return max;
  }

  /**
   * Returns the maximum value in an collection of <code>double</code>s.
   * @param values The collection of <code>double</code>s of which to find
   *     the  maximum value.
   * @return The maximum value in <code>values</code>.
   */
  public static double max(Iterable<Double> values) {
    double max = Double.NEGATIVE_INFINITY;
    for (double x : values) {
      if (x > max) {
        max = x;
      }
    }
    return max;
  }

  /**
   * Computes the mean of the values in the given array of
   * <code>double</code>s.
   * @param array The array of <code>double</code>s to compute the mean of.
   * @return The mean of the values in <code>array</code>.
   */
  public static double mean(double[] array) {
    return MathUtil.sum(array) / array.length;
  }

  /**
   * Computes the mean of the values in the given collection of
   * <code>double</code>s.
   * @param values The collection of <code>double</code>s to compute the mean
   *     of.
   * @return The mean of the values in <code>values</code>.
   */
  public static double mean(Collection<Double> values) {
    return MathUtil.sum(values) / values.size();
  }

  /**
   * Determines whether the values in the given array of <code>double</code>s
   * are all equal within a tolerance of {@value #EPSILON}
   * @param array The array of <code>double</code>s to compare.
   * @return A value indicating whether all values in <code>array</code> are
   *     within {@value #EPSILON} of one another.
   * @see #EPSILON
   */
  public static boolean areEqual(double[] array) {
    return areEqual(array, MathUtil.EPSILON);
  }

  /**
   * Determines whether the values in the given collection of
   * <code>double</code>s are all equal within a tolerance of
   * {@value #EPSILON}.
   * @param values The collection of <code>double</code>s to compare.
   * @return A value indicating whether all values in <code>values</code> are
   *     within {@value #EPSILON} of one another.
   * @see #EPSILON
   */
  public static boolean areEqual(Iterable<Double> values) {
    return areEqual(values, MathUtil.EPSILON);
  }

  /**
   * Determines whether the values in the given array of <code>double</code>s
   * are all equal within a specified tolerance.
   * @param array The array of <code>double</code>s to compare.
   * @param epsilon The tolerance.
   * @return A value indicating whether all values in <code>array</code> are
   *     within <code>epsilon</code> of one another.
   */
  public static boolean areEqual(double[] array, double epsilon) {
    if (array.length < 2) {
      return true;
    }

    double min = array[0];
    double max = array[0];

    for (int i = 1; i < array.length; i++) {
      if (array[i] < min) {
        min = array[i];
      }
      if (array[i] > max) {
        max = array[i];
      }
      if (!equal(min, max, epsilon)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines whether the values in the given collection of
   * <code>double</code>s are all equal within a specified tolerance.
   * @param values The collection of <code>double</code>s to compare.
   * @param epsilon The tolerance.
   * @return A value indicating whether all values in <code>values</code> are
   *     within <code>epsilon</code> of one another.
   */
  public static boolean areEqual(Iterable<Double> values, double epsilon) {
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;

    for (double x : values) {
      if (x < min) {
        min = x;
      }
      if (x > max) {
        max = x;
      }
      if (!equal(min, max, epsilon)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Computes the sum of the values in an array of <code>double</code>s.
   * @param array The array of <code>double</code>s of which to compute the
   *     sum.
   * @return The sum of the values in <code>array</code>.
   */
  public static double sum(double[] array) {
    double sum = 0.0;
    for (int i = 0; i < array.length; i++) {
      sum += array[i];
    }
    return sum;
  }

  /**
   * Computes the sum of the values in a collection of <code>double</code>s.
   * @param values The collection of <code>double</code>s of which to compute
   *     the  sum.
   * @return The sum of the values in <code>values</code>.
   */
  public static double sum(Iterable<Double> values) {
    double sum = 0.0;
    for (double x : values) {
      sum += x;
    }
    return sum;
  }

  /**
   * Computes the product of the values in an array of <code>double</code>s.
   * @param array The array of <code>double</code>s of which to compute the
   *     product.
   * @return The product of the values in <code>array</code>.
   */
  public static double product(double[] array) {
    double prod = 1.0;
    for (int i = 0; i < array.length; i++) {
      prod *= array[i];
    }
    return prod;
  }

  /**
   * Computes the product of the values in a collection of
   *     <code>double</code>s.
   * @param values The collection of <code>double</code>s of which to
   *     compute the product.
   * @return The product of the values in <code>values</code>.
   */
  public static double product(Iterable<Double> values) {
    double prod = 1.0;
    for (double x : values) {
      prod *= x;
    }
    return prod;
  }

  /**
   * Computes the sum of the values in an array of <code>int</code>s.
   * @param array The array of <code>int</code>s of which to compute the sum.
   * @return The sum of the values in <code>array</code>.
   */
  public static int sum(int[] array) {
    int sum = 0;
    for (int i = 0; i < array.length; i++) {
      sum += array[i];
    }
    return sum;
  }

  /**
   * Computes the product of the values in an array of <code>int</code>s.
   * @param array The array of <code>int</code>s of which to compute the
   *     product.
   * @return The product of the values in <code>array</code>.
   */
  public static int product(int[] array) {
    int prod = 1;
    for (int i = 0; i < array.length; i++) {
      prod *= array[i];
    }
    return prod;
  }

  /**
   * Scales the specified array of <code>double</code>s so that they sum to
   * one.
   * @param weights The array of <code>double</code>s to normalize (this
   *     array will be modified).
   * @return A reference to <code>weights</code>.
   */
  public static double[] normalize(double[] weights) {
    double sum = MathUtil.sum(weights);
    return MathUtil.scale(weights, 1.0 / sum);
  }

  /**
   * Adds the elements of one array of <code>double</code>s to another. The
   * lengths of <code>accumulator</code> and <code>summand</code> must be
   * equal.
   * @param accumulator The array of <code>double</code>s to add the
   *     elements of <code>summand</code> to (the elements of this array
   *     will be modified).  This may be <code>null</code>, in which case a
   *     new array will be created of the same length of
   *     <code>summand</code> and will be initialized to zeros before adding
   *     the values of <code>summand</code>.
   * @param summand The array of <code>double</code>s to add to each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static double[] add(double[] accumulator, double[] summand) {
    if (accumulator == null) {
      return summand.clone();
    }
    assert(accumulator.length == summand.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] += summand[i];
    }
    return accumulator;
  }

  /**
   * Adds the elements of one array of <code>int</code>s to another. The
   * lengths of <code>accumulator</code> and <code>summand</code> must be
   * equal.
   * @param accumulator The array of <code>int</code>s to add the
   *     elements of <code>summand</code> to (the elements of this array
   *     will be modified).  This may be <code>null</code>, in which case a
   *     new array will be created of the same length of
   *     <code>summand</code> and will be initialized to zeros before adding
   *     the values of <code>summand</code>.
   * @param summand The array of <code>int</code>s to add to each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static int[] add(int[] accumulator, int[] summand) {
    if (accumulator == null) {
      return summand.clone();
    }
    assert(accumulator.length == summand.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] += summand[i];
    }
    return accumulator;
  }

  /**
   * Adds the elements of one array of <code>long</code>s to another. The
   * lengths of <code>accumulator</code> and <code>summand</code> must be
   * equal.
   * @param accumulator The array of <code>long</code>s to add the
   *     elements of <code>summand</code> to (the elements of this array
   *     will be modified).  This may be <code>null</code>, in which case a
   *     new array will be created of the same length of
   *     <code>summand</code> and will be initialized to zeros before adding
   *     the values of <code>summand</code>.
   * @param summand The array of <code>long</code>s to add to each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static long[] add(long[] accumulator, long[] summand) {
    if (accumulator == null) {
      return summand.clone();
    }
    assert(accumulator.length == summand.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] += summand[i];
    }
    return accumulator;
  }

  /**
   * Adds a value to all elements of an array in place.
   * @param x The array to add to.
   * @param value The value to add to each element in <code>x</code>.
   * @return A reference to <code>x</code>.
   */
  public static double[] add(double[] x, double value) {
    for (int i = 0; i < x.length; i++) {
      x[i] += value;
    }
    return x;
  }

  /**
   * Subtracts a value from all elements of an array in place.
   * @param x The array to add to.
   * @param value The value to add to each element in <code>x</code>.
   * @return A reference to <code>x</code>.
   */
  public static double[] subtract(double[] x, double value) {
    for (int i = 0; i < x.length; i++) {
      x[i] -= value;
    }
    return x;
  }

  /**
   * Adds a value to all elements of an array in place.
   * @param x The array to add to.
   * @param value The value to add to each element in <code>x</code>.
   * @return A reference to <code>x</code>.
   */
  public static int[] add(int[] x, int value) {
    for (int i = 0; i < x.length; i++) {
      x[i] += value;
    }
    return x;
  }

  /**
   * Subtracts a value from all elements of an array in place.
   * @param x The array to add to.
   * @param value The value to add to each element in <code>x</code>.
   * @return A reference to <code>x</code>.
   */
  public static int[] subtract(int[] x, int value) {
    for (int i = 0; i < x.length; i++) {
      x[i] -= value;
    }
    return x;
  }

  /**
   * Adds a value to all elements of an array in place.
   * @param x The array to add to.
   * @param value The value to add to each element in <code>x</code>.
   * @return A reference to <code>x</code>.
   */
  public static long[] add(long[] x, long value) {
    for (int i = 0; i < x.length; i++) {
      x[i] += value;
    }
    return x;
  }

  /**
   * Subtracts a value from all elements of an array in place.
   * @param x The array to add to.
   * @param value The value to add to each element in <code>x</code>.
   * @return A reference to <code>x</code>.
   */
  public static long[] subtract(long[] x, long value) {
    for (int i = 0; i < x.length; i++) {
      x[i] -= value;
    }
    return x;
  }

  /**
   * Adds the elements of one array of <code>double</code>s to another.
   * @param accumulator The array of <code>double</code>s to add the
   *     elements of <code>summand</code> to (the elements of this array
   *     will be modified).
   * @param offset The index into <code>accumulator</code> at which to
   *     start adding elements of <code>summand</code>.
   * @param summand The array of <code>double</code>s to add to each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static double[] addRange(double[] accumulator, int offset, double[] summand) {
    assert(accumulator.length >= offset + summand.length);
    for (int i = offset, j = 0; j < summand.length; i++, j++) {
      accumulator[i] += summand[j];
    }
    return accumulator;
  }

  /**
   * Adds the elements of one array of <code>int</code>s to another.
   * @param accumulator The array of <code>int</code>s to add the
   *     elements of <code>summand</code> to (the elements of this array
   *     will be modified).
   * @param offset The index into <code>accumulator</code> at which to
   *     start adding elements of <code>summand</code>.
   * @param summand The array of <code>int</code>s to add to each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static int[] addRange(int[] accumulator, int offset, int[] summand) {
    assert(accumulator.length >= offset + summand.length);
    for (int i = offset, j = 0; j < summand.length; i++, j++) {
      accumulator[i] += summand[j];
    }
    return accumulator;
  }

  /**
   * Adds the elements of one array of <code>long</code>s to another.
   * @param accumulator The array of <code>long</code>s to add the
   *     elements of <code>summand</code> to (the elements of this array
   *     will be modified).
   * @param offset The index into <code>accumulator</code> at which to
   *     start adding elements of <code>summand</code>.
   * @param summand The array of <code>long</code>s to add to each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static long[] addRange(long[] accumulator, int offset, long[] summand) {
    assert(accumulator.length >= offset + summand.length);
    for (int i = offset, j = 0; j < summand.length; i++, j++) {
      accumulator[i] += summand[j];
    }
    return accumulator;
  }

  /**
   * Subtracts the elements of one array of <code>double</code>s from
   * another. The lengths of <code>accumulator</code> and <code>values</code>
   * must be equal.
   * @param accumulator The array of <code>double</code>s to subtract the
   *     elements of <code>values</code> from (the elements of this array
   *     will be modified).  This may be <code>null</code>, in which case a
   *     new array will be created and initialized to zeros before
   *     subtracting the values from <code>values</code>.
   * @param values The array of <code>double</code>s to subtract from each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static double[] subtract(double[] accumulator, double[] values) {
    if (accumulator == null) {
      accumulator = new double[values.length];
    }
    assert(accumulator.length == values.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] -= values[i];
    }
    return accumulator;
  }

  /**
   * Subtracts the elements of one array of <code>int</code>s from
   * another. The lengths of <code>accumulator</code> and <code>values</code>
   * must be equal.
   * @param accumulator The array of <code>int</code>s to subtract the
   *     elements of <code>values</code> from (the elements of this array
   *     will be modified).  This may be <code>null</code>, in which case a
   *     new array will be created and initialized to zeros before
   *     subtracting the values from <code>values</code>.
   * @param values The array of <code>int</code>s to subtract from each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static int[] subtract(int[] accumulator, int[] values) {
    if (accumulator == null) {
      accumulator = new int[values.length];
    }
    assert(accumulator.length == values.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] -= values[i];
    }
    return accumulator;
  }

  /**
   * Subtracts the elements of one array of <code>long</code>s from
   * another. The lengths of <code>accumulator</code> and <code>values</code>
   * must be equal.
   * @param accumulator The array of <code>long</code>s to subtract the
   *     elements of <code>values</code> from (the elements of this array
   *     will be modified).  This may be <code>null</code>, in which case a
   *     new array will be created and initialized to zeros before
   *     subtracting the values from <code>values</code>.
   * @param values The array of <code>long</code>s to subtract from each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static long[] subtract(long[] accumulator, long[] values) {
    if (accumulator == null) {
      accumulator = new long[values.length];
    }
    assert(accumulator.length == values.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] -= values[i];
    }
    return accumulator;
  }

  /**
   * Subtracts the elements of one array of <code>double</code>s from
   * another.
   * @param accumulator The array of <code>double</code>s to subtract the
   *     elements of <code>values</code> from (the elements of this array
   *     will be modified).
   * @param offset The index into <code>accumulator</code> at which to start
   *     subtracting the elements of <code>values</code>.
   * @param values The array of <code>double</code>s to subtract from each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static double[] subtractRange(double[] accumulator, int offset, double[] values) {
    assert(accumulator.length >= offset + values.length);
    for (int i = offset, j = 0; j < values.length; i++, j++) {
      accumulator[i] -= values[j];
    }
    return accumulator;
  }

  /**
   * Subtracts the elements of one array of <code>int</code>s from
   * another.
   * @param accumulator The array of <code>int</code>s to subtract the
   *     elements of <code>values</code> from (the elements of this array
   *     will be modified).
   * @param offset The index into <code>accumulator</code> at which to start
   *     subtracting the elements of <code>values</code>.
   * @param values The array of <code>int</code>s to subtract from each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static int[] subtractRange(int[] accumulator, int offset, int[] values) {
    assert(accumulator.length >= offset + values.length);
    for (int i = offset, j = 0; j < values.length; i++, j++) {
      accumulator[i] -= values[j];
    }
    return accumulator;
  }

  /**
   * Subtracts the elements of one array of <code>long</code>s from
   * another.
   * @param accumulator The array of <code>long</code>s to subtract the
   *     elements of <code>values</code> from (the elements of this array
   *     will be modified).
   * @param offset The index into <code>accumulator</code> at which to start
   *     subtracting the elements of <code>values</code>.
   * @param values The array of <code>long</code>s to subtract from each
   *     corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static long[] subtractRange(long[] accumulator, int offset, long[] values) {
    assert(accumulator.length >= offset + values.length);
    for (int i = offset, j = 0; j < values.length; i++, j++) {
      accumulator[i] -= values[j];
    }
    return accumulator;
  }

  /**
   * Multiplies all the elements in an array of <code>double</code>s by the
   * elements of an equally sized array of <code>double</code>s.  The lengths
   * of <code>accumulator</code> and <code>modulator</code> must be equal.
   * @param accumulator The array of <code>double</code>s that is to have its
   *     elements scaled (the elements of this array will be modified).
   *     This may be <code>null</code>, in which case a new array will be
   *     created and initialized to ones before multiplying by the values of
   *     <code>modulator</code>.
   * @param modulator The array of <code>double</code>s by which to multiply
   *     each corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static double[] multiply(double[] accumulator, double[] modulator) {
    if (accumulator == null) {
      return modulator.clone();
    }
    assert(accumulator.length == modulator.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] *= modulator[i];
    }
    return accumulator;
  }

  /**
   * Multiplies all the elements in an array of <code>int</code>s by the
   * elements of an equally sized array of <code>int</code>s.  The lengths
   * of <code>accumulator</code> and <code>modulator</code> must be equal.
   * @param accumulator The array of <code>int</code>s that is to have its
   *     elements scaled (the elements of this array will be modified).
   *     This may be <code>null</code>, in which case a new array will be
   *     created and initialized to ones before multiplying by the values of
   *     <code>modulator</code>.
   * @param modulator The array of <code>int</code>s by which to multiply
   *     each corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static int[] multiply(int[] accumulator, int[] modulator) {
    if (accumulator == null) {
      return modulator.clone();
    }
    assert(accumulator.length == modulator.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] *= modulator[i];
    }
    return accumulator;
  }

  /**
   * Multiplies all the elements in an array of <code>long</code>s by the
   * elements of an equally sized array of <code>long</code>s.  The lengths
   * of <code>accumulator</code> and <code>modulator</code> must be equal.
   * @param accumulator The array of <code>long</code>s that is to have its
   *     elements scaled (the elements of this array will be modified).
   *     This may be <code>null</code>, in which case a new array will be
   *     created and initialized to ones before multiplying by the values of
   *     <code>modulator</code>.
   * @param modulator The array of <code>long</code>s by which to multiply
   *     each corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static long[] multiply(long[] accumulator, long[] modulator) {
    if (accumulator == null) {
      return modulator.clone();
    }
    assert(accumulator.length == modulator.length);
    for (int i = 0; i < accumulator.length; i++) {
      accumulator[i] *= modulator[i];
    }
    return accumulator;
  }

  /**
   * Multiplies all the elements in an array of <code>double</code>s by the
   * elements of an equally sized array of <code>double</code>s.
   * @param accumulator The array of <code>double</code>s that is to have its
   *     elements scaled (the elements of this array will be modified).
   * @param offset The index into <code>accumulator</code> at which to start
   *     multiplying by elements of <code>modulator</code>.
   * @param modulator The array of <code>double</code>s by which to multiply
   *     each corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static double[] multiplyRange(double[] accumulator, int offset, double[] modulator) {
    assert(accumulator.length >= offset + modulator.length);
    for (int i = offset, j = 0; j < modulator.length; i++, j++) {
      accumulator[i] *= modulator[j];
    }
    return accumulator;
  }

  /**
   * Multiplies all the elements in an array of <code>int</code>s by the
   * elements of an equally sized array of <code>int</code>s.
   * @param accumulator The array of <code>int</code>s that is to have its
   *     elements scaled (the elements of this array will be modified).
   * @param offset The index into <code>accumulator</code> at which to start
   *     multiplying by elements of <code>modulator</code>.
   * @param modulator The array of <code>int</code>s by which to multiply
   *     each corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static int[] multiplyRange(int[] accumulator, int offset, int[] modulator) {
    assert(accumulator.length >= offset + modulator.length);
    for (int i = offset, j = 0; j < modulator.length; i++, j++) {
      accumulator[i] *= modulator[j];
    }
    return accumulator;
  }

  /**
   * Multiplies all the elements in an array of <code>long</code>s by the
   * elements of an equally sized array of <code>long</code>s.
   * @param accumulator The array of <code>long</code>s that is to have its
   *     elements scaled (the elements of this array will be modified).
   * @param offset The index into <code>accumulator</code> at which to start
   *     multiplying by elements of <code>modulator</code>.
   * @param modulator The array of <code>long</code>s by which to multiply
   *     each corresponding element of <code>accumulator</code>.
   * @return A reference to <code>accumulator</code>.
   */
  public static long[] multiplyRange(long[] accumulator, int offset, long[] modulator) {
    assert(accumulator.length >= offset + modulator.length);
    for (int i = offset, j = 0; j < modulator.length; i++, j++) {
      accumulator[i] *= modulator[j];
    }
    return accumulator;
  }

  /**
   * Multiplies all of the values in the specified array of
   * <code>double</code>s by the same value.
   * @param array The array of <code>double</code>s the elements of which are
   *     to be multiplied by a constant factor (the elements of this array
   *     will be modified).
   * @param factor The value by which to multiply each element of
   *     <code>array</code>.
   * @return A reference to <code>array</code>.
   */
  public static double[] scale(double[] array, double factor) {
    for (int i = 0; i < array.length; i++) {
      array[i] *= factor;
    }
    return array;
  }

  /**
   * Multiplies all of the values in the specified array of
   * <code>int</code>s by the same value.
   * @param array The array of <code>int</code>s the elements of which are
   *     to be multiplied by a constant factor (the elements of this array
   *     will be modified).
   * @param factor The value by which to multiply each element of
   *     <code>array</code>.
   * @return A reference to <code>array</code>.
   */
  public static int[] scale(int[] array, int factor) {
    for (int i = 0; i < array.length; i++) {
      array[i] *= factor;
    }
    return array;
  }

  /**
   * Multiplies all of the values in the specified array of
   * <code>long</code>s by the same value.
   * @param array The array of <code>long</code>s the elements of which are
   *     to be multiplied by a constant factor (the elements of this array
   *     will be modified).
   * @param factor The value by which to multiply each element of
   *     <code>array</code>.
   * @return A reference to <code>array</code>.
   */
  public static long[] scale(long[] array, long factor) {
    for (int i = 0; i < array.length; i++) {
      array[i] *= factor;
    }
    return array;
  }

  /**
   * Returns x if it is within the specified range, or the closest
   * value within the specified range if x is outside the range.
   * @param x The value to threshold.
   * @param min The minimum of the range to threshold to.
   * @param max The maximum of the range to threshold to.
   * @return x, if min &lt;= x &lt;= max.  min, if x &lt; min.  max, if x &gt;
   *     max.
   */
  public static double clamp(double x, double min, double max) {
    if (x < min) {
      return min;
    } else if (x > max) {
      return max;
    } else {
      return x;
    }
  }

  /**
   * Returns x if it is within the specified range, or the closest
   * value within the specified range if x is outside the range.
   * @param x The value to threshold.
   * @param min The minimum of the range to threshold to.
   * @param max The maximum of the range to threshold to.
   * @return x, if min &lt;= x &lt;= max.  min, if x &lt; min.  max, if x &gt;
   *     max.
   */
  public static int clamp(int x, int min, int max) {
    if (x < min) {
      return min;
    } else if (x > max) {
      return max;
    } else {
      return x;
    }
  }

  /**
   * Returns x if it is within the specified range, or the closest
   * value within the specified range if x is outside the range.
   * @param x The value to threshold.
   * @param min The minimum of the range to threshold to.
   * @param max The maximum of the range to threshold to.
   * @return x, if min &lt;= x &lt;= max.  min, if x &lt; min.  max, if x &gt;
   *     max.
   */
  public static long clamp(long x, long min, long max) {
    if (x < min) {
      return min;
    } else if (x > max) {
      return max;
    } else {
      return x;
    }
  }

  /**
   * Determines whether two floating point values are close enough
   * to be considered "equal" (i.e., the difference may be attributed
   * to rounding errors).
   * @param x The first value to compare.
   * @param y The second value to compare.
   * @param epsilon The minimum difference required for the two values
   *     to be considered distinguishable.
   * @return A value indicating whether the difference between x and y
   *     is less than the given threshold.
   */
  public static boolean equal(double x, double y, double epsilon) {
    return Math.abs(x - y) < epsilon;
  }

  /**
   * Determines whether two floating point values are close enough
   * to be considered "equal" (i.e., the difference may be attributed
   * to rounding errors).
   * @param x The first value to compare.
   * @param y The second value to compare.
   * @return A value indicating whether the difference between x and y
   *     is less than MathUtil.EPSILON.
   * @see #EPSILON
   */
  public static boolean equal(double x, double y) {
    return Math.abs(x - y) < MathUtil.EPSILON;
  }

  /**
   * Determines whether a floating point value is close enough to zero to be
   * considered "equal" to zero (i.e., the difference may be attributed to
   * rounding errors).
   * @param x The value to compare to zero.
   * @param epsilon The minimum absolute value of {@code x} for it to be
   *     considered non-zero.
   * @return A value indicates whether the difference between {@code x} and
   *     0.0 is less than {@code epsilon}.
   */
  public static boolean isZero(double x, double epsilon) {
    return Math.abs(x) < epsilon;
  }

  /**
   * Determines whether a floating point value is close enough to zero to be
   * considered "equal" to zero (i.e., the difference may be attributed to
   * rounding errors).
   * @param x The value to compare to zero.
   * @return A value indicates whether the difference between {@code x} and
   *     0.0 is less than {@link #EPSILON}.
   * @see #EPSILON
   */
  public static boolean isZero(double x) {
    return Math.abs(x) < MathUtil.EPSILON;
  }

  /**
   * Determines whether {@code x} falls within the open interval
   * {@code (minimum, maximum)}.
   * @param x The value to check.
   * @param minimum The lower bound of the interval to check against.
   * @param maximum The upper bound of the interval to check against.
   * @return A value indicating whether {@code x} is contained in the open
   *     interval {@code (minimum, maximum)}.
   */
  public static boolean inRangeOO(double x, double minimum, double maximum) {
    return minimum < x && x < maximum;
  }

  /**
   * Determines whether {@code x} falls within the interval
   * {@code [minimum, maximum)}.
   * @param x The value to check.
   * @param minimum The lower bound of the interval to check against.
   * @param maximum The upper bound of the interval to check against.
   * @return A value indicating whether {@code x} is contained in the
   *     interval {@code [minimum, maximum)}.
   */
  public static boolean inRangeCO(double x, double minimum, double maximum) {
    return minimum <= x && x < maximum;
  }

  /**
   * Determines whether {@code x} falls within the closed interval
   * {@code [minimum, maximum]}.
   * @param x The value to check.
   * @param minimum The lower bound of the interval to check against.
   * @param maximum The upper bound of the interval to check against.
   * @return A value indicating whether {@code x} is contained in the closed
   *     interval {@code [minimum, maximum]}.
   */
  public static boolean inRangeCC(double x, double minimum, double maximum) {
    return minimum <= x && x <= maximum;
  }

  /**
   * Determines whether {@code x} falls within the interval
   * {@code (minimum, maximum]}.
   * @param x The value to check.
   * @param minimum The lower bound of the interval to check against.
   * @param maximum The upper bound of the interval to check against.
   * @return A value indicating whether {@code x} is contained in the
   *     interval {@code (minimum, maximum]}.
   */
  public static boolean inRangeOC(double x, double minimum, double maximum) {
    return minimum < x && x <= maximum;
  }

  /**
   * Returns the value of the integer of highest magnitude (farthest from
   * zero) whose absolute value is at most that of <code>x</code>.
   * @param x The value to truncate.
   * @return The integer portion of <code>x</code>.
   */
  public static double truncate(double x) {
    return x > 0.0 ? Math.floor(x) : Math.ceil(x);
  }

  /**
   * Interpolates between two end points.
   * @param a The end point at <code>t = 0</code>.
   * @param b The end point at <code>t = 1</code>.
   * @param t The value at which to interpolate.
   * @return The value that is the fraction <code>t</code> of the way from
   *     <code>a</code> to <code>b</code>: <code>(1-t)a + tb</code>.
   */
  public static double interpolate(double a, double b, double t) {
    return a + t * (b - a);
  }

  /**
   * Interpolates between two points on a line.
   * @param x0 The x-coordinate of the first point.
   * @param y0 The y-coordinate of the first point.
   * @param x1 The x-coordinate of the second point.
   * @param y1 The y-coordinate of the second point.
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolate(double x0, double y0, double x1, double y1, double x) {
    double t = (x - x0) / (x1 - x0);
    return interpolate(y0, y1, t);
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param xs An array of x-coordinates (this must be sorted in ascending
   *     order).
   * @param ys An array of the y-coordinates (must be of the same length as
   *     <code>xs</code>, or one less if <code>wrap == true</code>).
   * @param x The x-coordinate at which to interpolate.
   * @param wrap A value indicating whether the curve is periodic.
   * @return The y-coordinate corresponding to <code>x0</code>.
   */
  public static double interpolate(double[] xs, double[] ys, double x, boolean wrap) {
    if (wrap) {
      return interpolateWrapped(xs, ys, x);
    } else {
      return interpolate(xs, ys, x);
    }
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param xs An array of x-coordinates (this must be sorted in ascending
   *     order).
   * @param ys An array of the y-coordinates (must be of the same length as
   *     <code>xs</code>).
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x0</code>.
   */
  public static double interpolate(double[] xs, double[] ys, double x) {
    if (x <= xs[0]) {
      return ys[0];
    }
    if (x >= xs[xs.length - 1]) {
      return ys[xs.length - 1];
    }

    int index = Arrays.binarySearch(xs, x);
    if (index < 0) {
      index = -(index + 1);
    }
    while (index < xs.length - 1 && !(x < xs[index])) {
      index++;
    }

    assert(index < xs.length);
    return interpolate(xs[index - 1], ys[index - 1], xs[index], ys[index], x);
  }

  /**
   * Interpolates a periodic piecewise linear curve.
   * @param xs An array of x-coordinates (this must be sorted in ascending
   *     order).
   * @param ys An array of the y-coordinates (must have length
   *     <code>xs.length - 1</code>).
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolateWrapped(double[] xs, double[] ys, double x) {
    int n = xs.length;
    double x0 = xs[0];
    double x1 = xs[n - 1];

    x -= (x1 - x0) * Math.floor((x - x0) / (x1 - x0));
    assert(inRangeCO(x, x0, x1));

    int index = Arrays.binarySearch(xs, x);
    if (index < 0) {
      index = -(index + 1);
    }
    while (index < n - 1 && !(x < xs[index])) {
      index++;
    }

    assert(index < n);

    int i = index - 1;
    int j = index;
    if (j == n - 1) { j = 0; }

    return interpolate(xs[i], ys[i], xs[index], ys[j], x);
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param xs An array of x-coordinates (this must be sorted in ascending
   *     order).
   * @param ys An array of the y-coordinates (must be of the same length as
   *     <code>xs</code>, or one less if <code>wrap == true</code>).
   * @param x The x-coordinate at which to interpolate.
   * @param wrap A value indicating whether the curve is periodic.
   * @return The y-coordinate corresponding to <code>x0</code>.
   */
  public static double interpolate(List<Double> xs, List<Double> ys, double x, boolean wrap) {
    if (wrap) {
      return interpolateWrapped(xs, ys, x);
    } else {
      return interpolate(xs, ys, x);
    }
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param xs An array of x-coordinates (this must be sorted in ascending
   *     order).
   * @param ys An array of the y-coordinates (must of length of
   *     <code>xs.size() - 1</code>).
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolate(List<Double> xs, List<Double> ys, double x) {
    if (x <= xs.get(0)) {
      return ys.get(0);
    }

    int n = xs.size();
    if (x >= xs.get(n - 1)) {
      return ys.get(n - 1);
    }

    int index = Collections.binarySearch(xs, x);
    if (index < 0) {
      index = -(index + 1);
    }
    while (index < n - 1 && !(x < xs.get(index))) {
      index++;
    }

    assert(index < n);
    return interpolate(xs.get(index - 1), ys.get(index - 1), xs.get(index), ys.get(index), x);
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param xs An array of x-coordinates (this must be sorted in ascending
   *     order).
   * @param ys An array of the y-coordinates (must be of the same length as
   *     <code>xs</code>, or one less if <code>wrap == true</code>).
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolateWrapped(List<Double> xs, List<Double> ys, double x) {
    int n = xs.size();
    double x0 = xs.get(0);
    double x1 = xs.get(n - 1);

    x -= (x1 - x0) * Math.floor((x - x0) / (x1 - x0));
    assert(inRangeCO(x, x0, x1));

    int index = Collections.binarySearch(xs, x);
    if (index < 0) {
      index = -(index + 1);
    }
    while (index < n - 1 && !(x < xs.get(index))) {
      index++;
    }

    assert(index < n);

    int i = index - 1;
    int j = index;
    if (j == n - 1) { j = 0; }

    return interpolate(xs.get(i), ys.get(i), xs.get(index), ys.get(j), x);
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param x0 The minimum value in the domain.
   * @param x1 The maximum value in the domain (must not be less than
   *     <code>x0</code>).
   * @param y An array of the y-coordinates (must have at least two elements).
   * @param x The x-coordinate at which to interpolate.
   * @param wrap A value indicating whether the curve is periodic.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolate(double x0, double x1, double[] y, double x, boolean wrap) {
    if (wrap) {
      return interpolateWrapped(x0, x1, y, x);
    } else {
      return interpolate(x0, x1, y, x);
    }
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param x0 The minimum value in the domain.
   * @param x1 The maximum value in the domain (must not be less than
   *     <code>x0</code>).
   * @param y An array of the y-coordinates (must have at least two elements).
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolate(double x0, double x1, double[] y, double x) {
    if (x <= x0) {
      return y[0];
    }
    if (x >= x1) {
      return y[y.length - 1];
    }

    double t = (y.length - 1) * ((x - x0) / (x1 - x0));
    int i = (int) Math.floor(t);

    return interpolate(y[i], y[i + 1], t - i);
  }

  /**
   * Interpolates a periodic piecewise linear curve.
   * @param x0 The minimum value in the domain.
   * @param x1 The maximum value in the domain (must not be less than
   *     <code>x0</code>).
   * @param y An array of the y-coordinates.
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolateWrapped(double x0, double x1, double[] y, double x) {
    double t = (x - x0) / (x1 - x0);
    t -= Math.floor(t);
    t *= y.length;

    int i = (int) Math.floor(t);
    int j = i + 1;
    if (j == y.length) { j = 0; }

    assert(0 <= i && i < y.length);
    return interpolate(y[i], y[j], t - i);
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param x0 The minimum value in the domain.
   * @param x1 The maximum value in the domain (must not be less than
   *     <code>x0</code>).
   * @param y An array of the y-coordinates (must have at least two elements
   *     if <code>wrap == false</code>).
   * @param x The x-coordinate at which to interpolate.
   * @param wrap A value indicating whether the curve is periodic.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolate(double x0, double x1, List<Double> y, double x, boolean wrap) {
    if (wrap) {
      return interpolateWrapped(x0, x1, y, x);
    } else {
      return interpolate(x0, x1, y, x);
    }
  }

  /**
   * Interpolates a piecewise linear curve.
   * @param x0 The minimum value in the domain.
   * @param x1 The maximum value in the domain (must not be less than
   *     <code>x0</code>).
   * @param y An array of the y-coordinates (must have at least two elements).
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolate(double x0, double x1, List<Double> y, double x) {
    if (x <= x0) {
      return y.get(0);
    }
    if (x > x1) {
      return y.get(y.size() - 1);
    }

    double t = (y.size() - 1) * ((x - x0) / (x1 - x0));
    int i = (int) Math.floor(t);

    return interpolate(y.get(i), y.get(i + 1), t - i);
  }

  /**
   * Interpolates a periodic piecewise linear curve.
   * @param x0 The minimum value in the domain.
   * @param x1 The maximum value in the domain (must not be less than
   *     <code>x0</code>).
   * @param y An array of the y-coordinates.
   * @param x The x-coordinate at which to interpolate.
   * @return The y-coordinate corresponding to <code>x</code>.
   */
  public static double interpolateWrapped(double x0, double x1, List<Double> y, double x) {
    double t = (x - x0) / (x1 - x0);
    t -= Math.floor(t);
    t *= y.size();

    int i = (int) Math.floor(t);
    int j = i + 1;
    if (j == y.size()) { j = 0; }

    assert(0 <= i && i < y.size());
    return interpolate(y.get(i), y.get(j), t - i);
  }

  /**
   * Performs a bilinear interpolation between four values.
   * @param _00 The value at <code>(t, u) = (0, 0)</code>.
   * @param _10 The value at <code>(t, u) = (1, 0)</code>.
   * @param _01 The value at <code>(t, u) = (0, 1)</code>.
   * @param _11 The value at <code>(t, u) = (1, 1)</code>.
   * @param t The first value at which to interpolate.
   * @param u The second value at which to interpolate.
   * @return The interpolated value at <code>(t, u)</code>.
   */
  public static double bilinearInterpolate(double _00, double _10,
      double _01, double _11, double t, double u) {
    return interpolate(
        interpolate(_00, _10, t),
        interpolate(_01, _11, t),
        u
    );
  }

  /**
   * Performs bilinear interpolation over a non-uniform grid.
   * @param xs The array of grid points along the x-axis (must have the same
   *     length as the number of rows in <code>z</code>).
   * @param ys The array of grid points along the y-axis (must have the same
   *     length as the number of columns in <code>z</code>).
   * @param z The <code>Matrix</code> of values to interpolate (must have
   *     <code>xs.length</code> rows and <code>ys.length</code> columns).
   * @param x The x-coordinate at which to interpolate.
   * @param y The y-coordinate at which to interpolate.
   * @return The interpolated value at <code>(x, y)</code>.
   * @throws IllegalArgumentException if <code>z</code> does not have
   *     dimensions of <code>xs.length</code> by <code>ys.length</code>.
   */
  public static double bilinearInterpolate(double xs[], double ys[],
      Matrix z, double x, double y) {
    return bilinearInterpolate(xs, ys, z, x, y, false, false);
  }

  /**
   * Performs bilinear interpolation over a non-uniform grid.
   * @param xs The array of grid points along the x-axis (must have the same
   *     length as the number of rows in <code>z</code>).
   * @param ys The array of grid points along the y-axis (must have the same
   *     length as the number of columns in <code>z</code>).
   * @param z The <code>Matrix</code> of values to interpolate (must have
   *     <code>xs.length - (wrapX ? 1 : 0)</code> rows and
   *     <code>ys.length - (wrapY ? 1 : 0)</code> columns).
   * @param x The x-coordinate at which to interpolate.
   * @param y The y-coordinate at which to interpolate.
   * @param wrapX A value indicating whether to wrap in the x-direction.  If
   *     <code>true</code>, then <code>xs</code> should have an extra entry
   *     at the end which maps to row <code>0</code>.
   * @param wrapY A value indicating whether to wrap in the y-direction.  If
   *     <code>true</code>, then <code>ys</code> should have an extra entry
   *     at the end which maps to column <code>0</code>.
   * @return The interpolated value at <code>(x, y)</code>.
   * @throws IllegalArgumentException if <code>z</code> does not have
   *     the correct dimensions.
   */
  public static double bilinearInterpolate(double xs[], double ys[],
      Matrix z, double x, double y, boolean wrapX, boolean wrapY) {
    return bilinearInterpolate(
        Matrix.rowMajor(1, xs.length, xs).elementsByRow(),
        Matrix.rowMajor(1, ys.length, ys).elementsByRow(),
        z, x, y, wrapX, wrapY);
  }

  /**
   * Performs bilinear interpolation over a non-uniform grid.
   * @param xs The list of grid points along the x-axis (must have the same
   *     length as the number of rows in <code>z</code>).
   * @param ys The list of grid points along the y-axis (must have the same
   *     length as the number of columns in <code>z</code>).
   * @param z The <code>Matrix</code> of values to interpolate (must have
   *     <code>xs.size()</code> rows and <code>ys.size()</code> columns).
   * @param x The x-coordinate at which to interpolate.
   * @param y The y-coordinate at which to interpolate.
   * @return The interpolated value at <code>(x, y)</code>.
   * @throws IllegalArgumentException if <code>z</code> does not have
   *     dimensions of <code>xs.size()</code> by <code>ys.size()</code>.
   */
  public static double bilinearInterpolate(List<Double> xs, List<Double> ys,
      Matrix z, double x, double y) {
    return bilinearInterpolate(xs, ys, z, x, y, false, false);
  }

  /**
   * Performs bilinear interpolation over a non-uniform grid.
   * @param xs The list of grid points along the x-axis (must have the same
   *     length as the number of rows in <code>z</code>).
   * @param ys The list of grid points along the y-axis (must have the same
   *     length as the number of columns in <code>z</code>).
   * @param z The <code>Matrix</code> of values to interpolate (must have
   *     <code>xs.length - (wrapX ? 1 : 0)</code> rows and
   *     <code>ys.length - (wrapY ? 1 : 0)</code> columns).
   * @param x The x-coordinate at which to interpolate.
   * @param y The y-coordinate at which to interpolate.
   * @param wrapX A value indicating whether to wrap in the x-direction.  If
   *     <code>true</code>, then <code>xs</code> should have an extra entry
   *     at the end which maps to row <code>0</code>.
   * @param wrapY A value indicating whether to wrap in the y-direction.  If
   *     <code>true</code>, then <code>ys</code> should have an extra entry
   *     at the end which maps to column <code>0</code>.
   * @return The interpolated value at <code>(x, y)</code>.
   * @throws IllegalArgumentException if <code>z</code> does not have
   *     the correct dimensions.
   */
  public static double bilinearInterpolate(List<Double> xs, List<Double> ys,
      Matrix z, double x, double y, boolean wrapX, boolean wrapY) {
    int nx = xs.size();
    int ny = ys.size();
    if (nx != z.rows() + (wrapX ? 1 : 0) || ny != z.columns() + (wrapY ? 1 : 0)) {
      throw new IllegalArgumentException("Matrix z must be xs.size() by ys.size()");
    }

    if (x <= xs.get(0)) {
      return interpolate(ys, z.row(0).elements(), y, wrapY);
    }
    if (x >= xs.get(nx - 1)) {
      return interpolate(ys, z.row(nx - 1).elements(), y, wrapY);
    }
    if (y <= ys.get(0)) {
      return interpolate(xs, z.column(0).elements(), x, wrapX);
    }
    if (y >= ys.get(ny - 1)) {
      return interpolate(xs, z.column(ny - 1).elements(), x, wrapX);
    }

    int ix = Collections.binarySearch(xs, x);
    if (ix < 0) {
      ix = -(ix + 1);
    }
    while (ix < nx - 1 && !(x < xs.get(ix))) {
      ix++;
    }

    int iy = Collections.binarySearch(ys, y);
    if (iy < 0) {
      iy = -(iy + 1);
    }
    while (iy < ny - 1 && !(y < ys.get(iy))) {
      iy++;
    }

    assert(ix < nx && iy < ny);
    ix--;
    iy--;

    double x0 = xs.get(ix);
    double x1 = xs.get(ix + 1);
    double y0 = ys.get(iy);
    double y1 = ys.get(iy + 1);

    double tx = (x - x0) / (x1 - x0);
    double ty = (y - y0) / (y1 - y0);

    int jx = ix + 1;
    int jy = iy + 1;
    if (jx == z.rows()) { jx = 0; }
    if (jy == z.columns()) { jy = 0; }

    double _00 = z.at(ix, iy);
    double _01 = z.at(ix, jy);
    double _10 = z.at(jx, iy);
    double _11 = z.at(jx, jy);

    return bilinearInterpolate(_00, _10, _01, _11, tx, ty);
  }

  /**
   * Performs bilinear interpolation over a uniform grid.
   * @param x0 The minimum value along the x-axis.
   * @param x1 The maximum value along the x-axis.
   * @param y0 The minimum value along the y-axis.
   * @param y1 The maximum value along the y-axis.
   * @param z The <code>Matrix</code> of values to interpolate (must have at
   *     least two rows and at least two columns).
   * @param x The x-coordinate at which to interpolate.
   * @param y The y-coordinate at which to interpolate.
   * @return The interpolated value at <code>(x, y)</code>.
   * @throws IllegalArgumentException if <code>z</code> has fewer than two
   *     rows or fewer than two columns.
   */
  public static double bilinearInterpolate(
      double x0, double x1, double y0, double y1,
      Matrix z, double x, double y) {
    return bilinearInterpolate(x0, x1, y0, y1, z, x, y, false, false);
  }

  /**
   * Performs bilinear interpolation over a uniform grid.
   * @param x0 The minimum value along the x-axis.
   * @param x1 The maximum value along the x-axis.
   * @param y0 The minimum value along the y-axis.
   * @param y1 The maximum value along the y-axis.
   * @param z The <code>Matrix</code> of values to interpolate.  It must have
   *     at least two rows (unless wrapping in X) and at least two columns
   *     (unless wrapping in Y).
   * @param x The x-coordinate at which to interpolate.
   * @param y The y-coordinate at which to interpolate.
   * @param wrapX A value indicating whether to wrap in the x-direction.
   * @param wrapY A value indicating whether to wrap in the y-direction.
   * @return The interpolated value at <code>(x, y)</code>.
   * @throws IllegalArgumentException if <code>z</code> has fewer than two
   *     rows (and does not wrap in X) or fewer than two columns (and does
   *     not wrap in Y).
   */
  public static double bilinearInterpolate(
      double x0, double x1, double y0, double y1,
      Matrix z, double x, double y, boolean wrapX, boolean wrapY) {
    if ((!wrapX && z.rows() < 2) || (!wrapY && z.columns() < 2)) {
      throw new IllegalArgumentException(
          "Matrix z must have length 2 in each non-wrapping dimension");
    }

    if (x <= x0) {
      return interpolate(y0, y1, z.row(0).elements(), y, wrapY);
    }
    if (x >= x1) {
      int j = z.rows() - 1;
      return interpolate(y0, y1, z.row(j).elements(), y, wrapY);
    }
    if (y <= y0) {
      return interpolate(x0, x1, z.column(0).elements(), x, wrapX);
    }
    if (y >= y1) {
      int j = z.columns() - 1;
      return interpolate(x0, x1, z.column(j).elements(), x, wrapX);
    }

    int nx = z.rows();
    int ny = z.columns();
    double tx = (nx - (wrapX ? 0 : 1)) * ((x - x0) / (x1 - x0));
    double ty = (ny - (wrapY ? 0 : 1)) * ((y - y0) / (y1 - y0));
    int ix = (int) Math.floor(tx);
    int iy = (int) Math.floor(ty);
    int jx = ix + 1;
    int jy = iy + 1;
    jx = jx < nx ? jx : 0;
    jy = jy < ny ? jy : 0;

    double _00 = z.at(ix, iy);
    double _01 = z.at(ix, jy);
    double _10 = z.at(jx, iy);
    double _11 = z.at(jx, jy);

    return bilinearInterpolate(_00, _10, _01, _11, tx - ix, ty - iy);
  }

  /**
   * Performs a trilinear interpolation between eight values.
   * @param _000 The value at <code>(t, u, v) = (0, 0, 0)</code>.
   * @param _100 The value at <code>(t, u, v) = (1, 0, 0)</code>.
   * @param _010 The value at <code>(t, u, v) = (0, 1, 0)</code>.
   * @param _110 The value at <code>(t, u, v) = (1, 1, 0)</code>.
   * @param _001 The value at <code>(t, u, v) = (0, 0, 1)</code>.
   * @param _101 The value at <code>(t, u, v) = (1, 0, 1)</code>.
   * @param _011 The value at <code>(t, u, v) = (0, 1, 1)</code>.
   * @param _111 The value at <code>(t, u, v) = (1, 1, 1)</code>.
   * @param t The first value at which to interpolate.
   * @param u The second value at which to interpolate.
   * @param v The third value at which to interpolate.
   * @return The interpolated value at <code>(t, u, v)</code>.
   */
  public static double trilinearInterpolate(double _000, double _100,
      double _010, double _110, double _001, double _101, double _011,
      double _111, double t, double u, double v) {
    return interpolate(
        bilinearInterpolate(_000, _001, _010, _011, u, v),
        bilinearInterpolate(_100, _101, _110, _111, u, v),
        t
    );
  }

  /**
   * Computes the integral of the piecewise linear function through the given
   * points (<code>i</code>, <code>y[i]</code>) using the trapezoidal
   * method.
   * @param y The y-coordinates of the vertices of the piecewise linear
   *     function to integrate.
   * @return The integral of the piecewise linear function.
   * @throws IllegalArgumentException if <code>y.length == 0</code>.
   */
  public static double trapz(double[] y) {
    if (y.length == 0) {
      throw new IllegalArgumentException("y.length == 0");
    }
    double value = 0.0;
    double y0 = y[0];
    for (int i = 0; i < y.length; i++) {
      double y1 = y[i];
      double ym = y0 + y1;
      value += ym;
      y0 = y1;
    }
    return value / 2.0;
  }

  /**
   * Computes the integral of the piecewise linear function through the given
   * points (<code>x[i]</code>, <code>y[i]</code>) using the trapezoidal
   * method.
   * @param x The x-coordinates of the vertices of the piecewise linear
   *     function to integrate.
   * @param y The y-coordinates of the vertices of the piecewise linear
   *     function to integrate.
   * @return The integral of the piecewise linear function.
   * @throws IllegalArgumentException if <code>x.length != y.length</code>.
   * @throws IllegalArgumentException if <code>y.length == 0</code>.
   */
  public static double trapz(double[] x, double[] y) {
    if (x.length != y.length) {
      throw new IllegalArgumentException("x.length != y.length");
    }
    if (y.length == 0) {
      throw new IllegalArgumentException("y.length == 0");
    }
    double value = 0.0;
    double x0 = x[0];
    double y0 = y[0];
    for (int i = 1; i < y.length; i++) {
      double x1 = x[i];
      double y1 = y[i];
      double dx = x1 - x0;
      double ym = y0 + y1;
      value += dx * ym;
      x0 = x1;
      y0 = y1;
    }
    return value / 2.0;
  }

  /**
   * Computes the integral of the piecewise linear function through the given
   * points (<code>i</code>, <code>y[i]</code>) using the trapezoidal
   * method.
   * @param y The y-coordinates of the vertices of the piecewise linear
   *     function to integrate.
   * @return The integral of the piecewise linear function.
   * @throws IllegalArgumentException if <code>y</code> is empty.
   */
  public static double trapz(Iterable<Double> y) {
    Iterator<Double> ys = y.iterator();
    if (!ys.hasNext()) {
      throw new IllegalArgumentException("y is empty.");
    }
    double value = 0.0;
    double y0 = ys.next();
    while (ys.hasNext()) {
      double y1 = ys.next();
      double ym = y0 + y1;
      value += ym;
      y0 = y1;
    }
    return value / 2.0;
  }

  /**
   * Computes the integral of the piecewise linear function through the given
   * points (<code>x[i]</code>, <code>y[i]</code>) using the trapezoidal
   * method.
   * @param x The x-coordinates of the vertices of the piecewise linear
   *     function to integrate.
   * @param y The y-coordinates of the vertices of the piecewise linear
   *     function to integrate.
   * @return The integral of the piecewise linear function.
   * @throws IllegalArgumentException if the lengths of <code>x</code> and
   *     <code>y</code> differ.
   * @throws IllegalArgumentException if <code>y</code> is empty.
   */
  public static double trapz(Iterable<Double> x, Iterable<Double> y) {
    Iterator<Double> xs = x.iterator();
    Iterator<Double> ys = y.iterator();
    if (!ys.hasNext()) {
      throw new IllegalArgumentException("y is empty.");
    }
    if (!xs.hasNext()) {
      throw new IllegalArgumentException("Lengths of x and y differ.");
    }
    double value = 0.0;
    double x0 = xs.next();
    double y0 = ys.next();
    while (ys.hasNext()) {
      if (!xs.hasNext()) {
        throw new IllegalArgumentException("Lengths of x and y differ.");
      }
      double x1 = xs.next();
      double y1 = ys.next();
      double dx = x1 - x0;
      double ym = y0 + y1;
      value += dx * ym;
      x0 = x1;
      y0 = y1;
    }
    if (xs.hasNext()) {
      throw new IllegalArgumentException("Lengths of x and y differ.");
    }
    return value / 2.0;
  }

  /**
   * Computes the cumulative sum of the specified array in place.
   *
   * Each element <code>x[i]</code> is replaced with
   * <code>x[0] + ... + x[i]</code>.
   *
   * @param x The array of doubles for which to compute the cumulative sum.
   * @return A reference to <code>x</code>.
   */
  public static double[] cumsum(double[] x) {
    for (int i = 1; i < x.length; i++) {
      x[i] += x[i - 1];
    }
    return x;
  }

  /**
   * Computes the cumulative sum of the specified array in place.
   *
   * Each element <code>x[i]</code> is replaced with
   * <code>x[0] + ... + x[i]</code>.
   *
   * @param x The array of ints for which to compute the cumulative sum.
   * @return A reference to <code>x</code>.
   */
  public static int[] cumsum(int[] x) {
    for (int i = 1; i < x.length; i++) {
      x[i] += x[i - 1];
    }
    return x;
  }

  /**
   * Computes the cumulative sum of the specified array in place.
   *
   * Each element <code>x[i]</code> is replaced with
   * <code>x[0] + ... + x[i]</code>.
   *
   * @param x The array of doubles for which to compute the cumulative sum.
   * @return A reference to <code>x</code>.
   */
  public static long[] cumsum(long[] x) {
    for (int i = 1; i < x.length; i++) {
      x[i] += x[i - 1];
    }
    return x;
  }

  /**
   * Computes the cumulative sum of the specified <code>List</code> in place.
   *
   * Each element <code>x.get(i)</code> is replaced with
   * <code>x.get(0) + ... + x.get(1)</code>.
   *
   * @param x The <code>List</code> of doubles for which to compute the
   *     cumulative sum.
   * @return A reference to <code>x</code>.
   */
  public static List<Double> cumsum(List<Double> x) {
    for (int i = 1, n = x.size(); i < n; i++) {
      x.set(i, x.get(i) + x.get(i - 1));
    }
    return x;
  }

  /**
   * Computes the cumulative product of the specified array in place.
   *
   * Each element <code>x[i]</code> is replaced with
   * <code>x[0] * ... * x[i]</code>.
   *
   * @param x The array of doubles for which to compute the cumulative
   *     product.
   * @return A reference to <code>x</code>.
   */
  public static double[] cumprod(double[] x) {
    for (int i = 1; i < x.length; i++) {
      x[i] *= x[i - 1];
    }
    return x;
  }

  /**
   * Computes the cumulative product of the specified array in place.
   *
   * Each element <code>x[i]</code> is replaced with
   * <code>x[0] * ... * x[i]</code>.
   *
   * @param x The array of ints for which to compute the cumulative product.
   * @return A reference to <code>x</code>.
   */
  public static int[] cumprod(int[] x) {
    for (int i = 1; i < x.length; i++) {
      x[i] *= x[i - 1];
    }
    return x;
  }

  /**
   * Computes the cumulative product of the specified array in place.
   *
   * Each element <code>x[i]</code> is replaced with
   * <code>x[0] * ... * x[i]</code>.
   *
   * @param x The array of longs for which to compute the cumulative product.
   * @return A reference to <code>x</code>.
   */
  public static long[] cumprod(long[] x) {
    for (int i = 1; i < x.length; i++) {
      x[i] *= x[i - 1];
    }
    return x;
  }

  /**
   * Computes the cumulative product of the specified <code>List</code> in
   * place.
   *
   * Each element <code>x.get(i)</code> is replaced with
   * <code>x.get(0) * ... * x.get(1)</code>.
   *
   * @param x The <code>List</code> of doubles for which to compute the
   *     cumulative product.
   * @return A reference to <code>x</code>.
   */
  public static List<Double> cumprod(List<Double> x) {
    for (int i = 1, n = x.size(); i < n; i++) {
      x.set(i, x.get(i) * x.get(i - 1));
    }
    return x;
  }

  /**
   * Computes the dot product of two vectors.
   * @param a The first array.
   * @param b The second array.
   * @return The sum of <code>a[i] * b[i]</code>.
   * @throws IllegalArgumentException if <code>a.length != b.length</code>.
   */
  public static double dot(double[] a, double[] b) {
    if (a.length != b.length) {
      throw new IllegalArgumentException("a.length != b.length");
    }
    double sum = 0.0;
    for (int i = 0; i < a.length; i++) {
      sum += a[i] * b[i];
    }
    return sum;
  }

  /**
   * Computes the dot product of two vectors.
   * @param a The first array.
   * @param b The second array.
   * @return The sum of <code>a[i] * b[i]</code>.
   * @throws IllegalArgumentException if <code>a.length != b.length</code>.
   */
  public static int dot(int[] a, int[] b) {
    if (a.length != b.length) {
      throw new IllegalArgumentException("a.length != b.length");
    }
    int sum = 0;
    for (int i = 0; i < a.length; i++) {
      sum += a[i] * b[i];
    }
    return sum;
  }

  /**
   * Computes the dot product of two vectors.
   * @param a The first array.
   * @param b The second array.
   * @return The sum of <code>a[i] * b[i]</code>.
   * @throws IllegalArgumentException if <code>a.length != b.length</code>.
   */
  public static long dot(long[] a, long[] b) {
    if (a.length != b.length) {
      throw new IllegalArgumentException("a.length != b.length");
    }
    long sum = 0;
    for (int i = 0; i < a.length; i++) {
      sum += a[i] * b[i];
    }
    return sum;
  }

  /**
   * Linearly remaps the values in an array to fit within the specified
   * interval.
   * @param x The array of doubles to remap.
   * @param min The value to remap the minimum value of <code>x</code> to.
   * @param max The value to remap the maximum value of <code>x</code> to.
   * @return A reference to <code>x</code>.
   */
  public static double[] remap(double[] x, double min, double max) {
    double xmin = Double.POSITIVE_INFINITY;
    double xmax = Double.NEGATIVE_INFINITY;

    for (int i = 0; i < x.length; i++) {
      if (x[i] < xmin) { xmin = x[i]; }
      if (x[i] > xmax) { xmax = x[i]; }
    }

    double xrange = xmax - xmin;
    double range = max - min;

    for (int i = 0; i < x.length; i++) {
      x[i] = min + range * ((x[i] - xmin) / xrange);
    }

    return x;
  }

  /**
   * Linearly remaps the values in an array to fit within the specified
   * interval.
   * @param x The array of doubles to remap.
   * @param I The <code>Interval</code> to remap <code>x</code> to.
   * @return A reference to <code>x</code>.
   */
  public static double[] remap(double[] x, Interval I) {
    return remap(x, I.minimum(), I.maximum());
  }

  /**
   * Linearly remaps the values in an array to fit within the interval
   * <code>[0,1]</code>.
   * @param x The array of doubles to remap.
   * @return A reference to <code>x</code>.
   */
  public static double[] remap(double[] x) {
    return remap(x, 0.0, 1.0);
  }

  /**
   * A comparison threshold value to be used when a very high degree
   * of precision is expected.
   */
  public static final double TINY_EPSILON = 1e-12;

  /**
   * A comparison threshold value to be used when a high degree of
   * precision is expected.
   */
  public static final double SMALL_EPSILON = 1e-9;

  /**
   * A comparison threshold value to be used when a normal degree of
   * precision is expected.
   */
  public static final double EPSILON = 1e-6;

  /**
   * A comparison threshold value to be used when a low degree of
   * precision is expected.
   */
  public static final double BIG_EPSILON = 1e-4;

  /**
   * The difference between 1.0 and the next highest representable value.
   */
  public static final double MACHINE_EPSILON = Math.ulp(1.0);

  /**
   * This class contains only static utility methods and static constants,
   * and therefore should not be creatable.
   */
  private MathUtil() {}

}
