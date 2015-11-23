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
package ca.eandb.jmist.framework.shader.pixel;

import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.math.Box2;

/**
 * A pixel shader decorator that averages the results of another pixel shader.
 * @author Brad Kimmel
 */
public final class AveragingPixelShader implements PixelShader {

  /**
   * Initializes the inner pixel shader.
   * @param numSamples The number of samples to average when shading a pixel.
   * @param pixelShader The pixel shader average the results from.
   */
  public AveragingPixelShader(int numSamples, PixelShader pixelShader) {
    if (numSamples <= 0) {
      throw new IllegalArgumentException("numSamples <= 0");
    }
    this.numSamples = numSamples;
    this.pixelShader = pixelShader;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.math.Box2)
   */
  public Color shadePixel(Box2 bounds) {
    Color pixel = null;

    for (int i = 0; i < this.numSamples; i++) {
      pixel = ColorUtil.add(pixel, pixelShader.shadePixel(bounds));
    }

    return pixel.divide(numSamples);
  }

  /** The number of samples to average from the decorated pixel shader. */
  private final int numSamples;

  /** The pixel shader from which to average the results. */
  private final PixelShader pixelShader;

  /** Serialization version ID. */
  private static final long serialVersionUID = -1978147732952459483L;

}