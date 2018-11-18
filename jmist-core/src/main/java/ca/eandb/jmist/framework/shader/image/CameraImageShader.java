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
package ca.eandb.jmist.framework.shader.image;

import ca.eandb.jmist.framework.ImageShader;
import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;

/**
 * An image shader that uses a Lens to shade rays corresponding to points on the
 * image plane.
 * @author Brad Kimmel
 */
public final class CameraImageShader implements ImageShader {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5027288502738131936L;

  /**
   * The lens to use to obtain rays corresponding to points on the image plane.
   */
  private final Lens lens;

  /** The shader to use to shade rays. */
  private final RayShader rayShader;

  /**
   * Initializes the lens and ray shader to use to shade points on the image
   * plane.
   * @param lens The lens to use to generate rays corresponding to points on the
   *     image plane.
   * @param rayShader The shader to use to shade rays.
   */
  public CameraImageShader(Lens lens, RayShader rayShader) {
    this.lens = lens;
    this.rayShader = rayShader;
  }

  @Override
  public Color shadeAt(Point2 p, WavelengthPacket lambda) {
    ScatteredRay sr = lens.rayAt(p, lambda, Random.DEFAULT);
    if (sr != null) {
      Ray3 ray = sr.getRay();
      Color scale = sr.getColor();
      Color shade = rayShader.shadeRay(ray, lambda);
      return shade.times(scale);
    } else {
      return ColorUtil.getBlack(lambda);
    }
  }

}
