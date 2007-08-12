/**
 *
 */
package org.jmist.packages;

import java.util.Random;

import org.jmist.framework.IRandom;

/**
 * @author bkimmel
 *
 */
public final class StratifiedRandom implements IRandom {

	public StratifiedRandom() {
		this.initialize(1);
	}

	public StratifiedRandom(int n) {
		this.initialize(n);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.IRandom#next()
	 */
	public double next() {
		int j = source.nextInt(this.nextPartition + 1);

		int temp = this.sequence[j];
		this.sequence[j] = this.sequence[nextPartition];
		this.sequence[nextPartition] = temp;

		if (--nextPartition < 0) {
			nextPartition = this.sequence.length - 1;
		}

		return (((double) temp) + source.nextDouble()) / ((double) this.sequence.length);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.IRandom#reset()
	 */
	public void reset() {
		this.nextPartition = this.sequence.length - 1;
	}

	public void reset(int n) {
		if (this.sequence.length == n) {
			this.reset();
		} else {
			this.initialize(n);
		}
	}

	private void initialize(int n) {
		this.sequence = new int[n];
		this.nextPartition = n - 1;

		for (int i = 0; i < this.sequence.length; i++) {
			this.sequence[i] = i;
		}
	}

	private int[] sequence;
	private int nextPartition;
	private final Random source = new Random();

}
