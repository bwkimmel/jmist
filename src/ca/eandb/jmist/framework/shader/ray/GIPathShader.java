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

package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.gi.EmissionNode;
import ca.eandb.jmist.framework.gi.EyeNode;
import ca.eandb.jmist.framework.gi.ScatteringNode;
import ca.eandb.jmist.math.Ray3;

/**
 * @author brad
 *
 */
public final class GIPathShader implements RayShader {

	private final int numLightSamples;

	private final Light light;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.RayShader#shadeRay(ca.eandb.jmist.math.Ray3)
	 */
	@Override
	public Color shadeRay(Ray3 ray) {




	}

	private EmissionNode sampleLight(ScatteringNode s) {
		return null;
	}

	private Color shadeLight(ScatteringNode s) {
		EmissionNode e = sampleLight(s);
		if (e != null) {

		}
	}

	private Color shadeLights(ScatteringNode s) {
		Color score = ColorModel.getInstance().getBlack();
		if (numLightSamples > 0) {
			for (int i = 0; i < numLightSamples; i++) {
				score = score.plus(shadeLight(s));
			}
			score = score.divide(numLightSamples);
		}
		return score;
	}

	private Color source(ScatteringNode n) {
		if (n.getDepth() <= 1) {
			return n.getSourceRadiance();
		} else {
			return ColorModel.getInstance().getBlack();
		}
	}

	public Color shadePixel(EyeNode e) {
		ScatteringNode n = e.expand();
		Color score = ColorModel.getInstance().getBlack();
		while (n != null) {
			score = score.plus(source(n));
			score = score.plus(shadeLights(n));
			n = n.expand();
		}
		return score;
	}


}
