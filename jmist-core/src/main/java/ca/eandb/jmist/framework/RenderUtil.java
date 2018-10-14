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

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * Rendering utility methods.
 * @author Brad Kimmel
 */
public final class RenderUtil {

  /**
   * This constructor is private because this class cannot be instantiated.
   */
  private RenderUtil() {}

  /**
   * A basic <code>SurfacePointGeometry</code> that has a position,
   * orientation, micro-surface orientation, and texture coordinates.
   * @author brad
   *
   */
  private static final class SimpleSurfacePointGeometry implements SurfacePointGeometry {

    private final Point3 position;
    private final Basis3 basis;
    private final Basis3 shadingBasis;
    private final Point2 textureCoordinates;

    /**
     * @param position
     * @param basis
     * @param shadingBasis
     * @param textureCoordinates
     */
    public SimpleSurfacePointGeometry(Point3 position, Basis3 basis,
        Basis3 shadingBasis, Point2 textureCoordinates) {
      this.position = position;
      this.basis = basis;
      this.shadingBasis = shadingBasis;
      this.textureCoordinates = textureCoordinates;
    }

    public Basis3 getBasis() {
      return basis;
    }

    public Vector3 getNormal() {
      return basis.w();
    }

    public Point3 getPosition() {
      return position;
    }

    public int getPrimitiveIndex() {
      return 0;
    }

    public Basis3 getShadingBasis() {
      return null;
    }

    public Vector3 getShadingNormal() {
      return shadingBasis.w();
    }

    public Vector3 getTangent() {
      return basis.u();
    }

    public Point2 getUV() {
      return textureCoordinates;
    }

  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param N The normal.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N) {
    Basis3 basis = Basis3.fromW(N);
    return new SimpleSurfacePointGeometry(p, basis, basis, Point2.ORIGIN);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param N The normal.
   * @param shadingNormal The micro-surface normal.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N, Vector3 shadingNormal) {
    Basis3 basis = Basis3.fromW(N);
    Basis3 shadingBasis = Basis3.fromW(shadingNormal);
    return new SimpleSurfacePointGeometry(p, basis, shadingBasis, Point2.ORIGIN);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param basis The orientation of the surface.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis) {
    return new SimpleSurfacePointGeometry(p, basis, basis, Point2.ORIGIN);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param basis The orientation of the surface.
   * @param shadingBasis The micro-surface orientation.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis, Basis3 shadingBasis) {
    return new SimpleSurfacePointGeometry(p, basis, shadingBasis, Point2.ORIGIN);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param N The normal.
   * @param uv The texture coordinates.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N, Point2 uv) {
    Basis3 basis = Basis3.fromW(N);
    return new SimpleSurfacePointGeometry(p, basis, basis, uv);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param N The normal.
   * @param shadingNormal The micro-surface normal.
   * @param uv The texture coordinates.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Vector3 N, Vector3 shadingNormal, Point2 uv) {
    Basis3 basis = Basis3.fromW(N);
    Basis3 shadingBasis = Basis3.fromW(shadingNormal);
    return new SimpleSurfacePointGeometry(p, basis, shadingBasis, uv);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param basis The orientation.
   * @param uv The texture coordinates.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis, Point2 uv) {
    return new SimpleSurfacePointGeometry(p, basis, basis, uv);
  }

  /**
   * Creates a <code>SurfacePointGeometry</code>.
   * @param p The position.
   * @param basis The orientation.
   * @param shadingBasis The micro-surface orientation.
   * @param uv The texture coordinates.
   * @return A <code>SurfacePointGeometry</code>.
   */
  public static SurfacePointGeometry createSurfacePointGeometry(Point3 p, Basis3 basis, Basis3 shadingBasis, Point2 uv) {
    return new SimpleSurfacePointGeometry(p, basis, shadingBasis, uv);
  }

}
