/*
 * Copyright (c) 2008 Bradley W. Kimmel
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

import ca.eandb.jmist.framework.IntersectionRecorder;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.math.Box3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;

/**
 * @author brad
 *
 */
public abstract class SceneElementDecorator implements SceneElement {

	private final SceneElement inner;

	/**
	 * @param inner
	 */
	public SceneElementDecorator(SceneElement inner) {
		this.inner = inner;
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.Bounded3#boundingBox()
	 */
	public Box3 boundingBox() {
		return inner.boundingBox();
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.Bounded3#boundingSphere()
	 */
	public Sphere boundingSphere() {
		return inner.boundingSphere();
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint()
	 */
	public SurfacePoint generateRandomSurfacePoint() {
		return inner.generateRandomSurfacePoint();
	}

	/**
	 * @param index
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#generateRandomSurfacePoint(int)
	 */
	public SurfacePoint generateRandomSurfacePoint(int index) {
		return inner.generateRandomSurfacePoint(index);
	}

	/**
	 * @param index
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingBox(int)
	 */
	public Box3 getBoundingBox(int index) {
		return inner.getBoundingBox(index);
	}

	/**
	 * @param index
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#getBoundingSphere(int)
	 */
	public Sphere getBoundingSphere(int index) {
		return inner.getBoundingSphere(index);
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#getNumPrimitives()
	 */
	public int getNumPrimitives() {
		return inner.getNumPrimitives();
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea()
	 */
	public double getSurfaceArea() {
		return inner.getSurfaceArea();
	}

	/**
	 * @param index
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#getSurfaceArea(int)
	 */
	public double getSurfaceArea(int index) {
		return inner.getSurfaceArea(index);
	}

	/**
	 * @param index
	 * @param ray
	 * @param recorder
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(int, ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(int index, Ray3 ray, IntersectionRecorder recorder) {
		inner.intersect(index, ray, recorder);
	}

	/**
	 * @param ray
	 * @param recorder
	 * @see ca.eandb.jmist.framework.SceneElement#intersect(ca.eandb.jmist.math.Ray3, ca.eandb.jmist.framework.IntersectionRecorder)
	 */
	public void intersect(Ray3 ray, IntersectionRecorder recorder) {
		inner.intersect(ray, recorder);
	}

	/**
	 * @param index
	 * @param box
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#intersects(int, ca.eandb.jmist.math.Box3)
	 */
	public boolean intersects(int index, Box3 box) {
		return inner.intersects(index, box);
	}

	/**
	 * @param index
	 * @param ray
	 * @param maximumDistance
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3, double)
	 */
	public boolean visibility(int index, Ray3 ray, double maximumDistance) {
		return inner.visibility(index, ray, maximumDistance);
	}

	/**
	 * @param index
	 * @param ray
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(int, ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(int index, Ray3 ray) {
		return inner.visibility(index, ray);
	}

	/**
	 * @param p
	 * @param q
	 * @return
	 * @see ca.eandb.jmist.framework.VisibilityFunction3#visibility(ca.eandb.jmist.math.Point3, ca.eandb.jmist.math.Point3)
	 */
	public boolean visibility(Point3 p, Point3 q) {
		return inner.visibility(p, q);
	}

	/**
	 * @param ray
	 * @param maximumDistance
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(ca.eandb.jmist.math.Ray3, double)
	 */
	public boolean visibility(Ray3 ray, double maximumDistance) {
		return inner.visibility(ray, maximumDistance);
	}

	/**
	 * @param ray
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#visibility(ca.eandb.jmist.math.Ray3)
	 */
	public boolean visibility(Ray3 ray) {
		return inner.visibility(ray);
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#createLight()
	 */
	public Light createLight() {
		return inner.createLight();
	}

	/**
	 * @return
	 * @see ca.eandb.jmist.framework.SceneElement#isEmissive()
	 */
	public boolean isEmissive() {
		return inner.isEmissive();
	}

}
