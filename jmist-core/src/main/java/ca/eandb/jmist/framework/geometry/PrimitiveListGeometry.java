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

import ca.eandb.jmist.framework.BoundingBoxBuilder3;
import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionDecorator;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.IntersectionRecorderDecorator;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

public final class PrimitiveListGeometry extends AbstractGeometry {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7468724116325274242L;

  private final List<PrimitiveGeometry> primitives = new ArrayList<>();

  public PrimitiveListGeometry addPrimitive(PrimitiveGeometry primitive) {
    primitives.add(primitive);
    return this;
  }

  @Override
  public Box3 getBoundingBox(int index) {
    return primitives.get(index).boundingBox();
  }

  @Override
  public Sphere getBoundingSphere(int index) {
    return primitives.get(index).boundingSphere();
  }

  @Override
  public int getNumPrimitives() {
    return primitives.size();
  }

  @Override
  public void intersect(final int index, Ray3 ray, IntersectionRecorder recorder) {
    primitives.get(index).intersect(ray, new IntersectionRecorderDecorator(recorder) {
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

  @Override
  public Box3 boundingBox() {
    BoundingBoxBuilder3 builder = new BoundingBoxBuilder3();
    for (PrimitiveGeometry primitive : primitives) {
      builder.add(primitive.boundingBox());
    }
    return builder.getBoundingBox();
  }

  @Override
  public Sphere boundingSphere() {
    Box3 box = boundingBox();
    return new Sphere(box.center(), 0.5 * box.diagonal());
  }

  @Override
  public double getSurfaceArea(int index) {
    return primitives.get(index).getSurfaceArea();
  }

  @Override
  public void generateRandomSurfacePoint(int index, ShadingContext context, double ru, double rv, double rj) {
    primitives.get(index).generateRandomSurfacePoint(context, ru, rv, rj);
    context.setPrimitiveIndex(index);
  }

}
