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
package ca.eandb.jmist.framework;

import java.io.Serializable;

import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Sphere;

/**
 * Builds a box that contains the elements added to it.
 * @author Brad Kimmel
 */
public class BoundingBoxBuilder3 implements Serializable {

  /**
   * Default constructor.
   * Initializes the bounding box builder to return an empty box.
   */
  public BoundingBoxBuilder3() {
    this.reset();
  }

  /**
   * Gets the smallest box that bounds everything that has been
   * added since the last reset.
   * @return The current bounding box.
   * @see #reset()
   */
  public Box3 getBoundingBox() {
    if (this.isEmpty()) {
      return Box3.EMPTY;
    }

    // FIXME epsilon checks should not be done at this level.
    return new Box3(minimumX - MathUtil.EPSILON, minimumY - MathUtil.EPSILON, minimumZ - MathUtil.EPSILON, maximumX + MathUtil.EPSILON, maximumY + MathUtil.EPSILON, maximumZ + MathUtil.EPSILON);
  }

  /**
   * Resets the bounding box builder to return the empty box.
   */
  public void reset() {
    minimumX = Double.NaN;
  }

  /**
   * Extends the bounding box to encompass the given point.
   * @param p The point to extend the bounding box to.
   */
  public void add(Point3 p) {
    if (this.isEmpty()) {
      minimumX = maximumX = p.x();
      minimumY = maximumY = p.y();
      minimumZ = maximumZ = p.z();
    } else {
      minimumX = Math.min(p.x(), minimumX);
      minimumY = Math.min(p.y(), minimumY);
      minimumZ = Math.min(p.z(), minimumZ);
      maximumX = Math.max(p.x(), maximumX);
      maximumY = Math.max(p.y(), maximumY);
      maximumZ = Math.max(p.z(), maximumZ);
    }
  }

  /**
   * Extends the bounding box to encompass the given sphere.
   * @param sphere The sphere to include in the bounding box.
   */
  public void add(Sphere sphere) {
    if (!sphere.isEmpty()) {
      double r = sphere.radius();
      Point3 c = sphere.center();

      if (this.isEmpty()) {
        minimumX = c.x() - r;
        minimumY = c.y() - r;
        minimumZ = c.z() - r;
        maximumX = c.x() + r;
        maximumY = c.y() + r;
        maximumZ = c.z() + r;
      } else {
        minimumX = Math.min(c.x() - r, minimumX);
        minimumY = Math.min(c.y() - r, minimumY);
        minimumZ = Math.min(c.z() - r, minimumZ);
        maximumX = Math.max(c.x() + r, maximumX);
        maximumY = Math.max(c.y() + r, maximumY);
        maximumZ = Math.max(c.z() + r, maximumZ);
      }
    }
  }

  /**
   * Extends the bounding box to encompass the given box.
   * @param box The box to include in the bounding box.
   */
  public void add(Box3 box) {
    if (!box.isEmpty()) {
      if (this.isEmpty()) {
        minimumX = box.minimumX();
        minimumY = box.minimumY();
        minimumZ = box.minimumZ();
        maximumX = box.maximumX();
        maximumY = box.maximumY();
        maximumZ = box.maximumZ();
      } else {
        minimumX = Math.min(box.minimumX(), minimumX);
        minimumY = Math.min(box.minimumY(), minimumY);
        minimumZ = Math.min(box.minimumZ(), minimumZ);
        maximumX = Math.max(box.maximumX(), maximumX);
        maximumY = Math.max(box.maximumY(), maximumY);
        maximumZ = Math.max(box.maximumZ(), maximumZ);
      }
    }
  }

  /**
   * Indicates whether the bounding box is currently empty.
   * Equivalent to this.getBoundingBox().isEmpty().
   * @return A value indicating whether the bounding box is
   *     currently empty.
   * @see #getBoundingBox()
   * @see Box3#isEmpty()
   */
  public boolean isEmpty() {
    return Double.isNaN(minimumX);
  }

  private double minimumX, minimumY, minimumZ;
  private double maximumX, maximumY, maximumZ;

  /** Serialization version ID. */
  private static final long serialVersionUID = 351994557507294139L;

}
