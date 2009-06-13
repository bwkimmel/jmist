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

package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.DirectionalTexture3;
import ca.eandb.jmist.framework.EmissionPoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.texture.UniformDirectionalTexture3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class VolumeEmissionPoint implements EmissionPoint {

	private final Ray3 ray;

	private final Color value;

	private final DirectionalTexture3 emission;

	/**
	 * @param ray
	 * @param value
	 * @param emission
	 */
	private VolumeEmissionPoint(Ray3 ray, Color value,
			DirectionalTexture3 emission) {
		this.ray = ray;
		this.value = value;
		this.emission = emission;
	}

	public static VolumeEmissionPoint create(Ray3 ray, Color value, DirectionalTexture3 emission) {
		return new VolumeEmissionPoint(ray, value, emission);
	}

	public static VolumeEmissionPoint create(Ray3 ray, Color value, Color radiance) {
		return new VolumeEmissionPoint(ray, value, new UniformDirectionalTexture3(radiance));
	}

	public static VolumeEmissionPoint create(Ray3 ray, Color value) {
		return create(ray, value, value);
	}

	public static VolumeEmissionPoint create(Point3 position, Vector3 direction, Color value, DirectionalTexture3 emission) {
		return create(new Ray3(position, direction), value, emission);
	}

	public static VolumeEmissionPoint create(Point3 position, Vector3 direction, Color value, Color radiance) {
		return create(new Ray3(position, direction), value, radiance);
	}

	public static VolumeEmissionPoint create(Point3 position, Vector3 direction, Color value) {
		return create(position, direction, value, value);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getDirection()
	 */
	@Override
	public Vector3 getDirection() {
		return ray.direction();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getEmittedRadiance(ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color getEmittedRadiance(Vector3 v) {
		return emission.evaluate(v);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getPosition()
	 */
	@Override
	public Point3 getPosition() {
		return ray.origin();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getRay()
	 */
	@Override
	public Ray3 getRay() {
		return ray;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getValue()
	 */
	@Override
	public Color getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#isAtInfinity()
	 */
	@Override
	public boolean isAtInfinity() {
		return false;
	}

}
