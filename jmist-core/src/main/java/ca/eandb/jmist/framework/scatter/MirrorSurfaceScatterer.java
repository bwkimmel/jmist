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
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> that specularly reflects.
 * @author Brad Kimmel
 */
public final class MirrorSurfaceScatterer implements SurfaceScatterer {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3125700858196796582L;

  /**
   * The <code>Function1</code> indicating the probability that an incident
   * ray is reflected.
   */
  private final Function1 reflectance;

  /**
   * Creates a new <code>MirrorSurfaceScatterer</code>.
   * @param reflectance The <code>Function1</code> indicating the
   *    probability that an incident ray is reflected.
   */
  public MirrorSurfaceScatterer(Function1 reflectance) {
    this.reflectance = reflectance;
  }

  /**
   * Creates a new <code>MirrorSurfaceScatterer</code> with unit reflectance.
   */
  public MirrorSurfaceScatterer() {
    this(Function1.ONE);
  }

  @Override
  public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
      double wavelength, Random rnd) {
    double R = reflectance.evaluate(wavelength);
    if (RandomUtil.bernoulli(R, rnd)) {
      Vector3 N = x.getNormal();
      return Optics.reflect(v, N);
    } else {
      return null;
    }
  }

}
