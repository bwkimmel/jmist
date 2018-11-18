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

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>CollectorSphere</code> that collects rays into two buckets, one for
 * the upper hemisphere and one for the lower hemisphere.
 * @author Brad Kimmel
 */
public final class SpectrophotometerCollectorSphere implements CollectorSphere {

  @Override
  public SphericalCoordinates getSensorCenter(int sensor) {
    assert(0 <= sensor && sensor < 2);
    return sensor == UPPER_HEMISPHERE ? SphericalCoordinates.NORMAL : SphericalCoordinates.ANTINORMAL;
  }

  @Override
  public double getSensorProjectedSolidAngle(int sensor) {
    return Math.PI;
  }

  @Override
  public double getSensorSolidAngle(int sensor) {
    return 2.0 * Math.PI;
  }

  @Override
  public void record(Vector3 v, Callback f) {
    f.record((v.z() > 0.0) ? UPPER_HEMISPHERE : LOWER_HEMISPHERE);
  }

  @Override
  public void record(SphericalCoordinates v, Callback f) {
    f.record((v.polar() < (Math.PI / 2.0)) ? UPPER_HEMISPHERE : LOWER_HEMISPHERE);
  }

  @Override
  public int sensors() {
    return 2;
  }

  /** The sensor ID for the upper hemisphere sensor. */
  private static final int UPPER_HEMISPHERE = 0;

  /** The sensor ID for the lower hemisphere sensor. */
  private static final int LOWER_HEMISPHERE = 1;

  /** Serialization version ID. */
  private static final long serialVersionUID = -6289494553073934175L;

}
