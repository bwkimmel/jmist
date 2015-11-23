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
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.math.Box2;

/**
 * A pixel shader decorator that averages the results of another pixel shader.
 * The number of samples used may vary within a specified range based on a
 * provided target variance.
 * @author Brad Kimmel
 */
public final class VarianceTargetPixelShader implements PixelShader {

  /** Serialization version ID. */
  private static final long serialVersionUID = 2790315811680362011L;

  /**
   * The target variance.  Sampling will stop once the variance drops below this
   * level.
   */
  private final double varianceTarget;

  /** Minimum number of samples to use. */
  private final int minSamples;

  /** Maximum number of samples to use. */
  private final int maxSamples;

  /** The number of samples to compute between variance tests. */
  private final int checkInterval;

  /** The pixel shader from which to average the results. */
  private final PixelShader pixelShader;

  /**
   * A value indicating whether to render in test mode.  In test mode, the color
   * of the pixel returned is a greyscale value indicating how many samples were
   * used (black represents the minimum number of samples were used, white
   * indicates that the maximum number of samples were used).
   */
  private final boolean testMode;

  /**
   * Creates a new <code>VarianceTargetPixelShader</code>.
   * @param varianceTarget The target variance.  Sampling will stop once the
   *     variance drops below this level.
   * @param minSamples The minimum number of samples to use.
   * @param maxSamples The maximum number of samples to use.
   * @param checkInterval The number of samples to compute between variance
   *     tests.
   * @param testMode A value indicating whether to render in test mode.  In test
   *     mode, the color of the pixel returned is a greyscale value indicating
   *     how many samples were used (black represents the minimum number of
   *     samples were used, white indicates that the maximum number of samples
   *     were used).
   * @param pixelShader The <code>PixelShader</code> to use to gather samples.
   */
  public VarianceTargetPixelShader(double varianceTarget, int minSamples, int maxSamples, int checkInterval, boolean testMode, PixelShader pixelShader) {
    if (minSamples < 2) {
      throw new IllegalArgumentException("minSamples < 2");
    }
    if (maxSamples < minSamples) {
      throw new IllegalArgumentException("maxSamples < minSamples");
    }
    if (varianceTarget < 0.0) {
      throw new IllegalArgumentException("varianceTarget < 0.0");
    }
    if (checkInterval < 1) {
      throw new IllegalArgumentException("checkInterval < 1");
    }
    this.varianceTarget = varianceTarget;
    this.minSamples = minSamples;
    this.maxSamples = maxSamples;
    this.checkInterval = checkInterval;
    this.testMode = testMode;
    this.pixelShader = pixelShader;
  }

  /**
   * Creates a new <code>VarianceTargetPixelShader</code>.
   * @param varianceTarget The target variance.  Sampling will stop once the
   *     variance drops below this level.
   * @param minSamples The minimum number of samples to use.
   * @param maxSamples The maximum number of samples to use.
   * @param checkInterval The number of samples to compute between variance
   *     tests.
   * @param pixelShader The <code>PixelShader</code> to use to gather samples.
   */
  public VarianceTargetPixelShader(double varianceTarget, int minSamples, int maxSamples, int checkInterval, PixelShader pixelShader) {
    this(varianceTarget, minSamples, maxSamples, checkInterval, false, pixelShader);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.math.Box2)
   */
  public Color shadePixel(Box2 bounds) {
    Color pixel = pixelShader.shadePixel(bounds);
    Color s = null;
    int i;
    int j = 1;

    for (i = 1; i < this.maxSamples; i++) {
      if (i >= minSamples) {
        if (--j <= 0) {
          j = checkInterval;
          if (ColorUtil.getMaxChannelValue(s) < varianceTarget * ((i - 1) * i)) {
            break;
          }
        }
      }

      Color sample = pixelShader.shadePixel(bounds);
      Color oldPixel = pixel;
      pixel = ColorUtil.add(pixel, ColorUtil.div(ColorUtil.sub(sample, pixel), i + 1));
      s = ColorUtil.add(s, ColorUtil.mul(ColorUtil.sub(sample, oldPixel), ColorUtil.sub(sample, pixel)));
    }

    if (testMode) {
      ColorModel cm = pixel.getColorModel();
      double value = (double) (i - minSamples) / (double) (maxSamples - minSamples);
      return cm.getGray(value, pixel.getWavelengthPacket());
    } else {
      return pixel;
    }
  }

}
