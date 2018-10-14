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

import java.io.Serializable;
import java.util.Arrays;

import ca.eandb.jmist.framework.measurement.CollectorSphere.Callback;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
public final class IntegerSensorArray implements Callback, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 2445861778987000123L;

  private final long[] hits;

  public IntegerSensorArray(long[] hits) {
    this.hits = hits;
  }

  public IntegerSensorArray(int numSensors) {
    this(new long[numSensors]);
  }

  public IntegerSensorArray(CollectorSphere collector) {
    this(collector.sensors());
  }

  public void reset() {
    Arrays.fill(hits, 0);
  }

  @Override
  public void record(int sensor) {
    hits[sensor]++;
  }

  public long hits(int sensor) {
    return hits[sensor];
  }

  public void merge(IntegerSensorArray other) {
    MathUtil.add(hits, other.hits);
  }

  public int sensors() {
    return hits.length;
  }

}
