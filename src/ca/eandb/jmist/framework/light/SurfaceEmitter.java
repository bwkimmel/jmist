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

import ca.eandb.jmist.framework.Emitter;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Photon;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteredRays;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Sphere;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class SurfaceEmitter implements Emitter {

	private final SurfacePoint x;

	public SurfaceEmitter(SurfacePoint x) {
		this.x = x;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Emitter#emit(ca.eandb.jmist.math.Sphere, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Photon emit(Sphere target, WavelengthPacket lambda, Random rng) {
		Material material = x.getMaterial();
		assert(material.isEmissive());

		ScatteredRays src = new ScatteredRays(x, lambda, rng, material);
		ScatteredRay sr = src.getRandomScatteredRay(false);
		return new Photon(sr.getRay(), sr.getColor());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Emitter#getEmittedRadiance(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color getEmittedRadiance(Vector3 v, WavelengthPacket lambda) {
		boolean toFront = x.getNormal().dot(v) > 0.0;
		if (toFront) {
			double ndotv = x.getShadingNormal().dot(v);
			if (ndotv > 0.0) {
				return x.getMaterial().emission(x, v, lambda).times(ndotv);
			}
		}
		return lambda.getColorModel().getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Emitter#getRadiantExitance(ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color getRadiantExitance(WavelengthPacket lambda) {
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Emitter#isAtInfinity()
	 */
	public boolean isAtInfinity() {
		return false;
	}

}
