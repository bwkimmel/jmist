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
package ca.eandb.jmist.framework.color;

import java.io.Serializable;

/**
 * Represents the "color" of something in an abstract sense.  A
 * <code>Spectrum</code> may be sampled given a <code>WavelengthPacket</code>
 * from a compatible <code>ColorModel</code>.
 * @author Brad Kimmel
 */
@FunctionalInterface
public interface Spectrum extends Serializable {

  /**
   * Samples this spectrum.
   * @param lambda The <code>WavelengthPacket</code> to use to sample the
   *     spectrum.
   * @return The <code>Color</code> sample.
   */
  Color sample(WavelengthPacket lambda);

  Spectrum BLACK = lambda -> lambda.getColorModel().getBlack(lambda);
  Spectrum WHITE = lambda -> lambda.getColorModel().getWhite(lambda);

  static Spectrum mix(double t, Spectrum a, Spectrum b) {
    return lambda -> a.sample(lambda).times(1.0 - t).plus(b.sample(lambda).times(t));
  }

}
