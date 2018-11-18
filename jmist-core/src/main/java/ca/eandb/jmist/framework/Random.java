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
package ca.eandb.jmist.framework;

import java.io.Serializable;

/**
 * A random number generator.
 * @author Brad Kimmel
 */
public interface Random extends Serializable {

  /**
   * Returns the next random number in the sequence.
   * @return A random number in [0, 1).
   */
  double next();

  /**
   * Resets the sequence of random numbers.
   */
  void reset();

  /**
   * Creates a new <code>Random</code> with the same behavior as this
   * <code>Random</code>.
   * @return The new <code>Random</code>.
   */
  Random createCompatibleRandom();

  /** A default implementation of <code>Random</code>. */
  Random DEFAULT = new Random() {

    /** Serialization version ID. */
    private static final long serialVersionUID = -6299190146689205359L;

    @Override
    public Random createCompatibleRandom() {
      return this;
    }

    @Override
    public double next() {
      return Math.random();
    }

    @Override
    public void reset() {
      /* nothing to do. */
    }

  };

}
