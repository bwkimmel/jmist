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
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> representing a absorptive layer with sieve
 * effects in the ABM-B/ABM-U light transport models.
 * @author Brad Kimmel
 * @see ABMSurfaceScatterer
 */
public final class ABMSieveAbsorbingSurfaceScatterer implements SurfaceScatterer {

  /** Serialization version ID. */
  private static final long serialVersionUID = -8261906690789778531L;

  /** The absorption coefficient of the medium (in m<sup>-1</sup>). */
  private final Function1 absorptionCoefficient;

  /** The thickness of the medium (in meters). */
  private final double thickness;

  /**
   * Creates a new <code>AbsorbingSurfaceScatterer</code>.
   * @param absorptionCoefficient The absorption coefficient of the medium
   *     (in m<sup>-1</sup>).
   * @param thickness The thickness of the medium (in meters).
   */
  public ABMSieveAbsorbingSurfaceScatterer(Function1 absorptionCoefficient,
      double thickness) {
    this.absorptionCoefficient = absorptionCoefficient;
    this.thickness = thickness;
  }

  @Override
  public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
      boolean adjoint, double lambda, Random rnd) {
    double abs = absorptionCoefficient.evaluate(lambda);
    double p = -Math.log(1.0 - rnd.next()) * Math.cos(Math.abs(x.getNormal().dot(v))) / abs;
    return (p > thickness) ? v : null;
  }

}
