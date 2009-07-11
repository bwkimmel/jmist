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

import java.util.EnumSet;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * @author brad
 *
 */
public final class DistributionShader implements Shader {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -4906025758765502067L;

	private final EnumSet<ScatteredRay.Type> filter;

	public DistributionShader() {
		this(EnumSet.allOf(ScatteredRay.Type.class));
	}

	/**
	 * @param type
	 */
	public DistributionShader(ScatteredRay.Type type) {
		this(EnumSet.of(type));
	}

	/**
	 * @param filter
	 */
	public DistributionShader(EnumSet<ScatteredRay.Type> filter) {
		this.filter = filter;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
	 */
	@Override
	public Color shade(ShadingContext sc) {

		WavelengthPacket lambda = sc.getWavelengthPacket();
		Color result = sc.getColorModel().getBlack(lambda);
		ScatteredRays rays = sc.getScatteredRays();

		for (int i = 0; i < rays.size(); i++) {
			ScatteredRay ray = rays.get(i);
			if (filter.contains(ray.getType())) {
				result = result.plus(sc.castRay(ray));
			}
		}

		return result;

	}

}
