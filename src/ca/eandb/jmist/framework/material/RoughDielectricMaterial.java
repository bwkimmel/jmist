/**
 * 
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * A <code>Material</code> representing a dielectric material having a rough
 * surface, based on:
 * 
 * B. Walter, S.R. Marschner, H. Li, K.E. Torrance, "Microfacet models for
 * reflection through rough surfaces", In proceedings of Eurographics Symposium
 * on Rendering, 2007.
 * 
 * @author Brad Kimmel
 */
public final class RoughDielectricMaterial extends AbstractMaterial {
	
	/** Serialization version ID */
	private static final long serialVersionUID = -2137062689589252464L;

	private final double n1;
	
	private final double n2;
	
	private final IsotropicMicrofacetModel microfacets;

	public RoughDielectricMaterial(double n2, IsotropicMicrofacetModel microfacets) {
		this(1.0, n2, microfacets);
	}

	public RoughDielectricMaterial(double n1, double n2, IsotropicMicrofacetModel microfacets) {
		this.n1 = n1;
		this.n2 = n2;
		this.microfacets = microfacets;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#extinctionIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color extinctionIndex(Point3 p, WavelengthPacket lambda) {
		return lambda.getColorModel().getBlack(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#refractiveIndex(ca.eandb.jmist.math.Point3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color refractiveIndex(Point3 p, WavelengthPacket lambda) {
		return lambda.getColorModel().getGray(n2, lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Medium#transmittance(ca.eandb.jmist.math.Ray3, double, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color transmittance(Ray3 ray, double distance,
			WavelengthPacket lambda) {
		return lambda.getColorModel().getWhite(lambda);
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#bsdf(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public Color bsdf(SurfacePoint x, Vector3 in, Vector3 out,
			WavelengthPacket lambda) {				
		return lambda.getColorModel().getGray(bsdf(x, in, out), lambda);
	}
	
	public double bsdf(SurfacePoint x, Vector3 in, Vector3 out) {
		Vector3 n = x.getNormal();
		double ndoti = -n.dot(in);
		double ndoto = n.dot(out);
		
		return (ndoti > 0.0) == (ndoto > 0.0)
				? brdf(x, in, out, n) 
				: btdf(x, in, out, n);
	}
	
	private double brdf(SurfacePoint x, Vector3 in, Vector3 out, Vector3 n) {
		double ndoti = -n.dot(in);
		double ndoto = n.dot(out);
		
		/* Eq. (13) */
		Vector3 hr = out.minus(in).times(Math.signum(ndoti)).unit();
		hr = hr.dot(n) > 0.0 ? hr : hr.opposite();
		
		double g = microfacets.getShadowingAndMasking(in, out, hr, n);
		double d = microfacets.getDistributionPDF(hr, n);
		double f = Optics.reflectance(in, n1, n2, hr);
		
		/* Eq. (20) */
		return f * g * d / (4.0 * Math.abs(ndoti * ndoto));
	}

	private double btdf(SurfacePoint x, Vector3 in, Vector3 out, Vector3 n) {
		double ndoti = -n.dot(in);
		double ni, no;
		
		if (ndoti > 0.0) {
			ni = n1;
			no = n2;
		} else {
			ni = n2;
			no = n1;
		}
		
		/* Eq. (16) */
		Vector3 ht = in.times(ni).minus(out.times(no)).unit();
		ht = ht.dot(n) > 0.0 ? ht : ht.opposite();
		
		double hdoti = -ht.dot(in);
		double hdoto = ht.dot(out);
		double ndoto = n.dot(out);
		
		double k = Math.abs(hdoti * hdoto / (ndoti * ndoto));
		double c = ni * hdoti + no * hdoto;
		
		double f = Optics.reflectance(in, n1, n2, ht);
		double g = microfacets.getShadowingAndMasking(in, out, ht, n);
		double d = microfacets.getDistributionPDF(ht, n);
		
		/* Eq. (21) */
		return k * no * no * (1.0 - f) * g * d / (c * c);		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#getScatteringPDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket)
	 */
	@Override
	public double getScatteringPDF(SurfacePoint x, Vector3 in, Vector3 out,
			boolean adjoint, WavelengthPacket lambda) {
		Vector3 n = x.getNormal();
		double ndoti = -n.dot(in);
		double ndoto = n.dot(out);
		boolean reflected = (ndoti / ndoto) > 0.0;
		
		if (reflected) {
			Vector3 hr = out.minus(in).times(Math.signum(ndoti)).unit();
			hr = hr.dot(n) > 0.0 ? hr : hr.opposite();

			double partial = 1.0 / (4.0 * Math.abs(hr.dot(out)));
			double d = microfacets.getDistributionPDF(hr, n);
			double pm = d * Math.abs(hr.dot(n));
			double po = pm * partial;
			return po;// / Math.abs(n.dot(out));
		} else {
			double ni, no;
			
			if (ndoti > 0.0) {
				ni = n1;
				no = n2;
			} else {
				ni = n2;
				no = n1;
			}

			//Vector3 ht = out.times(no).minus(in.times(ni)).unit();
			Vector3 ht = in.times(ni).minus(out.times(no)).unit();
			ht = ht.dot(n) > 0.0 ? ht : ht.opposite();
			
			double hdoti = -ht.dot(in);
			double hdoto = ht.dot(out);
			
			double k = Math.abs(hdoti * hdoto / (ndoti * ndoto));
			double c = ni * hdoti + no * hdoto;
			
			double partial = no * no * Math.abs(hdoto) / (c * c);
			double d = microfacets.getDistributionPDF(ht, n);
			double pm = d * Math.abs(ht.dot(n));
			double po = pm * partial;
			return po;// / Math.abs(n.dot(out));			
		}
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
	 */
	@Override
	public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
			WavelengthPacket lambda, double ru, double rv, double rj) {
		
		Vector3 n = x.getNormal();
		Vector3 m = microfacets.sample(ru, rv).toCartesian(x.getBasis());
		double mdoti = Math.abs(m.dot(v));
		double ndoti = Math.abs(n.dot(v));
		double mdotn = Math.abs(m.dot(n));
		
		double ni = -v.dot(n) >= 0.0 ? n1 : n2;
		double nt = -v.dot(n) >= 0.0 ? n2 : n1;
		
		double r = Optics.reflectance(v, n1, n2, m.dot(n) > 0.0 ? m : m.opposite());
		boolean reflected = RandomUtil.bernoulli(r, rj);
		Vector3 out = reflected ? Optics.reflect(v, m) : Optics.refract(v, n1, n2, m);
		
		double g = microfacets.getShadowingAndMasking(v, out, m, n);
		double weight = (mdoti / (ndoti * mdotn)) * g;
		double pdf = getScatteringPDF(x, v, out, adjoint, lambda);
		
		return new ScatteredRay(new Ray3(x.getPosition(), out), lambda.getColorModel().getGray(weight, lambda), ScatteredRay.Type.GLOSSY, pdf, !reflected);
		
	}
}
