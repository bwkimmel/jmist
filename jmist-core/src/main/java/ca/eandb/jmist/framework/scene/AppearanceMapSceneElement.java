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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.light.AbstractLight;
import ca.eandb.jmist.framework.light.PointLightSample;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.path.SurfaceLightNode;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.ByteArray;

/**
 * @author Brad
 *
 */
public final class AppearanceMapSceneElement extends SceneElementDecorator {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 280928578847394472L;

  private ByteArray map = new ByteArray();

  private List<Appearance> app = new ArrayList<Appearance>();

  private HashMap<String, Integer> nameLookup = null;

  public AppearanceMapSceneElement(SceneElement inner) {
    super(inner);
  }

  private static final class Appearance implements Serializable {
    
    /** Serialization version ID. */
    private static final long serialVersionUID = 1004659206467144573L;
    
    public final Material material;
    public final Shader shader;

    public Appearance(Material material, Shader shader) {
      this.material = material;
      this.shader = shader;
    }
    
  }

  private class AppearanceIntersectionRecorder extends IntersectionRecorderDecorator {

    /**
     * @param inner
     */
    public AppearanceIntersectionRecorder(IntersectionRecorder inner) {
      super(inner);
    }

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.IntersectionRecorderDecorator#record(ca.eandb.jmist.framework.Intersection)
     */
    @Override
    public void record(Intersection intersection) {
      inner.record(new IntersectionDecorator(intersection) {
        protected void transformShadingContext(ShadingContext context) {
          applyAppearance(context);
        }
      });
    }

  }

  public int addAppearance(Material material, Shader shader) {
    if (app.size() >= 256) {
      throw new IllegalStateException("Material/Shader map is full");
    }
    int key = app.size();
    app.add(new Appearance(material, shader));
    return key;
  }

  public AppearanceMapSceneElement addAppearance(String key, Material material, Shader shader) {
    if (nameLookup == null) {
      nameLookup = new HashMap<String, Integer>();
    }
    nameLookup.put(key, addAppearance(material, shader));
    return this;
  }

  public void setAppearance(int primitive, int key) {
    setAppearanceRange(primitive, 1, key);
  }

  public void setAppearanceRange(int start, int length, int key) {
    if (key < 0 || key >= app.size()) {
      throw new IllegalArgumentException();
    }
    if (map.size() < start + length) {
      map.resize(start + length);
    }
    for (int i = 0; i < length; i++) {
      map.set(start++, (byte) key);
    }
  }

  public AppearanceMapSceneElement setAppearanceRange(int start, int length, String name) {
    if (nameLookup == null || !nameLookup.containsKey(name)) {
      throw new IllegalArgumentException();
    }
    int key = nameLookup.get(name);
    setAppearanceRange(start, length, key);
    return this;
  }

  public AppearanceMapSceneElement setAppearance(int primitive, String name) {
    return setAppearanceRange(primitive, 1, name);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    super.intersect(index, ray, new AppearanceIntersectionRecorder(recorder));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
   */
  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    super.intersect(ray, new AppearanceIntersectionRecorder(recorder));
  }

  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv, double rj) {
    double weight = super.generateImportanceSampledSurfacePoint(index, x, context, ru, rv, rj);
    applyAppearance(context);
    return weight;
  }

  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    double weight = super.generateImportanceSampledSurfacePoint(x, context, ru, rv, rj);
    applyAppearance(context);
    return weight;
  }

  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
    super.generateRandomSurfacePoint(index, context, ru, rv, rj);
    applyAppearance(context);
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru, double rv, double rj) {
    super.generateRandomSurfacePoint(context, ru, rv, rj);
    applyAppearance(context);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scene.SceneElementDecorator#createLight()
   */
  @Override
  public Light createLight() {

    int numPrim = super.getNumPrimitives();
    ArrayList<Integer> emissive = new ArrayList<Integer>();
    for (int i = 0; i < numPrim; i++) {
      if (lookup(i).material.isEmissive()) {
        emissive.add(i);
      }
    }

    if (emissive.isEmpty()) {
      return null;
    }

    final int[] primIndex = new int[emissive.size()];
    final double[] weight = new double[emissive.size()];
    double totalSurfaceArea = 0.0;

    for (int i = 0; i < emissive.size(); i++) {
      primIndex[i] = emissive.get(i);
      weight[i] = super.getSurfaceArea(primIndex[i]); // TODO Factor in radiant exitance of material
      totalSurfaceArea += weight[i];
    }

    final double scale = totalSurfaceArea / emissive.size();
    final double totalWeight = totalSurfaceArea;

    final CategoricalRandom rnd = new CategoricalRandom(weight);

    return new AbstractLight() {

      private static final long serialVersionUID = -8364217558705142738L;

      public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target) {
        ShadingContext context = new MinimalShadingContext(rng);

        int index = rnd.next(rng);
        int primitive = primIndex[index];

        generateImportanceSampledSurfacePoint(primitive, x, context, rng.next(), rng.next(), rng.next());
        context.getModifier().modify(context);

        Point3 p = context.getPosition();
        Material mat = context.getMaterial();
        Vector3 v = x.getPosition().unitVectorFrom(p);
        Vector3 n = context.getShadingNormal();
        double d2 = x.getPosition().squaredDistanceTo(p);
        double atten = Math.max(n.dot(v), 0.0) * totalWeight / (4.0 * Math.PI * d2);
        Color ri = mat.emission(context, v, lambda).times(atten);
        LightSample sample = new PointLightSample(x, p, ri);

        target.addLightSample(sample);
      }

      public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
        ShadingContext context = new MinimalShadingContext(null);

        SeedReference ref = new SeedReference(rj);
        int index = rnd.next(ref);
        int primitive = primIndex[index];

        generateRandomSurfacePoint(primitive, context, ru, rv, ref.seed);
        context.getModifier().modify(context);

        return ScaledLightNode.create(1.0 / totalWeight,
            new SurfaceLightNode(pathInfo, context, ru, rv, ref.seed), rj);
      }

      public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
        return 1.0 / totalWeight;
      }

    };
  }

  private Appearance lookup(int primIndex) {
    if (primIndex >= 0 && primIndex < map.size()) {
      int matIndex = (int) map.get(primIndex);
      if (0 <= matIndex && matIndex < app.size()) {
        return app.get(matIndex);
      }
    }
    return null;
  }

  /**
   * @param context
   */
  private void applyAppearance(ShadingContext context) {
    int primIndex = context.getPrimitiveIndex();
    Appearance a = lookup(primIndex);
    if (a != null) {
      context.setMaterial(a.material);
      context.setShader(a.shader);
    }
  }


}
