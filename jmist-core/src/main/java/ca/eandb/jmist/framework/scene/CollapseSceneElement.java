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
package ca.eandb.jmist.framework.scene;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * A decorator <code>SceneElement</code> that collapses the decorated
 * <code>SceneElement</code> into a single primitive.
 * 
 * @author Brad Kimmel
 */
public final class CollapseSceneElement extends SceneElementDecorator {

  /** Serialization version ID. */
  private static final long serialVersionUID = 806155742528641002L;

  /**
   * Creates a new <code>CollapseSceneElement</code>.
   * @param inner The <code>SceneElement</code> to collapse.
   */
  public CollapseSceneElement(SceneElement inner) {
    super(inner);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv,
      double rj) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    return generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    double weight = super.generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
    context.setPrimitiveIndex(0);
    return weight;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context,
      double ru, double rv, double rj) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    generateRandomSurfacePoint(context, ru, rv, rj);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru,
      double rv, double rj) {
    super.generateRandomSurfacePoint(context, ru, rv, rj);
    context.setPrimitiveIndex(0);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getBoundingBox(int)
   */
  @Override
  public Box3 getBoundingBox(int index) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    return super.boundingBox();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getBoundingSphere(int)
   */
  @Override
  public Sphere getBoundingSphere(int index) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    return super.boundingSphere();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getNumPrimitives()
   */
  @Override
  public int getNumPrimitives() {
    return 1;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#getSurfaceArea(int)
   */
  @Override
  public double getSurfaceArea(int index) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    return super.getSurfaceArea();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    intersect(ray, recorder);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    super.intersect(ray, new IntersectionRecorderDecorator(recorder) {
      public void record(Intersection intersection) {
        inner.record(new IntersectionDecorator(intersection) {
          protected void transformShadingContext(
              ShadingContext context) {
            context.setPrimitiveIndex(0);
          }
        });
      }
    });
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersects(int, ca.eandb.jmist.math.Box3)
   */
  @Override
  public boolean intersects(int index, Box3 box) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    for (int i = 0, n = super.getNumPrimitives(); i < n; i++) {
      if (super.intersects(i, box)) {
        return true;
      }
    }
    return false;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#visibility(int, ca.eandb.jmist.math.Ray3)
   */
  @Override
  public boolean visibility(int index, Ray3 ray) {
    if (index != 0) {
      throw new IllegalArgumentException("index out of range.");
    }
    return super.visibility(ray);
  }

}
