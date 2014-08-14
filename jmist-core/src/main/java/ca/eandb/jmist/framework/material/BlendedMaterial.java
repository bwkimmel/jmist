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

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Material</code> that represents an interpolation of two other
 * <code>Material</code>s controlled by a <code>Mask2</code>.
 *
 * @author Brad Kimmel
 */
public final class BlendedMaterial implements Material {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3551402668466276594L;

  /** The <code>Material</code> to apply where the mask value is zero. */
  private final Material a;

  /** The <code>Material</code> to apply where the mask value is one. */
  private final Material b;

  /** The <code>Mask2</code> controlling the interpolation. */
  private final Mask2 mask;

  /**
   * The <code>Medium</code> within objects to which this material is
   * applied.
   */
  private final Medium medium;

  /**
   * Creates a new <code>BlendedMaterial</code>.
   * @param a The <code>Material</code> to apply where the mask value is
   *     zero.
   * @param b The <code>Material</code> to apply where the mask value is one.
   * @param mask The <code>Mask2</code> controlling the interpolation.
   * @param medium The <code>Medium</code> within objects to which this
   *     material is applied.
   */
  public BlendedMaterial(Material a, Material b, Mask2 mask, Medium medium) {
    this.a = a;
    this.b = b;
    this.mask = mask;
    this.medium = medium;
  }

  /**
   * Creates a new <code>BlendedMaterial</code>.
   * @param a The <code>Material</code> to apply where the mask value is
   *     zero.
   * @param b The <code>Material</code> to apply where the mask value is one.
   * @param mask The <code>Mask2</code> controlling the interpolation.
   */
  public BlendedMaterial(Material a, Material b, Mask2 mask) {
    this(a, b, mask, Medium.VACUUM);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
      WavelengthPacket lambda) {
    double t = mask.opacity(x.getUV());
    if (MathUtil.equal(t, 0.0)) {
      return a.bsdf(x, in, out, lambda);
    } else if (MathUtil.equal(t, 1.0)) {
      return b.bsdf(x, in, out, lambda);
    } else {
      return a.bsdf(x, in, out, lambda).times(1.0 - t).plus(
          b.bsdf(x, in, out, lambda).times(t));
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
    double t = mask.opacity(x.getUV());
    if (MathUtil.equal(t, 0.0)) {
      return a.emission(x, out, lambda);
    } else if (MathUtil.equal(t, 1.0)) {
      return b.emission(x, out, lambda);
    } else {
      return a.emission(x, out, lambda).times(1.0 - t).plus(
          b.emission(x, out, lambda).times(t));
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda,
      double ru, double rv, double rj) {
    ScatteredRay result = null;
    boolean aemit = a.isEmissive();
    boolean bemit = b.isEmissive();
    if (aemit && bemit) {
      double t = mask.opacity(x.getUV());
      SeedReference ref = new SeedReference(ru);
      if (RandomUtil.bernoulli(t, ref)) {
        result = b.emit(x, lambda, ref.seed, rv, rj);
        if (result != null) {
          result = new ScatteredRay(result.getRay(), result
              .getColor(), result.getType(), result.getPDF() * t,
              result.isTransmitted());
        }
      } else {
        result = a.emit(x, lambda, ref.seed, rv, rj);
        if (result != null) {
          result = new ScatteredRay(result.getRay(), result
              .getColor(), result.getType(), result.getPDF()
              * (1.0 - t), result.isTransmitted());
        }
      }
    } else if (aemit) {
      result = a.emit(x, lambda, ru, rv, rj);
      if (result != null) {
        double weight = 1.0 - mask.opacity(x.getUV());
        result = new ScatteredRay(result.getRay(), result.getColor()
            .times(weight), result.getType(), result.getPDF(),
            result.isTransmitted());
      }
    } else if (bemit) {
      result = b.emit(x, lambda, ru, rv, rj);
      if (result != null) {
        double weight = mask.opacity(x.getUV());
        result = new ScatteredRay(result.getRay(), result.getColor()
            .times(weight), result.getType(), result.getPDF(),
            result.isTransmitted());
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#getEmissionPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public double getEmissionPDF(SurfacePoint x, Vector3 out,
      WavelengthPacket lambda) {
    boolean aemit = a.isEmissive();
    boolean bemit = b.isEmissive();
    if (aemit && bemit) {
      double t = mask.opacity(x.getUV());
      return MathUtil.interpolate(
          a.getEmissionPDF(x, out, lambda),
          b.getEmissionPDF(x, out, lambda),
          t);
    } else if (aemit) {
      return a.getEmissionPDF(x, out, lambda);
    } else if (bemit) {
      return b.getEmissionPDF(x, out, lambda);
    } else {
      return 0.0;
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
      boolean adjoint, WavelengthPacket lambda) {
    double t = mask.opacity(x.getUV());
    if (MathUtil.equal(t, 0.0)) {
      return a.getScatteringPDF(x, in, out, adjoint, lambda);
    } else if (MathUtil.equal(t, 1.0)) {
      return b.getScatteringPDF(x, in, out, adjoint, lambda);
    } else {
      return MathUtil.interpolate(
          a.getScatteringPDF(x, in, out, adjoint, lambda),
          b.getScatteringPDF(x, in, out, adjoint, lambda),
          t);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#isEmissive()
   */
  @Override
  public boolean isEmissive() {
    return a.isEmissive() || b.isEmissive();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {
    double t = mask.opacity(x.getUV());
    if (MathUtil.equal(t, 0.0)) {
      return a.scatter(x, v, adjoint, lambda, ru, rv, rj);
    } else if (MathUtil.equal(t, 1.0)) {
      return b.scatter(x, v, adjoint, lambda, ru, rv, rj);
    } else {
      SeedReference ref = new SeedReference(rj);
      return RandomUtil.bernoulli(t, ref) ?
          b.scatter(x, v, adjoint, lambda, ru, rv, ref.seed) :
          a.scatter(x, v, adjoint, lambda, ru, rv, ref.seed);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
    return medium.extinctionIndex(p, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
    return medium.refractiveIndex(p, lambda);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
   */
  @Override
  public Color transmittance(Ray3 ray, double distance,
      WavelengthPacket lambda) {
    return medium.transmittance(ray, distance, lambda);
  }

}
