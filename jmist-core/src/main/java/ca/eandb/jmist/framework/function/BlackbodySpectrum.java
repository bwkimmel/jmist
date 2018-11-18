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
package ca.eandb.jmist.framework.function;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Physics;

/**
 * A <code>Function1</code> representing a blackbody emission spectrum.
 * @author Brad Kimmel
 */
public final class BlackbodySpectrum implements Function1 {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5120826959435651065L;

  /** The temperature of the black body to simulate (in Kelvin). */
  private final double temperature;

  /**
   * Creates a new <code>BlackbodySpectrum</code>.
   * @param temperature The temperature of the black body to simulate (in
   *     Kelvin).
   */
  public BlackbodySpectrum(double temperature) {
    this.temperature = temperature;
  }

  @Override
  public double evaluate(double wavelength) {
    if (MathUtil.isZero(temperature)) {
      return 0.0;
    }

    double a = (2.0 * Math.PI * Physics.PLANCK_CONSTANT
        * Physics.SPEED_OF_LIGHT * Physics.SPEED_OF_LIGHT)
        / Math.pow(wavelength, 5.0);
    double b = (1.0 / (Math.exp((Physics.PLANCK_CONSTANT * Physics.SPEED_OF_LIGHT)
        / (wavelength * Physics.BOLTZMANN_CONSTANT * temperature)) - 1.0));
    return a * b;
  }

}
