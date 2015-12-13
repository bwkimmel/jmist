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
package ca.eandb.jmist.framework.color.xyz.multi;

import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author Brad
 *
 */
/* package */ final class MultiXYZWavelengthPacket implements WavelengthPacket {

  private final MultiXYZColorModel owner;

  private final double[] wavelengths;

  public MultiXYZWavelengthPacket(double[] wavelengths, MultiXYZColorModel owner) {
    this.owner = owner;
    this.wavelengths = wavelengths;
  }

  public double getLambda(int channel) {
    return wavelengths[channel];
  }

  public double getLambdaX(int channel) {
    return wavelengths[channel + owner.getOffsetX()];
  }

  public double getLambdaY(int channel) {
    return wavelengths[channel + owner.getOffsetY()];
  }

  public double getLambdaZ(int channel) {
    return wavelengths[channel + owner.getOffsetZ()];
  }

  @Override
  public MultiXYZColorModel getColorModel() {
    return owner;
  }

}
