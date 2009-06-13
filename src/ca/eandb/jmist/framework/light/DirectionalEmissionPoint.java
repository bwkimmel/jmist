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

import ca.eandb.jmist.framework.EmissionPoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class DirectionalEmissionPoint implements EmissionPoint {

	private final Vector3 direction;

	private final Color radiance;

	private final Sphere sceneBoundingVolume;

	private DirectionalEmissionPoint(Vector3 direction, Color radiance, Sphere sceneBoundingVolume) {
		this.direction = direction;
		this.radiance = radiance;
		this.sceneBoundingVolume = sceneBoundingVolume;
	}

	public static DirectionalEmissionPoint create(Vector3 direction, Color radiance, Sphere sceneBoundingVolume) {
		return new DirectionalEmissionPoint(direction, radiance, sceneBoundingVolume);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#emit()
	 */
	@Override
	public Ray3 emit() {
		throw new UnexpectedException("not implemented");
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getEmittedRadiance(ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color getEmittedRadiance(Vector3 v) {
		return radiance;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#getRadiantExitance()
	 */
	@Override
	public Color getRadiantExitance() {
		return ColorModel.getInstance().getBlack();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.EmissionPoint#isAtInfinity()
	 */
	@Override
	public boolean isAtInfinity() {
		return true;
	}

}
