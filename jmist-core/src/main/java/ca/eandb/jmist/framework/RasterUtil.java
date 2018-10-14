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

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * A collection of static utility methods for working with
 * <code>Raster</code>s.
 * @author Brad Kimmel
 */
public final class RasterUtil {

  /**
   * This constructor is private because this class cannot be instantiated.
   */
  private RasterUtil() {}

  /**
   * Sets the value of the pixel at the specified location on the image
   * plane.
   * @param raster The <code>Raster</code> containing the pixel to set.
   * @param p The <code>Point2</code> on the image plane for which to set the
   *     corresponding pixel.
   * @param c The <code>Color</code> to set the pixel to.
   */
  public static void setPixel(RasterWriter raster, Point2 p, Color c) {
    int w = raster.getWidth();
    int h = raster.getHeight();
    int x = MathUtil.clamp((int) Math.floor(p.x() * w), 0, w - 1);
    int y = MathUtil.clamp((int) Math.floor(p.y() * h), 0, h - 1);
    raster.setPixel(x, y, c);
  }

  /**
   * Adds to the value of the pixel at the specified location on the image
   * plane.
   * @param raster The <code>Raster</code> containing the pixel to add to.
   * @param p The <code>Point2</code> on the image plane for which to add to
   *     the corresponding pixel.
   * @param c The <code>Color</code> to add to the pixel.
   */
  public static void addPixel(Raster raster, Point2 p, Color c) {
    int w = raster.getWidth();
    int h = raster.getHeight();
    int x = MathUtil.clamp((int) Math.floor(p.x() * w), 0, w - 1);
    int y = MathUtil.clamp((int) Math.floor(p.y() * h), 0, h - 1);
    raster.addPixel(x, y, c);
  }

  /**
   * Gets the value of the pixel at the specified location on the image
   * plane.
   * @param raster The <code>Raster</code> containing the pixel to get.
   * @param p The <code>Point2</code> on the image plane for which to get the
   *     corresponding pixel.
   * @return The <code>Color</code> of the pixel.
   */
  public static Color getPixel(Raster raster, Point2 p) {
    int w = raster.getWidth();
    int h = raster.getHeight();
    int x = MathUtil.clamp((int) Math.floor(p.x() * w), 0, w - 1);
    int y = MathUtil.clamp((int) Math.floor(p.y() * h), 0, h - 1);
    return raster.getPixel(x, y);
  }

}
