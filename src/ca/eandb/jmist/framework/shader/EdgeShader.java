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
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class EdgeShader implements Shader {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -2391452537161495498L;

	private final Spectrum edgeColor;

	private final Spectrum interiorColor;

	private final double threshold;

	/**
	 * @param edgeColor
	 * @param interiorColor
	 * @param threshold
	 */
	public EdgeShader(Spectrum edgeColor, Spectrum interiorColor, double threshold) {
		this.edgeColor = edgeColor;
		this.interiorColor = interiorColor;
		this.threshold = threshold;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
	 */
	public Color shade(ShadingContext sc) {
		WavelengthPacket lambda = sc.getWavelengthPacket();
		Vector3 n = sc.getNormal();
		Vector3 v = sc.getIncident().opposite();
		return Math.abs(n.dot(v)) < threshold ? edgeColor.sample(lambda) : interiorColor.sample(lambda);
	}

}