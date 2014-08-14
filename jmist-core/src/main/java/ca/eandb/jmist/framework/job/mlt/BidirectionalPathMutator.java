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
package ca.eandb.jmist.framework.job.mlt;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.job.bidi.MeasurementContributionMeasure;
import ca.eandb.jmist.framework.job.bidi.PathMeasure;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.Path;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.PathUtil;
import ca.eandb.jmist.framework.path.ScatteringNode;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author brad
 *
 */
public final class BidirectionalPathMutator implements PathMutator {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5762556459737798003L;

  private final PathMeasure importance;

  public BidirectionalPathMutator(PathMeasure importance) {
    this.importance = importance;
  }

  public BidirectionalPathMutator() {
    this(MeasurementContributionMeasure.getInstance());
  }

  private static final class IntPair {
    final int s;
    final int t;

    IntPair(int s, int t) {
      this.s = s;
      this.t = t;
    }
  };

  private final void bp() {}

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.job.mlt.PathMutator#getTransitionPDF(ca.eandb.jmist.framework.path.Path, ca.eandb.jmist.framework.path.Path)
   */
  public double getTransitionPDF(Path x, Path y) {
    IntPair slice = getCommonSlice(x, y);
    double pd = getSliceProbability(x, slice);
    int kx = x.getLength();
    int ky = y.getLength();
    int ka = ky - (slice.s + slice.t);
    int kd = kx - (slice.s + slice.t);

    double pa1 = getAddedPathLengthProbability(ka, kd);

    /* Equal probability is assigned to each of the ka ways of adding ka
     * edges to the path (see page 346 of Veach's thesis), therefore
     * pa2 == 1 / ka.
     */
    double pa = pa1 / (double) ka;

    if (ka == 1) {
      if (Double.isNaN(pd) || Double.isNaN(pa)) bp();
      return pd * pa;
    }

    int sy = y.getLightPathLength();
    int ty = y.getEyePathLength();

    PathNode lightNode = y.getLightTail();
    PathNode eyeNode = y.getEyeTail();
    double p0 = 1.0;
    for (int l1 = sy - slice.s; l1 > 0; l1--) { // FIXME check this
      p0 *= (lightNode.getPDF() * lightNode.getGeometricFactor());
      lightNode = lightNode.getParent();
    }
    for (int m1 = ty - slice.t; m1 > 0; m1--) { // FIXME check this
      p0 *= (eyeNode.getPDF() * eyeNode.getGeometricFactor());
      eyeNode = eyeNode.getParent();
    }

    lightNode = y.getLightTail();
    eyeNode = y.getEyeTail();
    double gle = (lightNode == null || eyeNode == null) ? 1.0
        : PathUtil.getGeometricFactor(lightNode, eyeNode);

    double[] p = new double[ka];

    if (sy - slice.s >= 0 || sy - slice.s < p.length) {
      p[sy - slice.s] = p0;
    }

    PathNode x0, x1, x2;
    double p1;

    x2 = y.getLightTail();
    x1 = y.getEyeTail();

    p1 = p0;
    x0 = x1 != null ? x1.getParent() : null;
    for (int l1 = sy - slice.s - 1; l1 >= 0; l1--) {
      assert(x2 != null);

      double g = (x1 == null || x1.isOnEyePath())
          ? gle : x1.getGeometricFactor();
      double rpdf;

      if (x1 == null) {
        rpdf = 0.0; // Aperture not modeled as part of scene
      } else if (x1.isOnEyePath()) {
        rpdf = x1.getPDF(PathUtil.getDirection(x1, x2));
      } else if (x0 == null) {
        rpdf = 0.0;
      } else if (x0.isOnLightPath() && x0.isSpecular()) {
        rpdf = x0.getPDF();
      } else {
        rpdf = x1.getReversePDF(PathUtil.getDirection(x0, x1));
      }

      double ratio = (rpdf * g) / (x2.getPDF() * x2.getGeometricFactor());
      p1 *= Double.isNaN(ratio) ? 0.0 : ratio;
      if (l1 >= 0 && l1 < p.length) {
        p[l1] = p1;
      }

      x0 = x1;
      x1 = x2;
      x2 = x2.getParent();
    }

    x2 = y.getEyeTail();
    x1 = y.getLightTail();

    p1 = p0;
    x0 = x1 != null ? x1.getParent() : null;
    for (int m1 = ty - slice.t - 1; m1 >= 0; m1--) {
      int l1 = ka - 1 - m1;
      assert(x2 != null);

      double g = (x1 == null || x1.isOnLightPath())
          ? gle : x1.getGeometricFactor();
      double rpdf;

      if (x1 == null) {
        rpdf = x2 instanceof ScatteringNode
            ? ((ScatteringNode) x2).getSourcePDF()
            : 0.0;
      } else if (x1.isOnLightPath()) {
        rpdf = x1.getPDF(PathUtil.getDirection(x1, x2));
      } else if (x0 == null) {
        rpdf = 1.0;
        bp();
      } else if (x0.isOnEyePath() && x0.isSpecular()) {
        rpdf = x0.getPDF();
      } else {
        rpdf = x1.getReversePDF(PathUtil.getDirection(x0, x1));
      }

      double ratio = (rpdf * g) / (x2.getPDF() * x2.getGeometricFactor());
      p1 *= Double.isNaN(ratio) ? 0.0 : ratio;
      if (l1 >= 0 && l1 < p.length) {
        p[l1] = p1;
      }

      x0 = x1;
      x1 = x2;
      x2 = x2.getParent();
    }

    /* FIXME do we need to account for specular nodes?  If so, then set the
     * appropriate entries c[i] to zero, otherwise we don't need to store
     * the array for c[] to compute its sum.
     */

    return (pd * pa) * MathUtil.sum(p);
  }

  /**
   * Finds the slice that is common between two paths.
   * @param x The first <code>Path</code>.
   * @param y The second <code>Path</code>.
   * @return An <code>IntPair</code>, <code>slice</code>, for which
   *     <code>x.slice(slice.s, slice.t)</code> and
   *     <code>y.slice(slice.s, slice.t)</code> yield the same
   *     <code>Path</code>, but for which <code>x.slice(s, t)</code> and
   *     <code>y.slice(s, t)</code> yield different paths for every pair
   *     <code>s</code>, </code>t</code> satisfying
   *     <code>s &gt; slice.s || t &gt; slice.t</code>.
   */
  private IntPair getCommonSlice(Path x, Path y) {

    PathNode[] xNodes = x.toPathNodes();
    PathNode[] yNodes = y.toPathNodes();
    int k = Math.min(xNodes.length, yNodes.length);
    int s, t;

    for (s = 0; s < k; s++) {
      if (!PathUtil.isSameNode(xNodes[s], yNodes[s])) {
        break;
      }
    }
    if (xNodes.length == yNodes.length && s == k) {
      return new IntPair(x.getLightPathLength(), x.getEyePathLength());
    }
    s -= 1;

    int i = xNodes.length - 1;
    int j = yNodes.length - 1;
    for (t = 0; t < k; t++) {
      if (!PathUtil.isSameNode(xNodes[i--], yNodes[j--])) {
        break;
      }
    }
    t -= 1;

    return new IntPair(s, t);

  }

  private double getSliceProbability(Path x, IntPair slice) {
    CategoricalRandom r = generateDeletedSubpathProbabilities(x);
    int index = getSliceIndex(x, slice);
    return r.getProbability(index);
  }

  private int getSliceIndex(Path x, IntPair slice) {
    int k = x.getLength();
    int kd = k - (slice.s + slice.t);

    // XXX very inefficient
    for (int l = -1, i = 0; l <= k; l++) {
      for (int m = l + 1; m <= k + 1; m++, i++) {
        if (l == slice.s && kd == (m - l)) {
          return i;
        }
      }
    }

    return -1;
  }

  private IntPair getSlice(Path x, int index) {
    int k = x.getLength();

    // XXX very inefficient
    for (int l = -1, i = 0; l <= k; l++) {
      for (int m = l + 1; m <= k + 1; m++, i++) {
        if (i == index) {
          return new IntPair(l, k - m);
        }
      }
    }

    return null;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.job.mlt.PathMutator#mutate(ca.eandb.jmist.framework.path.Path, ca.eandb.jmist.framework.Random)
   */
  public Path mutate(Path x, Random rnd) {

    int k = x.getLength();
    CategoricalRandom r = generateDeletedSubpathProbabilities(x);
    IntPair slice = getSlice(x, r.next(rnd));

    //slice = new IntPair(0, 0);

    PathNode newLightTail = x.getLightTail();
    PathNode newEyeTail = x.getEyeTail();

    while (newLightTail != null && newLightTail.getDepth() > slice.s) {
      newLightTail = newLightTail.getParent();
    }
    while (newEyeTail != null && newEyeTail.getDepth() > slice.t) {
      newEyeTail = newEyeTail.getParent();
    }


    int kd = k - (slice.s + slice.t);
    int ka = getAddedSubpathLength(kd, rnd);

    //ka = 2;

    if (ka == 1 && kd == 1) { // mutation has no effect
      return null;
    }

    int l1 = RandomUtil.discrete(0, ka - 1, rnd);
    int m1 = ka - 1 - l1;

    if (newLightTail == null && l1 > 0) {
      PathInfo pi = x.getPathInfo();
      Light light = pi.getScene().getLight();
      newLightTail = light.sample(pi, rnd.next(), rnd.next(), rnd.next());
      l1--;
    }
    while (l1-- > 0) {
      newLightTail = newLightTail.expand(rnd.next(), rnd.next(), rnd.next());
      if (newLightTail == null || newLightTail.isAtInfinity()) {
        return null;
      }
    }

    if (newEyeTail == null && m1 > 0) {
      PathInfo pi = x.getPathInfo();
      Lens lens = pi.getScene().getLens();
      Point2 p = RandomUtil.canonical2(rnd);
      newEyeTail = lens.sample(p, pi, rnd.next(), rnd.next(), rnd.next());
      m1--;
    }
    while (m1-- > 0) {
      newEyeTail = newEyeTail.expand(rnd.next(), rnd.next(), rnd.next());
      if (newEyeTail == null || newEyeTail.isAtInfinity()) {
        return null;
      }
    }

    if (newLightTail == null || newEyeTail == null
        || PathUtil.visibility(newLightTail, newEyeTail)) {
      return new Path(newLightTail, newEyeTail);
    }

    return null;

  }

  private double[] getAllUnweightedContributions(Path x) {
    int k = x.getLength();
    double[] c = new double[k + 2];

    for (int s = -1, t = k; s <= k; s++, t--) {
      Path y = x.slice(s, t);
      if (y != null) {
        Color color = importance.evaluate(y.getLightTail(), y.getEyeTail());
        c[s + 1] = color != null ? color.luminance() : 0.0; // FIXME use a ColorMeasure
      }
    }

    return c;
  }

  private CategoricalRandom generateDeletedSubpathProbabilities(Path path) {
    int k = path.getLength();
    int n = ((k + 2) * (k + 3)) / 2;
    double[] pd = new double[n];
    double[] c = getAllUnweightedContributions(path);

    for (int l = -1, i = 0; l <= k; l++) {
      for (int m = l + 1; m <= k + 1; m++, i++) {
        int kd = m - l;
        double pd1 = (kd == 1) ? 0.25 : (kd == 2) ? 0.5
            : Math.scalb(1.0, -kd);
        double pd2 = 0.0;

        for (int s = l + 1; s <= m; s++) {
          if (c[s] > MathUtil.SMALL_EPSILON) { // FIXME
            pd2 += 1.0 / c[s];
          }
        }

        pd[i] = pd1 * pd2;
      }
    }
    return new CategoricalRandom(pd);
  }

  private int getAddedSubpathLength(int kd, Random rnd) {
    double c = kd > 1 ? 1.0 - Math.scalb(1.0, -(kd - 1)) : 0.75;
    double x = c * rnd.next();
    double limit = 0.5;

    if (x < limit) {
      return kd;
    }

    limit += 0.15;
    if (x < limit) {
      return kd + 1;
    }

    if (kd > 1) {
      limit += 0.15;
      if (x < limit) {
        return kd - 1;
      }
    }

    for (int j = 2;; j++) {
      double p = Math.scalb(0.2, -j);
      limit += p;
      if (x < limit) {
        return kd + j;
      }
      if (kd > j) {
        limit += p;
        if (x < limit) {
          return kd - j;
        }
      }
    }
  }

  /**
   * Computes the probability of adding <code>ka</code> edges to the path,
   * given that <code>kd</code> edges were removed.
   * @param ka The number of edges being added (must be positive).
   * @param kd The number of edges that were deleted (must be positive).
   * @return The probability of adding <code>ka</code> edges to the path,
   *     given that <code>kd</code> edges were removed.  If <code>ka &lt;= 0
   *     || kd &lt;= 0</code>, the result is undefined.
   */
  private double getAddedPathLengthProbability(int ka, int kd) {
    double c = kd > 1 ? 1.0 - Math.scalb(1.0, -(kd - 1)) : 0.75;
    int j = Math.abs(ka - kd);

    switch (j) {
    case 0: return 0.5 / c;
    case 1: return 0.15 / c;
    default:
      return Math.scalb(0.2, -j) / c;
    }
  }

}
