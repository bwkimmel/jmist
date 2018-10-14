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
package ca.eandb.jmist.framework.display.visualizer;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.math.Interval;

/**
 * A <code>ColorVisualizer</code> that extracts a single channel from the
 * provided <code>Color</code>.
 *
 * @author Brad Kimmel
 */
public final class LinearChannelVisualizer extends StaticColorVisualizer {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7864894025681399665L;

  private final int channel;

  private final Interval range;

  public LinearChannelVisualizer(int channel, Interval range) {
    if (range.isInfinite()) {
      throw new IllegalArgumentException("range is infinite");
    } else if (range.isEmpty()) {
      throw new IllegalArgumentException("range is empty");
    } else if (channel < 0) {
      throw new IllegalArgumentException("channel < 0");
    }

    this.channel = channel;
    this.range = range;
  }

  public LinearChannelVisualizer(int channel, double minimum, double maximum) {
    this(channel, new Interval(minimum, maximum));
  }

  public LinearChannelVisualizer(int channel, double maximum) {
    this(channel, 0.0, maximum);
  }

  public LinearChannelVisualizer(int channel) {
    this(channel, 0.0, 1.0);
  }

  @Override
  public RGB visualize(Color color) {
    double value = (color.getValue(channel) - range.minimum()) / range.length();
    return new RGB(value, value, value);
  }

}
