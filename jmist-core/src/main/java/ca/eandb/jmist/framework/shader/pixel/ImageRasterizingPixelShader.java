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

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.PixelShader;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * Represents a pixel shader that rasterizes an image represented by
 * an image shader.  The inheriting pixel shader's sole responsibility
 * will be anti-aliasing.
 * @author Brad Kimmel
 */
public abstract class ImageRasterizingPixelShader implements PixelShader {

  /** Serialization version ID. */
  private static final long serialVersionUID = -6845318344104841243L;

  /**
   * Initializes the image shader to use for this pixel shader.
   * @param shader The <code>ImageShader</code> to use for this pixel shader.
   * @param model The <code>ColorModel</code> to use for sampling in te
   *     wavelength domain.
   */
  protected ImageRasterizingPixelShader(ImageShader shader, ColorModel model) {
    this.shader = shader;
    this.model = model;
  }

  /**
   * Shades the specified pixel using this shader's image shader.
   * @param p The point on the image plane to shade.
   * @return The shaded pixel.
   */
  protected Color shadeAt(Point2 p) {
    Color        sample = model.sample(Random.DEFAULT);
    WavelengthPacket  lambda = sample.getWavelengthPacket();

    Color        shade = shader.shadeAt(p, lambda);
    return shade.times(sample);
  }

  /** The <code>ImageShader</code> to use for shading points. */
  private final ImageShader shader;

  /**
   * The <code>ColorModel</code> to use for sampling in the wavelength
   * domain.
   */
  private final ColorModel model;

}
