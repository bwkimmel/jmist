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
package ca.eandb.jmist.framework.light;

import java.io.Serializable;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.LightTerminalNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.HPoint3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Light</code> that emits from a single point.
 * @author Brad Kimmel
 */
public final class PointLight extends AbstractLight implements Serializable {

	/**
	 * Creates a new <code>PointLight</code>.
	 * @param position The <code>Point3</code> where the light is to emit from.
	 * @param emittedPower The <code>Spectrum</code> representing the emitted
	 * 		power of the light.
	 * @param shadows A value indicating whether the light should be affected
	 * 		by shadows.
	 */
	public PointLight(Point3 position, Spectrum emittedPower, boolean shadows) {
		this.position = position;
		this.emittedPower = emittedPower;
		this.shadows = shadows;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#illuminate(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.Illuminable)
	 */
	public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, Illuminable target) {

		Vector3		lightIn			= x.getPosition().vectorTo(this.position);
		double		dSquared		= lightIn.squaredLength();

		lightIn = lightIn.divide(Math.sqrt(dSquared));

		double		ndotl			= x.getShadingNormal().dot(lightIn);
		double		attenuation		= Math.abs(ndotl) / (4.0 * Math.PI * dSquared);

		target.addLightSample(new PointLightSample(x, position, emittedPower.sample(lambda).times(attenuation), shadows));

	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#sample(ca.eandb.jmist.framework.path.PathInfo, double, double, double)
	 */
	public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
		return new Node(pathInfo, ru, rv, rj);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Light#getSamplePDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.path.PathInfo)
	 */
	public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
		return 0;
	}

	private final class Node extends LightTerminalNode {

		public Node(PathInfo pathInfo, double ru, double rv, double rj) {
			super(pathInfo, ru, rv, rj);
		}

		public double getCosine(Vector3 v) {
			return 1.0;
		}

		public double getPDF() {
			return 1.0;
		}

		public HPoint3 getPosition() {
			return position;
		}

		public boolean isSpecular() {
			return true;
		}

		public ScatteredRay sample(double ru, double rv, double rj) {
			Vector3 v = RandomUtil.uniformOnSphere(ru, rv).toCartesian();
			Ray3 ray = new Ray3(position, v);
			return ScatteredRay.diffuse(ray, sample(emittedPower),
					1.0 / (4.0 * Math.PI));
		}

		public Color scatter(Vector3 v) {
			return sample(emittedPower).divide(4.0 * Math.PI);
		}

		public double getPDF(Vector3 v) {
			return 1.0 / (4.0 * Math.PI);
		}

	}

	/** The <code>Point3</code> where the light is to emit from. */
	private final Point3 position;

	/** The emission <code>Spectrum</code> of the light. */
	private final Spectrum emittedPower;

	/** A value indicating whether the light should be affected by shadows. */
	private final boolean shadows;

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = -5220350307274318220L;

}
