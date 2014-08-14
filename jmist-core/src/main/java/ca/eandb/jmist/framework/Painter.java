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

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * Applies a <code>Color</code> to a <code>SurfacePoint</code>.  The color may
 * be based on any property of the surface point: e.g., the texture coordinates
 * (for texture mapping), the point (for 3D texture mapping), the normal, etc.
 * The <code>Color</code> may then be used in any context by a
 * <code>Shader</code> or a <code>Material</code>.  For example, it may simply
 * be used to control the diffuse color of a material, or the emitted color,
 * or it may be used to control the parameters of some more complex material
 * model.
 * @author Brad Kimmel
 */
public interface Painter extends Serializable {

  /**
   * Gets the <code>Color</code> to apply. 
   * @param p The <code>SurfacePoint</code> to consider.
   * @param lambda The <code>WavelengthPacket</code> to use to generate
   *     <code>Color</code>s from <code>Spectrum</code>s.
   * @return The <code>Color</code> to use.
   */
  Color getColor(SurfacePoint p, WavelengthPacket lambda);

  /** A <code>Painter</code> that always returns black. */
  public static final Painter BLACK = new Painter() {
    private static final long serialVersionUID = -8689283716208944079L;
    public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
      return lambda.getColorModel().getBlack(lambda);
    }
  };

}
