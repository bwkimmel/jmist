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
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Complex;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A conductive <code>Material</code> with a complex refractive index.
 * @author Brad Kimmel
 */
public final class ConductiveMaterial extends AbstractMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = -8516201792111267898L;

  /**
   * Creates a new <code>ConductiveMaterial</code>.
   * @param n The real part of the refractive index <code>Spectrum</code>.
   * @param k The imaginary part of the refractive index
   *     <code>Spectrum</code>.
   * @param alpha The absorption coefficient <code>Spectrum</code>.
   * @param disperse A value indicating if this material is dispersive.  If
   *     <code>alpha == null</code>, this value has no effect.
   */
  public ConductiveMaterial(Spectrum n, Spectrum k, Spectrum alpha, boolean disperse) {
    this.n = n;
    this.k = k;
    this.alpha = alpha;
    this.disperse = disperse;
  }

  /**
   * Creates a new <code>ConductiveMaterial</code>.
   * @param n The real part of the refractive index <code>Spectrum</code>.
   * @param k The imaginary part of the refractive index
   *     <code>Spectrum</code>.
   * @param alpha The absorption coefficient <code>Spectrum</code>.
   */
  public ConductiveMaterial(Spectrum n, Spectrum k, Spectrum alpha) {
    this(n, k, alpha, true);
  }

  @Override
  public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
    return k.sample(lambda);
  }

  @Override
  public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
    return n.sample(lambda);
  }

  @Override
  public Color transmittance(Ray3 ray, final double distance, WavelengthPacket lambda) {
    return alpha != null ? alpha.sample(lambda).times(-distance).exp() : lambda.getColorModel().getBlack(lambda);
  }

  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint, WavelengthPacket lambda, double ru, double rv, double rj) {
    ColorModel cm = lambda.getColorModel();
    Point3 p = x.getPosition();
    Medium medium = x.getAmbientMedium();
    Color n1 = medium.refractiveIndex(p, lambda);
    Color k1 = medium.extinctionIndex(p, lambda);
    Color n2 = n.sample(lambda);
    Color k2 = k.sample(lambda);
    Vector3 normal = x.getShadingNormal();
    boolean fromSide = x.getNormal().dot(v) < 0.0;
    Color R = MaterialUtil.reflectance(v, n1, k1, n2, k2, normal);
    Color T = cm.getWhite(lambda).minus(R);
    double r = ColorUtil.getMeanChannelValue(R);

    if (RandomUtil.bernoulli(r, rj)) {
      Vector3 out = Optics.reflect(v, normal);
      boolean toSide = x.getNormal().dot(out) >= 0.0;

      if (fromSide == toSide) {
        return ScatteredRay.specular(new Ray3(p, out), R.divide(r), r);
      }
    } else {
      if (alpha != null) {
//        if (disperse) {
//          for (int i = 0, channels = cm.getNumChannels(); i < channels; i++) {
//            Complex    eta1  = new Complex(n1.getValue(i), k1.getValue(i));
//            Complex    eta2  = new Complex(n2.getValue(i), k2.getValue(i));
//            Vector3    out    = Optics.refract(v, eta1, eta2, normal);
//            boolean    toSide  = x.getNormal().dot(out) >= 0.0;
//
//            if (fromSide != toSide) {
//              recorder.add(ScatteredRay.transmitSpecular(new Ray3(p, out), T.disperse(i), 1.0));
//            }
//          }
//        } else { // !disperse
          double n1avg = ColorUtil.getMeanChannelValue(n1);
          double k1avg = ColorUtil.getMeanChannelValue(k1);
          double n2avg = ColorUtil.getMeanChannelValue(n2);
          double k2avg = ColorUtil.getMeanChannelValue(k2);
          Complex eta1 = new Complex(n1avg, k1avg);
          Complex eta2 = new Complex(n2avg, k2avg);
          Vector3 out = Optics.refract(v, eta1, eta2, normal);
          boolean toSide = x.getNormal().dot(out) >= 0.0;

          if (fromSide != toSide) {
            return ScatteredRay.transmitSpecular(new Ray3(p, out), T.divide(1 - r), 1 - r);
          }
//        }
      }
    }

    return null;
  }

  /** The real part of the refractive index. */
  private final Spectrum n;

  /** The imaginary part of the refractive index. */
  private final Spectrum k;

  /** The absorption coefficient. */
  private final Spectrum alpha;

  /** A value indciating if this material is dispersive. */
  private final boolean disperse;

}
