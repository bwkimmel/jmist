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
package ca.eandb.jmist.framework.color.xyz;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
/* package */ final class XYZWavelengthPacket implements WavelengthPacket {

  private final double lambdaX;

  private final double lambdaY;

  private final double lambdaZ;

  /**
   * @param lambdaX
   * @param lambdaY
   * @param lambdaZ
   */
  public XYZWavelengthPacket(double lambdaX, double lambdaY, double lambdaZ) {
    this.lambdaX = lambdaX;
    this.lambdaY = lambdaY;
    this.lambdaZ = lambdaZ;
  }

  /**
   * @return the lambdaX
   */
  public double getLambdaX() {
    return lambdaX;
  }

  /**
   * @return the lambdaY
   */
  public double getLambdaY() {
    return lambdaY;
  }

  /**
   * @return the lambdaZ
   */
  public double getLambdaZ() {
    return lambdaZ;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.color.WavelengthPacket#getColorModel()
   */
  public ColorModel getColorModel() {
    return XYZColorModel.getInstance();
  }

}
