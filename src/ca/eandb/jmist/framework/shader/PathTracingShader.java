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

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;

/**
 * A <code>Shader</code> that traces up to one randomly selected scattered ray.
 * @author Brad Kimmel
 */
public final class PathTracingShader implements Shader {

	/** Serialization version ID. */
	private static final long serialVersionUID = -3619786295095920623L;

	/** The default maximum path depth. */
	private static final int DEFAULT_MAX_DEPTH = 10;

	/** The maximum path depth. */
	private final int maxDepth;

	/**
	 * Creates a new <code>PathTracingShader</code>.
	 */
	public PathTracingShader() {
		this(DEFAULT_MAX_DEPTH);
	}

	/**
	 * Creates a new <code>PathTracingShader</code>.
	 * @param maxDepth The maximum path depth.
	 */
	public PathTracingShader(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
	 */
	public Color shade(ShadingContext sc) {

		if (sc.getPathDepth() < maxDepth) {
			ScatteredRay ray = sc.getScatteredRay();
			double prob = ColorUtil.getMeanChannelValue(ray.getColor());
			if (prob < 1.0) {
				if (RandomUtil.bernoulli(prob, sc.getRandom())) {
					ray = ScatteredRay.select(ray, prob);
				} else {
					ray = null;
				}
			}

			if (ray != null) {
				return sc.castRay(ray);
			}
		}

		WavelengthPacket lambda = sc.getWavelengthPacket();
		return sc.getColorModel().getBlack(lambda);

	}

}
