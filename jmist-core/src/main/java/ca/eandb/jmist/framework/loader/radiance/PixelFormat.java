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
package ca.eandb.jmist.framework.loader.radiance;

import java.io.Serializable;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.RGB;

/**
 * Represents a pixel format for a <code>RadiancePicture</code>.
 * 
 * @author Brad Kimmel
 */
interface PixelFormat extends Serializable {

  /**
   * Converts an <code>RGB</code> color to the internal representation.
   * @param rgb The <code>RGB</code> color to convert.
   * @return The internal representation of <code>rgb</code>.
   */
  int toRaw(RGB rgb);
    
  /**
   * Converts an <code>CIEXYZ</code> color to the internal representation.
   * @param xyz The <code>CIEXYZ</code> color to convert.
   * @return The internal representation of <code>xyz</code>.
   */
  int toRaw(CIEXYZ xyz);
  
  /**
   * Converts an internal representation of a color to <code>RGB</code>.
   * @param raw The internal representation of the color.
   * @return The corresponding <code>RGB</code> color.
   */
  RGB toRGB(int raw);
  
  /**
   * Converts an internal representation of a color to <code>CIEXYZ</code>.
   * @param raw The internal representation of the color.
   * @return The corresponding <code>CIEXYZ</code> color.
   */
  CIEXYZ toXYZ(int raw);
  
}
