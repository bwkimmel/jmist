/**
 * 
 */
package ca.eandb.jmist.framework.random;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Random;
import ca.eandb.util.DoubleArray;

/**
 * @author Brad
 *
 */
public final class RepeatableRandom implements Random {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -6178171395488135601L;

	private final Random inner;
	
	private final List<DoubleArray> values = new ArrayList<DoubleArray>();
	
	private int sequence = 0;
	
	private int position = 0;
	
	public RepeatableRandom(Random inner) {
		this.inner = inner;
		values.add(new DoubleArray());
	}
	
	public RepeatableRandom cloneSequence() {
		RepeatableRandom rnd = new RepeatableRandom(
				inner.createCompatibleRandom());
		for (DoubleArray seq : values) {
			rnd.values.add(seq.clone());
		}
		return rnd;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#createCompatibleRandom()
	 */
	public Random createCompatibleRandom() {
		return new RepeatableRandom(inner.createCompatibleRandom());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#next()
	 */
	public double next() {
		DoubleArray seq = values.get(sequence);
		while (position >= seq.size()) {
			seq.add(inner.next());
		}
		return seq.get(position++);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Random#reset()
	 */
	public void reset() {
		sequence = 0;
		position = 0;
	}
	
	public void truncateAll() {
		truncate();
		values.subList(sequence + 1, values.size()).clear();
	}
	
	public void truncate() {
		values.get(sequence).resize(position);
	}
	
	public void mark() {
		if (++sequence >= values.size()) {
			values.add(new DoubleArray());
		}
		position = 0;
	}
	
	public void mutate(double width) {
		DoubleArray seq = values.get(sequence);
		for (int i = position, n = seq.size(); i < n; i++) {
			seq.set(i, mutate(seq.get(i), width));
		}
	}
	
	public void mutate(int n, double width) {
		DoubleArray seq = values.get(sequence);
		for (int i = position, j = 0; j < n; i++, j++) {
			seq.set(i, mutate(seq.get(i), width));
		}
	}
	
//	private double mutate(double x, double width) {
//		x = x + (inner.next() - 0.5) * width;
//		return x - Math.floor(x);
//	}
	
	private static final double s1 = 1.0 / 512.0;
	private static final double s2 = 1.0 / 16.0;
	private double mutate(double x, double width) {
		final double rnd = inner.next();
		final double dx = s1 / (s1 / s2 + Math.abs(2.0 * inner.next() - 1.0)) -
				s1 / (s1 / s2 + 1.0);
		if (rnd < 0.5) {
			double x1 = x + dx;
			return (x1 < 1.0) ? x1 : x1 - 1.0;
		} else {
			double x1 = x - dx;
			return (x1 < 0.0) ? x1 + 1.0 : x1;
		}
	}
	
	public void clear() {
		values.clear();
		values.add(new DoubleArray());
		sequence = 0;
		position = 0;
	}

}
