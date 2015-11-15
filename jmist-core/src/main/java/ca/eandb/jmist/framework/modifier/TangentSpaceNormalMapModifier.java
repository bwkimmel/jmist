/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.modifier;

import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.color.rgb.RGBColorModel;
import ca.eandb.jmist.framework.painter.Texture2Painter;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Vector3;

/**
 * Transforms the shading basis using a normal map.
 * @see <a href="http://en.wikipedia.org/wiki/Normal_mapping">Normal mapping</a>
 * @see <a href="http://wiki.polycount.com/NormalMap/#Tangent-space_normal_map">Tangent-space normal map</a>
 */
public final class TangentSpaceNormalMapModifier implements Modifier {

  /** Serialization version ID. */
  private static final long serialVersionUID = 3272308697162050809L;

  /** Stored wavelength packet for RGB color model. */
  private static final WavelengthPacket WAVELENGTH_PACKET =
      RGBColorModel.getInstance().sample(Random.DEFAULT).getWavelengthPacket();

  /** Painter to obtain normal-map colours from shading context. */
  private final Painter painter;

  /**
   * Create a new <code>NormalMapModifier</code>.
   * @param painter The <code>Painter</code> to use to obtain normal-map
   *     colours from the shading context.  It must use the
   *     <code>RGBColorModel</code>.
   * @see ca.eandb.jmist.framework.color.rgb.RGBColorModel
   */
  public TangentSpaceNormalMapModifier(Painter painter) {
    this.painter = painter;
  }

  /**
   * Create a new <code>NormalMapModifier</code>.
   * @param texture The <code>Texture2</code> to use to obtain normal-map
   *     colours from the shading context.  It must use the
   *     <code>RGBColorModel</code>.
   * @see ca.eandb.jmist.framework.color.rgb.RGBColorModel
   */
  public TangentSpaceNormalMapModifier(Texture2 texture) {
    this(new Texture2Painter(texture));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Modifier#modify(ca.eandb.jmist.framework.ShadingContext)
   */
  @Override
  public void modify(ShadingContext context) {
    Basis3 basis = context.getShadingBasis();
    RGB c = painter.getColor(context, WAVELENGTH_PACKET).toRGB();
    Vector3 n = basis.toStandard(c.r() - 0.5, 0.5 - c.g(), c.b() - 0.5).unit();
    Vector3 u = basis.u();
    Vector3 v = basis.v();

    basis = Basis3.fromWUV(n, u, v);
    context.setShadingBasis(basis);
  }

}
