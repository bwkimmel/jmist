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

import org.apache.commons.math3.util.FastMath;

import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>CollectorSphere</code> where each sensor spans the same solid angle:
 * <code>4*PI / this.sensors()</code> (in steradians).
 * @author Brad Kimmel
 */
public final class EqualSolidAnglesCollectorSphere implements CollectorSphere {

  /**
   * Creates a new <code>EqualSolidAnglesCollectorSphere</code>.
   * @param stacks The number of stacks to divide each hemisphere into.
   * @param slices The number of slices to divide the sphere into (about the
   *     azimuthal angle).
   * @param upper A value indicating whether to record hits for the upper
   *     hemisphere.
   * @param lower A value indicating whether to record hits for the lower
   *     hemisphere.
   * @throws IllegalArgumentException if both <code>upper</code> and
   *     <code>lower</code> are <code>false</code>.
   */
  public EqualSolidAnglesCollectorSphere(int stacks, int slices, boolean upper, boolean lower) {

    if (!upper && !lower) {
      throw new IllegalArgumentException("One of upper or lower must be true.");
    }

    int hemispheres = (upper ? 1 : 0) + (lower ? 1 : 0);

    /* Each stack has "slices" sensors, except for the ones at the very top
     * of the upper hemisphere and the bottom of the lower hemisphere,
     * which have one (circular) patch.
     */
    this.sensors = hemispheres * ((stacks - 1) * slices + 1);

    this.stacks = stacks;
    this.slices = slices;
    this.upper = upper;
    this.lower = lower;

  }

  @Override
  public SphericalCoordinates getSensorCenter(int sensor) {

    int hemispheres = (upper ? 1 : 0) + (lower ? 1 : 0);
    assert(hemispheres > 0);

    int patchesPerHemisphere = this.sensors() / hemispheres;

    /* The sensor is on the lower hemisphere if there is no upper
     * hemisphere or if the sensor ID is in the second half of the
     * sensors.
     */
    boolean sensorOnLower = !upper || sensor >= patchesPerHemisphere;

    /* If both hemispheres are present, and we are computing the center of
     * a patch on the lower hemisphere, then compute the center of the
     * corresponding patch on the upper hemisphere and then adjust it
     * after.
     */
    if (upper && lower && sensorOnLower) {
      sensor = this.sensors() - 1 - sensor;
    }

    int stack;
    int slice;

    if (sensor == 0) {

      /* The first sensor is the one at the top (or bottom). */
      stack = slice = 0;

    } else { /* sensor > 0 */

      stack = (sensor - 1) / slices + 1;
      slice = (sensor - 1) % slices;

      if (this.upper && this.lower && sensorOnLower) {
        slice = slices - 1 - slice;
      }

    }

    double phi = 2.0 * Math.PI * ((double) slice / (double) slices);
    double t0 = 1.0 / (double) patchesPerHemisphere;
    double t = (double) slices * t0;
    double z = (stack > 0) ? (1.0 - t0 - (stack - 0.5) * t) : 1.0;

    if (sensorOnLower) {
      z = -z;
    }

    return SphericalCoordinates.canonical(FastMath.acos(z), phi);

  }

  @Override
  public double getSensorProjectedSolidAngle(int sensor) {

    int hemispheres = (upper ? 1 : 0) + (lower ? 1 : 0);
    int patchesPerHemisphere = this.sensors() / hemispheres;

    /* The sensor is on the lower hemisphere if there is no upper
     * hemisphere or if the sensor ID is in the second half of the
     * sensors.
     */
    boolean sensorOnLower = !upper || sensor >= patchesPerHemisphere;

    /* If both hemispheres are present, and we are computing the projected
     * solid angle of a patch on the lower hemisphere, then compute the
      * projected solid angle of the corresponding patch on the upper
      * hemisphere (they will be equal).
     */
    if (upper && lower && sensorOnLower) {
      sensor = this.sensors() - 1 - sensor;
    }

    assert(0 <= sensor && sensor < patchesPerHemisphere);

    int stack = (sensor > 0) ? (sensor - 1) / slices + 1 : 0;

    double t0 = 1.0 / (double) patchesPerHemisphere;

    if (sensor > 0) {

      assert(stack > 0);

      double t = (double) slices * t0;
      double z0 = (double) (stacks - 1 - stack) * t;
      double z1 = (double) (stacks - stack) * t;

      return Math.PI * (z1 * z1 - z0 * z0) / (double) slices;

    } else { /* sensor == 0 */

      return Math.PI * t0 * (2.0 - t0);

    }

  }

  @Override
  public double getSensorSolidAngle(int sensor) {
    double hemispheres = (upper ? 1.0 : 0.0) + (lower ? 1.0 : 0.0);
    return hemispheres * (2.0 * Math.PI) / (double) this.sensors();
  }

  private int getSensor(SphericalCoordinates v) {
    v = v.canonical();
    return this.getSensor(v.azimuthal(), Math.cos(v.polar()));
  }

  private int getSensor(Vector3 v) {
    return this.getSensor(Math.atan2(v.y(), v.x()), v.z() / v.length());
  }

  /**
   * Gets the sensor struck by the specified unit vector.
   * @param azimuthal The azimuthal angle of the vector.
   * @param z The z-coordinate of the vector.
   * @return The sensor struck by the specified vector.
   */
  private int getSensor(double azimuthal, double z) {

    boolean hitUpper = z > 0.0;

    if ((hitUpper && !upper) || (!hitUpper && !lower)) {
      return -1;
    }

    z = upper ? z : -z;

    int hemispheres = (upper ? 1 : 0) + (lower ? 1 : 0);
    int patchesPerHemisphere = this.sensors() / hemispheres;

    double t0 = 1.0 / (double) patchesPerHemisphere;

    if (z >= 1.0 - t0) {
      return 0;
    }

    if (z < -1.0 + t0) {
      return this.sensors() - 1;
    }

    double t = (double) slices * t0;
    int stack = (int) Math.ceil((1.0 - t0 - z) / t);
    double phi = azimuthal + Math.PI / (double) slices;

    if (phi < 0.0) phi += 2.0 * Math.PI;
    if (phi >= 2.0 * Math.PI) phi -= 2.0 * Math.PI;

    int slice = (int) ((double) slices * (phi / (2.0 * Math.PI)));

    stack = MathUtil.clamp(stack, 1, hemispheres * (stacks - 1));
    slice = MathUtil.clamp(slice, 0, slices - 1);

    return 1 + (stack - 1) * slices + slice;

  }

  @Override
  public void record(Vector3 v, Callback f) {
    int sensor = getSensor(v);
    if (sensor >= 0) {
      f.record(sensor);
    }
  }

  @Override
  public void record(SphericalCoordinates v, Callback f) {
    int sensor = getSensor(v);
    if (sensor >= 0) {
      f.record(sensor);
    }
  }

  @Override
  public int sensors() {
    return sensors;
  }

  /** The total number of sensors. */
  private final int sensors;

  /** The number of stacks per hemisphere. */
  private final int stacks;

  /** The number of slices. */
  private final int slices;

  /** A value indicating whether the upper hemisphere is measured. */
  private final boolean upper;

  /** A value indicating whether the lower hemisphere is measured. */
  private final boolean lower;

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 6947672588017728172L;

}
