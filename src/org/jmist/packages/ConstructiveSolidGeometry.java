/**
 *
 */
package org.jmist.packages;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jmist.framework.Geometry;
import org.jmist.framework.Intersection;
import org.jmist.framework.IntersectionRecorder;
import org.jmist.framework.Material;
import org.jmist.framework.Medium;
import org.jmist.framework.Spectrum;
import org.jmist.toolkit.Basis3;
import org.jmist.toolkit.Interval;
import org.jmist.toolkit.Point2;
import org.jmist.toolkit.Point3;
import org.jmist.toolkit.Ray3;
import org.jmist.toolkit.Vector3;

/**
 * @author bkimmel
 *
 */
public abstract class ConstructiveSolidGeometry implements Geometry {

	public ConstructiveSolidGeometry addGeometry(Geometry geometry) {
		this.args.add(geometry);
		return this;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#intersect(org.jmist.toolkit.Ray3, org.jmist.framework.IntersectionRecorder)
	 */
	@Override
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		CsgIntersectionRecorder csg = new CsgIntersectionRecorder();

		for (Geometry geometry : this.args) {
			if (geometry.isClosed()) {
				geometry.intersect(ray, csg);
			}
			csg.advance();
		}

		csg.transfer(recorder);

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.Geometry#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.VisibilityFunction3#visibility(org.jmist.toolkit.Ray3, org.jmist.toolkit.Interval)
	 */
	@Override
	public boolean visibility(Ray3 ray, Interval I) {

		NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);

		this.intersect(ray, recorder);

		return recorder.isEmpty() || !I.contains(recorder.nearestIntersection().distance());

	}

	/* (non-Javadoc)
	 * @see org.jmist.framework.VisibilityFunction3#visibility(org.jmist.toolkit.Point3, org.jmist.toolkit.Point3)
	 */
	@Override
	public boolean visibility(Point3 p, Point3 q) {

		/*
		 * Determine the visibility in terms of the other overloaded
		 * method.
		 */
		Vector3		d		= p.vectorTo(q);
		Ray3		ray		= new Ray3(p, d.unit());
		Interval	I		= new Interval(0.0, d.length());

		return this.visibility(ray, I);

	}

	protected abstract boolean isInside(int nArgs, BitSet args);


	/**
	 * @author bkimmel
	 *
	 */
	private final class CsgIntersectionRecorder implements IntersectionRecorder {

		/* (non-Javadoc)
		 * @see org.jmist.framework.IntersectionRecorder#needAllIntersections()
		 */
		@Override
		public boolean needAllIntersections() {
			return true;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IntersectionRecorder#interval()
		 */
		@Override
		public Interval interval() {
			return Interval.UNIVERSE;
		}

		/* (non-Javadoc)
		 * @see org.jmist.framework.IntersectionRecorder#record(org.jmist.framework.Intersection)
		 */
		@Override
		public void record(Intersection intersection) {
			this.intersectionSet.add(new CsgIntersection(this.argumentIndex, intersection));
		}

		public void transfer(IntersectionRecorder recorder) {

			int							nArgs			= this.argumentIndex;
			BitSet						args			= new BitSet(nArgs);
			Iterator<CsgIntersection>	i				= this.intersectionSet.iterator();
			boolean						fromInside		= false;
			boolean						toInside;

			while (i.hasNext()) {

				CsgIntersection			x				= i.next();
				Intersection			inner			= x.getInnerIntersection();

				if (x.getArgumentIndex() < nArgs && inner.closed()) {

					args.set(x.getArgumentIndex(), inner.front());
					toInside = isInside(nArgs, args);

					if (fromInside != toInside
							&& recorder.interval().contains(inner.distance())) {

						x.setFront(toInside);
						recorder.record(x);

						if (!recorder.needAllIntersections()) {
							break;
						}

					}

					fromInside = toInside;

				}

			}

		}

		public void advance() {
			this.argumentIndex++;
		}

		private final class CsgIntersection implements Intersection {

			public CsgIntersection(int argumentIndex, Intersection inner) {
				this.argumentIndex = argumentIndex;
				this.inner = inner;
			}

			@Override
			public double distance() {
				return this.inner.distance();
			}

			@Override
			public boolean front() {
				return this.flipped ? !this.inner.front() : this.inner.front();
			}

			@Override
			public Vector3 incident() {
				return this.inner.incident();
			}

			@Override
			public Medium ambientMedium() {
				return this.inner.ambientMedium();
			}

			@Override
			public Basis3 basis() {
				Basis3 basis = this.inner.basis();
				return this.flipped ? basis.opposite() : basis;
			}

			@Override
			public boolean closed() {
				return true;
			}

			@Override
			public void illuminate(Vector3 from, Spectrum irradiance) {
				// TODO Auto-generated method stub

			}

			@Override
			public Point3 location() {
				return this.inner.location();
			}

			@Override
			public Material material() {
				return this.inner.material();
			}

			@Override
			public Basis3 microfacetBasis() {
				Basis3 basis = this.inner.microfacetBasis();
				return this.flipped ? basis.opposite() : basis;
			}

			@Override
			public Vector3 microfacetNormal() {
				Vector3 n = this.inner.microfacetNormal();
				return this.flipped ? n.opposite() : n;
			}

			@Override
			public Vector3 normal() {
				Vector3 n = this.inner.normal();
				return this.flipped ? n.opposite() : n;
			}

			@Override
			public Vector3 tangent() {
				return this.inner.tangent();
			}

			@Override
			public Point2 textureCoordinates() {
				return this.inner.textureCoordinates();
			}

			public int getArgumentIndex() {
				return this.argumentIndex;
			}

			public Intersection getInnerIntersection() {
				return this.inner;
			}

			public void setFront(boolean front) {
				this.flipped = (front != inner.front());
			}

			private boolean flipped = false;
			private final int argumentIndex;
			private final Intersection inner;

		}

		private int argumentIndex = 0;
		private final SortedSet<CsgIntersection> intersectionSet = new TreeSet<CsgIntersection>(new Comparator<Intersection>() {

			@Override
			public int compare(Intersection arg0, Intersection arg1) {
				return Double.compare(arg0.distance(), arg1.distance());
			}

		});

	}

	private final Collection<Geometry> args = new ArrayList<Geometry>();

}
