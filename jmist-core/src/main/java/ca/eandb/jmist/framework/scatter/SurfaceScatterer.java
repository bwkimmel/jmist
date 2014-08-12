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

import java.io.Serializable;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Vector3;

/**
 * Represents the surface scattering properties of a material.
 *
 * @author Brad Kimmel
 */
public interface SurfaceScatterer extends Serializable {

	/**
	 * Simulates surface scattering.
	 * @param x The <code>SurfacePointGeometry</code> describing the geometry
	 * 		at the point on the surface to which the light is incident.
	 * @param v The <code>Vector3</code> indicating the incident direction.
	 * @param adjoint A value indicating whether ray tracing is proceeding
	 * 		backwards (i.e., from the eye).
	 * @param wavelength The wavelength of the incident light (in meters).
	 * @param rnd The <code>Random</code> number generator to use.
	 * @return A <code>Vector3</code> indicating the direction of scattering,
	 * 		or <code>null</code> if the ray is absorbed.
	 */
	Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
			double wavelength, Random rnd);

	/** A <code>SurfaceScatterer</code> that absorbs all incident light. */
	public static final SurfaceScatterer ABSORB = new SurfaceScatterer() {
		private static final long serialVersionUID = 8194517746654987095L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return null;
		}
	};

	/** A <code>SurfaceScatterer</code> that transmits all incident light. */
	public static final SurfaceScatterer TRANSMIT = new SurfaceScatterer() {
		private static final long serialVersionUID = -927167595504776971L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return v;
		}
	};

	/** A <code>SurfaceScatterer</code> that reflects all incident light. */
	public static final SurfaceScatterer REFLECT = new SurfaceScatterer() {
		private static final long serialVersionUID = 3125984180868998388L;
		public Vector3 scatter(SurfacePointGeometry x, Vector3 v,
				boolean adjoint, double wavelength, Random rnd) {
			return Optics.reflect(v, x.getNormal());
		}
	};

}
