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

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

public interface CollectorSphere extends Serializable {

  interface Callback {
    void record(int sensor);
  };

  void record(Vector3 v, Callback f);
  void record(SphericalCoordinates v, Callback f);

  int sensors();

  double getSensorSolidAngle(int sensor);
  double getSensorProjectedSolidAngle(int sensor);

  SphericalCoordinates getSensorCenter(int sensor);

  /** A dummy <code>CollectorSphere</code> with no sensors. */
  CollectorSphere NULL = new CollectorSphere() {
    /** Serialization version ID. */
    private static final long serialVersionUID = 6788498446054635921L;

    @Override
    public void record(Vector3 v, Callback f) {}

    @Override
    public void record(SphericalCoordinates v, Callback f) {}

    @Override
    public int sensors() { return 0; }

    @Override
    public double getSensorSolidAngle(int sensor) {
      throw new IllegalArgumentException("sensor out of range");
    }

    @Override
    public double getSensorProjectedSolidAngle(int sensor) {
      throw new IllegalArgumentException("sensor out of range");
    }

    @Override
    public SphericalCoordinates getSensorCenter(int sensor) {
      throw new IllegalArgumentException("sensor out of range");
    }
  };
}
