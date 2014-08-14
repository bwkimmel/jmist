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
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.geometry.PrimitiveGeometry;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad Kimmel
 *
 */
public final class NurbsGeometry extends PrimitiveGeometry {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -3053193751309757912L;

  /**
   * @param material
   */
  public NurbsGeometry(Material material) {
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.geometry.PrimitiveGeometry#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#isClosed()
   */
  public boolean isClosed() {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
   */
  public Box3 boundingBox() {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
   */
  public Sphere boundingSphere() {
    // TODO Auto-generated method stub
    return null;
  }

}
