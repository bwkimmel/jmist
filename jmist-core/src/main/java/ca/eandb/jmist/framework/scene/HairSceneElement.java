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

import java.util.Random;

import ca.eandb.jmist.framework.Bounded3;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.random.RandomAdapter;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.GeometryUtil;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Plane3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * A <code>SceneElement</code> decorator that adds hair to the underlying
 * geometry.
 * @author Brad Kimmel
 */
public final class HairSceneElement implements SceneElement {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7426131707749501794L;

  private final SceneElement emitter;

  private final Material hairMaterial;

  private final int base;

  private final int amount;

  private final int segments;

  private final Vector3 meanInitialVelocity;

  private final double randomInitialVelocity;

  private final double roughness;

  private final boolean renderEmitter;

  private final double baseWidth;

  private final double tipWidth;

  public interface Builder1 {
    Builder setEmitter(SceneElement emitter);
  }

  public static final class Builder implements Builder1 {
    private SceneElement emitter = null;
    private Material hairMaterial = null;
    private int amount = 1000;//500000;
    private int segments = 4;
    private Vector3 meanInitialVelocity = new Vector3(0.0, 0.0, 0.2);//0.05);
    private double randomInitialVelocity = 0.02;
    private double roughness = 0.05;//0.01;
    private boolean renderEmitter = true;
    private double baseWidth = 0.01;
    private double tipWidth = 0.01;

    private Builder() {}

    public Builder setEmitter(SceneElement emitter) {
      this.emitter = emitter;
      return this;
    }

    public Builder setHairMaterial(Material hairMaterial) {
      this.hairMaterial = hairMaterial;
      return this;
    }

    public Builder setAmount(int amount) {
      this.amount = amount;
      return this;
    }

    public Builder setSegments(int segments) {
      this.segments = segments;
      return this;
    }

    public Builder setMeanInitialVelocity(Vector3 meanInitialVelocity) {
      this.meanInitialVelocity = meanInitialVelocity;
      return this;
    }

    public Builder setRandomInitialVelocity(double randomInitialVelocity) {
      this.randomInitialVelocity = randomInitialVelocity;
      return this;
    }

    public Builder setRoughness(double roughness) {
      this.roughness = roughness;
      return this;
    }

    public Builder setRenderEmitter(boolean renderEmitter) {
      this.renderEmitter = renderEmitter;
      return this;
    }

    public Builder setBaseWidth(double baseWidth) {
      this.baseWidth = baseWidth;
      return this;
    }

    public Builder setTipWidth(double tipWidth) {
      this.tipWidth = tipWidth;
      return this;
    }

    public HairSceneElement build() {
      return new HairSceneElement(emitter, hairMaterial, amount, segments,
          meanInitialVelocity, randomInitialVelocity, roughness, renderEmitter,
          baseWidth, tipWidth);
    }
  }

  public static Builder1 newBuilder() {
    return new Builder();
  }

  private HairSceneElement(SceneElement emitter, Material hairMaterial,
      int amount, int segments, Vector3 meanInitialVelocity,
      double randomInitialVelocity, double roughness, boolean renderEmitter,
      double baseWidth, double tipWidth) {
    this.emitter = emitter;
    this.hairMaterial = hairMaterial;
    this.amount = amount;
    this.segments = segments;
    this.meanInitialVelocity = meanInitialVelocity;
    this.randomInitialVelocity = randomInitialVelocity;
    this.roughness = roughness;
    this.renderEmitter = renderEmitter;
    this.baseWidth = baseWidth;
    this.tipWidth = tipWidth;
    this.base = renderEmitter ? emitter.getNumPrimitives() : 0;
  }

  private class Strand implements Bounded3 {

    private Point3[] vertices;

    private ShadingContext emitterContext;

    private int strandIndex;

    public Box3 boundingBox() {
      BoundingBoxBuilder3 bound = new BoundingBoxBuilder3();
      for (Point3 vertex : vertices) {
        bound.add(vertex);
      }
      return bound.getBoundingBox();
    }

    public Sphere boundingSphere() {
      Box3 box = boundingBox();
      return new Sphere(box.center(), box.diagonal() / 2.0);
    }

    public void intersect(final Ray3 ray, IntersectionRecorder recorder) {
      for (int i = 0; i < vertices.length - 3; i++) {
        final double t = GeometryUtil.rayIntersectTriangle(ray, vertices[i], vertices[i+1], vertices[i+2]);
        final int vertexIndex = i;
        if (!Double.isNaN(t)) {
          recorder.record(new Intersection() {
            public double getDistance() {
              return t;
            }
            public double getTolerance() {
              return MathUtil.SMALL_EPSILON;
            }
            public boolean isFront() {
              return true;
            }
            public void prepareShadingContext(ShadingContext context) {
              Plane3 plane = Plane3.throughPoints(vertices[vertexIndex], vertices[vertexIndex+1], vertices[vertexIndex+2]);
              Vector3 n = plane.normal();
              Vector3 v = ray.direction();
              context.setPosition(ray.pointAt(t));
              context.setNormal(v.dot(n) > 0.0 ? n.opposite() : n);
              context.setMaterial(hairMaterial != null ? hairMaterial : emitterContext.getMaterial());
              context.setModifier(emitterContext.getModifier());
              context.setPrimitiveIndex(base + strandIndex);
              context.setShader(emitterContext.getShader());
              context.setUV(emitterContext.getUV());
              context.setAmbientMedium(emitterContext.getAmbientMedium());
            }

          });
        }
      }
    }

    public boolean visibility(Ray3 ray) {
      for (int i = 0; i < vertices.length - 3; i++) {
        final double t = GeometryUtil.rayIntersectTriangle(ray, vertices[i], vertices[i+1], vertices[i+1]);
        if (!Double.isNaN(t) && t > MathUtil.SMALL_EPSILON) {
          return false;
        }
      }
      return true;
    }

    public boolean intersects(Box3 box) {
      throw new UnimplementedException();
    }

  };

  private Strand createStrand(int index) {
    Random tempRnd = new Random(index);
    Random rnd = new Random(tempRnd.nextLong());
    RandomAdapter adapter = new RandomAdapter(rnd);
    MinimalShadingContext context = new MinimalShadingContext();
    emitter.generateRandomSurfacePoint(context, rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble());
    Strand strand = new Strand();
    strand.vertices = new Point3[2 * (segments + 1)];
    strand.emitterContext = context;
    strand.strandIndex = index;

    Point3 pos = context.getPosition();
    Vector3 vel = context.getBasis().toStandard(meanInitialVelocity).plus(
        RandomUtil.uniformInsideSphere(randomInitialVelocity, adapter)
            .toCartesian());
    double dt = 1.0 / segments;
    double orientation = 2.0 * Math.PI * rnd.nextDouble();
    double co = Math.cos(orientation);
    double so = Math.sin(orientation);
    Basis3 basis = Basis3.fromWU(vel, context.getTangent());

    int segment = 0;
    int i = 0;
    while (true) {
      double t = (double) segment / (double) segments;
      double width = MathUtil.interpolate(baseWidth, tipWidth, t);

      strand.vertices[i++] = pos.plus(basis.toStandard(-0.5 * width * co, -0.5 * width * so, 0.0));
      strand.vertices[i++] = pos.plus(basis.toStandard(0.5 * width * co, 0.5 * width * so, 0.0));

      if (++segment > segments) {
        break;
      }

      pos = pos.plus(vel.times(dt));
      pos = pos.plus(RandomUtil.uniformInsideSphere(roughness, adapter).toCartesian());
    }

    return strand;
  }

  @Override
  public Light createLight() {
    throw new UnsupportedOperationException();
  }

  @Override
  public double generateImportanceSampledSurfacePoint(int index,
      SurfacePoint x, ShadingContext context, double ru, double rv,
      double rj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public double generateImportanceSampledSurfacePoint(SurfacePoint x,
      ShadingContext context, double ru, double rv, double rj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context,
      double ru, double rv, double rj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void generateRandomSurfacePoint(ShadingContext context, double ru,
      double rv, double rj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Box3 getBoundingBox(int index) {
    return index < base ? emitter.getBoundingBox(index) : createStrand(index - base).boundingBox();
  }

  @Override
  public Sphere getBoundingSphere(int index) {
    return index < base ? emitter.getBoundingSphere(index) : createStrand(index - base).boundingSphere();
  }

  @Override
  public int getNumPrimitives() {
    return base + amount;
  }

  @Override
  public double getSurfaceArea() {
    throw new UnsupportedOperationException();
  }

  @Override
  public double getSurfaceArea(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    if (index < base) {
      emitter.intersect(index, ray, recorder);
    } else {
      createStrand(index - base).intersect(ray, recorder);
    }
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    // Warning: EXTREMELY SLOW - ALWAYS combine with an accelerator
    for (int i = 0; i < amount; i++) {
      intersect(base + i, ray, recorder);
    }
    if (renderEmitter) {
      emitter.intersect(ray, recorder);
    }
  }

  @Override
  public boolean intersects(int index, Box3 box) {
    return index < base ? emitter.intersects(index, box) : createStrand(
        index - base).intersects(box);
  }

  @Override
  public boolean visibility(int index, Ray3 ray) {
    return index < base ? emitter.visibility(index, ray) : createStrand(
        index - base).visibility(ray);
  }

  @Override
  public Box3 boundingBox() {
    return emitter.boundingBox().expand(this.meanInitialVelocity.length() + randomInitialVelocity + tipWidth / 2.0);
  }

  @Override
  public Sphere boundingSphere() {
    return emitter.boundingSphere().expand(this.meanInitialVelocity.length() + randomInitialVelocity + tipWidth / 2.0);
  }

  @Override
  public boolean visibility(Ray3 ray) {
    // Warning: EXTREMELY SLOW - ALWAYS combine with an accelerator
    for (int i = 0, n = amount; i < n; i++) {
      if (!visibility(base + i, ray)) {
        return false;
      }
    }
    return renderEmitter ? emitter.visibility(ray) : true;
  }

}
