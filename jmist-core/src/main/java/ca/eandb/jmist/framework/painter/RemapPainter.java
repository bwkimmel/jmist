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
package ca.eandb.jmist.framework.painter;

import ca.eandb.jmist.framework.Painter;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Interval;

/**
 * @author brad
 *
 */
public final class RemapPainter implements Painter {

  /** Serialization version ID. */
  private static final long serialVersionUID = -4526940092822564018L;

  private final Painter inner;

  private final Interval range;

  public RemapPainter(Interval range, Painter inner) {
    this.range = range;
    this.inner = inner;

    if (range.isInfinite()) {
      throw new IllegalArgumentException("range may not be infinite");
    }
  }

  @Override
  public Color getColor(SurfacePoint p, WavelengthPacket lambda) {
    Color result = inner.getColor(p, lambda);
    Color min = lambda.getColorModel().getGray(range.minimum(), lambda);
    return min.plus(result.times(range.length()));
  }

}
