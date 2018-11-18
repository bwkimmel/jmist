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

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.painter.UniformPainter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Material</code> that reflects light equally in all directions in
 * the upper hemisphere.
 * @author Brad Kimmel
 */
public final class LambertianMaterial extends OpaqueMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = 485410070543495668L;

  /** The reflectance <code>Painter</code> of this <code>Material</code>. */
  private final Painter reflectance;

  /** The emittance <code>Painter</code> of this <code>Material</code>. */
  private final Painter emittance;

  /**
   * Creates a new <code>LambertianMaterial</code> that does not emit light.
   * @param reflectance The reflectance <code>Painter</code>.
   */
  public LambertianMaterial(Painter reflectance) {
    this(reflectance, null);
  }

  /**
   * Creates a new <code>LambertianMaterial</code> that emits light.
   * @param reflectance The reflectance <code>Painter</code>.
   * @param emittance The emission <code>Painter</code>.
   */
  public LambertianMaterial(Painter reflectance, Painter emittance) {
    this.reflectance = reflectance;
    this.emittance = emittance;
  }

  /**
   * Creates a new <code>LambertianMaterial</code> that does not emit light.
   * @param reflectance The reflectance <code>Spectrum</code>.
   */
  public LambertianMaterial(Spectrum reflectance) {
    this(reflectance != null ? new UniformPainter(reflectance) : null);
  }

  /**
   * Creates a new <code>LambertianMaterial</code> that emits light.
   * @param reflectance The reflectance <code>Spectrum</code>.
   * @param emittance The emission <code>Spectrum</code>.
   */
  public LambertianMaterial(Spectrum reflectance, Spectrum emittance) {
    this(reflectance != null ? new UniformPainter(reflectance) : null, emittance != null ? new UniformPainter(emittance) : null);
  }

  @Override
  public boolean isEmissive() {
    return (this.emittance != null);
  }

  @Override
  public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
    if (this.emittance != null && x.getNormal().dot(out) > 0.0) {
      return emittance.getColor(x, lambda).divide(Math.PI);
    } else {
      return lambda.getColorModel().getBlack(lambda);
    }
  }

  @Override
  public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda, double ru, double rv, double rj) {
    if (this.emittance != null) {
      SphericalCoordinates out = RandomUtil.diffuse(ru, rv);
      Ray3 ray = new Ray3(x.getPosition(), out.toCartesian(x.getShadingBasis()));
      if (x.getNormal().dot(ray.direction()) > 0.0) {
        return ScatteredRay.diffuse(ray, emittance.getColor(x, lambda), 1.0 / Math.PI);
      }
    }
    return null;
  }

  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {
    if (this.reflectance != null) {
      boolean fromFront = x.getNormal().dot(v) < 0.0;
      SphericalCoordinates out = RandomUtil.diffuse(ru, rv);
      if (!fromFront) {
        out = out.opposite();
      }

      Ray3 ray = new Ray3(x.getPosition(), out.toCartesian(x.getShadingBasis()));
      boolean toFront = ray.direction().dot(x.getNormal()) > 0.0;

      if (fromFront == toFront) {
        return ScatteredRay.diffuse(ray, reflectance.getColor(x, lambda), 1.0 / Math.PI);
      }
    }
    return null;
  }

  @Override
  public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
    Vector3 n = x.getNormal();
    boolean fromFront = (n.dot(in) < 0.0);
    boolean toFront = (n.dot(out) > 0.0);

    Vector3 n1 = x.getShadingNormal();
    boolean fromFront1 = (n1.dot(in) < 0.0);

    if (this.reflectance != null && (toFront == fromFront) && (toFront == fromFront1)) {
      return reflectance.getColor(x, lambda).divide(Math.PI);
    } else {
      return lambda.getColorModel().getBlack(lambda);
    }
  }

  @Override
  public double getEmissionPDF(SurfacePoint x, Vector3 out,
      WavelengthPacket lambda) {
    return emittance != null ? 1.0 / Math.PI : 0.0;
  }

  @Override
  public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
      boolean adjoint, WavelengthPacket lambda) {
    return reflectance != null ? 1.0 / Math.PI : 0.0;
  }

}
