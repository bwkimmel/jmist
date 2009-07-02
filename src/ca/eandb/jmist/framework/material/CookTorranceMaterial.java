/**
 *
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Medium;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRayRecorder;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
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

	private final Spectrum n;

	private final Spectrum k;

	public CookTorranceMaterial(double m, Spectrum n, Spectrum k) {
		this.mSquared = m * m;
		this.n = n;
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random, ca.eandb.jmist.framework.ScatteredRayRecorder)
	 */
	@Override
	public void scatter(SurfacePoint x, Vector3 v, WavelengthPacket lambda, Random rng, ScatteredRayRecorder recorder) {
		// TODO Auto-generated method stub
		super.scatter(x, v, lambda, rng, recorder);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scattering(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color scattering(SurfacePoint x, Vector3 in, Vector3 out, WavelengthPacket lambda) {
		Vector3		E = in.opposite();
		Vector3		L = in;
		Vector3		H = E.plus(E).times(0.5).unit();
		Vector3		N = x.getShadingNormal();
		double		HdotN = H.dot(N);
		double		EdotH = E.dot(H);
		double		EdotN = E.dot(N);
		double		LdotN = L.dot(N);
		double		tanAlpha = Math.tan(Math.acos(HdotN));
		double		cos4Alpha = HdotN * HdotN * HdotN * HdotN;

		Medium		medium = x.getAmbientMedium();
		Color		n1 = medium.refractiveIndex(x.getPosition(), lambda);
		Color		k1 = medium.extinctionIndex(x.getPosition(), lambda);
		Color		n2 = n.sample(lambda);
		Color		k2 = k.sample(lambda);
		Color		F = Optics.reflectance(E, N, n1, k1, n2, k2);
		double		D = Math.exp(-(tanAlpha * tanAlpha / mSquared)) / (4.0 * mSquared * cos4Alpha);
		double		G = Math.min(1.0, Math.min(2.0 * HdotN * EdotN / EdotH, 2.0 * HdotN * LdotN / EdotH));

		return F.times(D * G / EdotN);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return k.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return n.sample(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color transmittance(Ray3 ray, double distance, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

}
