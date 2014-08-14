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

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.util.UnimplementedException;

/**
 * A <code>SceneElement</code> consisting of a range of primitives from another
 * <code>SceneElement</code>.
 * @author Brad Kimmel
 */
public class RangeSceneElement implements SceneElement {
  
  /** Serialization version ID. */
  private static final long serialVersionUID = 6345176989327302632L;

  private final SceneElement inner;
  
  private final int offset;
  
  private final int size;
  
  private double surfaceArea = Double.NaN;
  
  private Box3 bbox = null;
  
  public RangeSceneElement(int offset, int size, SceneElement inner) {
    this.inner = inner;
    this.offset = offset;
    this.size = size;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#createLight()
   */
  @Override
  public Light createLight() {
    throw new UnimplementedException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv,
      double rj) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    double weight = inner.generateImportanceSampledSurfacePoint(offset + index, x, context, ru, rv, rj);
    context.setPrimitiveIndex(index);
    return weight;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    double weight = inner.generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
    int index = context.getPrimitiveIndex();
    if (index >= offset && index - offset < size) {
      return weight;
    }
    return -1.0;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context,
      double ru, double rv, double rj) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    inner.generateRandomSurfacePoint(offset + index, context, ru, rv, rj);
    context.setPrimitiveIndex(index);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext, double, double, double)
   */
  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru,
      double rv, double rj) {
    throw new UnimplementedException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
   */
  @Override
  public Box3 getBoundingBox(int index) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return inner.getBoundingBox(offset + index);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
   */
  @Override
  public Sphere getBoundingSphere(int index) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return inner.getBoundingSphere(offset + index);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
   */
  @Override
  public int getNumPrimitives() {
    return size;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
   */
  @Override
  public double getSurfaceArea(int index) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return inner.getSurfaceArea(offset + index);
  }
  
  private synchronized void computeSurfaceArea() {
    if (Double.isNaN(surfaceArea)) {
      double area = 0.0;
      for (int i = 0, j = offset; i < size; i++, j++) {
        area += inner.getSurfaceArea(j);
      }
      surfaceArea = area;
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
   */
  @Override
  public double getSurfaceArea() {
    if (Double.isNaN(surfaceArea)) {
      computeSurfaceArea();
    }
    return surfaceArea;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    inner.intersect(offset + index, ray, new IntersectionRecorderDecorator(recorder) {
      @Override
      public void record(Intersection intersection) {
        inner.record(new IntersectionDecorator(intersection) {
          @Override
          protected void transformShadingContext(
              ShadingContext context) {
            context.setPrimitiveIndex(index);
          }
        });
      }
    });
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    recorder = new IntersectionRecorderDecorator(recorder) {

      @Override
      public void record(Intersection intersection) {
        inner.record(new IntersectionDecorator(intersection) {

          @Override
          protected void transformShadingContext(
              ShadingContext context) {
            context.setPrimitiveIndex(context.getPrimitiveIndex() - offset);
          }
          
        });
      }
      
    };
    for (int i = 0, j = offset; i < size; i++, j++) {
      inner.intersect(j, ray, recorder);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
   */
  @Override
  public boolean intersects(int index, Box3 box) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return inner.intersects(offset + index, box);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
   */
  @Override
  public boolean visibility(int index, Ray3 ray) {
    if (index < 0 || index >= size) {
      throw new IllegalArgumentException("index out of bounds");
    }
    return inner.visibility(offset + index, ray);
  }
  
  private synchronized void computeBoundingBox() {
    if (bbox == null) {
      BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
      for (int i = 0, j = offset; i < size; i++, j++) {
        builder.add(inner.getBoundingBox(j));
      }
      bbox = builder.getBoundingBox();
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
   */
  @Override
  public Box3 boundingBox() {
    if (bbox == null) {
      computeBoundingBox();
    }
    return bbox;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
   */
  @Override
  public Sphere boundingSphere() {
    if (bbox == null) {
      computeBoundingBox();
    }
    return new Sphere(bbox.center(), 0.5 * bbox.diagonal());
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Ray3)
   */
  @Override
  public boolean visibility(Ray3 ray) {
    for (int i = 0, j = offset; i < size; i++, j++) {
      if (!inner.visibility(j, ray)) {
        return false;
      }
    }
    return true;
  }

}
