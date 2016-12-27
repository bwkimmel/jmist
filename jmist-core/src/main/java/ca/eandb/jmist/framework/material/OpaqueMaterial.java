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
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;

/**
 * @author Brad Kimmel
 *
 */
public abstract class OpaqueMaterial extends AbstractMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = -1152259343035236209L;

  @Override
  public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
    return lambda.getColorModel().getGray(Double.POSITIVE_INFINITY, lambda);
  }

  @Override
  public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
    return lambda.getColorModel().getWhite(lambda);
  }

  @Override
  public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
    return lambda.getColorModel().getBlack(lambda);
  }

}
