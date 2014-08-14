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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;


/**
 * A light source.
 * @author Brad Kimmel
 */
public interface Light extends Serializable {

  /**
   * Causes the <code>Light</code> to record light samples on the provided
   * <code>SurfacePoint</code>.
   * @param x The <code>SurfacePoint</code> to illuminate.
   * @param lambda The <code>WavelengthPacket</code> at which to emit.
   * @param rnd A <code>Random</code> number generator to use.
   * @param target The <code>Illuminable</code> to record the light samples.
   */
  void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rnd, Illuminable target);

  /**
   * Creates the terminal <code>LightNode</code> for path-integral based
   * rendering algorithms.
   * @param pathInfo The <code>PathInfo</code> describing the context in
   *     which the path is being generated.
   * @param ru The first random variable (must be in [0, 1]).
   * @param rv The second random variable (must be in [0, 1]).
   * @param rj The third random variable (must be in [0, 1]).
   * @return A new <code>LightNode</code>.
   */
  LightNode sample(PathInfo pathInfo, double ru, double rv, double rj);

  /**
   * Emits a light ray.
   * @param lambda The <code>WavelengthPacket</code> at which to emit.
   * @param rnd The <code>Random</code> number generator to use.
   * @return A <code>ScatteredRay</code> describing the direction and color
   *     of the emitted light.
   */
  ScatteredRay emit(WavelengthPacket lambda, Random rnd);

  /**
   * Gets the marginal probability that this <code>Light</code> will generate
   * a <code>LightNode</code> from the specified <code>SurfacePoint</code>.
   * @param x The <code>SurfacePoint</code> at which to evaluate the
   *     probability density function.
   * @param pathInfo The <code>PathInfo</code> describing the context in
   *     in which the path is generated.
   * @return The value of the PDF.
   * @see #sample(PathInfo, double, double, double)
   */
  double getSamplePDF(SurfacePoint x, PathInfo pathInfo);

  /** A dummy <code>Light</code> that emits no illumination. */
  public static final Light NULL = new Light() {
    private static final long serialVersionUID = 5058166013868688853L;
    public ScatteredRay emit(WavelengthPacket lambda, Random rnd) {
      return null;
    }
    public void illuminate(SurfacePoint x, WavelengthPacket lambda,
        Random rng, Illuminable target) {
    }
    public LightNode sample(PathInfo pathInfo, double ru, double rv,
        double rj) {
      return null;
    }
    public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
      return 0;
    }
  };

}
