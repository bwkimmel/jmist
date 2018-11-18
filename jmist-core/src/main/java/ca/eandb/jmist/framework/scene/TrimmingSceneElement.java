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

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Mask2;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;

/**
 * A <code>SceneElement</code> decorator that trims the decorated
 * <code>SceneElement</code>.  Ray-intersection tests with the decorated
 * <code>SceneElement</code> only record a hit if the trimming
 * <code>Mask2</code> opacity is greater than <code>0.5</code>.
 * @author Brad Kimmel
 */
public final class TrimmingSceneElement extends SceneElementDecorator {

  /** Serialization version ID. */
  private static final long serialVersionUID = -208418976070459240L;

  /** The <code>Mask2</code> to trim with. */
  private final Mask2 trim;

  public TrimmingSceneElement(Mask2 trim, SceneElement inner) {
    super(inner);
    this.trim = trim;
  }

  @Override
  public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
    Interval I = recorder.interval();

    do {
      NearestIntersectionRecorder rec = new NearestIntersectionRecorder(I);
      super.intersect(index, ray, rec);

      if (rec.isEmpty()) {
        break;
      }
      Intersection x = rec.nearestIntersection();
      MinimalShadingContext ctx = new MinimalShadingContext();
      x.prepareShadingContext(ctx);
      Point2 uv = ctx.getUV();
      if (trim.opacity(new Point2(uv.x(), 1.0 - uv.y())) > 0.5) {
        recorder.record(x);
        if (!recorder.needAllIntersections()) {
          break;
        }
      }
      ray = ray.advance(x.getDistance());
      I = new Interval(0, I.maximum() - x.getDistance());
    } while (true);
  }

  @Override
  public void intersect(Ray3 ray, IntersectionRecorder recorder) {
    Interval I = recorder.interval();

    do {
      NearestIntersectionRecorder rec = new NearestIntersectionRecorder(I);
      super.intersect(ray, rec);

      if (rec.isEmpty()) {
        break;
      }
      Intersection x = rec.nearestIntersection();
      MinimalShadingContext ctx = new MinimalShadingContext();
      x.prepareShadingContext(ctx);
      Point2 uv = ctx.getUV();
      if (trim.opacity(new Point2(uv.x(), 1.0 - uv.y())) > 0.5) {
        recorder.record(x);
        if (!recorder.needAllIntersections()) {
          break;
        }
      }
      ray = ray.advance(x.getDistance());
      I = new Interval(0, I.maximum() - x.getDistance());
    } while (true);
  }

}
