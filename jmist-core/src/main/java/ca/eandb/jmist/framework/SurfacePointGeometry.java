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
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents the geometric properties of a point on the surface of some
 * element in a scene.
 * @author Brad Kimmel
 */
public interface SurfacePointGeometry {

  /**
   * The position of the surface point.
   * @return The position of the surface point.
   */
  Point3 getPosition();

  /**
   * The direction perpendicular to the surface.
   * @return The direction perpendicular to the surface.
   */
  Vector3 getNormal();

  /**
   * The orientation of the surface.
   * @return The orientation of the surface.
   */
  Basis3 getBasis();

  /**
   * The micro-surface normal.
   * @return The micro-surface normal.
   */
  Vector3 getShadingNormal();

  /**
   * The micro-surface orientation.
   * @return The micro-surface orientation.
   */
  Basis3 getShadingBasis();

  /**
   * The first tangent vector.
   * @return The first tangent vector.
   */
  Vector3 getTangent();

  /**
   * The texture coordinates.
   * @return The texture coordinates.
   */
  Point2 getUV();

  /**
   * The ID of the primitive from the <code>SceneElement</code>.
   * @return The ID of the primitive from the <code>SceneElement</code>.
   */
  int getPrimitiveIndex();

  /**
   * A surface point at the origin oriented according to the standard basis
   * (the normal in the positive z-direction).
   */
  public static final SurfacePointGeometry STANDARD = new SurfacePointGeometry() {
    public Basis3 getBasis() {
      return Basis3.STANDARD;
    }
    public Vector3 getNormal() {
      return Vector3.K;
    }
    public Point3 getPosition() {
      return Point3.ORIGIN;
    }
    public int getPrimitiveIndex() {
      return 0;
    }
    public Basis3 getShadingBasis() {
      return Basis3.STANDARD;
    }
    public Vector3 getShadingNormal() {
      return Vector3.K;
    }
    public Vector3 getTangent() {
      return Vector3.I;
    }
    public Point2 getUV() {
      return Point2.ORIGIN;
    }
  };

}
