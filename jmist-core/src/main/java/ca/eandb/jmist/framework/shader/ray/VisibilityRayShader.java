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
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;

/**
 * A ray shader that shades based on the evaluation of a visibility
 * function.
 * @author Brad Kimmel
 */
public final class VisibilityRayShader implements RayShader {

  /** Serialization version ID. */
  private static final long serialVersionUID = 6959718984918444342L;

  /** The visibility function to evaluate. */
  private final VisibilityFunction3 visibilityFunction;

  /** The value to assign to rays that hit an object. */
  private final Spectrum hitValue;

  /** The value to assign to rays that do not hit an object. */
  private final Spectrum missValue;

  /**
   * Initializes the visibility function to evaluate.
   * @param visibilityFunction The visibility function to evaluate.
   */
  public VisibilityRayShader(VisibilityFunction3 visibilityFunction) {
    this(visibilityFunction, null, null);
  }

  /**
   * Initializes the visibility function to evaluate and the values to assign
   * to rays that hit or do not hit an object.
   * @param visibilityFunction The visibility function to evaluate.
   * @param hitValue The <code>Spectrum</code> to assign to rays that hit an
   *     object.
   * @param missValue The <code>Spectrum</code> to assign to rays that do not
   *     hit an object.
   */
  public VisibilityRayShader(VisibilityFunction3 visibilityFunction,
      Spectrum hitValue, Spectrum missValue) {
    this.visibilityFunction = visibilityFunction;
    this.hitValue = hitValue;
    this.missValue = missValue;
  }

  @Override
  public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
    if (ray == null || this.visibilityFunction.visibility(ray)) {
      return (missValue != null) ? missValue.sample(lambda) : lambda.getColorModel().getBlack(lambda);
    } else {
      return (hitValue != null) ? hitValue.sample(lambda) : lambda.getColorModel().getWhite(lambda);
    }
  }

}
