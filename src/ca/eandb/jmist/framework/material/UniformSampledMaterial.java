/**
 * 
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A decorator <code>Material</code> that samples the underlying
 * <code>Material</code> uniformly about each hemisphere.
 * 
 * @author Brad Kimmel
 */
public final class UniformSampledMaterial implements Material {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = 7213197062895581035L;
	
	private final Material inner;
	
	private final double reflectance;
	
	public UniformSampledMaterial(Material inner, double reflectance) {
		this.inner = inner;
		this.reflectance = reflectance;
	}
	
	public UniformSampledMaterial(Material inner) {
		this(inner, 1.0);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
			WavelengthPacket lambda) {
		return inner.bsdf(x, in, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
		return inner.emission(x, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda,
			double ru, double rv, double rj) {
		Vector3 r = RandomUtil.diffuse(ru, rv).toCartesian(x.getBasis());
		Ray3 ray = new Ray3(x.getPosition(), r);
		double pdf = 1.0 / Math.PI;
		Color color = inner.emission(x, r, lambda).divide(pdf);
		return ScatteredRay.diffuse(ray, color, pdf);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getEmissionPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public double getEmissionPDF(SurfacePoint x, Vector3 out,
			WavelengthPacket lambda) {
		return inner.getEmissionPDF(x, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda) {
		return 1.0 / Math.PI;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	@Override
	public boolean isEmissive() {
		return inner.isEmissive();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {
		SeedReference ref = new SeedReference(ru);
		boolean reflect = RandomUtil.bernoulli(reflectance, ref);
		Vector3 r = RandomUtil.diffuse(ref.seed, rv).toCartesian(x.getBasis());
		double pdf = 1.0 / Math.PI;
		
		if (!reflect) {
			r = r.opposite();
			pdf *= (1.0 - reflectance);
		} else {
			pdf *= reflectance;
		}
		
		Ray3 ray = new Ray3(x.getPosition(), r);
		Color color = inner.bsdf(x, v, r, lambda).divide(pdf);
		return ScatteredRay.diffuse(ray, color, pdf);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return inner.extinctionIndex(p, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return inner.refractiveIndex(p, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color transmittance(Ray3 ray, double distance,
			WavelengthPacket lambda) {
		return inner.transmittance(ray, distance, lambda);
	}

}
