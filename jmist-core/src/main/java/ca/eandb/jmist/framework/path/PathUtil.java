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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * Static utility methods for paths.
 * @author Brad Kimmel
 */
public final class PathUtil {

  public static double getGeometricFactor(PathNode a, PathNode b) {
    boolean aAtInf = a.isAtInfinity();
    boolean bAtInf = b.isAtInfinity();
    if (aAtInf && bAtInf) {
      return 0.0;
    } else if (bAtInf) {
      Vector3 v = (Vector3) b.getPosition();
      return Math.max(a.getCosine(v), 0.0);
    } else if (aAtInf) {
      Vector3 v = (Vector3) a.getPosition();
      return Math.max(b.getCosine(v), 0.0);
    } else {
      Point3 p = (Point3) a.getPosition();
      Point3 q = (Point3) b.getPosition();
      Vector3 v = p.vectorTo(q);
      return Math.max(a.getCosine(v), 0.0) * Math.max(b.getCosine(v.opposite()), 0.0)
          / v.squaredLength();
    }
  }

  public static boolean visibility(PathNode a, PathNode b) {
    Ray3 ray = Ray3.create(a.getPosition(), b.getPosition());
    if (ray != null) {
      PathInfo path = a.getPathInfo();
      VisibilityFunction3 vf = path.getScene().getRoot();
      return vf.visibility(ray);
    } else { // ray == null
      return false;
    }
  }

  public static Color join(PathNode a, PathNode b) {

    Vector3 v = PathUtil.getDirection(a, b);

    if (v == null) {
      return null;
    }

    double g = PathUtil.getGeometricFactor(a, b);

    if (g <= 0.0) {
      return null;
    }

    Color etol = a.scatter(v);
    Color ltoe = b.scatter(v.opposite());
    Color c = etol.times(ltoe);

    if (ColorUtil.getTotalChannelValue(c) > 0.0
        && PathUtil.visibility(a, b)) {

      c = c.times(g);

      c = c.times(a.getCumulativeWeight()).times(
          b.getCumulativeWeight());

      return c;

    } else { // No mutual scattering or nodes not mutually visible
      return null;
    }
  }

  public static Vector3 getDirection(PathNode from, PathNode to) {
    boolean fromInfinity = from.isAtInfinity();
    boolean toInfinity = to.isAtInfinity();
    if (fromInfinity && toInfinity) {
      return null;
    } else if (fromInfinity) {
      return from.getPosition().toVector3().unit().opposite();
    } else if (toInfinity) {
      return to.getPosition().toVector3().unit();
    } else {
      return from.getPosition().toPoint3().unitVectorTo(
          to.getPosition().toPoint3());
    }
  }

  public static PathNode expand(PathNode node, int depth, Random rnd) {
    if (node != null) {
      for (int i = 0; i < depth; i++) {
        double ru = RandomUtil.canonical(rnd);
        double rv = RandomUtil.canonical(rnd);
        double rj = RandomUtil.canonical(rnd);
        PathNode next = node.expand(ru, rv, rj);
        if (next == null) {
          break;
        }
        node = next;
      }
    }
    return node;
  }

  public static boolean isSameNode(PathNode a, PathNode b) {
    HPoint3 p = a.getPosition();
    HPoint3 q = b.getPosition();
    double epsilon = MathUtil.MACHINE_EPSILON;
    return (p.isPoint() == q.isPoint())
        && MathUtil.equal(p.x(), q.x(), epsilon)
        && MathUtil.equal(p.y(), q.y(), epsilon)
        && MathUtil.equal(p.z(), q.z(), epsilon);
  }

  private PathUtil() {}

}
