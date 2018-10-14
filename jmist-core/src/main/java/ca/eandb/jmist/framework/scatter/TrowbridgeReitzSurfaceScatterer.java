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
package ca.eandb.jmist.framework.scatter;

import org.apache.commons.math3.util.FastMath;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public final class TrowbridgeReitzSurfaceScatterer implements SurfaceScatterer {

  /** Serialization version ID. */
  private static final long serialVersionUID = 2947832022516453405L;

  private final double oblateness;

  private final Function1 riBelow;

  private final Function1 riAbove;

  public TrowbridgeReitzSurfaceScatterer(double oblateness, Function1 riBelow, Function1 riAbove) {
    this.oblateness = oblateness;
    this.riBelow = riBelow;
    this.riAbove = riAbove;
  }

  public TrowbridgeReitzSurfaceScatterer(double oblateness, double riBelow, double riAbove) {
    this(oblateness, new ConstantFunction1(riBelow), new ConstantFunction1(riAbove));
  }

  @Override
  public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
      double lambda, Random rnd) {
    double n1 = riAbove.evaluate(lambda);
    double n2 = riBelow.evaluate(lambda);
    Vector3 N = x.getNormal();
    double R = Optics.reflectance(v, n1, n2, N);

    if (RandomUtil.bernoulli(R, rnd)) {
      Basis3 basis = x.getBasis();
      double sigma2 = oblateness * oblateness;
      double sigma4 = sigma2 * sigma2;
      Vector3 out;
      double theta = FastMath.acos(Math.sqrt(((sigma2 / Math.sqrt(sigma4 + (1.0 - sigma4) * rnd.next())) - 1.0) / (sigma2 - 1.0)));
      double phi = 2.0 * Math.PI * rnd.next();
      SphericalCoordinates sc = new SphericalCoordinates(theta, phi);
      Vector3 microN = sc.toCartesian(basis);
      out = Optics.reflect(v, microN);
      if (out.dot(N) <= 0.0) {
        return null;
      }
      return out;
    } else {
      return Optics.refract(v, n1, n2, N);
    }
  }

}
