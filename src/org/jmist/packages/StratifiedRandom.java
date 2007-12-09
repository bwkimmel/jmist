/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.Random;

/**
 * A random number generator that stratifies the results into a
 * number, {@code n}, of equally sized divisions of {@code [0, 1)}.  The
 * random number generator is guaranteed to generate exactly one value
 * in each interval {@code [i/n, (i+1)/n)} for each block of {@code n}
 * successive calls to {@link #next()}.
 * @author bkimmel
 */
public final class StratifiedRandom implements Random {

	/**
	 * Default constructor.  Creates a random number generator equivalent to
	 * a {@link SimpleRandom}.  Also equivalent to {@link #StratifiedRandom(int)}
	 * with {@code n == 1}.
	 */
	public StratifiedRandom() {
		this.initialize(1);
	}

	/**
	 * Creates a random number generator that is guaranteed to generate one
	 * value in each of the intervals {@code [i/n, (i+1)/n)}, where
	 * {@code 0 <= i < n} for each block of {@code n} successive calls to
	 * {@link #next()}.
	 * @param n The number of intervals to divide the interval [0, 1) into.
	 * @see {@link #next()}.
	 */
	public StratifiedRandom(int n) {
		this.initialize(n);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.Random#next()
	 */
	public double next() {

		// Randomly pick a bucket from which to generate a random number.
		int j							= source.nextInt(this.nextPartition + 1);

		// Swap the bucket index to the back portion of the list so that
		// we don't use it again (until the next block).
		int temp						= this.sequence[j];
		this.sequence[j]				= this.sequence[nextPartition];
		this.sequence[nextPartition]	= temp;

		// Decrement the index to mark the current bucket as used.  If all
		// buckets have been used after this call, then reset.
		if (--nextPartition < 0) {
			this.nextPartition			= this.sequence.length - 1;
		}

		// Generate a random number in the chosen bucket.
		return (((double) temp) + source.nextDouble()) / ((double) this.sequence.length);

	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.Random#reset()
	 */
	public void reset() {
		this.nextPartition = this.sequence.length - 1;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Random#createCompatibleRandom()
	 */
	@Override
	public StratifiedRandom createCompatibleRandom() {
		return new StratifiedRandom(this.sequence.length);
	}

	/**
	 * Resets this random number generator.  Following invocation of this
	 * method, each block of {@code n} successive calls to {@link #next()}
	 * is guaranteed to yield exactly one value from each interval
	 * {@code [i/n, (i+1)/n)}, where {@code 0 <= i < n}.
	 * @param n The number of intervals to divide the interval
	 * 		{@code [0, 1)} into.
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
	 * 		into.
	 */
	private void initialize(int n) {

		this.nextPartition		= n - 1;
		this.sequence			= new int[n];

		for (int i = 0; i < this.sequence.length; i++) {
			this.sequence[i]	= i;
		}

	}

	/**
	 * This array will hold the indices {@code i}, where {@code 0 <= i < n},
	 * representing each of the {@code n} buckets {@code [i/n, (i+1)/n)}.
	 * During each call to {@link #next()}, we will randomly choose one of
	 * the remaining buckets and generate a random number in that bucket.
	 */
	private int[] sequence;

	/**
	 * The index (into {@link #sequence} of the last unused bucket.  All
	 * buckets in {@link #sequence} after this index have been used, all
	 * buckets not after this index have not yet been used.
	 */
	private int nextPartition;

	/**
	 * The underlying uniform random number generator.
	 */
	private final java.util.Random source = new java.util.Random();

}
