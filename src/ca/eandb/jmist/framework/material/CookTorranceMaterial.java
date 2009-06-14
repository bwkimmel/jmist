/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.IntersectionGeometry;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class CookTorranceMaterial extends AbstractMaterial {

	private final double mSquared;

	private final Color n;

	private final Color k;

	public CookTorranceMaterial(double m, Color n, Color k) {
		this.mSquared = m * m;
		this.n = n;
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.IntersectionGeometry, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void scatter(IntersectionGeometry x, ScatteredRayRecorder recorder) {
		// TODO Auto-generated method stub
		super.scatter(x, recorder);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scattering(ca.eandb.jmist.framework.IntersectionGeometry, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public Color scattering(IntersectionGeometry x, Vector3 in) {
		Vector3		E = x.incident().opposite();
		Vector3		L = in;
		Vector3		H = E.plus(E).times(0.5).unit();
		Vector3		N = x.shadingNormal();
		double		HdotN = H.dot(N);
		double		EdotH = E.dot(H);
		double		EdotN = E.dot(N);
		double		LdotN = L.dot(N);
		double		tanAlpha = Math.tan(Math.acos(HdotN));
		double		cos4Alpha = HdotN * HdotN * HdotN * HdotN;

		Color		n1 = x.ambientMedium().refractiveIndex(x.location());
		Color		k1 = x.ambientMedium().extinctionIndex(x.location());
		Color		F = Optics.reflectance(E, N, n1, k1, n, k);
		double		D = Math.exp(-(tanAlpha * tanAlpha / mSquared)) / (4.0 * mSquared * cos4Alpha);
		double		G = Math.min(1.0, Math.min(2.0 * HdotN * EdotN / EdotH, 2.0 * HdotN * LdotN / EdotH));

		return F.times(D * G / EdotN);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public Color extinctionIndex(Point3 p) {
		return k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3)
	 */
	@Override
	public Color refractiveIndex(Point3 p) {
		return n;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double)
	 */
	@Override
	public Color transmittance(Ray3 ray, double distance) {
		return ColorModel.getInstance().getBlack();
	}

}
