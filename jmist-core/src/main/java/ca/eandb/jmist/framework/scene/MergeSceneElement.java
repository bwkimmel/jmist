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

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.SurfacePointDecorator;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.light.AbstractLight;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.util.IntegerArray;
import ca.eandb.util.UnimplementedException;

/**
 * A <code>SceneElement</code> that merges one or more child scene elements
 * into a single <code>SceneElement</code>.  The primitives of this
 * <code>SceneElement</code> consist of the primitives of each of the child
 * <code>SceneElement</code>s.
 * @author Brad Kimmel
 */
public final class MergeSceneElement implements SceneElement {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5677918761822356900L;

  private final List<SceneElement> children = new ArrayList<SceneElement>();

  private transient IntegerArray offsets = null;

  private double surfaceArea = Double.NaN;

  private Box3 bbox = null;

  public MergeSceneElement addChild(SceneElement child) {
    children.add(child);
    offsets = null;
    surfaceArea = Double.NaN;
    bbox = null;
    return this;
  }

  private synchronized void computeOffsets() {
    if (offsets != null) {
      return;
    }
    IntegerArray newOffsets = new IntegerArray();
    int offset = 0;
    for (int i = 0, n = children.size(); i < n; i++) {
      newOffsets.add(offset);
      offset += children.get(i).getNumPrimitives();
    }
    offsets = newOffsets;
  }

  private void checkOffsets() {
    if (offsets == null) {
      computeOffsets();
    }
  }

  private int getChildIndex(int primIndex) {
    int lo = 0;
    int hi = children.size() - 1;
    int m;
    do {
      m = lo + (hi - lo + 1) / 2;
      if (primIndex >= offsets.get(m)) {
        lo = m;
      } else {
        hi = m - 1;
      }
    } while (lo < hi);
    return lo;
  }

  @Override
  public Light createLight() {
    final List<Light> lights = new ArrayList<Light>();
    Light lastLight = null;
    int numLights = 0;
    for (SceneElement child : children) {
      Light light = child.createLight();
      lights.add(light);
      if (light != null) {
        lastLight = light;
        numLights++;
      }
    }
    final int lightCount = numLights;
    switch (numLights) {
    case 0:
      return null;
    case 1:
      return lastLight;
    default:
      return new AbstractLight() {

        /** Serialization version ID. */
        private static final long serialVersionUID = 6299798465595032610L;

        @Override
        public double getSamplePDF(final SurfacePoint x, final PathInfo pathInfo) {
          int index = MergeSceneElement.this.getChildIndex(x.getPrimitiveIndex());
          SurfacePoint sp = new SurfacePointDecorator(x) {
            @Override
            public int getPrimitiveIndex() {
              return super.getPrimitiveIndex() - offsets.get(index);
            }
          };
          return lights.get(index).getSamplePDF(sp, pathInfo) / (double) lightCount;
        }

        @Override
        public void illuminate(SurfacePoint x, WavelengthPacket lambda,
            Random rnd, Illuminable target) {
          int index = RandomUtil.discrete(0, lightCount - 1, rnd);
          for (int i = 0, n = lights.size(); i < n; i++) {
            Light light = lights.get(i);
            if (light != null) {
              if (index-- == 0) {
                light.illuminate(x, lambda, rnd, target);
                return;
              }
            }
          }
        }

        @Override
        public LightNode sample(PathInfo pathInfo, double ru,
            double rv, double rj) {
          SeedReference ref = new SeedReference(rj);
          int index = RandomUtil.discrete(0, lightCount - 1, ref);
          for (int i = 0, n = lights.size(); i < n; i++) {
            Light light = lights.get(i);
            if (light != null) {
              if (index-- == 0) {
                return ScaledLightNode.create(1.0 / (double) lightCount,
                    light.sample(pathInfo, ru, rv, ref.seed), rj);
              }
            }
          }
          return null;
        }

      };
    }
  }


  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv,
      double rj) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    double weight = children.get(childIndex).generateImportanceSampledSurfacePoint(childPrimIndex, x, context, ru, rv, rj);
    context.setPrimitiveIndex(index);
    return weight;
  }

  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    throw new UnimplementedException();
  }

  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context,
      double ru, double rv, double rj) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    children.get(childIndex).generateRandomSurfacePoint(childPrimIndex, context, ru, rv, rj);
    context.setPrimitiveIndex(index);
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru,
      double rv, double rj) {
    throw new UnimplementedException();
  }

  @Override
  public Box3 getBoundingBox(int index) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    return children.get(childIndex).getBoundingBox(childPrimIndex);
  }

  @Override
  public Sphere getBoundingSphere(int index) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    return children.get(childIndex).getBoundingSphere(childPrimIndex);
  }

  @Override
  public int getNumPrimitives() {
    checkOffsets();
    int numChildren = offsets.size();
    return numChildren > 0 ? offsets.get(numChildren - 1) + children.get(numChildren - 1).getNumPrimitives() : 0;
  }

  @Override
  public double getSurfaceArea(int index) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    return children.get(childIndex).getSurfaceArea(childPrimIndex);
  }

  private synchronized void computeSurfaceArea() {
    if (Double.isNaN(surfaceArea)) {
      double area = 0.0;
      for (SceneElement child : children) {
        area += child.getSurfaceArea();
      }
      surfaceArea = area;
    }
  }

  @Override
  public double getSurfaceArea() {
    if (Double.isNaN(surfaceArea)) {
      computeSurfaceArea();
    }
    return surfaceArea;
  }

  @Override
  public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    children.get(childIndex).intersect(childPrimIndex, ray, new IntersectionRecorderDecorator(recorder) {
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

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    checkOffsets();
    for (int i = 0, n = children.size(); i < n; i++) {
      final int offset = offsets.get(i);
      children.get(i).intersect(ray, new IntersectionRecorderDecorator(recorder) {
        @Override
        public void record(Intersection intersection) {
          inner.record(new IntersectionDecorator(intersection) {
            @Override
            protected void transformShadingContext(
                ShadingContext context) {
              context.setPrimitiveIndex(offset + context.getPrimitiveIndex());
            }
          });
        }
      });
    }
  }

  @Override
  public boolean intersects(int index, Box3 box) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    return children.get(childIndex).intersects(childPrimIndex, box);
  }

  @Override
  public boolean visibility(int index, Ray3 ray) {
    checkOffsets();
    int childIndex = getChildIndex(index);
    int childPrimIndex = index - offsets.get(childIndex);
    return children.get(childIndex).visibility(childPrimIndex, ray);
  }

  private synchronized void computeBoundingBox() {
    if (bbox == null) {
      BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
      for (SceneElement child : children) {
        builder.add(child.boundingBox());
      }
      bbox = builder.getBoundingBox();
    }
  }

  @Override
  public Box3 boundingBox() {
    if (bbox == null) {
      computeBoundingBox();
    }
    return bbox;
  }

  @Override
  public Sphere boundingSphere() {
    if (bbox == null) {
      computeBoundingBox();
    }
    return new Sphere(bbox.center(), 0.5 * bbox.diagonal());
  }

  @Override
  public boolean visibility(Ray3 ray) {
    for (SceneElement child : children) {
      if (!child.visibility(ray)) {
        return false;
      }
    }
    return true;
  }

}
