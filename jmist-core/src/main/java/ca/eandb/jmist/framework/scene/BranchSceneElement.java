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
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.light.AbstractLight;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.util.UnimplementedException;

/**
 * A composite <code>SceneElement</code>.  Each child <code>SceneElement</code>
 * is treated as a single primitive.
 * @author Brad Kimmel
 */
public final class BranchSceneElement implements SceneElement {

  /** Serialization version ID. */
  private static final long serialVersionUID = -8500645819577622768L;

  private final List<SceneElement> children = new ArrayList<SceneElement>();

  private CategoricalRandom rnd = null;

  public BranchSceneElement addChild(SceneElement child) {
    children.add(child);
    return this;
  }

  private synchronized void buildChildSelector() {
    if (rnd != null) {
      return;
    }

    double[] weight = new double[children.size()];
    for (int i = 0; i < weight.length; i++) {
      weight[i] = children.get(i).getSurfaceArea();
    }
    rnd = new CategoricalRandom(weight);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#createLight()
   */
  public Light createLight() {
    final List<Light> lights = new ArrayList<Light>();
    Light light = null;
    int numLights = 0;
    for (SceneElement child : children) {
      light = child.createLight();
      lights.add(light);
      if (light != null) {
        numLights++;
      }
    }
    final int lightCount = numLights;
    switch (numLights) {
    case 0:
      return null;
    case 1:
      return light;
    default:
      return new AbstractLight() {

        /** Serialization version ID. */
        private static final long serialVersionUID = 6299798465595032610L;

        @Override
        public double getSamplePDF(final SurfacePoint x, final PathInfo pathInfo) {
          return lights.get(x.getPrimitiveIndex()).getSamplePDF(x, pathInfo) / (double) lightCount;
        }

        @Override
        public void illuminate(SurfacePoint x, WavelengthPacket lambda,
            Random rnd, Illuminable target) {
          throw new UnimplementedException();
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

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(int, ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
   */
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
    double weight = children.get(index).generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
    context.setPrimitiveIndex(index);
    return weight;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateImportanceSampledSurfacePoint(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.ShadingContext)
   */
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    if (rnd == null) {
      buildChildSelector();
    }
    SeedReference ref = new SeedReference(rj);
    int index = rnd.next(ref);
    return generateImportanceSampledSurfacePoint(index, x, context, ru, rv, ref.seed);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int, ca.eandb.jmist.framework.ShadingContext)
   */
  public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
    children.get(index).generateRandomSurfacePoint(context, ru, rv, rj);
    context.setPrimitiveIndex(index);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(ca.eandb.jmist.framework.ShadingContext)
   */
  public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
    if (rnd == null) {
      buildChildSelector();
    }
    SeedReference ref = new SeedReference(rj);
    int index = rnd.next(ref);
    generateRandomSurfacePoint(index, context, ru, rv, ref.seed);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
   */
  public Box3 getBoundingBox(int index) {
    return children.get(index).boundingBox();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
   */
  public Sphere getBoundingSphere(int index) {
    return children.get(index).boundingSphere();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
   */
  public int getNumPrimitives() {
    return children.size();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
   */
  public double getSurfaceArea(int index) {
    return children.get(index).getSurfaceArea();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
   */
  public double getSurfaceArea() {
    double area = 0.0;
    for (SceneElement child : children) {
      area += child.getSurfaceArea();
    }
    return area;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
    children.get(index).intersect(ray, new IntersectionRecorderDecorator(recorder) {
      public void record(Intersection intersection) {
        inner.record(new IntersectionDecorator(intersection) {
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
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    for (int i = 0; i < children.size(); i++) {
      intersect(i, ray, recorder);
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
   */
  public boolean intersects(int index, Box3 box) {
    SceneElement child = children.get(index);
    int n = child.getNumPrimitives();
    for (int i = 0; i < n; i++) {
      if (child.intersects(i, box)) {
        return true;
      }
    }
    return false;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
   */
  public boolean visibility(int index, Ray3 ray) {
    return children.get(index).visibility(ray);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.SceneElement#visibility(ca.eandb.jmist.math.Ray3)
   */
  public boolean visibility(Ray3 ray) {
    for (int i = 0; i < children.size(); i++) {
      if (!visibility(i, ray)) {
        return false;
      }
    }
    return true;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
   */
  public Box3 boundingBox() {
    BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
    for (SceneElement child : children) {
      builder.add(child.boundingBox());
    }
    return builder.getBoundingBox();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
   */
  public Sphere boundingSphere() {
    Box3 boundingBox = boundingBox();
    return new Sphere(boundingBox.center(), boundingBox.diagonal() / 2.0);
  }

}
