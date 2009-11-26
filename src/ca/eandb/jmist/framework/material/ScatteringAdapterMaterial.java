/**
 * 
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.ScatteringStrategy;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.ScatteredRay.Type;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class ScatteringAdapterMaterial implements Material {
	
	/** Serialization version ID. */
	private static final long serialVersionUID = -3988915073092568279L;

	private final Material inner;
	
	private final ScatteringStrategy strategy;
	
	private final double weight;
	
	/**
	 * @param inner
	 * @param strategy
	 * @param weight
	 */
	public ScatteringAdapterMaterial(Material inner, ScatteringStrategy strategy, double weight) {
		this.inner = inner;
		this.strategy = strategy;
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
			WavelengthPacket lambda) {
		return inner.bsdf(x, in, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emission(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color emission(SurfacePoint x, Vector3 out, WavelengthPacket lambda) {
		return inner.emission(x, out, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#emit(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	public ScatteredRay emit(SurfacePoint x, WavelengthPacket lambda,
			double ru, double rv, double rj) {
		if (inner.isEmissive()) {
			double w = weight * strategy.getWeight(x, lambda);
			SeedReference ref = new SeedReference(rj);
			if (RandomUtil.bernoulli(w, ref)) {
				ScatteredRay sr = strategy.emit(x, lambda, ru, rv, ref.seed);
				if (sr != null) {
					if (sr.getType() == Type.SPECULAR) {
						return ScatteredRay.select(sr, w);
					} else {
						Ray3 ray = sr.getRay();
						Vector3 v = ray.direction();
						double pdf = w * sr.getPDF() + (1.0 - w) * inner.getEmissionPDF(x, v, lambda);
						Color edf = inner.emission(x, sr.getRay().direction(), lambda);
						return new ScatteredRay(ray, edf.divide(pdf), sr.getType(), pdf, sr.isTransmitted());	
					}
				}
			} else {
				ScatteredRay sr = inner.emit(x, lambda, ru, rv, ref.seed);
				if (sr != null) {
					if (sr.getType() == Type.SPECULAR) {
						return ScatteredRay.select(sr, 1.0 - w);
					} else {
						Ray3 ray = sr.getRay();
						Vector3 v = ray.direction();
						double pdf = w * strategy.getEmissionPDF(x, v, lambda) + (1.0 - w) * sr.getPDF();
						Color edf = inner.emission(x, sr.getRay().direction(), lambda);
						return new ScatteredRay(ray, edf.divide(pdf), sr.getType(), pdf, sr.isTransmitted());							
					}
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getEmissionPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public double getEmissionPDF(SurfacePoint x, Vector3 out,
			WavelengthPacket lambda) {
		if (inner.isEmissive()) {
			double w = weight * strategy.getWeight(x, lambda);
			return w * strategy.getEmissionPDF(x, out, lambda)
					+ (1.0 - w) * inner.getEmissionPDF(x, out, lambda);
		}
		return 0.0;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda) {
		double w = weight * strategy.getWeight(x, lambda);
		return w * strategy.getScatteringPDF(x, in, out, adjoint, lambda)
				+ (1.0 - w) * inner.getScatteringPDF(x, in, out, adjoint, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#isEmissive()
	 */
	public boolean isEmissive() {
		return inner.isEmissive();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Material#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {
		double w = weight * strategy.getWeight(x, lambda);
		SeedReference ref = new SeedReference(rj);
		if (RandomUtil.bernoulli(w, ref)) {
			ScatteredRay sr = strategy.scatter(x, v, adjoint, lambda, ru, rv, ref.seed);
			if (sr != null) {
				if (sr.getType() == Type.SPECULAR) {
					return ScatteredRay.select(sr, w);
				} else {
					Ray3 ray = sr.getRay();
					Vector3 r = ray.direction();
					Vector3 in = adjoint ? r.opposite() : v;
					Vector3 out = adjoint ? v.opposite() : r;
					double pdf = w * sr.getPDF() + (1.0 - w) * inner.getScatteringPDF(x, v, r, adjoint, lambda);
					Color bsdf = inner.bsdf(x, in, out, lambda);
					return new ScatteredRay(ray, bsdf.divide(pdf), sr.getType(), pdf, sr.isTransmitted());	
				}
			}
		} else {
			ScatteredRay sr = inner.scatter(x, v, adjoint, lambda, ru, rv, ref.seed);
			if (sr != null) {
				if (sr.getType() == Type.SPECULAR) {
					return ScatteredRay.select(sr, 1.0 - w);
				} else {
					Ray3 ray = sr.getRay();
					Vector3 r = ray.direction();
					Vector3 in = adjoint ? r.opposite() : v;
					Vector3 out = adjoint ? v.opposite() : r;
					double pdf = w * strategy.getScatteringPDF(x, v, r, adjoint, lambda) + (1.0 - w) * sr.getPDF();
					Color bsdf = inner.bsdf(x, in, out, lambda);
					return new ScatteredRay(ray, bsdf.divide(pdf), sr.getType(), pdf, sr.isTransmitted());							
				}
			}
		}
	
		
		
//		if (RandomUtil.bernoulli(weight, ref)) {
//			ScatteredRay sr = strategy.scatter(x, v, adjoint, lambda, ru, rv, ref.seed);
//			if (sr == null) {
//				return null;
//			}
//			Color bsdf = inner.bsdf(x, v, sr.getRay().direction(), lambda);
//			double pdf = sr.getPDF();
//			return new ScatteredRay(sr.getRay(), bsdf.divide(pdf), sr.getType(), pdf, sr.isTransmitted());
//		} else {
//			ScatteredRay sr = inner.scatter(x, v, adjoint, lambda, ru, rv, ref.seed);
//			return sr != null ? ScatteredRay.select(sr, 1.0 - weight) : null;
//		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return inner.extinctionIndex(p, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return inner.refractiveIndex(p, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	public Color transmittance(Ray3 ray, double distance,
			WavelengthPacket lambda) {
		return inner.transmittance(ray, distance, lambda);
	}

}
