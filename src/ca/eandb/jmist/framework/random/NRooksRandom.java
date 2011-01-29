/**
 *
 */
package ca.eandb.jmist.framework.random;

import java.io.IOException;
import java.io.ObjectInputStream;

import ca.eandb.jmist.framework.Random;

/**
 * @author Brad Kimmel
 *
 */
public final class NRooksRandom implements Random {

	/** Serialization version ID. */
	private static final long serialVersionUID = 2399501352737062538L;

	private final Random inner;
	private final int n;
	private final int dimensions;
	private transient StratifiedRandom[] sources;
	private transient int nextSourceIndex = 0;

	public NRooksRandom(int n, int dimensions) {
		this(n, dimensions, new SimpleRandom());
	}

	public NRooksRandom(int n, int dimensions, Random inner) {
		this.inner = inner;
		this.n = n;
		this.dimensions = dimensions;
		initialize();
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

	private void initialize() {
		this.sources = new StratifiedRandom[dimensions];

		for (int i = 0; i < dimensions; i++) {
			this.sources[i] = new StratifiedRandom(n, inner);
		}

		this.nextSourceIndex = 0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
	 */
	public NRooksRandom createCompatibleRandom() {
		return new NRooksRandom(n, dimensions, inner.createCompatibleRandom());
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		initialize();
	}

}
