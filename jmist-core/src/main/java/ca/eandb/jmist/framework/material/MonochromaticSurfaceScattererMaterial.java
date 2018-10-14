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

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.framework.scatter.SurfaceScatterer;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Material</code> that adapts a <code>SurfaceScatterer</code> using a
 * fixed wavelength.
 *
 * @author Brad Kimmel
 */
public final class MonochromaticSurfaceScattererMaterial extends
    AbstractMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = 2504834676929479253L;

  private final SurfaceScatterer surface;

  private final ColorModel colorModel;

  private final Spectrum absorptionCoefficient;

  private final double wavelength;

  private final Random rnd = new ThreadLocalRandom(new SimpleRandom());

  public MonochromaticSurfaceScattererMaterial(SurfaceScatterer surface,
      ColorModel colorModel, Spectrum absorptionCoefficient,
      double wavelength) {
    this.surface = surface;
    this.colorModel = colorModel;
    this.absorptionCoefficient = absorptionCoefficient;
    this.wavelength = wavelength;
  }

  @Override
  public Color transmittance(Ray3 ray, double distance,
      WavelengthPacket lambda) {
    return absorptionCoefficient != null
        ? absorptionCoefficient.sample(lambda).times(-distance).exp()
        : lambda.getColorModel().getBlack(lambda);
  }

  @Override
  public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
    return null;
  }

  @Override
  public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
    return null;
  }

  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {

    Vector3 r = surface.scatter(x, v, adjoint, wavelength, rnd);

    if (r != null) {

      Vector3 n = x.getNormal();
      Point3 p = x.getPosition();
      Ray3 ray = new Ray3(p, r);

      Color color = colorModel.getWhite(lambda);

      if (r.dot(n) > 0.0) {
        return ScatteredRay.specular(ray, color, 0.0);
      } else {
        return ScatteredRay.transmitSpecular(ray, color, 0);
      }

    } else { // absorbed
      return null;
    }

  }

}
