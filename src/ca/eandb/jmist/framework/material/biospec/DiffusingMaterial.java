/**
 *
 */
package ca.eandb.jmist.framework.material.biospec;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>SurfaceScatterer</code> representing a Lambertian surface with unit
 * reflectance.
 * @see <a href="http://en.wikipedia.org/wiki/Lambertian_reflectance">Lambertian reflectance (wikipedia)</a>
 * @author Brad Kimmel
 */
public final class DiffusingMaterial extends OpaqueMaterial {

	/** Serialization version ID. */
	private static final long serialVersionUID = -7231770766590876956L;

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {

		Vector3 N = x.getNormal();

		if (v.dot(N) < 0.0) {
			N = N.opposite();
		}

		return ScatteredRay.transmitDiffuse(new Ray3(x.getPosition(),
				RandomUtil.diffuse(ru, rv).toCartesian(Basis3.fromW(N))),
				lambda.getColorModel().getWhite(lambda), 1.0);
	}

}
