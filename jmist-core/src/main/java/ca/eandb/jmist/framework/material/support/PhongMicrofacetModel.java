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
package ca.eandb.jmist.framework.material.support;

import org.apache.commons.math3.util.FastMath;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class PhongMicrofacetModel implements IsotropicMicrofacetModel {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5818588950337807120L;

  public static final PhongMicrofacetModel GROUND = new PhongMicrofacetModel(1.410);

  public static final PhongMicrofacetModel FROSTED = new PhongMicrofacetModel(1.162);

  public static final PhongMicrofacetModel ETCHED = new PhongMicrofacetModel(0.848);

  public static final PhongMicrofacetModel ANTIGLARE = new PhongMicrofacetModel(11.188);

  private final double alpha;

  public PhongMicrofacetModel(double alpha) {
    this.alpha = alpha;
  }

  @Override
  public double getDistributionPDF(Vector3 m, Vector3 n) {
    double mdotn = m.dot(n);

    if (mdotn <= 0.0) {
      return 0.0;
    }

    double ca = Math.pow(mdotn, alpha);
    double k = (alpha + 2.0) / (2.0 * Math.PI);

    return k * ca;

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
    double ai = Math.sqrt(0.5 * alpha + 1.0) / ti;
    double gi = ai < 1.6 ? (3.535 * ai + 2.181 * ai * ai) / (1.0 + 2.276 * ai + 2.577 * ai * ai) : 1.0;

    double to = Math.tan(FastMath.acos(Math.abs(ndoto)));
    double ao = Math.sqrt(0.5 * alpha + 1.0) / to;
    double go = ao < 1.6 ? (3.535 * ao + 2.181 * ao * ao) / (1.0 + 2.276 * ao + 2.577 * ao * ao) : 1.0;

    return gi * go;

  }

  @Override
  public SphericalCoordinates sample(double ru, double rv) {
    return new SphericalCoordinates(
        FastMath.acos(Math.pow(ru, 1.0 / (alpha + 2.0))),
        2.0 * Math.PI * rv);
  }

}
