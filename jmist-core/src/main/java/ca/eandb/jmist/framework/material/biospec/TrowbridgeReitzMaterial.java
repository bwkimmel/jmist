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
package ca.eandb.jmist.framework.material.biospec;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public final class TrowbridgeReitzMaterial extends OpaqueMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = 1839137815524190898L;

  private final double oblateness;

  private final double riBelow;

  private final double riAbove;

  public TrowbridgeReitzMaterial(double oblateness, double riBelow, double riAbove) {
    this.oblateness = oblateness;
    this.riBelow = riBelow;
    this.riAbove = riAbove;
  }

  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {
    double n1 = riAbove;
    double n2 = riBelow;
    Vector3 N = x.getNormal();
    double R = Optics.reflectance(v, n1, n2, N);

    if (RandomUtil.bernoulli(R, rj)) {
      Basis3 basis = x.getBasis();
      double sigma2 = oblateness * oblateness;
      double sigma4 = sigma2 * sigma2;
      Vector3 out;
      double theta = Math.acos(Math.sqrt(((sigma2 / Math.sqrt(sigma4 + (1.0 - sigma4) * ru)) - 1.0) / (sigma2 - 1.0)));
      double phi = 2.0 * Math.PI * rv;
      SphericalCoordinates sc = new SphericalCoordinates(theta, phi);
      Vector3 microN = sc.toCartesian(basis);
      out = Optics.reflect(v, microN);
      if (out.dot(N) <= 0.0) {
        return null;
      }
      return ScatteredRay.diffuse(new Ray3(x.getPosition(), out), lambda.getColorModel().getWhite(lambda), 1.0);
    } else {
      v = Optics.refract(v, n1, n2, N);
      return ScatteredRay.transmitDiffuse(new Ray3(x.getPosition(), v), lambda.getColorModel().getWhite(lambda), 1.0);
    }
  }

}
