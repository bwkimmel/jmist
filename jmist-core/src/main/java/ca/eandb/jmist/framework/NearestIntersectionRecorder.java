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
package ca.eandb.jmist.framework;

import ca.eandb.jmist.math.Interval;
import ca.eandb.jmist.math.Ray3;

/**
 * An intersection recorder that only keeps the nearest intersection
 * recorded.
 * @author Brad Kimmel
 */
public final class NearestIntersectionRecorder implements IntersectionRecorder {

  /**
   * Creates a new <code>NearestIntersectionRecorder</code> that records
   * <code>Intersection</code>s with a non-negligible positive distance.
   */
  public NearestIntersectionRecorder() {
    this.interval = Interval.POSITIVE;
  }

  /**
   * Creates a new <code>NearestIntersectionRecorder</code> that records
   * <code>Intersection</code>s within the specified <code>Interval</code>.
   * @param interval The <code>Interval</code> within which to accept
   *     <code>Intersection</code>s.
   */
  public NearestIntersectionRecorder(Interval interval) {
    this.interval = interval;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.IntersectionRecorder#needAllIntersections()
   */
  public boolean needAllIntersections() {
    return false;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.IntersectionRecorder#interval()
   */
  public Interval interval() {
    return this.interval;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.IntersectionRecorder#record(ca.eandb.jmist.framework.Intersection)
   */
  public void record(Intersection intersection) {
    if (this.interval.contains(intersection.getDistance(), intersection.getTolerance())) {
      this.nearest = intersection;
      this.interval = new Interval(interval.minimum(), nearest.getDistance());
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.IntersectionRecorder#isEmpty()
   */
  public boolean isEmpty() {
    return (this.nearest == null);
  }

  /**
   * Gets the intersection with the smallest ray parameter that has
   * been recorded.
   * @return The nearest intersection that has been recorded.
   */
  public Intersection nearestIntersection() {
    return this.nearest;
  }

  /**
   * Computes the nearest intersection of a <code>Ray3</code> with a
   * <code>SceneElement</code>.
   * @param ray The <code>Ray3</code> to intersect with.
   * @param geometry The <code>SceneElement</code> to test for an intersection
   *     with.
   * @param index The index of the primitive to intersect the ray with.
   * @return The nearest <code>Intersection</code>, or <code>null</code> if
   *     none exists.
   */
  public static Intersection computeNearestIntersection(Ray3 ray, SceneElement geometry, int index) {
    Interval I = new Interval(0.0, ray.limit());
    NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);
    geometry.intersect(index, ray, recorder);
    return recorder.nearestIntersection();
  }

  /**
   * Computes the nearest intersection of a <code>Ray3</code> with a
   * <code>SceneElement</code>.
   * @param ray The <code>Ray3</code> to intersect with.
   * @param geometry The <code>SceneElement</code> to test for an intersection
   *     with.
   * @return The nearest <code>Intersection</code>, or <code>null</code> if
   *     none exists.
   */
  public static Intersection computeNearestIntersection(Ray3 ray, SceneElement geometry) {
    Interval I = new Interval(0.0, ray.limit());
    NearestIntersectionRecorder recorder = new NearestIntersectionRecorder(I);
    geometry.intersect(ray, recorder);
    return recorder.nearestIntersection();
  }

  /** The nearest intersection that has been recorded so far. */
  private Intersection nearest = null;

  /**
   * The <code>Interval</code> within which to accept
   * <code>Intersection</code>s.
   */
  private Interval interval;

}
