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
package ca.eandb.jmist.framework.random;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import ca.eandb.jmist.framework.Random;
import ca.eandb.util.DoubleArray;

/**
 * A categorical random variable (i.e., a discrete random variable that selects
 * a value with probability proportional to specified weights).
 * @author Brad Kimmel
 */
public final class CategoricalRandom implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1462018249783769480L;

  /** The cumulative probability function. */
  private final double[] cpf;

  /**
   * The <code>Random</code> number generator to use to seed this
   * <code>CategoricalRandom</code>.
   */
  private final Random source;

  /**
   * Creates a new <code>CategoricalRandom</code>.
   * @param weights An array of the weights associated with each integer
   *     from zero to <code>weights.length - 1</code>.
   * @param source The <code>Random</code> number generator to use to seed
   *     this <code>CategoricalRandom</code>.
   */
  public CategoricalRandom(DoubleArray weights, Random source) {
    this.source = source;
    this.cpf = weights.toDoubleArray();
    initialize();
  }

  /**
   * Creates a new <code>CategoricalRandom</code>.
   * @param weights An array of the weights associated with each integer
   *     from zero to <code>weights.length - 1</code>.
   */
  public CategoricalRandom(DoubleArray weights) {
    this(weights, null);
  }

  /**
   * Creates a new <code>CategoricalRandom</code>.
   * @param weights An array of the weights associated with each integer
   *     from zero to <code>weights.length - 1</code>.
   * @param source The <code>Random</code> number generator to use to seed
   *     this <code>CategoricalRandom</code>.
   */
  public CategoricalRandom(List<Double> weights, Random source) {
    this.source = source;
    this.cpf = new double[weights.size()];
    for (int i = 0; i < cpf.length; i++) {
      cpf[i] = weights.get(i);
    }
    initialize();
  }

  /**
   * Creates a new <code>CategoricalRandom</code>.
   * @param weights An array of the weights associated with each integer
   *     from zero to <code>weights.length - 1</code>.
   */
  public CategoricalRandom(List<Double> weights) {
    this(weights, null);
  }

  /**
   * Creates a new <code>CategoricalRandom</code>.
   * @param weights An array of the weights associated with each integer
   *     from zero to <code>weights.length - 1</code>.
   * @param source The <code>Random</code> number generator to use to seed
   *     this <code>CategoricalRandom</code>.
   */
  public CategoricalRandom(double[] weights, Random source) {
    this.source = source;
    this.cpf = weights.clone();
    initialize();
  }

  /**
   * Creates a new <code>CategoricalRandom</code>.
   * @param weights An array of the weights associated with each integer
   *     from zero to <code>weights.length - 1</code>.
   */
  public CategoricalRandom(double[] weights) {
    this(weights, null);
  }

  /**
   * Initializes the cumulative probability function for this
   * <code>CategoricalRandom</code>.
   */
  private void initialize() {
    /* Compute the cumulative probability function. */
    for (int i = 1; i < cpf.length; i++) {
      this.cpf[i] += this.cpf[i - 1];
    }

    for (int i = 0; i < cpf.length; i++) {
      this.cpf[i] /= this.cpf[cpf.length - 1];
    }
  }

  /**
   * Generates a new sample of this <code>CategoricalRandom</code>
   * variable.
   * @param random The <code>Random</code> to use to generate random number
   *     samples.
   * @return The next sample.
   */
  public int next(Random random) {
    return next(source != null ? source.next() : RandomUtil.canonical(random));
  }

  /**
   * Generates a new sample of this <code>CategoricalRandom</code>
   * variable.
   * @param seed The seed value.  This method is guaranteed to return the
   *     same value given the same seed.
   * @return The next sample.
   */
  public int next(double seed) {
    int index = Arrays.binarySearch(this.cpf, seed);
    return index >= 0 ? index : -(index + 1);
  }

  private void bp() {}

  public int next(SeedReference ref) {
    int index = Arrays.binarySearch(this.cpf, ref.seed);
    if (ref.seed > 1.0) {
      bp();
    }
    index = (index >= 0) ? index : -(index + 1);
    if (index > 0) {
      ref.seed -= cpf[index - 1];
    }
    ref.seed /= getProbability(index);
    if (ref.seed > 1.0) bp();
    return index;
  }

  /**
   * Gets the probability that this <code>CategoricalRandom</code> yields
   * the specified number.
   * @param value The value to get the probability of.
   * @return The probability for the specified value.
   */
  public double getProbability(int value) {
    if (value < 0 || value >= cpf.length) {
      return 0.0;
    } else if (value == 0) {
      return cpf[0];
    } else {
      return cpf[value] - cpf[value - 1];
    }
  }

}
