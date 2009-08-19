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

package ca.eandb.jmist.framework.gi;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class SurfaceNode extends AbstractScatteringNode implements ScatteringNode {

	private final Ray3 ray;

	private final MinimalShadingContext context = new MinimalShadingContext(Random.DEFAULT) {

		/* (non-Javadoc)
		 * @see ca.eandb.jmist.framework.shader.AbstractShadingContext#getImportance()
		 */
		@Override
		public Color getImportance() {
			return getValue();
		}

	};

	/* package */ SurfaceNode(Ray3 ray, Intersection x, Color value, PathNode parent) {
		super(value, parent);
		this.ray = ray;
		x.prepareShadingContext(context);
	}

	public Color getEmittedRadiance() {
		WavelengthPacket lambda = getValue().getWavelengthPacket();
		Material mat = context.getMaterial();
		Vector3 out = ray.direction().opposite();
		return mat.emission(context, out, lambda);
	}

	public ScatteringNode expand(Random rnd) {
		WavelengthPacket lambda = getValue().getWavelengthPacket();
		Material mat = context.getMaterial();
		Vector3 in = ray.direction();
		ScatteredRays scat = new ScatteredRays(context, in, lambda, rnd, mat);
		ScatteredRay sr = scat.getRandomScatteredRay(false);

		if (sr != null) {
			return trace(sr.getRay(), getValue().times(sr.getColor()));
		} else {
			return null;
		}
	}

	public Color scatter(Vector3 v) {
		WavelengthPacket lambda = getValue().getWavelengthPacket();
		Material mat = context.getMaterial();
		Vector3 in = ray.direction();
		Vector3 n = context.getShadingNormal();
		double dot = n.dot(v);

		return mat.scattering(context, in, v, lambda)
				.times(getValue())
				.times(Math.abs(dot));
	}

	public Point3 getPosition() {
		return context.getPosition();
	}

}
