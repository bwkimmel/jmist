/**
 *
 */
package ca.eandb.jmist.framework.random;

import ca.eandb.jmist.framework.Random;

/**
 * @author Brad Kimmel
 *
 */
public final class NRooksRandom implements Random {

	public NRooksRandom(int n, int dimensions) {
		this.initialize(n, dimensions);
	}

	private NRooksRandom(StratifiedRandom[] sources) {
		this.sources = new StratifiedRandom[sources.length];
		for (int i = 0; i < sources.length; i++) {
			this.sources[i] = sources[i].createCompatibleRandom();
		}
		this.nextSourceIndex = sources.length - 1;
	}

	/*
	 * (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
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
	 * @see ca.eandb.jmist.framework.Random#reset()
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
	 */
	public NRooksRandom createCompatibleRandom() {
		return new NRooksRandom(this.sources);
	}

	private StratifiedRandom[] sources;
	private int nextSourceIndex = 0;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 2399501352737062538L;

}
