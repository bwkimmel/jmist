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

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.RGB;

/**
 * Represents the XYZE pixel format for a <code>RadiancePicture</code>.
 * This class is a singleton.
 * @see #INSTANCE
 * @author Brad Kimmel
 */
final class XYZEPixelFormat implements PixelFormat {

  /** Serialization version ID. */
  private static final long serialVersionUID = 8026681085259479612L;

  /** The single <code>XYZEPixelFormat</code> instance. */
  public static final XYZEPixelFormat INSTANCE = new XYZEPixelFormat();

  /**
   * Creates a new <code>XYZEPixelFormat</code>.
   * This constructor is private because this class is a singleton.
   */
  private XYZEPixelFormat() {}

  @Override
  public RGB toRGB(int raw) {
    return toXYZ(raw).toRGB();
  }

  @Override
  public int toRaw(RGB rgb) {
    return rgb.toXYZ().toXYZE();
  }

  @Override
  public int toRaw(CIEXYZ xyz) {
    return xyz.toXYZE();
  }

  @Override
  public CIEXYZ toXYZ(int raw) {
    return CIEXYZ.fromXYZE(raw);
  }

}
