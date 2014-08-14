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
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> representing the interface between two
 * dielectric media.
 * @author Brad Kimmel
 */
public final class FresnelMaterial extends OpaqueMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = 3330860331562235908L;

  /** The refractive index of the medium above the interface. */
  private final double riBelow;

  /** The refractive index of the medium below the interface. */
  private final double riAbove;

  /**
   * Creates a new <code>FresnelSurfaceScatterer</code>.
   * @param riBelow The refractive index of the medium below the interface.
   * @param riAbove The refractive index of the medium above the interface.
   */
  public FresnelMaterial(double riBelow, double riAbove) {
    this.riBelow = riBelow;
    this.riAbove = riAbove;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {

    double n1 = riAbove;
    double n2 = riBelow;
    Vector3 N = x.getNormal();
    double R = Optics.reflectance(v, n1, n2, N);

    if (RandomUtil.bernoulli(R, ru)) {
      v = Optics.reflect(v, N);
      return ScatteredRay.specular(new Ray3(x.getPosition(), v),
          lambda.getColorModel().getWhite(lambda), 1.0);
    } else {
      v = Optics.refract(v, n1, n2, N);
      return ScatteredRay.transmitSpecular(new Ray3(x.getPosition(), v),
          lambda.getColorModel().getWhite(lambda), 1.0);
    }
    
  }

}
