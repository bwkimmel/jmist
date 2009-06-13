/**
 *
 */
package ca.eandb.jmist.framework.geometry;

import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import ca.eandb.jmist.framework.Geometry;
import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.IntersectionGeometryDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * An abstract <code>Geometry</code> that combines other geometries using a
 * boolean expression.
 * @author Brad Kimmel
 */
public abstract class ConstructiveSolidGeometry extends CompositeGeometry {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#intersect(ca.eandb.jmist.toolkit.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		/* Use a special intersection recorder that knows how to combine the
		 * results of multiple sets of intersections.
		 */
		CsgIntersectionRecorder csg = new CsgIntersectionRecorder();

		/* Compute the ray-geometry intersections for each child geometry in
		 * turn.
		 */
		for (Geometry geometry : this.children()) {

			/* Ignore open geometries. */
			if (geometry.isClosed()) {
				geometry.intersect(ray, csg);
			}

			/* Advance to next argument. */
			csg.advance();

		}

		/* combine the results and transfer the resulting intesections to the
		 * provided intersection recorder.
		 */
		csg.transfer(recorder);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return true;
	}

	/**
	 * Evaluates the boolean expression for this class of
	 * <code>ConstructiveSolidGeometry</code>.
	 * @param nArgs The number of child geometries.
	 * @param args A <code>BitSet</code> of values indicating whether the ray
	 * 		is inside each of the child geometries.  This argument should not
	 * 		be modified by the method.
	 * @return A value indicating whether the ray is inside the combined
	 * 		geometry.  The result should be deterministic.  That is, two calls
	 * 		to this method with the same value for <code>nArgs</code> and for
	 * 		<code>args</code> should yield the same result.
	 */
	protected abstract boolean isInside(int nArgs, BitSet args);

	/**
	 * An <code>IntersectionRecorder</code> that combines the results of
	 * multiple sets of intersections using a boolean expression.
	 * @author Brad Kimmel
	 */
	private final class CsgIntersectionRecorder implements IntersectionRecorder {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#needAllIntersections()
		 */
		public boolean needAllIntersections() {
			return true;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorder#isEmpty()
		 */
		public boolean isEmpty() {
			return this.intersectionSet.isEmpty();
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#interval()
		 */
		public Interval interval() {
			return Interval.UNIVERSE;
		}

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.IntersectionGeometry)
		 */
		public void record(IntersectionGeometry intersection) {
			this.intersectionSet.add(new CsgIntersection(this.argumentIndex, intersection));
		}

		/**
		 * Combines the results of each set of intersections and transfers the
		 * resulting intersections to the specified
		 * <code>IntersectionRecorder</code>.
		 * @param recorder The <code>IntersectionRecorder</code> to transfer
		 * 		the resulting intersections to.
		 */
		public void transfer(IntersectionRecorder recorder) {

			int							nArgs			= this.argumentIndex;
			BitSet						args			= new BitSet(nArgs);
			Iterator<CsgIntersection>	i				= this.intersectionSet.iterator();
			boolean						fromInside		= false;
			boolean						toInside;

			/* Loop through each intersection. */
			while (i.hasNext()) {

				CsgIntersection			x				= i.next();
				IntersectionGeometry			inner			= x.getInnerIntersection();

				assert(x.getArgumentIndex() < nArgs);

				/* Ignore intersections on non-closed geometries. */
				if (inner.closed()) {

					args.set(x.getArgumentIndex(), inner.front());
					toInside = isInside(nArgs, args);

					/* If the intersection represents the traversal from
					 * outside the geometry to inside, or vice versa, then the
					 * intersection is at the boundary of the combined geometry.
					 * Only consider this intersection if it is within the
					 * range expected by the recorder.
					 */
					if (fromInside != toInside
							&& recorder.interval().contains(inner.distance())) {

						/* The intersection is a front intersection if the ray
						 * is passing into the geometry.
						 */
						x.setFront(toInside);
						recorder.record(x);

						/* If we don't need all intersections, then we're
						 * done.
						 */
						if (!recorder.needAllIntersections()) {
							break;
						}

					}

					fromInside = toInside;

				} /* inner.closed() */

			} /* i.hasNext() */

		}

		/**
		 * Completes a set of intersections and advances to the set of
		 * intersections for the next geometry.
		 */
		public void advance() {
			this.argumentIndex++;
		}

		/**
		 * An <code>IntersectionGeometry</code> decorator that adds functionality
		 * needed to support constructive solid geometry.  This decorator
		 * keeps track of which child geometry the intersection is for, and it
		 * can be flipped, which toggles the {@link IntersectionGeometry#front()}
		 * property and negates the basis and normal.
		 * @author Brad Kimmel
		 */
		private final class CsgIntersection extends IntersectionGeometryDecorator {

			/**
			 * Creates a new <code>CsgIntersection</code>.
			 * @param argumentIndex The
			 * @param inner
			 */
			public CsgIntersection(int argumentIndex, IntersectionGeometry inner) {
				super(inner);
				this.argumentIndex = argumentIndex;
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#front()
			 */
			@Override
			public boolean front() {
				return this.flipped ? !this.inner.front() : this.inner.front();
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#basis()
			 */
			@Override
			public Basis3 basis() {
				Basis3 basis = this.inner.basis();
				return this.flipped ? basis.opposite() : basis;
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#closed()
			 */
			@Override
			public boolean closed() {
				return true;
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#shadingBasis()
			 */
			@Override
			public Basis3 shadingBasis() {
				Basis3 basis = this.inner.shadingBasis();
				return this.flipped ? basis.opposite() : basis;
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#shadingNormal()
			 */
			@Override
			public Vector3 shadingNormal() {
				Vector3 n = this.inner.shadingNormal();
				return this.flipped ? n.opposite() : n;
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#normal()
			 */
			@Override
			public Vector3 normal() {
				Vector3 n = this.inner.normal();
				return this.flipped ? n.opposite() : n;
			}

			/* (non-Javadoc)
			 * @see ca.eandb.jmist.framework.IntersectionGeometryDecorator#tangent()
			 */
			@Override
			public Vector3 tangent() {
				return this.inner.tangent();
			}

			/**
			 * Gets the value identifying which of the component geometries
			 * this <code>IntersectionGeometry</code> came from.
			 * @return The value identifying which of the component geometries
			 * 		this <code>IntersectionGeometry</code> came from.
			 */
			public int getArgumentIndex() {
				return this.argumentIndex;
			}

			/**
			 * Gets the <code>IntersectionGeometry</code> that was recorded by the
			 * component geometry.
			 * @return The <code>IntersectionGeometry</code> that was recorded by the
			 * 		component geometry.
			 */
			public IntersectionGeometry getInnerIntersection() {
				return this.inner;
			}

			/**
			 * Sets whether this <code>CsgIntersection</code> is an entry point
			 * or an exit point from the geometry.
			 * @param front A value indicating if this
			 * 		<code>CsgIntersection</code> represents an intersection
			 * 		with the outside of the geometry.
			 */
			public void setFront(boolean front) {
				this.flipped = (front != inner.front());
			}

			/**
			 * A value indicating if this <code>CsgIntersection</code> was
			 * flipped from the underlying <code>IntersectionGeometry</code>.
			 */
			private boolean flipped = false;

			/**
			 * A value indicating which component geometry the inner
			 * <code>IntersectionGeometry</code> came from.
			 */
			private final int argumentIndex;

		}

		/**
		 * The index of the component geometry whose intersections are
		 * currently being recorded.
		 */
		private int argumentIndex = 0;

		/**
		 * The set of all the intersections recorded by each of the component
		 * geometries, sorted by distance (least to greatest).
		 */
		private final SortedSet<CsgIntersection> intersectionSet = new TreeSet<CsgIntersection>(
				new Comparator<IntersectionGeometry>() {

					public int compare(IntersectionGeometry arg0, IntersectionGeometry arg1) {
						return Double.compare(arg0.distance(), arg1.distance());
					}

				});

	}

}
