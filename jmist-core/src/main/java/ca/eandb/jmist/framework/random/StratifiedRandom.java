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
package ca.eandb.jmist.framework.random;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ca.eandb.jmist.framework.Random;

/**
 * A random number generator that stratifies the results into a
 * number, {@code n}, of equally sized divisions of {@code [0, 1)}.  The
 * random number generator is guaranteed to generate exactly one value
 * in each interval {@code [i/n, (i+1)/n)} for each block of {@code n}
 * successive calls to {@link #next()}.
 * @author Brad Kimmel
 */
public final class StratifiedRandom implements Random {

  /**
   * Default constructor.  Creates a random number generator equivalent to
   * a {@link SimpleRandom}.  Also equivalent to {@link #StratifiedRandom(int)}
   * with {@code n == 1}.
   */
  public StratifiedRandom(Random inner) {
    this(1, inner);
  }

  /**
   * Creates a random number generator that is guaranteed to generate one
   * value in each of the intervals {@code [i/n, (i+1)/n)}, where
   * {@code 0 <= i < n} for each block of {@code n} successive calls to
   * {@link #next()}.
   * @param n The number of intervals to divide the interval [0, 1) into.
   * @see #next()
   */
  public StratifiedRandom(int n, Random inner) {
    this.inner = inner;
    this.initialize(n);
  }

  /**
   * Default constructor.  Creates a random number generator equivalent to
   * a {@link SimpleRandom}.  Also equivalent to {@link #StratifiedRandom(int)}
   * with {@code n == 1}.
   */
  public StratifiedRandom() {
    this(1, new SimpleRandom());
  }

  /**
   * Creates a random number generator that is guaranteed to generate one
   * value in each of the intervals {@code [i/n, (i+1)/n)}, where
   * {@code 0 <= i < n} for each block of {@code n} successive calls to
   * {@link #next()}.
   * @param n The number of intervals to divide the interval [0, 1) into.
   * @see #next()
   */
  public StratifiedRandom(int n) {
    this(n, new SimpleRandom());
  }

  /*
   * (non-Javadoc)
   * @see ca.eandb.jmist.framework.Random#next()
   */
  public double next() {

    // Randomly pick a bucket from which to generate a random number.
    int j              = RandomUtil.discrete(0, this.nextPartition, inner);

    // Swap the bucket index to the back portion of the list so that
    // we don't use it again (until the next block).
    int temp            = this.sequence[j];
    this.sequence[j]        = this.sequence[nextPartition];
    this.sequence[nextPartition]  = temp;

    // Decrement the index to mark the current bucket as used.  If all
    // buckets have been used after this call, then reset.
    if (--nextPartition < 0) {
      this.nextPartition      = this.sequence.length - 1;
    }

    // Generate a random number in the chosen bucket.
    return (((double) temp) + RandomUtil.canonical(inner)) / ((double) this.sequence.length);

  }

  /*
   * (non-Javadoc)
   * @see ca.eandb.jmist.framework.Random#reset()
   */
  public void reset() {
    this.nextPartition = this.sequence.length - 1;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
   */
  public StratifiedRandom createCompatibleRandom() {
    return new StratifiedRandom(this.sequence.length, inner.createCompatibleRandom());
  }

  /**
   * Resets this random number generator.  Following invocation of this
   * method, each block of {@code n} successive calls to {@link #next()}
   * is guaranteed to yield exactly one value from each interval
   * {@code [i/n, (i+1)/n)}, where {@code 0 <= i < n}.
   * @param n The number of intervals to divide the interval
   *     {@code [0, 1)} into.
   */
  public void reset(int n) {
    if (this.sequence.length == n) {
      this.reset();
    } else {
      this.initialize(n);
    }
  }

  /**
   * Initializes this random number generator to yield exactly one number
   * in each interval {@code [i/n, (i+1)/n)} ({@code 0 <= i < n}) for each
   * block of {@code n} successive calls to {@link #next()}.
   * @param n The number of intervals to divide the interval {@code [0, 1)}
   *     into.
   */
  private void initialize(int n) {

    this.nextPartition    = n - 1;
    this.sequence      = new int[n];

    for (int i = 0; i < this.sequence.length; i++) {
      this.sequence[i]  = i;
    }

  }


  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    oos.writeInt(sequence.length);
  }

  private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    int n = ois.readInt();
    this.initialize(n);
  }

  /**
   * This <code>Random</code> is used to permute the buckets and to select
   * a random number within each bucket.
   */
  private final Random inner;

  /**
   * This array will hold the indices {@code i}, where {@code 0 <= i < n},
   * representing each of the {@code n} buckets {@code [i/n, (i+1)/n)}.
   * During each call to {@link #next()}, we will randomly choose one of
   * the remaining buckets and generate a random number in that bucket.
   */
  private transient int[] sequence;

  /**
   * The index (into {@link #sequence} of the last unused bucket.  All
   * buckets in {@link #sequence} after this index have been used, all
   * buckets not after this index have not yet been used.
   */
  private transient int nextPartition;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -8086239299607234353L;

}
