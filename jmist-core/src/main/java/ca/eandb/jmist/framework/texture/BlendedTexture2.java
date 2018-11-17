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
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that combines two other <code>Texture2</code>s using
 * a <code>Mask2</code>.
 *
 * At areas where the mask value is zero, the first texture is used.  Where the
 * mask value is one, the second texture is used.  For other areas, the texture
 * is interpolated between the first and second textures.
 *
 * @author Brad Kimmel
 */
public final class BlendedTexture2 implements Texture2 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 3825212205211905179L;

  /** The <code>Texture2</code> to use where the mask value is zero. */
  private final Texture2 a;

  /** The <code>Texture2</code> to use where the mask value is one. */
  private final Texture2 b;

  /** The <code>Mask2</code> that controls the interpolation. */
  private final Mask2 mask;

  /**
   * Creates a new <code>BlendedTexture2</code>.
   * @param a The <code>Texture2</code> to use where the mask value is zero.
   * @param b The <code>Texture2</code> to use where the mask value is one.
   * @param mask The <code>Mask2</code> that controls the interpolation.
   */
  public BlendedTexture2(Texture2 a, Texture2 b, Mask2 mask) {
    this.a = a;
    this.b = b;
    this.mask = mask;
  }

  @Override
  public Color evaluate(Point2 p, WavelengthPacket lambda) {
    double t = mask.opacity(p);
    return a.evaluate(p, lambda).times(1.0 - t).plus(
        b.evaluate(p, lambda).times(t));
  }

}
