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

import ca.eandb.jmist.framework.DirectionalTexture3;
import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Light</code> that illuminates from all directions in the specified
 * hemisphere.
 * @author Brad Kimmel
 */
public final class HemisphericalLight extends AbstractLight {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7440635665736022550L;

  /**
   * A <code>DirectionalTexture3</code> representing the incident radiance.
   */
  private final DirectionalTexture3 environment;

  /**
   * The <code>Basis3</code> representing the coordinate system of the
   * illuminating hemisphere.
   */
  private final Basis3 basis;

  /** A value indicating whether shadows should be computed. */
  private final boolean shadows;

  /**
   * Creates a new <code>HemisphericalLight</code>.
   * @param environment A <code>DirectionalTexture3</code> representing the
   *     distribution of incoming radiance.
   * @param zenith A <code>Vector3</code> indicating the direction toward the
   *     center of the illuminating hemisphere.
   */
  public HemisphericalLight(DirectionalTexture3 environment, Vector3 zenith) {
    this(environment, zenith, true);
  }

  /**
   * Creates a new <code>HemisphericalLight</code>.
   * @param environment A <code>DirectionalTexture3</code> representing the
   *     distribution of incoming radiance.
   * @param zenith A <code>Vector3</code> indicating the direction toward the
   *     center of the illuminating hemisphere.
   * @param shadows A value indicating whether shadows should be computed.
   */
  public HemisphericalLight(DirectionalTexture3 environment, Vector3 zenith, boolean shadows) {
    this.environment = environment;
    this.basis = Basis3.fromW(zenith);
    this.shadows = shadows;
  }

  @Override
  public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target) {

    Vector3  source = RandomUtil.uniformOnUpperHemisphere(rng).toCartesian(basis);
    double  dot = x.getShadingNormal().dot(source);

    target.addLightSample(new DirectionalLightSample(x, source, environment.evaluate(source, lambda).times(dot), shadows));

  }

}
