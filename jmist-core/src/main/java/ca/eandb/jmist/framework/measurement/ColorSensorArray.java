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
package ca.eandb.jmist.framework.measurement;

import java.util.Arrays;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.MathUtil;

public final class ColorSensorArray {

  private final ColorModel colorModel;

  private final double[] weight;

  private final int numSensors;

  private final int numChannels;

  private class Callback implements CollectorSphere.Callback {

    private final Color color;

    public Callback(Color color) {
      this.color = color;
    }

    @Override
    public void record(int sensor) {
      ColorSensorArray.this.record(sensor, color);
    }

  }

  public ColorSensorArray(int numSensors, ColorModel colorModel) {
    this.colorModel = colorModel;
    this.numSensors = numSensors;
    this.numChannels = colorModel.getNumChannels();
    this.weight = new double[numSensors * numChannels];
  }

  public Callback createCallback(Color color) {
    return new Callback(color);
  }

  public void record(int sensor, Color color) {
    if (color != null) {
      double[] channels = color.toArray();
      for (int i = 0, j = sensor * numChannels; i < numChannels; i++, j++) {
        weight[j] += channels[i];
      }
    }
  }

  public Color getTotalWeight(int sensor) {
    int from = sensor * numChannels;
    int to = from + numChannels;
    return colorModel.fromArray(Arrays.copyOfRange(weight, from, to), null);
  }

  public void reset() {
    Arrays.fill(weight, 0);
  }

  public void merge(ColorSensorArray other) {
    MathUtil.add(weight, other.weight);
  }

  public int sensors() {
    return numSensors;
  }

}
