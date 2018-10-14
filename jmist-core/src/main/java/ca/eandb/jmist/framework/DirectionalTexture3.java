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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

/**
 * Maps directions in three dimensional space to spectra.
 * @author Brad Kimmel
 */
@Deprecated
public interface DirectionalTexture3 extends Serializable {

  /**
   * Computes the color at the specified direction in the domain.
   * @param v The <code>Vector3</code> in the domain.
   * @param lambda The <code>WavelengthPacket</code> representing the
   *     wavelengths at which to evaluate the texture.
   * @return The <code>Color</code> at in the direction of <code>v</code>.
   */
  Color evaluate(Vector3 v, WavelengthPacket lambda);

  /**
   * Computes the spectrum at the specified direction in the domain.
   * @param v The <code>Vector3</code> in the domain.
   * @return The <code>Spectrum</code> at in the direction of <code>v</code>.
   */
  Spectrum evaluate(Vector3 v);

}
