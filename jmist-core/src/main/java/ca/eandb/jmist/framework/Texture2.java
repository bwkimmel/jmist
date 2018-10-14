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

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point2;

/**
 * Maps two dimensional space to colors.
 * @author Brad Kimmel
 */
public interface Texture2 extends Serializable {

  /**
   * Computes the color at the specified <code>Point2</code> in the
   * domain.
   * @param p The <code>Point2</code> in the domain.
   * @param lambda The <code>WavelengthPacket</code> denoting the wavelengths
   *     at which to evaluate the texture.
   * @return The <code>Color</code> at <code>p</code>.
   */
  Color evaluate(Point2 p, WavelengthPacket lambda);

  /** A solid black <code>Texture2</code>. */
  public static final Texture2 BLACK = new Texture2() {
    private static final long serialVersionUID = 7911041095970042301L;
    public Color evaluate(Point2 p, WavelengthPacket lambda) {
      return lambda.getColorModel().getBlack(lambda);
    }
  };

  /** A solid white <code>Texture2</code>. */
  public static final Texture2 WHITE = new Texture2() {
    private static final long serialVersionUID = 8200945534829723005L;
    public Color evaluate(Point2 p, WavelengthPacket lambda) {
      return lambda.getColorModel().getWhite(lambda);
    }
  };

}
