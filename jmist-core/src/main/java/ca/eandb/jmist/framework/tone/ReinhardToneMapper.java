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
package ca.eandb.jmist.framework.tone;

import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.CIExyY;
import ca.eandb.jmist.framework.color.ColorUtil;

public final class ReinhardToneMapper implements ToneMapper {

  /** Serialization version ID. */
  private static final long serialVersionUID = 260645449581170296L;

  private final double yWhiteSquared;

  private final double yScale;

  public ReinhardToneMapper(double yScale, double yWhite) {
    this.yScale = yScale;
    this.yWhiteSquared = yWhite * yWhite;
  }

  @Override
  public CIEXYZ apply(CIEXYZ hdr) {
    CIExyY xyY = CIExyY.fromXYZ(hdr);
    double Y = xyY.Y() * yScale;
    if (yWhiteSquared < Double.POSITIVE_INFINITY) {
      Y = Y * (1.0 + Y / yWhiteSquared) / (1.0 + Y);
    } else {
      Y = Y / (1.0 + Y);
    }
    return ColorUtil.convertxyY2XYZ(xyY.x(), xyY.y(), Y);
  }

}
