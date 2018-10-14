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
package ca.eandb.jmist.framework.geometry.primitive;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.geometry.AbstractGeometry;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author Brad Kimmel
 *
 */
public final class PolygonalSurfaceOfRevolutionGeometry extends
    AbstractGeometry {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 5027391031027922071L;

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    // TODO Auto-generated method stub

  }

  @Override
  public Box3 boundingBox() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Sphere boundingSphere() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Box3 getBoundingBox(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Sphere getBoundingSphere(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getNumPrimitives() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    // TODO Auto-generated method stub

  }

}
