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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;

/**
 * Estimates the colour channel responses along a given ray.
 * @author Brad Kimmel
 */
public interface RayShader extends Serializable {

  /**
   * Computes an estimate of the colour channel responses at the origin
   * of the ray traveling in the direction opposite the direction of
   * the ray.
   * @param ray The ray indicating the point and direction along which to
   *     compute the colour channel responses.
   * @param lambda The wavelengths at which to compute the color channel
   *     responses.
   * @return The colour channel responses.
   */
  Color shadeRay(Ray3 ray, WavelengthPacket lambda);

  /** A <code>RayShader</code> that shades all rays black. */
  RayShader BLACK = new RayShader() {
    private static final long serialVersionUID = -6360034977196703057L;
    public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
      return lambda.getColorModel().getBlack(lambda);
    }
  };

}
