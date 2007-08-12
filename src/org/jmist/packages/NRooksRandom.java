/**
 *
 */
package org.jmist.packages;

import org.jmist.framework.IRandom;

/**
 * @author bkimmel
 *
 */
public final class NRooksRandom implements IRandom {

	public NRooksRandom(int n, int dimensions) {
		this.initialize(n, dimensions);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.IRandom#next()
	 */
	public double next() {
		double result = sources[nextSourceIndex].next();

		if (++this.nextSourceIndex >= this.sources.length) {
			this.nextSourceIndex = 0;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jmist.framework.IRandom#reset()
	 */
	public void reset() {
		for (int i = 0; i < this.sources.length; i++) {
			this.sources[i].reset();
		}
		this.nextSourceIndex = 0;
	}

	public void reset(int n) {
		for (int i = 0; i < this.sources.length; i++) {
			this.sources[i].reset(n);
		}
		this.nextSourceIndex = 0;
	}

	public void reset(int n, int dimensions) {
		if (dimensions == this.sources.length) {
			this.reset(n);
		} else {
			this.initialize(n, dimensions);
		}
	}

	private void initialize(int n, int dimensions) {
		this.sources = new StratifiedRandom[dimensions];

		for (int i = 0; i < dimensions; i++) {
			this.sources[i] = new StratifiedRandom(n);
		}

		this.nextSourceIndex = 0;
	}

	private StratifiedRandom[] sources;
	private int nextSourceIndex = 0;

}
