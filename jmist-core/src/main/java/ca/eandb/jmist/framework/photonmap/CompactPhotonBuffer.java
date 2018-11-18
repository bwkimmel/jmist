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
package ca.eandb.jmist.framework.photonmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents a compact array of photons used for photon mapping.
 * @author brad
 */
final class CompactPhotonBuffer implements PhotonBuffer {

  /** The <code>ByteBuffer</code> in which to store the photons. */
  private final ByteBuffer buffer;

  /** The number of bytes required for a single photon. */
  private static final int ELEMENT_SIZE = 20;

  /**
   * The offset into the storage for a photon at which its x coordinate is
   * stored.
   */
  private static final int OFFSET_X = 0;

  /**
   * The offset into the storage for a photon at which its y coordinate is
   * stored.
   */
  private static final int OFFSET_Y = 4;

  /**
   * The offset into the storage for a photon at which its z coordinate is
   * stored.
   */
  private static final int OFFSET_Z = 8;

  /**
   * The offset into the storage for a photon at which its power is stored.
   */
  private static final int OFFSET_POWER = 12;

  /**
   * The offset into the storage for a photon at which its direction is
   * stored.  The direction is stored in a compact, two byte format.
   * @see ca.eandb.jmist.math.Vector3#toCompactDirection()
   */
  private static final int OFFSET_DIR = 16;

  /**
   * The offset into the storage for a photon at which a short (two byte)
   * value indicating the orientation of the dividing plane is stored.
   * Zero represents the YZ-plane, one represents the XZ-plane, and two
   * represents the XY-plane.
   * @see #store(Point3, double, Vector3, short)
   */
  private static final int OFFSET_PLANE = 18;

  /**
   * Creates a <code>CompactPhotonBuffer</code> large enough to hold
   * <code>capacity</code> photons.
   * @param capacity The number of photons to allocate storage for.
   */
  public CompactPhotonBuffer(int capacity) {
    buffer = ByteBuffer.allocateDirect(capacity * ELEMENT_SIZE);
    buffer.order(ByteOrder.nativeOrder());  // gives a slight performance boost.
  }

  /**
   * Moves the cursor to the specified index.  Subsequent calls to
   * {@link #store(Point3, double, Vector3, short)} will write
   * photons starting at that index.
   * @param index The location to move the cursor to.
   * @see #store(Point3, double, Vector3, short)
   */
  public void moveTo(int index) {
    buffer.position(index * ELEMENT_SIZE);
  }

  /**
   * Stores a photon at the current cursor position.
   * @param p The location of the photon.
   * @param power The power of the photon.
   * @param dir The compact direction of the photon (see
   *     {@link ca.eandb.jmist.math.Vector3#toCompactDirection()}).
   * @param plane The value indicating the orientation of the dividing plane.
   *     (0 - perpendicular to the x-axis, 1 - perpendicular to the y-axis,
   *     or 2 - perpendicular to the z-axis).
   * @see #moveTo(int)
   * @see ca.eandb.jmist.math.Vector3#toCompactDirection()
   */
  public void store(Point3 p, double power, Vector3 dir, short plane) {
    buffer.putFloat((float) p.x());
    buffer.putFloat((float) p.y());
    buffer.putFloat((float) p.z());
    buffer.putFloat((float) power);
    buffer.putShort(dir.toCompactDirection());
    buffer.putShort(plane);
  }

  @Override
  public Point3 getPosition(int index) {
    int offset = index * ELEMENT_SIZE;
    return new Point3(buffer.getFloat(offset + OFFSET_X), buffer.getFloat(offset + OFFSET_Y), buffer.getFloat(offset + OFFSET_Z));
  }

  @Override
  public double getPosition(int index, int element) {
    return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_X + (element * 4));
  }

  @Override
  public double getX(int index) {
    return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_X);
  }

  @Override
  public double getY(int index) {
    return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_Y);
  }

  @Override
  public double getZ(int index) {
    return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_Z);
  }

  /**
   * Gets the power of the specified photon.
   * @param index The index of the photon to obtain the power of.
   * @return The power of the specified photon.
   */
  public double getPower(int index) {
    return buffer.getFloat(index * ELEMENT_SIZE + OFFSET_POWER);
  }

  /**
   * Gets the direction of the specified photon.  The direction is
   * represented in a compact, two byte format.
   * @param index The index of the photon to obtain the direction of.
   * @return The direction of the specified photon.
   * @see ca.eandb.jmist.math.Vector3#toCompactDirection()
   */
  public Vector3 getDir(int index) {
    return Vector3.fromCompactDirection(buffer.getShort(index * ELEMENT_SIZE + OFFSET_DIR));
  }

  @Override
  public short getPlane(int index) {
    return buffer.getShort(index * ELEMENT_SIZE + OFFSET_PLANE);
  }

  /**
   * Sets the power of the specified photon.
   * @param index The index of the photon to set the power of.
   * @param power The power to assign to the photon.
   */
  public void setPower(int index, double power) {
    buffer.putFloat(index * ELEMENT_SIZE + OFFSET_POWER, (float) power);
  }

  /**
   * Scales the power of the specified photon by a given factor.
   * @param index The index of the photon whose power to scale.
   * @param scale The factor by which to scale the photon's power.
   */
  public void scalePower(int index, double scale) {
    int offset = index * ELEMENT_SIZE + OFFSET_POWER;
    buffer.putFloat(offset, (float) scale * buffer.getFloat(offset));
  }

  @Override
  public void setPlane(int index, short plane) {
    buffer.putShort(index * ELEMENT_SIZE + OFFSET_PLANE, plane);
  }

  @Override
  public void copyPhoton(int src, int dst) {
    buffer.position(src * ELEMENT_SIZE);
    ByteBuffer slice = buffer.slice();
    slice.limit(ELEMENT_SIZE);

    buffer.position(dst * ELEMENT_SIZE);
    buffer.put(slice);
  }

}
