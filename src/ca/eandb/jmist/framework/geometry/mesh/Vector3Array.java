/**
 * 
 */
package ca.eandb.jmist.framework.geometry.mesh;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.DoubleArray;

/**
 * A <code>List</code> of <code>Vector3</code>s that stores its elements more
 * compactly than <code>ArrayList&lt;Vector3&gt;</code>.
 * 
 * @author Brad Kimmel
 */
public final class Vector3Array extends AbstractList<Vector3> implements
		RandomAccess, Serializable {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -2687276741624883709L;

	/**
	 * A thread local array used to allow all coordinates to be inserted in a
	 * single operation, rather than as three separate inserts.
	 * @see #add(int, Vector3) 
	 */
	private final static ThreadLocal<double[]> buffer = new ThreadLocal<double[]>() {
		protected double[] initialValue() {
			return new double[3];
		}		
	};

	/** The <code>DoubleArray</code> that holds the coordinates. */
	private final DoubleArray coords = new DoubleArray();

	/* (non-Javadoc)
	 * @see java.util.AbstractList#get(int)
	 */
	@Override
	public Vector3 get(int index) {
		int base = index * 3;
		return new Vector3(
				coords.get(base + 0),
				coords.get(base + 1),
				coords.get(base + 2));
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		return coords.size() / 3;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractList#set(int, java.lang.Object)
	 */
	@Override
	public Vector3 set(int index, Vector3 element) {
		int base = index * 3;
		Vector3 p = new Vector3(
				coords.get(base + 0),
				coords.get(base + 1),
				coords.get(base + 2));
		coords.set(base + 0, element.x());
		coords.set(base + 1, element.y());
		coords.set(base + 2, element.z());
		return p;	
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#add(int, java.lang.Object)
	 */
	@Override
	public void add(int index, Vector3 element) {
		double[] buf = buffer.get();
		buf[0] = element.x();
		buf[1] = element.y();
		buf[2] = element.z();
		coords.addAll(index * 3, buf);		
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractList#remove(int)
	 */
	@Override
	public Vector3 remove(int index) {
		int base = index * 3;
		Vector3 p = new Vector3(
				coords.get(base + 0),
				coords.get(base + 1),
				coords.get(base + 2));
		coords.remove(base);
		coords.remove(base);
		coords.remove(base);
		return p;
	}

}
