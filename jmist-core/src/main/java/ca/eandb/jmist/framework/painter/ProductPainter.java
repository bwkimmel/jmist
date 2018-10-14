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
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
public final class ProductPainter implements Painter {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -2530604202721199134L;

  private final Painter a;

  private final Painter b;

  /**
   * Creates a new <code>ProductPainter</code>.
   * @param a The first <code>Painter</code>.
   * @param b The second <code>Painter</code>.
   */
  public ProductPainter(Painter a, Painter b) {
    this.a = a;
    this.b = b;
  }

  /**
   * Creates a new <code>ProductPainter</code>.
   * @param a The <code>Spectrum</code> to be multiplied.
   * @param b The <code>Painter</code> to be multiplied.
   */
  public ProductPainter(Spectrum a, Painter b) {
    this(new UniformPainter(a), b);
  }

  /**
   * Creates a new <code>ProductPainter</code>.
   * @param a The <code>Painter</code> to be multiplied.
   * @param b The <code>Spectrum</code> to be multiplied.
   */
  public ProductPainter(Painter a, Spectrum b) {
    this(a, new UniformPainter(b));
  }

  @Override
  public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
    return a.getColor(p, lambda).times(b.getColor(p, lambda));
  }

}
