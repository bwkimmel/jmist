/**
 * 
 */
package ca.eandb.jmist.framework.geometry.nurbs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;

import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.util.CollectionUtil;
import ca.eandb.util.DoubleArray;
import ca.eandb.util.IntegerArray;
import ca.eandb.util.UnimplementedException;

/**
 * @author Brad
 *
 */
public final class KnotVector implements Serializable {
	
	private final DoubleArray knots = new DoubleArray();
	private transient IntegerArray unique = new IntegerArray(1);
	
	public KnotVector() {
		unique.add(0);
	}
	
	private void computeUniqueIndices() {
		unique.clear();
		double prev = Double.NEGATIVE_INFINITY;
		int n = knots.size();
		for (int i = 0; i < n; i++) {
			double value = knots.get(i);
			if (value > prev) {
				unique.add(i);
			}
			prev = value;
		}
		unique.add(n);		
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		computeUniqueIndices();
	}
	
	public void merge(KnotVector other) {
		int n = knots.size();
		knots.addAll(other.knots);
		CollectionUtil.merge(knots, n);
		computeUniqueIndices();
	}
	
	public int insert(double u) {
		return insert(u, 1);
	}
	
	public int insert(double u, int count) {
		
	}
	
	public void append(double u) {
		append(u, 1);
	}
	
	public void append(double u, int count) {
		double oldMax = knots.isEmpty() ? Double.NEGATIVE_INFINITY : maximum();
		if (u < oldMax) {
			throw new IllegalArgumentException("u must not be less than the maximum knot value");
		}
		
		knots.addAll(Collections.nCopies(count, u));
		if (u > oldMax) {
			unique.add(knots.size());
		} else {
			unique.set(unique.size() - 1, knots.size());
		}
	}
	
	public void remove(int index) {
		if (!(0 <= index && index < knots.size())) {
			throw new IllegalArgumentException("index out of bounds");
		}

		throw new UnimplementedException();
	}
	
	public void clear() {
		knots.clear();
		unique.clear();
		unique.add(0);
	}
	
	public void open(int order) {
		throw new UnimplementedException();
	}
	
	public void closeEnds(int order) {
	}
	
	public void closeAll(int order) {
		int oldSize = knots.size();
		for (int i = 0; i < numUniqueKnots(); i++) {
			int targetMult = (i == 0 || i == numUniqueKnots() - 1) ? order : order - 1;
			int mult = multiplicity(i);
			if (order > mult) {
				knots.addAll(Collections.nCopies(targetMult - mult, unique(i)));
			}
		}
		if (knots.size() > oldSize) {
			CollectionUtil.merge(knots, oldSize);
			computeUniqueIndices();
		}
	}
		
	public double get(int index) {
		return knots.get(index);
	}
	
	public double unique(int uniqueIndex) {
		return knots.get(unique.get(uniqueIndex));
	}
	
	public Interval interval(int uniqueIndex) {
		return new Interval(
				knots.get(unique.get(uniqueIndex)), 
				knots.get(unique.get(uniqueIndex + 1)));
	}
	
	public int multiplicity(int uniqueIndex) {
		return unique.get(uniqueIndex + 1) - unique.get(uniqueIndex);
	}
	
	public int multiplicity(double u) {
		
	}
	
	public int getIntervalKnotIndex(int uniqueIndex) {
		return unique.get(uniqueIndex + 1) - 1;
	}
	
	public int numUniqueKnots() {
		return unique.size() - 1;
	}
	
	public int numIntervals() {
		return unique.size() - 2;
	}
	
	public int size() {
		return knots.size();
	}
	
	public boolean isEmpty() {
		return knots.isEmpty();
	}
	
	public double minimum() {
		return knots.get(0);
	}
	
	public double maximum() {
		return knots.get(knots.size() - 1);
	}
	
	public Interval domain(int order) {
		return new Interval(
				knots.get(order - 1),
				knots.get(knots.size() - order));
	}
	
	public int findUnique(double u) {
		
	}
	
	public int find(double u) {
		throw new UnimplementedException();
	}
	
	public void mergeClusters() {
		mergeClusters(MathUtil.EPSILON);
	}
	
	public void mergeClusters(double tolerance) {
		unique.clear();
		unique.add(0);
		if (!knots.isEmpty()) {
			for (int i = 1, n = knots.size(); i < n; i++) {
				if (MathUtil.equal(knots.get(i - 1), knots.get(i), tolerance)) {
					knots.set(i, knots.get(i - 1));
				} else {
					unique.add(i);
				}
			}
			unique.add(knots.size());
		}
	}
	
	public void normalize() {
		if (!knots.isEmpty()) {
			double c = 1.0 / (maximum() - minimum());
			for (int i = 0, n = knots.size(); i < n; i++) {
				knots.set(i, (knots.get(i) - minimum()) * c);
			}
		}
	}
	
}
