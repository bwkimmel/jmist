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
package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Light</code> that emits from a single point.
 * @author Brad Kimmel
 */
public final class PointSpotLight extends AbstractLight implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5220350307274318220L;

  /** The <code>Point3</code> where the light is to emit from. */
  private final Point3 position;

  private final Vector3 axis;

  private final double minDot, blendDot;

  /** The emission <code>Spectrum</code> of the light. */
  private final Spectrum emittedPower;

  /** A value indicating whether the light should be affected by shadows. */
  private final boolean shadows;

  /**
   * Creates a new <code>PointLight</code>.
   * @param position The <code>Point3</code> where the light is to emit from.
   * @param axis The <code>Vector3</code> indicating the direction that the
   *     spot light is pointed.
   * @param angle The angle, in radians, between the axis and the edge of the
   *     spot light.
   * @param blend The fraction of <code>angle</code>at which to begin blending
   *     the spot toward black.
   * @param emittedPower The <code>Spectrum</code> representing the emitted
   *     power of the light.
   * @param shadows A value indicating whether the light should be affected
   *     by shadows.
   */
  public PointSpotLight(Point3 position, Vector3 axis, double angle, double blend, Spectrum emittedPower, boolean shadows) {
    this.position = position;
    this.axis = axis;
    this.emittedPower = emittedPower;
    this.shadows = shadows;
    this.minDot = Math.cos(angle);
    this.blendDot = Math.cos(Math.asin(Math.sin(angle) * (1.0 - blend)));
  }

  @Override
  public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target) {
    Vector3 lightIn = x.getPosition().vectorTo(this.position);
    double dSquared = lightIn.squaredLength();

    lightIn = lightIn.divide(Math.sqrt(dSquared));

    double vdotl = -axis.dot(lightIn);
    if (vdotl <= minDot) {
      return;
    }

    double ndotl = x.getShadingNormal().dot(lightIn);
    double attenuation = Math.abs(ndotl) / (4.0 * Math.PI * dSquared);
    if (vdotl < blendDot) {
      attenuation *= (vdotl - minDot) / (blendDot - minDot);
    }

    target.addLightSample(new PointLightSample(x, position, emittedPower.sample(lambda).times(attenuation), shadows));
  }

}
