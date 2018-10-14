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
 * @author Brad Kimmel
 *
 */
public final class GGXMicrofacetModel implements IsotropicMicrofacetModel {

  /** Serialization version ID. */
  private static final long serialVersionUID = 4961148298787587229L;

  public static final GGXMicrofacetModel GROUND = new GGXMicrofacetModel(0.394);

  public static final GGXMicrofacetModel FROSTED = new GGXMicrofacetModel(0.454);

  public static final GGXMicrofacetModel ETCHED = new GGXMicrofacetModel(0.553);

  public static final GGXMicrofacetModel ANTIGLARE = new GGXMicrofacetModel(0.027);

  private final double alpha;

  public GGXMicrofacetModel(double alpha) {
    this.alpha = alpha;
  }

  @Override
  public double getDistributionPDF(Vector3 m, Vector3 n) {
    double mdotn = m.dot(n);
    if (mdotn < 0.0) {
      return 0.0;
    }

    double a2 = alpha * alpha;
    double c4 = mdotn * mdotn * mdotn * mdotn;
    double t = Math.tan(FastMath.acos(mdotn));
    double t2 = t * t;
    double a2pt2 = a2 + t2;

    return a2 / (Math.PI * c4 * a2pt2 * a2pt2);
  }

  @Override
  public double getShadowingAndMasking(Vector3 in, Vector3 out, Vector3 m,
      Vector3 n) {
    double mdoti = -m.dot(in);
    double ndoti = -n.dot(in);
    double mdoto = m.dot(out);
    double ndoto = n.dot(out);

    if (mdoti / ndoti < 0.0 || mdoto / ndoto < 0.0) {
      return 0.0;
    }

    double a2 = alpha * alpha;
    double ti = Math.tan(FastMath.acos(ndoti));
    double t2i = ti * ti;
    double to = Math.tan(FastMath.acos(ndoto));
    double t2o = to * to;

    return 4.0 / ((1.0 + Math.sqrt(1.0 + a2 * t2i)) * (1.0 + Math.sqrt(1.0 + a2 * t2o)));
  }

  @Override
  public SphericalCoordinates sample(double ru, double rv) {
    return new SphericalCoordinates(Math.atan(alpha
        * Math.sqrt(ru / (1.0 - ru))), 2.0 * Math.PI * rv);
  }

}
