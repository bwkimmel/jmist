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

package ca.eandb.jmist.framework.shader;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.PathContext;
import ca.eandb.jmist.framework.RayCaster;
import ca.eandb.jmist.framework.RenderContext;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Interval;

/**
 * @author brad
 *
 */
public final class DistanceShader implements Shader {

	private final Interval distanceInterval;

	private final Color minDistanceColor;

	private final Color maxDistanceColor;

	private final Color nearColor;

	private final Color farColor;

	/**
	 * @param distanceInterval
	 * @param minDistanceColor
	 * @param maxDistanceColor
	 * @param nearColor
	 * @param farColor
	 */
	public DistanceShader(Interval distanceInterval, Color minDistanceColor, Color maxDistanceColor,
			Color nearColor, Color farColor) {
		this.distanceInterval = distanceInterval;
		this.minDistanceColor = minDistanceColor;
		this.maxDistanceColor = maxDistanceColor;
		this.nearColor = nearColor;
		this.farColor = farColor;
	}

	/**
	 * @param distanceInterval
	 * @param minDistanceColor
	 * @param maxDistanceColor
	 */
	public DistanceShader(Interval distanceInterval, Color minDistanceColor, Color maxDistanceColor) {
		this(distanceInterval, minDistanceColor, maxDistanceColor, minDistanceColor, maxDistanceColor);
	}

	/**
	 * @param distanceInterval
	 */
	public DistanceShader(Interval distanceInterval) {
		this(distanceInterval, ColorModel.getInstance().getWhite(), ColorModel.getInstance().getBlack());
	}

	/**
	 * @param maxDistance
	 */
	public DistanceShader(double maxDistance) {
		this(new Interval(0, maxDistance), ColorModel.getInstance().getWhite(), ColorModel.getInstance().getBlack());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.Intersection, ca.eandb.jmist.framework.RayCaster, ca.eandb.jmist.framework.ScatteredRays, ca.eandb.jmist.framework.PathContext, ca.eandb.jmist.framework.RenderContext)
	 */
	@Override
	public Color shade(Intersection x, RayCaster caster, ScatteredRays rays,
			PathContext pc, RenderContext rc) {

		double d = x.distance();
		if (distanceInterval.contains(d)) {
			double t = (d - distanceInterval.minimum()) / distanceInterval.length();
			return minDistanceColor.times(1.0 - t).plus(maxDistanceColor.times(t));
		} else if (d < distanceInterval.minimum()) {
			return nearColor;
		} else {
			return farColor;
		}

	}

}
