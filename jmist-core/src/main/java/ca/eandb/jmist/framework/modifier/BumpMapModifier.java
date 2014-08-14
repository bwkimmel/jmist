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

import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.texture.RasterMask2;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Vector3;

/**
 * Transforms the shading basis using a normal map.
 * @see http://en.wikipedia.org/wiki/Normal_mapping
 */
public final class BumpMapModifier implements Modifier {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7424539062657560286L;

  /** The mask indicating the amount of displacement for each UV coordinate. */
  private final Mask2 height;

  /** The factor by which to scale the height map. */
  private final double scale;

  /**
   * The distance in texture space (in the U-direction) to use for finite
   * difference estimation.
   */
  private final double deltaU;

  /**
   * The distance in texture space (in the V-direction) to use for finite
   * difference estimation.
   */
  private final double deltaV;

  /**
   * Create a new <code>BumpMapModifier</code>.
   * @param height The <code>Mask2</code> indicating the displacement for
   *     each (u, v) coordinate.
   * @param scale The factor by which to scale the height map.
   * @param deltaU The distance in texture space (in the U-direction) to use
   *     for finite difference estimation.  For a height map derived from a
   *     raster image, this should correspond to one pixel in the
   *     x-direction (i.e., 1 / width).
   * @param deltaV The distance in texture space (in the V-direction) to use
   *     for finite difference estimation.  For a height map derived from a
   *     raster image, this should correspond to one pixel in the
   *     y-direction (i.e., 1 / height).
   */
  public BumpMapModifier(Mask2 height, double scale, double deltaU, double deltaV) {
    this.height = height;
    this.scale = scale;
    this.deltaU = deltaU;
    this.deltaV = deltaV;
  }

  /**
   * Create a new <code>BumpMapModifier</code>.
   * @param height The <code>Mask2</code> indicating the displacement for
   *     each (u, v) coordinate.
   * @param scale The factor by which to scale the height map.
   * @return The new <code>BumpMapModifier</code>.
   */
  public static BumpMapModifier fromTiledRasterMask(RasterMask2 height, double scale) {
    return new BumpMapModifier(new TiledMask2(height), scale,
        1.0 / height.getWidth(), 1.0 / height.getHeight());
  }

  /**
   * Create a new <code>BumpMapModifier</code>.
   * @param height The <code>Mask2</code> indicating the displacement for
   *     each (u, v) coordinate.
   * @return The new <code>BumpMapModifier</code>.
   */
  public static BumpMapModifier fromRasterMask(RasterMask2 height) {
    return new BumpMapModifier(height, 1.0,
        1.0 / height.getWidth(), 1.0 / height.getHeight());
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Modifier#modify(ca.eandb.jmist.framework.ShadingContext)
   */
  @Override
  public void modify(ShadingContext context) {

    Basis3 basis = context.getShadingBasis();

    Point2 p = context.getUV();
    Point2 pu = new Point2(p.x() + deltaU, p.y());
    Point2 pv = new Point2(p.x(), p.y() + deltaV);
    double h = scale * height.opacity(p);
    double hu = scale * height.opacity(pu);
    double hv = scale * height.opacity(pv);

    Vector3 n = basis.toStandard(h - hu, h - hv, 1.0).unit();
    Vector3 u = basis.u();
    Vector3 v = basis.v();

    basis = Basis3.fromWUV(n, u, v);
    context.setShadingBasis(basis);

  }

}
