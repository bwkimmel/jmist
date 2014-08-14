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
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * @author Brad Kimmel
 */
public final class CookTorranceMaterial extends AbstractMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = -4693726623498649118L;

  private final double mSquared;

  private final Spectrum n;

  private final Spectrum k;

  public CookTorranceMaterial(double m, Spectrum n, Spectrum k) {
    this.mSquared = m * m;
    this.n = n;
    this.k = k;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {
    throw new UnimplementedException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
      boolean adjoint, WavelengthPacket lambda) {
    throw new UnimplementedException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
    Vector3    E = out;
    Vector3    L = in.opposite();
    Vector3    H = L.plus(E).unit();
    Vector3    N = x.getShadingNormal();
    double    HdotN = H.dot(N);
    double    EdotH = E.dot(H);
    double    EdotN = E.dot(N);
    double    LdotN = L.dot(N);
    double    tanAlpha = Math.tan(Math.acos(HdotN));
    double    cos4Alpha = HdotN * HdotN * HdotN * HdotN;

    Medium    medium = x.getAmbientMedium();
    Color    n1 = medium.refractiveIndex(x.getPosition(), lambda);
    Color    k1 = medium.extinctionIndex(x.getPosition(), lambda);
    Color    n2 = n.sample(lambda);
    Color    k2 = k.sample(lambda);
    Color    F = MaterialUtil.reflectance(E, n1, k1, n2, k2, N);
    double    D = Math.exp(-(tanAlpha * tanAlpha / mSquared)) / (Math.PI * mSquared * cos4Alpha);
    double    G = Math.min(1.0, Math.min(2.0 * HdotN * EdotN / EdotH, 2.0 * HdotN * LdotN / EdotH));

    return F.times(D * G / (4.0 * EdotN * LdotN));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
    return k.sample(lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
    return n.sample(lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
    return lambda.getColorModel().getBlack(lambda);
  }

}
