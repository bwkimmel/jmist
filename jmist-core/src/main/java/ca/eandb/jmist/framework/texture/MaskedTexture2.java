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
package ca.eandb.jmist.framework.texture;

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> decorator that applies a <code>Mask2</code> to
 * another <code>Texture2</code>.
 * @author Brad Kimmel
 */
public final class MaskedTexture2 implements Texture2 {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 6665456950969793279L;

  /**
   * Creates a new <code>MaskedTexture2</code>.
   * @param mask The <code>Mask2</code> to apply to the
   *     <code>Texture2</code>.
   * @param texture The <code>Texture2</code> to mask.
   */
  public MaskedTexture2(Mask2 mask, Texture2 texture) {
    this.mask = mask;
    this.texture = texture;
  }

  @Override
  public Color evaluate(Point2 p, WavelengthPacket lambda) {
    return texture.evaluate(p, lambda).times(mask.opacity(p));
  }

  /** The <code>Texture2</code> to mask. */
  private final Texture2 texture;

  /** The <code>Mask2</code> to apply to the <code>Texture2</code>. */
  private final Mask2 mask;

}
