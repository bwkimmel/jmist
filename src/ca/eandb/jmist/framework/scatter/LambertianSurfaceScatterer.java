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
package ca.eandb.jmist.framework.scatter;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> representing a Lambertian surface.
 * @see <a href="http://en.wikipedia.org/wiki/Lambertian_reflectance">Lambertian reflectance (wikipedia)</a>
 * @author Brad Kimmel
 */
public final class LambertianSurfaceScatterer implements SurfaceScatterer {

	/** Serialization version ID. */
	private static final long serialVersionUID = 6806933821578898186L;

	/** The reflectance of the surface. */
	private final Function1 reflectance;

	/** The transmittance of the surface. */
	private final Function1 transmittance;

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code>.
	 * @param reflectance The reflectance of the surface.
	 * @param transmittance The transmittance of the surface.
	 */
	public LambertianSurfaceScatterer(Function1 reflectance, Function1 transmittance) {
		this.reflectance = reflectance;
		this.transmittance = transmittance;
	}

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code>.
	 * @param reflectance The reflectance of the surface.
	 */
	public LambertianSurfaceScatterer(Function1 reflectance) {
		this(reflectance, Function1.ZERO);
	}

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code>.
	 * @param reflectance The reflectance of the surface.
	 * @param transmittance The transmittance of the surface.
	 */
	public LambertianSurfaceScatterer(double reflectance, double transmittance) {
		this(new ConstantFunction1(reflectance), new ConstantFunction1(transmittance));
	}

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code>.
	 * @param reflectance The reflectance of the surface.
	 */
	public LambertianSurfaceScatterer(double reflectance) {
		this(new ConstantFunction1(reflectance));
	}

	/**
	 * Creates a new <code>LambertianSurfaceScatterer</code> with unit
	 * reflectance.
	 */
	public LambertianSurfaceScatterer() {
		this(Function1.ONE, Function1.ZERO);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
	 */
	public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
			boolean adjoint, double lambda, Random rnd) {

		double r = reflectance.evaluate(lambda);
		double u = rnd.next() - r;
		if (u < 0.0) {
			Vector3 out = RandomUtil.diffuse(rnd).toCartesian(x.getBasis());
			return (v.dot(x.getNormal()) < 0.0) ? out : out.opposite();
		}
		double t = transmittance.evaluate(lambda);
		u -= t;
		if (u < 0.0) {
			Vector3 out = RandomUtil.diffuse(rnd).toCartesian(x.getBasis());
			return (v.dot(x.getNormal()) < 0.0) ? out.opposite() : out;
		}

		return null;
	}

}
