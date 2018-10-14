/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.accel.Grid3;
import ca.eandb.jmist.framework.accel.Grid3.Cell;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Matrix;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * A polygonal <code>SceneElement</code> with a uniform grid for the <code>x</code>
 * and <code>z</code> coordinates of the verticies.
 * @author Brad Kimmel
 */
public final class HeightFieldGeometry extends PrimitiveGeometry {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -9067100310767210560L;

  /**
   * Creates a new <code>HeightFieldGeometry</code>.
   * @param xz The <code>Box2</code> describing the extent of the height
   *     field along the <code>x</code> and <code>z</code> axes (must not be
   *     empty).
   * @param height A <code>Matrix</code> with at least <code>2</code> rows
   *     and <code>2</code> columns, with <code>height.at(i, j)</code>
   *     being the <code>y</code> coordinate corresponding to
   *     <code>x == xz.interpolateX(i / (height.rows() - 1))</code> and
   *     <code>z == xz.interpolateY(j / (height.columns() - 1))</code> for
   *     <code>0 &lt;= i &lt; height.rows()</code> and
   *     <code>0 &lt;= j &lt; height.columns()</code>.
   */
  public HeightFieldGeometry(Box2 xz, Matrix height) {
    if (xz.isEmpty()) {
      throw new IllegalArgumentException("xz must be non-empty");
    }

    if (height.rows() < 2 || height.columns() < 2) {
      throw new IllegalArgumentException("height must have at least two rows and two columns");
    }

    Box3 bounds = new Box3(xz.spanX(), height.range().expand(MathUtil.EPSILON), xz.spanY());
    this.grid = new Grid3(bounds, height.rows() - 1, 1, height.columns() - 1);
    this.height = height;
  }

  @Override
  public void intersect(Ray3 ray, final IntersectionRecorder recorder) {

    this.grid.intersect(ray, recorder.interval(), new Grid3.Visitor() {

      @Override
      public boolean visit(Ray3 ray, Interval I, Cell cell) {

        /* Get the points at which the ray enters and exits the cell.
         */
        Point3 p0 = ray.pointAt(I.minimum());
        Point3 p1 = ray.pointAt(I.maximum());

        /* Get the range of y-values for the ray within the cell, and
         * get the portion of the height matrix within the cell.
         */
        Interval h = Interval.between(p0.y(), p1.y());
        Matrix slice = height.slice(cell.getX(), cell.getZ(), 2, 2);

        boolean hit = false;

        /* If the range of y-values intersect, then there may be an
         * intersection.
         */
        if (h.intersects(slice.range())) {

          Box3 bounds = cell.getBoundingBox();

          /* Get the points on the height field. */
          Point3 p00 = new Point3(bounds.minimumX(), slice.at(0, 0), bounds.minimumZ());
          Point3 p01 = new Point3(bounds.minimumX(), slice.at(0, 1), bounds.maximumZ());
          Point3 p10 = new Point3(bounds.maximumX(), slice.at(1, 0), bounds.minimumZ());
          Point3 p11 = new Point3(bounds.maximumX(), slice.at(1, 1), bounds.maximumZ());

          Plane3 plane;
          double t;

          /* Divide the four points into two triangles and check for
           * an intersection with each triangle.
           */
          plane = Plane3.throughPoints(p00, p10, p11);
          t = plane.intersect(ray);

          if (I.contains(t)) {
            Point3 p = ray.pointAt(t);

            /* Get the normalized (x,z) coordinates, (cx, cz),
             * within the bounds of the cell.  If cx > cz, then
             * the intersection hit the triangle, otherwise, it hit
             * the plane, but on the other half of the cell.
             */
            double cx = (p.x() - p00.x()) / (p10.x() - p00.x());
            double cz = (p.z() - p10.z()) / (p11.z() - p10.z());

            if (cx > cz) {
              Intersection x = newIntersection(ray, t, ray.direction().dot(plane.normal()) < 0.0)
                .setBasis(Basis3.fromW(plane.normal(), Basis3.Orientation.RIGHT_HANDED))
                .setLocation(p);

              recorder.record(x);

              hit = true;
            }
          }

          plane = Plane3.throughPoints(p00, p11, p01);
          t = plane.intersect(ray);

          if (I.contains(t)) {
            Point3 p = ray.pointAt(t);

            /* Get the normalized (x,z) coordinates, (cx, cz),
             * within the bounds of the cell.  If cx < cz, then
             * the intersection hit the triangle, otherwise, it hit
             * the plane, but on the other half of the cell.
             */
            double cx = (p.x() - p00.x()) / (p10.x() - p00.x());
            double cz = (p.z() - p10.z()) / (p11.z() - p10.z());

            if (cx < cz) {
              Intersection x = newIntersection(ray, t, ray.direction().dot(plane.normal()) < 0.0)
                .setBasis(Basis3.fromW(plane.normal(), Basis3.Orientation.RIGHT_HANDED))
                .setLocation(p);

              recorder.record(x);

              hit = true;
            }
          }

        }

        /* If we got a hit, and if the recorder does not need all
         * intersections, then we are done.
         */
        return !hit || recorder.needAllIntersections();

      }

    });

  }

  @Override
  protected Point2 getTextureCoordinates(GeometryIntersection x) {
    Point3 p = x.getPosition();
    Box3 bounds = grid.getBoundingBox();
    return new Point2(
        (p.x() - bounds.minimumX()) / (bounds.maximumX() - bounds.minimumX()),
        (p.z() - bounds.minimumZ()) / (bounds.maximumZ() - bounds.minimumZ())
    );
  }

  @Override
  public Box3 boundingBox() {
    return grid.getBoundingBox();
  }

  @Override
  public Sphere boundingSphere() {

    List<Point3> points = new ArrayList<Point3>();
    Box3 bounds = grid.getBoundingBox();
    int nx = height.rows();
    int nz = height.columns();

    for (int ix = 0; ix < nx; ix++) {
      double x = (double) ix / (double) (nx - 1);
      for (int iz = 0; iz < nz; iz++) {
        double z = (double) iz / (double) (nz - 1);
        points.add(new Point3(bounds.interpolateX(x), height.at(ix, iz), bounds.interpolateZ(z)));
      }
    }

    return Sphere.smallestContaining(points);

  }

  /** The <code>Grid3</code> elements of the height field. */
  private final Grid3 grid;

  /** The <code>Matrix</code> of <code>y</code> coordinates. */
  private final Matrix height;

}
