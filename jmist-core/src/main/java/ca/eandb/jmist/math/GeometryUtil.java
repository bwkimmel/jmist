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
package ca.eandb.jmist.math;

/**
 * Static geometric utility methods.
 * @author brad
 */
public final class GeometryUtil {

  public static Vector2 rayIntersectRay(Ray2 r1, Ray2 r2) {
    Vector2 d1 = r1.direction();
    Vector2 d2 = r2.direction();
    LinearMatrix2 T = new LinearMatrix2(d1.x, -d2.x, d1.y, -d2.y);
    Vector2 o12 = r1.origin().vectorTo(r2.origin());

    return T.inverse().times(o12);
  }

  public static boolean rayIntersectsRay(Ray2 r1, Ray2 r2) {
    Vector2 r = rayIntersectRay(r1, r2);
    return MathUtil.inRangeOO(r.x, 0.0, r1.limit())
        && MathUtil.inRangeOO(r.y, 0.0, r2.limit());
  }

  public static boolean lineSegmentsIntersect(Point2 a1, Point2 a2, Point2 b1, Point2 b2) {
    return rayIntersectsRay(new Ray2(a1, a2), new Ray2(b1, b2));
  }

  public static boolean triangleIntersectsTriangle(Point2 a1, Point2 b1, Point2 c1, Point2 a2, Point2 b2, Point2 c2) {

    Point2 p1 = Point2.centroid(a1, b1, c1);
    Point2 p2 = Point2.centroid(a2, b2, c2);
    if (pointInTriangle(p1, a2, b2, c2) || pointInTriangle(p2, a1, b1, c1)) {
      return true;
    }

    Ray2[] r1 = { new Ray2(a1, b1), new Ray2(b1, c1), new Ray2(c1, a1) };
    Ray2[] r2 = { new Ray2(a2, b2), new Ray2(b2, c2), new Ray2(c2, a2) };

    for (int i = 0; i < r1.length; i++) {
      for (int j = 0; j < r2.length; j++) {
        if (rayIntersectsRay(r1[i], r2[j])) {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean boxIntersectsTriangle(Box2 box, Point2 a, Point2 b, Point2 c) {
    if (box.contains(a) || box.contains(b) || box.contains(c) || pointInTriangle(box.center(), a, b, c)) {
      return true;
    }

    Point2 b0 = box.corner(0);
    Point2 b1 = box.corner(1);
    Point2 b2 = box.corner(2);
    Point2 b3 = box.corner(3);

    Ray2[] bs = { new Ray2(b0, b1), new Ray2(b1, b2), new Ray2(b2, b3), new Ray2(b3, b0) };
    Ray2[] ts = { new Ray2(a, b), new Ray2(b, c), new Ray2(c, a) };

    for (int i = 0; i < bs.length; i++) {
      for (int j = 0; j < ts.length; j++) {
        if (rayIntersectsRay(bs[i], ts[j])) {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean pointInTriangle(Point2 p, Point2 a, Point2 b, Point2 c) {
    Vector2 ab = a.vectorTo(b);
    Vector2 ac = a.vectorTo(c);
    Vector2 ap = a.vectorTo(p);

    double dot00 = ab.dot(ab);
    double dot01 = ab.dot(ac);
    double dot02 = ab.dot(ap);
    double dot11 = ac.dot(ac);
    double dot12 = ac.dot(ap);

    // Compute barycentric coordinates
    double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
    double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

    // Check if point is in triangle
    return (u > 0) && (v > 0) && (u + v < 1);
  }

  public static double areaOfTriangle(Point3 a, Point3 b, Point3 c) {
    Vector3 ab = a.vectorTo(b);
    Vector3 bc = b.vectorTo(c);
    Vector3 ca = c.vectorTo(a);

    double abDOTbc = ab.dot(bc);
    double bcDOTca = bc.dot(ca);
    double caDOTab = ca.dot(ab);

    if (abDOTbc < bcDOTca && abDOTbc < caDOTab) {
      return 0.5 * ab.cross(bc).length();
    } else if (bcDOTca < caDOTab) {
      return 0.5 * bc.cross(ca).length();
    } else {
      return 0.5 * ca.cross(ab).length();
    }
  }

  public static double areaOfTriangle(Point2 a, Point2 b, Point2 c) {
    return Math.abs(0.5 * (
        a.x() * (b.y() - c.y()) +
        b.x() * (c.y() - a.y()) +
        c.x() * (a.y() - b.y())));
  }

  public static double rayIntersectTriangle(Ray3 ray, Point3 a, Point3 b, Point3 c) {
    Plane3 plane = Plane3.throughPoints(a, b, c);
    double t = plane.intersect(ray);
    Point3 p = ray.pointAt(t);
    double abp = areaOfTriangle(a, b, p);
    double bcp = areaOfTriangle(b, c, p);
    double cap = areaOfTriangle(c, a, p);
    double abc = areaOfTriangle(a, b, c);

    if (abp + bcp > abc || bcp + cap > abc || cap + abp > abc) {
      return Double.NaN;
    } else {
      return t;
    }
  }

  /**
   * This class cannot be instantiated.
   */
  private GeometryUtil() {}

}
