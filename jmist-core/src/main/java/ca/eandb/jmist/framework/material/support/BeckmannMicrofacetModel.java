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
package ca.eandb.jmist.framework.material.support;

import org.apache.commons.math3.util.FastMath;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class BeckmannMicrofacetModel implements IsotropicMicrofacetModel {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5818588950337807120L;

  public static final BeckmannMicrofacetModel GROUND = new BeckmannMicrofacetModel(0.344);

  public static final BeckmannMicrofacetModel FROSTED = new BeckmannMicrofacetModel(0.400);

  public static final BeckmannMicrofacetModel ETCHED = new BeckmannMicrofacetModel(0.493);

  public static final BeckmannMicrofacetModel ANTIGLARE = new BeckmannMicrofacetModel(0.023);

  private final double alpha;

  public BeckmannMicrofacetModel(double alpha) {
    this.alpha = alpha;
  }

  @Override
  public double getDistributionPDF(Vector3 m, Vector3 n) {
    double mdotn = m.dot(n);

    if (mdotn <= 0.0) {
      return 0.0;
    }

    double c4 = mdotn * mdotn * mdotn * mdotn;
    double t = Math.tan(FastMath.acos(mdotn));
    double a2 = alpha * alpha;

    return Math.exp(-t * t / a2) / (Math.PI * a2 * c4);
  }

  @Override
  public double getShadowingAndMasking(Vector3 in, Vector3 out, Vector3 m,
      Vector3 n) {

    double ndoti = -n.dot(in);
    double ndoto = n.dot(out);
    double mdoti = -m.dot(in);
    double mdoto = m.dot(out);

    if (mdoti / ndoti <= 0.0 || mdoto / ndoto <= 0.0) {
      return 0.0;
    }

    double ti = Math.tan(FastMath.acos(Math.abs(ndoti)));
    double ai = 1.0 / (alpha * ti);
    double gi = ai < 1.6 ? (3.535 * ai + 2.181 * ai * ai) / (1.0 + 2.276 * ai + 2.577 * ai * ai) : 1.0;

    double to = Math.tan(FastMath.acos(Math.abs(ndoto)));
    double ao = 1.0 / (alpha * to);
    double go = ao < 1.6 ? (3.535 * ao + 2.181 * ao * ao) / (1.0 + 2.276 * ao + 2.577 * ao * ao) : 1.0;

    return gi * go;

  }

  @Override
  public SphericalCoordinates sample(double ru, double rv) {
    return new SphericalCoordinates(
        Math.atan(Math.sqrt(-alpha * alpha * Math.log(1.0 - ru))),
        2.0 * Math.PI * rv);
  }

}
