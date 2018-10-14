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

import ca.eandb.jmist.framework.Random;

/**
 * Adapts a <code>java.util.Random</code>.
 * @author Brad Kimmel
 */
public final class RandomAdapter implements Random {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 3052795767906803323L;

  /** The wrapped <code>java.util.Random</code>. */
  private final java.util.Random rnd;

  /**
   * Creates a new <code>RandomAdapter</code>.
   * @param rnd The <code>java.util.Random</code> to adapt.
   */
  public RandomAdapter(java.util.Random rnd) {
    this.rnd = rnd;
  }

  /**
   * Creates a new <code>RandomAdapter</code>.
   */
  public RandomAdapter() {
    this(new java.util.Random());
  }

  /**
   * Creates a new <code>RandomAdapter</code>.
   * @param seed The seed used by the random number generator.
   */
  public RandomAdapter(long seed) {
    this(new java.util.Random(seed));
  }

  /**
   * Sets the seed used by the inner <code>java.util.Random</code>.
   * @param seed The seed used by the random number generator.
   */
  public void setSeed(long seed) {
    rnd.setSeed(seed);
  }

  @Override
  public Random createCompatibleRandom() {
    return new RandomAdapter(rnd);
  }

  @Override
  public double next() {
    return rnd.nextDouble();
  }

  @Override
  public void reset() {
    /* nothing to do. */
  }

}
