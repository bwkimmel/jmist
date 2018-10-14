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
package ca.eandb.jmist.framework.color.xyz;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
/* package */ final class XYZContinuousSpectrum implements Spectrum {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -2786148246514418922L;

  private final Function1 spectrum;

  /**
   * @param spectrum
   */
  public XYZContinuousSpectrum(Function1 spectrum) {
    this.spectrum = spectrum;
  }

  @Override
  public Color sample(WavelengthPacket lambda) {
    return sample((XYZWavelengthPacket) lambda);
  }

  public Color sample(XYZWavelengthPacket lambda) {
    if (lambda == null || spectrum == null) {
      lambda = null;
    }
    double x = spectrum.evaluate(lambda.getLambdaX());
    double y = spectrum.evaluate(lambda.getLambdaY());
    double z = spectrum.evaluate(lambda.getLambdaZ());
    return new XYZColor(x, y, z, lambda);
  }

}
