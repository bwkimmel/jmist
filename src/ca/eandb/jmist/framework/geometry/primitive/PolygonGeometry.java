/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.geometry.primitive;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.BoundingBoxBuilder2;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.util.ArrayUtil;

/**
 * @author Brad Kimmel
 *
 */
public final class PolygonGeometry extends PrimitiveGeometry {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4557209073883064041L;

	/**
	 * @param material
	 */
	public PolygonGeometry(Point3[] vertices, int[][] components) {

		if (vertices.length < 3) {
			throw new IllegalArgumentException("vertices.length < 3");
		}

		if (components.length == 0) {
			throw new IllegalArgumentException("components.length == 0");
		}

		for (int i = 0; i < components.length; i++) {
			if (components[i].length < 3) {
				throw new IllegalArgumentException(String.format("components[%d].length < 3", i));
			}
		}

		this.origin = vertices[components[0][0]];

		Vector3 u = this.origin.vectorTo(vertices[components[0][1]]);
		Vector3 v = this.origin.vectorTo(vertices[components[0][components[0].length - 1]]);

		this.basis = Basis3.fromUV(u, v);
		this.plane = Plane3.throughPoint(this.origin, this.basis.w());

		this.vertices.clear();
		for (int i = 0; i < vertices.length; i++) {
			Vector3 r = this.origin.vectorTo(vertices[i]);
			this.vertices.add(new Point2(r.dot(basis.u()), r.dot(basis.v())));
		}

		for (int i = 0; i < components.length; i++) {
			this.insert(new Component(components[i]), null);
		}

		BoundingBoxBuilder2 builder = new BoundingBoxBuilder2();
		for (Component component : this.components) {
			builder.add(component.bound);
		}

		this.bound = builder.getBoundingBox();
		this.area = this.getArea();

	}

	private void insert(Component component, Component parent) {

		List<Component> children = parent != null ? parent.children : this.components;

		for (int i = 0; i < children.size(); i++) {
			if (this.inside(component, children.get(i))) {
				this.insert(component, children.get(i));
				return;
			}
		}

		for (int i = 0; i < children.size(); i++) {
			if (this.inside(children.get(i), component)) {
				this.insert(children.get(i), component);
				children.remove(i--);
			}
		}

		children.add(component);

	}

	private boolean inside(Component a, Component b) {
		boolean in = false;
		boolean out = false;

		for (int i = 0; i < a.indices.length; i++) {
			if (this.inside(this.vertices.get(a.indices[i]), b, true))
				in = true;
			else
				out = true;

			if (in && out) {
				throw new IllegalArgumentException("Components cross.");
			}
		}

		return in;
	}

	private boolean inside(Point2 p, Component component, boolean direct) {

		if (!component.bound.contains(p))
			return false;

		int i, count = 0;

		for (i = 0; i < component.indices.length; i++) {
			int		ni = (i < component.indices.length - 1) ? i + 1 : 0;
			Point2	a = this.vertices.get(component.indices[i]);
			Point2	b = this.vertices.get(component.indices[ni]);

			if (this.intersects(p, a, b)) count++;
		}

		if (count % 2 == 0)
			return false;

		if (!direct)
			for (i = 0; i < component.children.size(); i++)
				if (this.inside(p, component.children.get(i)))
					return false;

		return true;

	}

	private boolean inside(Point2 p, Component component) {
		return this.inside(p, component, false);
	}

	private boolean intersects(Point2 p, Point2 a, Point2 b) {

		if ((a.y() > p.y() && b.y() >= p.y()) || (a.y() < p.y() && b.y() <= p.y()))
			return false;	// both endpoints are on the same side of the x-axis

		if (a.x() < p.x() && b.x() <= p.x())
			return false;	// both endpoints are on the negative side of the y-axis

		if (a.y() == p.y() && b.y() == p.y())
			return false;	// dont count intersection when lines are coincident

		if (a.x() >= p.x() && b.x() > p.x())	// both endpoints on the positive side of the
			return true;				// y-axis and on opposite sides of the x-axis

		// endpoints are on opposite sides of x axis and on opposite sides of the y-axis,
		// must check for intersection point
		double	t = (p.y() - a.y()) / (b.y() - a.y());
		double	x = a.x() + t * (b.x() - a.x());

		return x >= p.x();

	}

	private boolean inside(Point2 p) {
		if (!this.bound.contains(p))
			return false;

		for (int i = 0; i < this.components.size(); i++)
			if (this.inside(p, this.components.get(i)))
				return true;

		return false;
	}

	/**
	 * @param material
	 */
	public PolygonGeometry(Point3[] vertices) {
		this(vertices, new int[][]{ ArrayUtil.range(0, vertices.length - 1) });
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {

		if (this.components.isEmpty())
			return;

		double			t = this.plane.intersect(ray);

		if (!recorder.interval().contains(t))
			return;

		Point3			p = ray.pointAt(t);
		Vector3			r = p.vectorFrom(this.origin);
		Point2			uv = new Point2(r.dot(basis.u()), r.dot(basis.v()));

		if (!this.inside(uv))
			return;

		Point2			inversion = new Point2(
							(uv.x() - this.bound.minimumX()) / this.bound.lengthX(),
							(uv.y() - this.bound.minimumY()) / this.bound.lengthY()
						);

		Vector3			N = this.plane.normal();

		double			ndotv		= N.dot(ray.direction());
		int				surfaceId	= (ndotv < 0.0) ? POLYGON_SURFACE_TOP : POLYGON_SURFACE_BOTTOM;

		Intersection	x = super.newIntersection(ray, t, ndotv < 0.0, surfaceId)
								.setLocation(p)
								.setUV(inversion)
								.setBasis((surfaceId == POLYGON_SURFACE_TOP) ? this.basis : this.basis.opposite())
								.setNormal((surfaceId == POLYGON_SURFACE_TOP) ? N : N.opposite());

		recorder.record(x);

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.SceneElement#isClosed()
	 */
	public boolean isClosed() {
		return false;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return Box3.smallestContainingPoints(this.getVertices());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return Sphere.smallestContaining(this.getVertices());
	}

//	/* (non-Javadoc)
//	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.VisibilityFunction3, ca.eandb.jmist.framework.Illuminable)
//	 */
//	public void illuminate(SurfacePoint x, VisibilityFunction3 vf,
//			Illuminable target) {
//
//		if (categorical == null) {
//			double[] weights = new double[components.size()];
//			for (int i = 0; i < components.size(); i++) {
//				weights[i] = components.get(i).getArea();
//			}
//			categorical = new CategoricalRandom(weights);
//		}
//
//		Component component = this.components.get(categorical.next());
//		component.illuminate(x, vf, target);
//
//	}

	private Point3 pointAt(Point2 uv) {
		return this.origin.plus(basis.u().times(uv.x())).plus(basis.v().times(uv.y()));
	}

	private double getArea() {
		double area = 0.0;
		for (Component component : this.components) {
			area += component.getArea();
		}
		return area;
	}

	private Iterable<Point3> getVertices() {

		List<Point3> vertexList = new ArrayList<Point3>();

		for (int i = 0; i < this.vertices.size(); i++) {
			Point2 p = this.vertices.get(i);
			Point3 vertex = this.pointAt(p);

			vertexList.add(vertex);
		}

		return vertexList;

	}

	private class Component {

		/**
		 * @param indices
		 */
		public Component(int[] indices) {
			BoundingBoxBuilder2 builder = new BoundingBoxBuilder2();
			for (int i = 0; i < indices.length; i++) {
				builder.add(vertices.get(indices[i]));
			}

			this.indices = indices;
			this.bound = builder.getBoundingBox();
		}

		public double getArea() {
			double area = this.getOuterArea();
			for (Component child : this.children) {
				area -= child.getArea();
			}
			return area;
		}

		private double getOuterArea() {
			double area = 0.0;
			for (int i = 0; i < indices.length - 1; i++) {
				Point2 a = vertices.get(indices[i]);
				Point2 b = vertices.get(indices[i + 1]);

				area += a.x() * b.y() - b.x() * a.y();
			}
			area /= 2.0;
			return area;
		}

//		public Color illuminate(Intersection x, VisibilityFunction3 vf) {
//
//			Point2 uv;
//			do {
//				uv = bound.interpolate(random.next(), random.next());
//			} while (!PolygonGeometry.this.inside(uv, this));
//
//			Point3 p = PolygonGeometry.this.pointAt(uv);
//
//			if (vf.visibility(p, x.location())) {
//
//				// FIXME Select from appropriate side when two-sided.
//				Intersection sp = PolygonGeometry.super.newIntersection(null, 0.0, true, POLYGON_SURFACE_TOP)
//					.setLocation(p)
//					.setTextureCoordinates(uv)
//					.setNormal(PolygonGeometry.this.plane.normal());
//
//				/* Compute the attenuation according to distance. */
//				Vector3 from = x.location().vectorTo(p);
//				double r = from.length();
//				double attenuation = PolygonGeometry.this.area / (4.0 * Math.PI * r * r);
//				from = from.divide(r);
//
//				/* Sample the material radiance. */
//				Color radiance = sp.material().emission(sp, from.opposite());
//
//				/* Illuminate the point. */
//				target.illuminate(from, radiance.times(attenuation));
//
//			}
//
//		}

		private final Box2 bound;
		private final int[] indices;
		private final List<Component> children = new ArrayList<Component>();

	}

	private static final int POLYGON_SURFACE_TOP = 0;
	private static final int POLYGON_SURFACE_BOTTOM = 1;

	private final List<Point2> vertices = new ArrayList<Point2>();
	private final List<Component> components = new ArrayList<Component>();
	private final Plane3 plane;

	private final Point3 origin;
	private final Basis3 basis;
	private final Box2 bound;
	private CategoricalRandom categorical = null;
	private final double area;
	private final Random random = new SimpleRandom();
}
