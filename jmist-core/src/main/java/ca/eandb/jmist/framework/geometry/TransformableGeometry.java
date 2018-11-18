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
package ca.eandb.jmist.framework.geometry;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.AffineTransformable3;
import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.InvertibleAffineTransformation3;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>CompositeGeometry</code> that may be transformed.
 * @author Brad Kimmel
 */
public final class TransformableGeometry extends AbstractGeometry implements
    AffineTransformable3 {

  /** Serialization version ID. */
  private static final long serialVersionUID = -1312285205292950959L;

  /** The <code>SceneElement</code> to be made transformable. */
  private final SceneElement geometry;

  /** The transformation to apply to this <code>SceneElement</code>. */
  private final InvertibleAffineTransformation3 model = new InvertibleAffineTransformation3();

  /**
   * Creates a new <code>TransformableGeometry</code>.
   * @param geometry The <code>SceneElement</code> to make transformable.
   */
  public TransformableGeometry(SceneElement geometry) {
    this.geometry = geometry;
  }

  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    ray = this.model.applyInverse(ray);
    recorder = new TransformedIntersectionRecorder(recorder);
    geometry.intersect(index, ray, recorder);
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    ray = this.model.applyInverse(ray);
    recorder = new TransformedIntersectionRecorder(recorder);
    geometry.intersect(ray, recorder);
  }

  @Override
  public Box3 boundingBox() {
    BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
    Box3 childBoundingBox = geometry.boundingBox();
    for (int i = 0; i < 8; i++) {
      builder.add(this.model.apply(childBoundingBox.corner(i)));
    }
    return builder.getBoundingBox();
  }

  @Override
  public Sphere boundingSphere() {
    List<Point3> corners = new ArrayList<>(8);
    Box3 childBoundingBox = geometry.boundingBox();
    for (int i = 0; i < 8; i++) {
      corners.add(this.model.apply(childBoundingBox.corner(i)));
    }
    return Sphere.smallestContaining(corners);
  }

  @Override
  public Box3 getBoundingBox(int index) {
    BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
    Box3 childBoundingBox = geometry.getBoundingBox(index);
    for (int i = 0; i < 8; i++) {
      builder.add(this.model.apply(childBoundingBox.corner(i)));
    }
    return builder.getBoundingBox();
  }

  @Override
  public Sphere getBoundingSphere(int index) {
    List<Point3> corners = new ArrayList<>(8);
    Box3 childBoundingBox = geometry.getBoundingBox(index);
    for (int i = 0; i < 8; i++) {
      corners.add(this.model.apply(childBoundingBox.corner(i)));
    }
    return Sphere.smallestContaining(corners);
  }

  @Override
  public int getNumPrimitives() {
    return geometry.getNumPrimitives();
  }

  /**
   * An <code>IntersectionRecorder</code> that transforms the recorded
   * intersections according to the transformation for this
   * <code>TransformableGeometry</code>.
   * @author Brad Kimmel
   */
  private final class TransformedIntersectionRecorder extends
      IntersectionRecorderDecorator {

    /**
     * Creates a new <code>TransformedIntersectionRecorder</code>.
     * @param inner The <code>IntersectionRecorder</code> to record
     *     intersections to.
     */
    public TransformedIntersectionRecorder(IntersectionRecorder inner) {
      super(inner);
    }

    @Override
    public void record(Intersection intersection) {
      this.inner.record(new TransformedIntersection(intersection));
    }

  }

  /**
   * An <code>Intersection</code> that has been transformed according to the
   * transformation applied to this <code>TransformableGeometry</code>.
   * @author Brad Kimmel
   */
  private final class TransformedIntersection extends IntersectionDecorator {

    /**
     * Creates a new <code>TransformedIntersection</code>.
     * @param local The <code>Intersection</code> in local coordinate
     *     space.
     */
    public TransformedIntersection(Intersection local) {
      super(local);
    }

    @Override
    protected void transformShadingContext(ShadingContext context) {
      context.setPosition(model.apply(context.getPosition()));
      context.setBasis(transformBasis(context.getBasis()));
      context.setShadingBasis(transformBasis(context.getShadingBasis()));
    }

    private Basis3 transformBasis(Basis3 basis) {
      return Basis3.fromUV(
          model.apply(basis.u()),
          model.apply(basis.v()),
          basis.orientation());
    }

  }

  @Override
  public void transform(AffineMatrix3 T) {
    this.model.transform(T);
  }

  @Override
  public void transform(LinearMatrix3 T) {
    this.model.transform(T);
  }

  @Override
  public void rotate(Vector3 axis, double angle) {
    this.model.rotate(axis, angle);
  }

  @Override
  public void rotateX(double angle) {
    this.model.rotateX(angle);
  }

  @Override
  public void rotateY(double angle) {
    this.model.rotateY(angle);
  }

  @Override
  public void rotateZ(double angle) {
    this.model.rotateZ(angle);
  }

  @Override
  public void translate(Vector3 v) {
    this.model.translate(v);
  }

  @Override
  public void scale(double c) {
    this.model.scale(c);
  }

  @Override
  public void stretch(Vector3 axis, double c) {
    this.model.stretch(axis, c);
  }

  @Override
  public void stretch(double cx, double cy, double cz) {
    this.model.stretch(cx, cy, cz);
  }

  @Override
  public void stretchX(double cx) {
    this.model.stretchX(cx);
  }

  @Override
  public void stretchY(double cy) {
    this.model.stretchY(cy);
  }

  @Override
  public void stretchZ(double cz) {
    this.model.stretchZ(cz);
  }

}
