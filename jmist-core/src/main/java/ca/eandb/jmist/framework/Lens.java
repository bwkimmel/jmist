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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.EyeNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.math.Box2;
import ca.eandb.jmist.math.Point2;

/**
 * Generates the ray to cast corresponding to given points on the
 * image plane.
 * @author Brad Kimmel
 */
public interface Lens extends Serializable {

  /**
   * Gets a ray indicating from which point and direction the camera is
   * sensitive to incoming light at the specified point on its image plane.
   * This will correspond to the direction to cast a ray in order to shade
   * the specified point on the image plane.
   * @param p The point on the image plane in normalized device coordinates
   *     (must fall within {@code Box2.UNIT}).
   * @param lambda The <code>WavelengthPacket</code> indicating the colour of
   *     light being traced.
   * @param rnd The <code>Random</code> number generated to use in generating
   *     the ray.
   * @return The ray to cast for ray shading.
   * @see Box2#UNIT
   */
  ScatteredRay rayAt(Point2 p, WavelengthPacket lambda, Random rnd);

  /**
   * Creates the terminal <code>EyeNode</code> for use by path-integration
   * based rendering algorithms.
   * @param p The point on the image plane in normalized device coordinates
   *     (must fall within {@code Box2.UNIT}).
   * @param pathInfo The <code>PathInfo</code> describing the context in
   *     which the path is being generated.
   * @param ru The first random variable (must be in [0, 1]).
   * @param rv The second random variable (must be in [0, 1]).
   * @param rj The third random variable (must be in [0, 1]).
   * @return A new <code>EyeNode</code>.
   */
  EyeNode sample(Point2 p, PathInfo pathInfo, double ru, double rv, double rj);

  /** A dummy <code>Lens</code> that does not render anything. */
  Lens NULL = new Lens() {
    private static final long serialVersionUID = 2076070894932926479L;
    public ScatteredRay rayAt(Point2 p, WavelengthPacket lambda, Random rnd) {
      return null;
    }
    public EyeNode sample(Point2 p, PathInfo pathInfo, double ru,
        double rv, double rj) {
      return null;
    }
  };

//
//  /**
//   * Projects a point in three-dimensional space onto the image plane.
//   * @param p The <code>Point3</code> to project onto the image plane.
//   * @return The <code>Projection</code> representing the projection of
//   *     <code>p</code> onto the image plane, or <code>null</code> if
//   *     <code>p</code> does not project onto the image plane.
//   */
//  Projection project(Point3 p);
//
//  /**
//   * Projects a point at infinite distance in three-dimensional space onto
//   * the image plane.
//   * @param v The <code>Vector3</code> indicating the direction of the point
//   *     at an infinite distance.  This is assumed to be a unit vector.  If
//   *     it is not, the results are undefined.
//   * @return The <code>Projection</code> representing the projection of a
//   *     point at an infinite distance in the direction of <code>v</code>
//   *     onto the image plane, or <code>null</code> if <code>v</code> does
//   *     not project onto the image plane.
//   */
//  Projection project(Vector3 v);
//
//  /**
//   * Projects a homogenized point in three-dimensional space onto the image
//   * plane.  This is equivalent to {@link #project(Point3)} or to
//   * {@link #project(Vector3)} depending on whether the homogenized point is
//   * a point or a vector.
//   * @param p The <code>HPoint3</code> representing the homogenized point to
//   *     project.
//   * @return The <code>Projection</code> representing the projection of
//   *     <code>p</code> onto the image plane, or <code>null</code> if
//   *     <code>p</code> does not project onto the image plane.
//   */
//  Projection project(HPoint3 p);
//
//  /**
//   * A representation of the projection of a point in three dimensional space
//   * onto the image plane represented by a <code>Lens</code>.
//   * @author Brad Kimmel
//   */
//  public static interface Projection {
//
//    /**
//     * Returns the <code>Point2</code> representing the normalized point on
//     * the image plane.
//     * @return The <code>Point2</code> representing the normalized point on
//     *     the image plane.
//     */
//    Point2 pointOnImagePlane();
//
//    /**
//     * Returns the <code>Point3</code> representing the physical point on
//     * the <code>Lens</code>.
//     * @return The <code>Point3</code> representing the physical point on
//     *     the <code>Lens</code>.
//     */
//    Point3 pointOnLens();
//
//    /**
//     * Returns the importance associated with this projection.  This is the
//     * value that should be used to attenuate contribution rays during
//     * light tracing.
//     * @return The importance associated with this projection.
//     */
//    double importance();
//
//  }

}
