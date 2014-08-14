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
package ca.eandb.jmist.framework.geometry;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Sphere;

/**
 * A <code>SceneElement</code> that is the intersection of its component
 * geometries.
 * @author Brad Kimmel
 */
public final class IntersectionGeometry extends ConstructiveSolidGeometry {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -91390515433295892L;

  /* (non-Javadoc)
   * @see ca.eandb.jmist.packages.ConstructiveSolidGeometry#isInside(int, java.util.BitSet)
   */
  @Override
  protected boolean isInside(int nArgs, BitSet args) {
    return args.nextClearBit(0) >= nArgs;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
   */
  @Override
  public Box3 boundingBox() {

    Collection<Box3> boxes = new ArrayList<Box3>();

    for (SceneElement geometry : this.children()) {
      boxes.add(geometry.boundingBox());
    }

    return Box3.intersection(boxes);

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
   */
  @Override
  public Sphere boundingSphere() {
    Box3 boundingBox = this.boundingBox();
    return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
  }

}
