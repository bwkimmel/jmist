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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.BitSet;
import java.util.Iterator;

/**
 * An immutable array of double precision values.
 * @author Brad Kimmel
 */
public final class Tuple extends AbstractList<Double> implements Serializable {

  /**
   * Creates an empty Tuple.
   */
  public Tuple() {
    this.values = new double[0];
  }

  /**
   * Creates a Tuple with one element.
   * @param _0 The first element.
   */
  public Tuple(double _0) {
    this.values = new double[]{_0};
  }

  /**
   * Creates a Tuple with two elements.
   * @param _0 The first element.
   * @param _1 The second element.
   */
  public Tuple(double _0, double _1) {
    this.values = new double[]{_0, _1};
  }

  /**
   * Creates a Tuple with three elements.
   * @param _0 The first element.
   * @param _1 The second element.
   * @param _2 The third element.
   */
  public Tuple(double _0, double _1, double _2) {
    this.values = new double[]{_0, _1, _2};
  }

  /**
   * Creates a Tuple with four elements.
   * @param _0 The first element.
   * @param _1 The second element.
   * @param _2 The third element.
   * @param _3 The fourth element.
   */
  public Tuple(double _0, double _1, double _2, double _3) {
    this.values = new double[]{_0, _1, _2, _3};
  }

  /**
   * Initializes a Tuple from an array.
   * @param values An array containing the elements of
   *     the new Tuple.
   */
  public Tuple(double... values) {
    this.values = (double[]) values.clone();
  }

  /**
   * Creates an uninitialized Tuple of the specified length.
   * This is used internally to create tuples within other
   * methods which will be populated within the method and
   * then returned.
   * @param size The length of the Tuple.
   */
  private Tuple(int size) {
    this.values = new double[size];
  }

  /**
   * Gets an element of the Tuple.
   * @param index The index of the element to obtain.  Must
   *     be less than {@code this.size()}.
   * @return The element at the specified index.
   * @see #size()
   */
  public double at(int index) {
    return this.values[index];
  }

  /**
   * Gets the number of elements in the Tuple.
   * @return The number of elements.
   */
  public int size() {
    return this.values.length;
  }

  /**
   * Gets a value indicating whether the Tuple is empty.
   * Equivalent to {@code this.size() == 0}.
   * @return A value indicating whether the Tuple is empty.
   * @see #size()
   */
  public boolean isEmpty() {
    return this.values.length == 0;
  }

  /**
   * Appends another Tuple to this one.
   * @param other The tuple to append.
   * @return The concatenation of this Tuple with the other
   *     Tuple.
   */
  public Tuple append(Tuple other) {

    Tuple result = new Tuple(this.values.length + other.values.length);
    int i, j;

    // Add the elements of the first tuple.
    for (i = 0; i < this.values.length; i++) {
      result.values[i] = this.values[i];
    }

    // Append the elements of the second tuple.
    for (i = 0, j = this.values.length; i < other.values.length; i++, j++) {
      result.values[j] = other.values[i];
    }

    return result;

  }

  /**
   * Appends a single value to the end of the Tuple.
   * Equivalent to {@code this.append(new Tuple(value))}.
   * @param value The value to append.
   * @return A Tuple with the elements of this Tuple plus
   *     the specified value.
   * @see #Tuple(double)
   * @see #append(Tuple)
   */
  public Tuple append(double value) {

    Tuple result = new Tuple(this.values.length + 1);

    // Copy the elements of this tuple.
    for (int i = 0; i < this.values.length; i++) {
      result.values[i] = this.values[i];
    }

    // Add the new value to the end.
    result.values[result.values.length - 1] = value;

    return result;

  }

  /**
   * Appends an array of values to the end of this Tuple.
   * Equivalent to {@code this.append(new Tuple(values))}.
   * @param values The array of values to append.
   * @return The concatenation of this Tuple with the given
   *     array of values.
   * @see #Tuple(double[])
   * @see #append(Tuple)
   */
  public Tuple append(double[] values) {

    Tuple result = new Tuple(this.values.length + values.length);
    int i, j;

    // Copy the elements of this tuple.
    for (i = 0; i < this.values.length; i++) {
      result.values[i] = this.values[i];
    }

    // Add the elements of the array to the end.
    for (i = 0, j = this.values.length; i < values.length; i++, j++) {
      result.values[j] = values[i];
    }

    return result;

  }

  /**
   * Returns a Tuple containing the specified number of
   * the leftmost elements of this Tuple.  Equivalent to
   * {@code this.slice(0, length)}.
   * @param length The number of elements.  Must not exceed
   *     {@code this.size()}.
   * @return A Tuple containing the specified number of
   *     the left most elements of this Tuple.
   * @see #slice(int, int)
   * @see #size()
   */
  public Tuple left(int length) {

    assert(length <= this.values.length);

    Tuple result = new Tuple(length);

    for (int i = 0; i < length; i++) {
      result.values[i] = this.values[i];
    }

    return result;

  }

  /**
   * Returns a Tuple containing the specified number of
   * the rightmost elements of this Tuple.  Equivalent to
   * {@code this.slice(this.size() - length, this.size())}.
   * @param length The number of elements.  Must not exceed
   *     {@code this.size()}.
   * @return A Tuple containing the specified number of
   *     the left most elements of this Tuple.
   * @see #slice(int, int)
   * @see #size()
   */
  public Tuple right(int length) {

    assert(length <= this.values.length);

    Tuple result = new Tuple(length);
    int i, j;

    for (i = 0, j = this.values.length - length; i < length; i++, j++) {
      result.values[i] = this.values[j];
    }

    return result;

  }

  /**
   * Returns a contiguous subset of this Tuple.
   * @param start The inclusive lower bound of the range
   *     to return.  Must be non-negative.
   * @param end The exclusive upper bound of the range to
   *     return.  Must be no less than {@code start} and
   *     no greater than {@code this.size()}.
   * @return A Tuple containing the elements at the indices
   *     in [start, end).
   * @see #size()
   */
  public Tuple slice(int start, int end) {

    assert(0 <= start && end <= this.values.length);

    Tuple result = new Tuple(end - start);
    int i, j;

    for (i = start, j = 0; i < end; i++, j++) {
      result.values[j] = this.values[i];
    }

    return result;

  }

  /**
   * Reverses this Tuple.
   * @return A Tuple containing the elements of this Tuple
   *     in the opposite order.
   */
  public Tuple reverse() {

    Tuple result = new Tuple(this.values.length);
    int i, j;

    for (i = 0, j = this.values.length - 1; i < this.values.length; i++, j--) {
      result.values[i] = this.values[j];
    }

    return result;

  }

  /**
   * Permutes this Tuple.
   * @param indices The indices to extract from this Tuple.
   *     Each index must be less than {@code this.size()}.
   * @return A Tuple containing the elements of this Tuple
   *     at the indices specified in "indices".
   * @see #size()
   */
  public Tuple permute(int[] indices) {

    Tuple result = new Tuple(indices.length);

    for (int i = 0; i < indices.length; i++) {
      result.values[i] = this.values[indices[i]];
    }

    return result;

  }

  /**
   * Represents a function from the real numbers to the
   * real numbers.  Used by {@code Tuple.map}.
   * @see #map(ca.eandb.jmist.math.Tuple.Function)
   */
  public interface Function {
    double apply(double value);
  }

  /**
   * Applies a function to each element in this Tuple.
   * The resulting Tuple will have the same size as this
   * Tuple.
   * @param f The function to apply.
   * @return A Tuple containing the elements
   *     {@code f.apply(x)}, for each element {@code x} in
   *     this Tuple.
   * @see Tuple.Function
   * @see #size()
   */
  public Tuple map(Tuple.Function f) {
    Tuple result = new Tuple(this.values.length);

    for (int i = 0; i < this.values.length; i++) {
      result.values[i] = f.apply(this.values[i]);
    }

    return result;
  }

  /**
   * A binary operator on the real numbers.  Used by
   * {@code Tuple.combine} and {@code Tuple.reduce}.
   * @see Tuple#combine(Tuple, ca.eandb.jmist.math.Tuple.Operator)
   * @see Tuple#reduce(ca.eandb.jmist.math.Tuple.Operator)
   * @see Tuple#reduce(ca.eandb.jmist.math.Tuple.Operator, double)
   */
  public interface Operator {
    double apply(double a, double b);
  }

  /**
   * Combines this Tuple with another by applying a binary
   * operator.
   * @param other The Tuple to combine with this Tuple.  Must
   *     have the same size as this Tuple.
   * @param operator The binary operator to use to combine
   *     each element.
   * @return A Tuple with the same number of elements as this
   *     Tuple (and {@code other}), consisting of the result
   *     of the binary operator applied to each pair of
   *     elements in the operand Tuples.
   * @see Tuple.Operator
   * @see #size()
   */
  public Tuple combine(Tuple other, Tuple.Operator operator) {

    assert(this.values.length == other.values.length);

    Tuple result = new Tuple(this.values.length);

    for (int i = 0; i < this.values.length; i++) {
      result.values[i] = operator.apply(this.values[i], other.values[i]);
    }

    return result;

  }

  /**
   * Accumulates the elements of this Tuple using the
   * specified operator.
   * @param operator The operator to use to accumulate the
   *     elements of this Tuple.
   * @return {@code Double.NaN} if this Tuple is empty
   *     ({@code this.isEmpty()}).  {@code this.at(0)} if
   *     the Tuple has one element ({@code this.size() == 1}).
   *     {@code operator.apply(this.left(this.size() - 1).reduce(operator), this.at(this.size() - 1))}
   *     if the Tuple has more than one element ({@code this.size() > 1}).
   * @see Tuple.Operator
   * @see Double#NaN
   * @see #isEmpty()
   * @see #size()
   * @see #at(int)
   * @see #left(int)
   */
  public double reduce(Tuple.Operator operator) {
    if (this.values.length != 0) {
      double result = this.values[0];

      for (int i = 1; i < this.values.length; i++) {
        result = operator.apply(result, this.values[i]);
      }

      return result;
    } else {
      return Double.NaN;
    }
  }

  /**
   * Accumulates the elements of this Tuple using the
   * specified operator.  Equivalent to
   * {@code new Tuple(initializer).append(this).reduce(operator)}.
   * @param operator The operator to use to accumulate the
   *     elements of this Tuple.
   * @param initializer The value to which to initiailize
   *     the accumulator.
   * @return {@code initializer} if this Tuple is empty
   *     ({@code this.isEmpty()}).
   *     {@code operator.apply(this.left(this.size() - 1).reduce(operator, initializer), this.at(this.size() - 1))}
   *     if the Tuple is non-empty ({@code !this.isEmpty()}).
   * @see Tuple.Operator
   * @see #isEmpty()
   * @see #at(int)
   * @see #left(int)
   * @see #reduce(ca.eandb.jmist.math.Tuple.Operator)
   * @see #Tuple(double)
   * @see #append(double)
   */
  public double reduce(Tuple.Operator operator, double initializer) {
    double result = initializer;

    for (int i = 0; i < this.values.length; i++) {
      result = operator.apply(result, this.values[i]);
    }

    return result;
  }

  /**
   * Filters the real numbers.  Used by {@code Tuple.filter}.
   * @see Tuple#filter(ca.eandb.jmist.math.Tuple.Filter)
   */
  public interface Filter {
    boolean apply(double value);
  }

  /**
   * Filters this Tuple.
   * @param filter The filter to apply.
   * @return A Tuple containing the elements from this
   *     Tuple that passed the filter (i.e., the elements,
   *     x, for which {@code filter.apply(x) == true}.
   * @see Tuple.Filter
   */
  public Tuple filter(Tuple.Filter filter) {

    Tuple result;
    BitSet mask = new BitSet(this.values.length);
    int count = 0;
    int i, j;

    // Count the number of elements that pass the filter
    // and remember which elements to include in the
    // result.
    for (i = 0; i < this.values.length; i++) {
      if (filter.apply(this.values[i])) {
        mask.set(i);
        count++;
      } else {
        mask.clear(i);
      }
    }

    // Create a new tuple of length given by the count
    // from above.
    result = new Tuple(count);

    // Copy the elements that passed the filter into
    // the result.
    for (i = 0, j = 0; i < this.values.length; i++) {
      if (mask.get(i)) {
        result.values[j++] = this.values[i];
      }
    }

    return result;

  }

  @Override
  public Iterator<Double> iterator() {
    return new TupleIterator();
  }

  @Override
  public Double get(int index) {
    return at(index);
  }

  /**
   * An <code>Iterator</code> for iterating a <code>Tuple</code>.
   */
  private class TupleIterator implements Iterator<Double> {

    /** The index of the next element. */
    private int index = 0;

    @Override
    public boolean hasNext() {
      return index < values.length;
    }

    @Override
    public Double next() {
      return values[index++];
    }

  }

  /**
   * Convers this Tuple to an array of doubles.
   * @return An array containing the elements of this Tuple.
   */
  public double[] toDoubleArray() {
    return (double[]) this.values.clone();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < this.values.length; i++) {
      if (i > 0) {
        builder.append(" ");
      }
      builder.append(this.values[i]);
    }
    return builder.toString();
  }

  /**
   * Returns a Tuple containing all zeros.
   * @param length The length of the Tuple.
   * @return A Tuple containing the specified number of
   *     zeros.
   */
  public static Tuple zeros(int length) {

    // no need to specifically set each element to
    // zero since Java already does this.
    return new Tuple(length);

  }

  /**
   * Returns a Tuple containing all ones.
   * @param length The length of the Tuple.
   * @return A Tuple containing the specified number of
   *     ones.
   */
  public static Tuple ones(int length) {

    Tuple result = new Tuple(length);

    for (int i = 0; i < length; i++) {
      result.values[i] = 1.0;
    }

    return result;

  }

  /**
   * A binary operator that adds its operands.
   */
  public static final Operator SUM_OPERATOR = new Operator() {

    public double apply(double a, double b) {
      return a + b;
    }

  };

  /**
   * A binary operator that multiplies its operands.
   */
  public static final Operator PRODUCT_OPERATOR = new Operator() {

    public double apply(double a, double b) {
      return a * b;
    }

  };

  /**
   * The empty Tuple (i.e., {@code Tuple.EMPTY.size() == 0}).
   * @see #isEmpty()
   * @see #size()
   */
  public static final Tuple EMPTY = new Tuple();

  /** The elements of the Tuple. */
  private final double[] values;

  /** Serialization version ID. */
  private static final long serialVersionUID = 5809622978157652345L;

}
