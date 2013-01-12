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
package ca.eandb.jmist.framework.shader;

import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Interval;

/**
 * @author brad
 *
 */
public final class DistanceShader implements Shader {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -6744005479057812858L;

	private final Interval distanceInterval;

	private final Spectrum minDistanceValue;

	private final Spectrum maxDistanceValue;

	private final Spectrum nearValue;

	private final Spectrum farValue;

	/**
	 * @param distanceInterval
	 * @param minDistanceValue
	 * @param maxDistanceValue
	 * @param nearValue
	 * @param farValue
	 */
	public DistanceShader(Interval distanceInterval, Spectrum minDistanceValue, Spectrum maxDistanceValue,
			Spectrum nearValue, Spectrum farValue) {
		this.distanceInterval = distanceInterval;
		this.minDistanceValue = minDistanceValue;
		this.maxDistanceValue = maxDistanceValue;
		this.nearValue = nearValue;
		this.farValue = farValue;
	}

	/**
	 * @param distanceInterval
	 * @param minDistanceValue
	 * @param maxDistanceValue
	 */
	public DistanceShader(Interval distanceInterval, Spectrum minDistanceValue, Spectrum maxDistanceValue) {
		this(distanceInterval, minDistanceValue, maxDistanceValue, minDistanceValue, maxDistanceValue);
	}

	/**
	 * @param distanceInterval
	 */
	public DistanceShader(Interval distanceInterval) {
		this(distanceInterval, null, null);
	}

	/**
	 * @param maxDistance
	 */
	public DistanceShader(double maxDistance) {
		this(new Interval(0, maxDistance));
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
	 */
	public Color shade(ShadingContext sc) {

		WavelengthPacket lambda = sc.getWavelengthPacket();
		double d = sc.getDistance();
		if (distanceInterval.contains(d)) {
			double t = (d - distanceInterval.minimum()) / distanceInterval.length();
			Color ncol = getColorWhiteDefault(minDistanceValue, lambda);
			Color fcol = getColorBlackDefault(maxDistanceValue, lambda);
			return ncol.times(1.0 - t).plus(fcol.times(t));
		} else if (d < distanceInterval.minimum()) {
			return getColorWhiteDefault(nearValue, lambda);
		} else {
			return getColorBlackDefault(farValue, lambda);
		}

	}

	public Color getColorWhiteDefault(Spectrum s, WavelengthPacket lambda) {
		return (s != null) ? s.sample(lambda) : lambda.getColorModel().getWhite(lambda);
	}

	public Color getColorBlackDefault(Spectrum s, WavelengthPacket lambda) {
		return (s != null) ? s.sample(lambda) : lambda.getColorModel().getBlack(lambda);
	}

}
