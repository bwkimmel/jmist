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
import ca.eandb.jmist.math.Box2;

/**
 * A pixel shader decorator that averages stratified samples from the
 * decorated pixel shader.
 * @author Brad Kimmel
 */
public final class StratifyingPixelShader implements PixelShader {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 1059443381015930105L;

  /**
   * Initializes the number of rows and columns to divide each pixel
   * into as well as the underlying decorated pixel shader.
   * @param columns The number of columns to divide each pixel into.
   * @param rows The number of rows to divide each pixel into.
   * @param pixelShader The decorated pixel shader.
   */
  public StratifyingPixelShader(int columns, int rows, PixelShader pixelShader) {
    this.columns = columns;
    this.rows = rows;
    this.pixelShader = pixelShader;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.PixelShader#shadePixel(ca.eandb.jmist.math.Box2)
   */
  public Color shadePixel(Box2 bounds) {
    double x0, x1, y0, y1;
    Color pixel = null;
    Color sample;
    Box2 subpixel;

    for (int i = 0; i < this.rows; i++) {
      x0 = bounds.interpolateX((double) i / (double) this.rows);
      x1 = bounds.interpolateX((double) (i + 1) / (double) this.rows);

      for (int j = 0; j < this.columns; j++) {
        y0 = bounds.interpolateY((double) j / (double) this.columns);
        y1 = bounds.interpolateY((double) (j + 1) / (double) this.columns);

        subpixel = new Box2(x0, y0, x1, y1);
        sample = pixelShader.shadePixel(subpixel);
        pixel = add(pixel, sample);
      }
    }

    return pixel.divide(this.rows * this.columns);
  }

  /**
   * Adds two <code>Color</code>s.
   * @param a The first <code>Color</code> (may be null).
   * @param b The second <code>Color</code>.
   * @return The sum of the two <code>Color</code>s.
   */
  public Color add(Color a, Color b) {
    return (a != null) ? a.plus(b) : b;
  }

  /** The number of columns to divide each pixel into. */
  private final int columns;

  /** The number of rows to divide each pixel into. */
  private final int rows;

  /** The pixel shader to average the results from. */
  private final PixelShader pixelShader;

}
