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
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * A dummy <code>SceneElement</code> that provides no geometry.
 * @author Brad Kimmel
 */
public final class NullSceneElement implements SceneElement {

  /** Serialization version ID. */
  private static final long serialVersionUID = 102708499493815006L;

  public static final SceneElement INSTANCE = new NullSceneElement();

  private NullSceneElement() {
    /* nothing to do. */
  }

  @Override
  public Light createLight() {
    return null;
  }

  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Box3 getBoundingBox(int index) {
    return Box3.EMPTY;
  }

  @Override
  public Sphere getBoundingSphere(int index) {
    return Sphere.EMPTY;
  }

  @Override
  public int getNumPrimitives() {
    return 0;
  }

  @Override
  public double getSurfaceArea(int index) {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public double getSurfaceArea() {
    return 0.0;
  }

  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    /* nothing to do. */
  }

  @Override
  public boolean intersects(int index, Box3 box) {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public boolean visibility(int index, Ray3 ray) {
    throw new IndexOutOfBoundsException();
  }

  @Override
  public Box3 boundingBox() {
    return Box3.EMPTY;
  }

  @Override
  public Sphere boundingSphere() {
    return Sphere.EMPTY;
  }

  @Override
  public boolean visibility(Ray3 ray) {
    return true;
  }

}
