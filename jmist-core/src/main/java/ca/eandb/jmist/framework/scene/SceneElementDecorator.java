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

import java.io.Serializable;

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * An abstract <code>SceneElement</code> that decorates another, underlying
 * <code>SceneElement</code>.  This class provides default implementations
 * that delegate to the underlying <code>SceneElement</code>.  It is the
 * responsibility of the concrete derived <code>SceneElement</code> to
 * override the appropriate methods.
 * @author Brad Kimmel
 */
public abstract class SceneElementDecorator implements SceneElement, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7406144984143234198L;

  private final SceneElement inner;

  public SceneElementDecorator(SceneElement inner) {
    this.inner = inner;
  }

  public Box3 boundingBox() {
    return inner.boundingBox();
  }

  public Sphere boundingSphere() {
    return inner.boundingSphere();
  }

  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
    inner.generateRandomSurfacePoint(index, context, ru, rv, rj);
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
    inner.generateRandomSurfacePoint(context, ru, rv, rj);
  }

  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
    return inner.generateImportanceSampledSurfacePoint(index, x, context, ru, rv, rj);
  }

  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    return inner.generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
  }

  public Box3 getBoundingBox(int index) {
    return inner.getBoundingBox(index);
  }

  public Sphere getBoundingSphere(int index) {
    return inner.getBoundingSphere(index);
  }

  public int getNumPrimitives() {
    return inner.getNumPrimitives();
  }

  public double getSurfaceArea() {
    return inner.getSurfaceArea();
  }

  public double getSurfaceArea(int index) {
    return inner.getSurfaceArea(index);
  }

  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    inner.intersect(index, ray, recorder);
  }

  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    inner.intersect(ray, recorder);
  }

  public boolean intersects(int index, Box3 box) {
    return inner.intersects(index, box);
  }

  public boolean visibility(int index, Ray3 ray) {
    return inner.visibility(index, ray);
  }

  public boolean visibility(Ray3 ray) {
    return inner.visibility(ray);
  }

  public Light createLight() {
    return inner.createLight();
  }

}
