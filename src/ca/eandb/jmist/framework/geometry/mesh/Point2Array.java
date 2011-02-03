/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

import ca.eandb.jmist.math.Point2;
import ca.eandb.util.DoubleArray;

/**
 * A <code>List</code> of <code>Point2</code>s that stores its elements more
 * compactly than <code>ArrayList&lt;Point2&gt;</code>.
 * 
 * @author Brad Kimmel
 */
public final class Point2Array extends AbstractList<Point2> implements
		RandomAccess, Serializable {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -2687276741624882709L;

	/**
	 * A thread local array used to allow all coordinates to be inserted in a
	 * single operation, rather than as three separate inserts.
	 * @see #add(int, Point2) 
	 */
	private final static ThreadLocal<double[]> buffer = new ThreadLocal<double[]>() {
		protected double[] initialValue() {
			return new double[2];
		}		
	};

	/** The <code>DoubleArray</code> that holds the coordinates. */
	private final DoubleArray coords = new DoubleArray();

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public Point2 get(int index) {
		int base = index * 2;
		return new Point2(
				coords.get(base + 0),
				coords.get(base + 1));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return coords.size() / 2;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public Point2 set(int index, Point2 element) {
		int base = index * 2;
		Point2 p = new Point2(
				coords.get(base + 0),
				coords.get(base + 1));
		coords.set(base + 0, element.x());
		coords.set(base + 1, element.y());
		return p;	
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Point2 element) {
		double[] buf = buffer.get();
		buf[0] = element.x();
		buf[1] = element.y();
		coords.addAll(index * 2, buf);		
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public Point2 remove(int index) {
		int base = index * 2;
		Point2 p = new Point2(
				coords.get(base + 0),
				coords.get(base + 1));
		coords.remove(base);
		coords.remove(base);
		return p;
	}

}
