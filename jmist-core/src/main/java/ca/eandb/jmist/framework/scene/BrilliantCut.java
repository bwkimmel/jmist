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
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.geometry.MeshBuilder;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * Static methods for creating the geometry of a brilliant cut diamond, as
 * defined by the Gemological Institute of America (GIA).
 * @see <a href="http://www.gia.edu/diamondcut/05_diamond_anatomy.html">Diamond anatomy</a>
 * @author Brad Kimmel
 */
public final class BrilliantCut {

  /**
   * Specification of the cut geometry, as described by the Gemological
   * Institute of America (GIA).  See
   * <a href="http://www.gia.edu/diamondcut/05_diamond_anatomy.html">
   * http://www.gia.edu/diamondcut/05_diamond_anatomy.html</a>.
   */
  public static final class Info {

    /**
     * Distance measured between two opposing points along the girdle's
     * outline.<br>
     * <br>
     * This field is required.
     */
    public double diameter = 1.0;

    /**
     * Total depth (measured from table plane to culet) relative to the
     * average diameter.<br>
     * <br>
     * This field is required if {@link #relMaxGirdleThickness} is not
     * specified.  If specified, this value must be at least the sum of
     * {@link #relCrownHeight} and {@link #relPavilionDepth}.
     * @see #relMaxGirdleThickness
     * @see #relCrownHeight
     * @see #relPavilionDepth
     */
    public double relTotalDepth = Double.NaN; // out

    /**
     * Avg table size relative to the avg diameter.  Table size is measured
     * from bezel point to bezel point.<br>
     * <br>
     * This field is required.
     */
    public double relTableSize = 0.573;

    /**
     * The angle of the bezel facet plane relative to the table plane.<br>
     * <br>
     * This field is required if {@link #relCrownHeight} is not specified.
     * @see #relCrownHeight
     */
    public double crownAngle = Math.toRadians(34.78);

    /**
     * Average crown height relative to the average diameter. Crown height
     * is measured from the table plane to the intersection of the bezel
     * facet with the girdle.<br>
     * <br>
     * This field is required if {@link #crownAngle} is not specified.
     * @see #crownAngle
     */
    public double relCrownHeight = Double.NaN; // out

    /**
     * The angle of the main facet plane relative to the table plane.<br>
     * <br>
     * This field is required if {@link #relPavilionDepth} is not
     * specified.
     * @see #relPavilionDepth
     */
    public double pavilionAngle = Math.toRadians(40.7);

    /**
     * Average pavilion depth relative to the average diameter. Pavilion
     * depth is measured from the culet to the intersection of the pavilion
     * main facet with the girdle.<br>
     * <br>
     * This field is required if {@link #pavilionAngle} is not specified.
     * @see #pavilionAngle
     */
    public double relPavilionDepth = Double.NaN; // out

    /**
     * The horizontally projected distance from the point of the star facet
     * to the edge of the table, relative to the distance between the table
     * edge and the girdle edge.<br>
     * <br>
     * This field is required.
     */
    public double relStarLength = 0.523;

    /**
     * The horizontally projected distance from the point where two
     * pavilion mains meet to the closest edge of the girdle, relative to
     * the distance between the girdle edge and the center of the
     * culet.<br>
     * <br>
     * This field is required.
     */
    public double relLowerHalfLength = 0.761;

    /**
     * The thickness of the girdle at its thickest point (between the top
     * of the pavilion main and the bottom of the crown bezel) relative to
     * the average diameter.<br>
     * <br>
     * This field is required if {@link #relTotalDepth} is not specified.
     * @see #relTotalDepth
     */
    public double relMaxGirdleThickness = 0.021;

    /**
     * Avg culet size relative to the avg diameter.<br>
     * <br>
     * This field is required.
     */
    public double relCuletSize = 0.004;

  };

  /**
   * Creates a <code>SceneElement</code> representing the geometry of a
   * brilliant cut diamond using the default specifications.
   * @return A <code>SceneElement</code> representing the geometry of a
   *     brilliant cut diamond.
   */
  public static SceneElement createGeometry() {
    return createMeshBuilder().createGeometry();
  }

  /**
   * Creates a <code>SceneElement</code> representing the geometry of a
   * brilliant cut diamond using the provided specifications.
   * @param cut The <code>BrilliantCut.Info</code> describing the cut
   *     geometry.  Information not provided will be computed and the
   *     corresponding fields will be populated.
   * @return A <code>SceneElement</code> representing the geometry of a
   *     brilliant cut diamond.
   * @throws IllegalArgumentException If insufficient information is
   *     provided to determine the cut geometry.
   */
  public static SceneElement createGeometry(Info cut) {
    return createMeshBuilder(cut).createGeometry();
  }

  /**
   * Creates a <code>MeshBuilder</code> prepared with the geometry of a
   * brilliant cut diamond using the default specifications.
   * @return A <code>SceneElement</code> representing the geometry of a
   *     brilliant cut diamond.
   */
  public static MeshBuilder createMeshBuilder() {
    return createMeshBuilder(new Info());
  }

  /**
   * Creates a <code>MeshBuilder</code> prepared with the geometry of a
   * brilliant cut diamond using the provided specifications.
   * @param cut The <code>BrilliantCut.Info</code> describing the cut
   *     geometry.  Information not provided will be computed and the
   *     corresponding fields will be populated.
   * @return A <code>SceneElement</code> representing the geometry of a
   *     brilliant cut diamond.
   * @throws IllegalArgumentException If insufficient information is
   *     provided to determine the cut geometry.
   */
  public static MeshBuilder createMeshBuilder(Info cut) {

    validateInfo(cut);

    double radius = 0.5 * cut.diameter;
    double crownHeight = cut.relCrownHeight * cut.diameter;

    double halfTableDiag = radius * cut.relTableSize;
    double halfTableWidth = halfTableDiag * Math.cos(Math.PI / 8.0);

    double kiteBottom = -cut.relCrownHeight * cut.diameter;
    double lowerMainTop = kiteBottom - cut.relMaxGirdleThickness
        * cut.diameter;

    double pavilionPointDepth = radius * Math.tan(cut.pavilionAngle);

    double culetBottom = -cut.diameter * cut.relTotalDepth;

    double starPointRadius = halfTableWidth * (1.0 - cut.relStarLength)
        + radius * cut.relStarLength;
    double lowerGirdleInner = radius * (1.0 - cut.relLowerHalfLength);

    MeshBuilder b = MeshBuilder.fromCylinder(radius, -culetBottom, 64);
    b.translate(new Vector3(0, 0, culetBottom));

    for (int i = 0; i < 8; i++) {

      double theta0 = 2.0 * Math.PI * ((double) i) / 8.0;
      double theta1 = 2.0 * Math.PI * (((double) i) + 0.5) / 8.0;
      double theta2 = 2.0 * Math.PI * (((double) i) + 1.0) / 8.0;
      double theta3 = 2.0 * Math.PI * (((double) i) + 1.5) / 8.0;
      double cost0 = Math.cos(theta0);
      double sint0 = Math.sin(theta0);
      double cost1 = Math.cos(theta1);
      double sint1 = Math.sin(theta1);
      double cost2 = Math.cos(theta2);
      double sint2 = Math.sin(theta2);
      double cost3 = Math.cos(theta3);
      double sint3 = Math.sin(theta3);

      Point3 p = new Point3(halfTableDiag * cost1, halfTableDiag * sint1, 0.0);
      Point3 q = new Point3(radius * cost1, radius * sint1, -crownHeight);
      Point3 r;

      Basis3 basis = Basis3.fromUW(p.vectorTo(q), Vector3.K);
      Plane3 planeKite = Plane3.throughPoint(p, basis);

      b.slice(planeKite, true);

      q = new Point3(halfTableDiag * cost3, halfTableDiag * sint3, 0.0);
      r = new Point3(starPointRadius * cost2, starPointRadius * sint2, 0.0);

      Ray3 ray = new Ray3(r, Vector3.K);
      r = ray.pointAt(planeKite.intersect(ray));

      Plane3 planeStar = Plane3.throughPoints(q, p, r);
      b.slice(planeStar, true);

      p = new Point3(radius * cost2, radius * sint2, -crownHeight);
      q = new Point3(radius * cost1, radius * sint1, -crownHeight);

      Plane3 planeGirdle = Plane3.throughPoints(q, p, r);
      b.slice(planeGirdle, true);

      q = new Point3(radius * cost3, radius * sint3, -crownHeight);
      planeGirdle = Plane3.throughPoints(p, q, r);
      b.slice(planeGirdle, true);

      p = new Point3(radius * cost1, radius * sint1, lowerMainTop);
      q = new Point3(0.0, 0.0, lowerMainTop - pavilionPointDepth);
      basis = Basis3.fromUW(p.vectorTo(q), Vector3.NEGATIVE_K);

      Plane3 planeMain = Plane3.throughPoint(p, basis);
      b.slice(planeMain, true);

      q = new Point3(radius * cost0, radius * sint0, lowerMainTop);
      r = new Point3(lowerGirdleInner * cost0, lowerGirdleInner * sint0, 0.0);
      ray = new Ray3(r, Vector3.NEGATIVE_K);
      r = ray.pointAt(planeMain.intersect(ray));

      planeGirdle = Plane3.throughPoints(p, q, r);
      b.slice(planeGirdle, true);

      q = new Point3(radius * cost2, radius * sint2, lowerMainTop);
      r = new Point3(lowerGirdleInner * cost2, lowerGirdleInner * sint2, 0.0);
      ray = new Ray3(r, Vector3.NEGATIVE_K);
      r = ray.pointAt(planeMain.intersect(ray));
      planeGirdle = Plane3.throughPoints(q, p, r);
      b.slice(planeGirdle, true);

    }

    b.mergeVertices(MathUtil.EPSILON);
    return b;

  }

  /**
   * Validates the provided <code>BrilliantCut.Info</code> and populates
   * missing fields.
   * @param cut The <code>BrilliantCut.Info</code> describing the cut
   *     geometry.
   */
  private static void validateInfo(Info cut) {
    if (Double.isNaN(cut.diameter)) {
      throw new IllegalArgumentException("cut.diameter is required");
    }
    if (Double.isNaN(cut.relTableSize)) {
      throw new IllegalArgumentException("cut.relTableSize is required");
    }
    if (Double.isNaN(cut.relStarLength)) {
      throw new IllegalArgumentException("cut.relStarLength is required");
    }
    if (Double.isNaN(cut.relLowerHalfLength)) {
      throw new IllegalArgumentException("cut.relLowerHalfLength is required");
    }
    if (Double.isNaN(cut.relCuletSize)) {
      throw new IllegalArgumentException("cut.relCuletSize is required");
    }
    if (Double.isNaN(cut.pavilionAngle)) {
      if (Double.isNaN(cut.relPavilionDepth)) {
        throw new IllegalArgumentException("cut is underspecified, require cut.relPavilionDepth or cut.pavilionAngle");
      }
      cut.pavilionAngle = Math.atan2(2.0 * cut.relPavilionDepth,
          1.0 - cut.relCuletSize);
    } else if (Double.isNaN(cut.relPavilionDepth)) {
      cut.relPavilionDepth = 0.5 * (1.0 - cut.relCuletSize)
          * Math.tan(cut.pavilionAngle);
    }
    if (Double.isNaN(cut.crownAngle)) {
      if (Double.isNaN(cut.relCrownHeight)) {
        throw new IllegalArgumentException("cut is underspecified, require cut.relCrownHeight or cut.crownAngle");
      }
      cut.crownAngle = Math.atan2(2.0 * cut.relCrownHeight,
          1.0 - cut.relTableSize);
    } else if (Double.isNaN(cut.relCrownHeight)) {
      cut.relCrownHeight = 0.5 * (1.0 - cut.relTableSize)
          * Math.tan(cut.crownAngle);
    }
    if (Double.isNaN(cut.relTotalDepth)) {
      if (Double.isNaN(cut.relMaxGirdleThickness)) {
        throw new IllegalArgumentException("cut is underspecified, require cut.relTotalDepth or cut.relMaxGirdleThickness");
      }
      cut.relTotalDepth = cut.relCrownHeight + cut.relMaxGirdleThickness
          + cut.relPavilionDepth;
    } else if (Double.isNaN(cut.relMaxGirdleThickness)) {
      cut.relMaxGirdleThickness = cut.relTotalDepth - cut.relCrownHeight
          - cut.relPavilionDepth;
      if (cut.relMaxGirdleThickness < 0.0) {
        throw new IllegalArgumentException("cut.relTotalDepth is to small");
      }
    }
  }

}
