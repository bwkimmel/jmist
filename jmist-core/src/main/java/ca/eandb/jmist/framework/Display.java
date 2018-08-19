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

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;

/**
 * A target for rendering.  Examples include an image file, or an on-screen
 * display.
 * @author Brad Kimmel
 */
public interface Display {

  /**
   * Initialize the display.
   * @param w The width, in pixels, of the image to initialize.
   * @param h The height, in pixels, of the image to initialize.
   * @param colorModel The <code>ColorModel</code> to use for the image.
   */
  void initialize(int w, int h, ColorModel colorModel);

  /**
   * Fills a rectangular area with the specified <code>Color</code>.
   * @param x The x-coordinate of the upper-left corner of the rectangle.
   * @param y The y-coordinate of the upper-left corner of the rectangle.
   * @param w The width, in pixels, of the rectangle to fill.
   * @param h The height, in pixels, of the rectangle to fill.
   * @param color The <code>Color</code> to fill the area with.
   */
  void fill(int x, int y, int w, int h, Color color);

  /**
   * Sets the specified pixel to the provided <code>Color</code>.
   * @param x The x-coordinate of the pixel to set.
   * @param y The y-coordinate of the pixel to set.
   * @param pixel The <code>Color</code> to set the pixel to.
   */
  void setPixel(int x, int y, Color pixel);

  /**
   * Copies the provided <code>Raster</code> image onto the display.
   * @param x The x-coordinate of the upper-left corner of the area in which
   *     to place the <code>Raster</code> image onto.
   * @param y The y-coordinate of the upper-left corner of the area in which
   *     to place the <code>Raster</code> image onto.
   * @param pixels The <code>Raster</code> image to write to the display.
   */
  void setPixels(int x, int y, Raster pixels);

  /** Finalize the displayed image. */
  void finish();

  /** Sends results into the void, never to be heard from again. */
  public static final Display NULL = new Display() {
    @Override public void initialize(int w, int h, ColorModel colorModel) {}
    @Override public void fill(int x, int y, int w, int h, Color color) {}
    @Override public void setPixel(int x, int y, Color pixel) {}
    @Override public void setPixels(int x, int y, Raster pixels) {}
    @Override public void finish() {}
  };
}
