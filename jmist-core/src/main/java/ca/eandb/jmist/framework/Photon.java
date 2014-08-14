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
package ca.eandb.jmist.framework;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Ray3</code> with an associated <code>Color</code>.
 * @author Brad Kimmel
 */
public final class Photon {

  /** The <code>Ray3</code>. */
  private final Ray3 ray;

  /** The <code>Color</code>. */
  private final Color power;

  /**
   * Creates a <code>Photon</code>.
   * @param ray The <code>Ray3</code>.
   * @param power The <code>Color</code>.
   */
  public Photon(Ray3 ray, Color power) {
    this.ray = ray;
    this.power = power;
  }

  /**
   * Creates a <code>Photon</code>.
   * @param position The <code>Point3</code> at the origin of the ray.
   * @param direction The <code>Vector3</code> indicating the direction of
   *     the ray.
   * @param power The <code>Color</code>.
   */
  public Photon(Point3 position, Vector3 direction, Color power) {
    this(new Ray3(position, direction), power);
  }

  /** The <code>Ray3</code>. */
  public Ray3 ray() {
    return ray;
  }

  /** The <code>Color</code>. */
  public Color power() {
    return power;
  }

  /** The <code>Point3</code> at the origin of the ray. */
  public Point3 position() {
    return ray.origin();
  }

  /** The <code>Vector3</code>indicating the direction of the ray. */
  public Vector3 direction() {
    return ray.direction();
  }

}
