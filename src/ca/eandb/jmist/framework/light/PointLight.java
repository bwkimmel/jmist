/**
 *
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
import ca.eandb.jmist.framework.gi2.LightNode;
import ca.eandb.jmist.framework.gi2.LightTerminalNode;
import ca.eandb.jmist.framework.gi2.PathInfo;
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
	 * @param emission The <code>Spectrum</code> representing the emitted power
	 * 		of the light.
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
		double		attenuation		= ndotl / (4.0 * Math.PI * dSquared);

		target.addLightSample(new PointLightSample(x, position, emittedPower.sample(lambda).times(attenuation), shadows));

	}

	public LightNode sample(PathInfo pathInfo, Random rnd) {
		return new Node(pathInfo);
	}

	private final class Node extends LightTerminalNode {

		public Node(PathInfo pathInfo) {
			super(pathInfo);
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

		public ScatteredRay sample(Random rnd) {
			Vector3 v = RandomUtil.uniformOnSphere(rnd).toCartesian();
			Ray3 ray = new Ray3(position, v);
			return ScatteredRay.diffuse(ray, sample(emittedPower),
					1.0 / (4.0 * Math.PI));
		}

		public Color scatter(Vector3 v) {
			return sample(emittedPower).divide(4.0 * Math.PI);
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
