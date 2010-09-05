/**
 * 
 */
package ca.eandb.jmist.framework.material;

import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.math.Optics;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class EtchedGlassMaterial extends AbstractMaterial {
	
	/** Serialization version ID */
	private static final long serialVersionUID = -2137062689589252464L;

	private final double n1 = 1.0;
	
	private final double n2 = 1.5;
	
	private final double alpha = 0.454;
	

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
		
		/* Eq. (20) */
		return fresnel(in, hr) * shadowAndMask(in, out, hr, n) * distribution(hr, n) / (4.0 * Math.abs(ndoti * ndoto));
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
		double d = ni * hdoti + no * hdoto;
		
		/* Eq. (21) */
		return k * no * no * (1.0 - fresnel(in, ht)) * shadowAndMask(in, out, ht, n) * distribution(ht, n) / (d * d);		
	}
	
	private double fresnel(Vector3 in, Vector3 n) {
		return Optics.reflectance(in, n1, n2, n);
//		double c = Math.abs(in.dot(n));
//		double det = (nt * nt) / (ni * ni) - 1.0 + c * c;
//		
//		if (det < 0.0) {
//			return 1.0;
//		}
//		
//		double g = Math.sqrt(det);
//		double gmc = g - c;
//		double gpc = g + c;
//		double a = c * gpc - 1.0;
//		double b = c * gmc + 1.0;
//		double f = 0.5 * ((gmc * gmc) / (gpc * gpc)) * (1.0 + ((a * a) / (b * b)));
//		
//		return f;
	}
	
	private double shadowAndMask(Vector3 in, Vector3 out, Vector3 m, Vector3 n) {
		double mdoti = -m.dot(in);
		double ndoti = -n.dot(in);
		double mdoto = m.dot(out);
		double ndoto = n.dot(out);
		
		if (mdoti / ndoti < 0.0 || mdoto / ndoto < 0.0) {
			return 0.0;
		}
		
		double a2 = alpha * alpha;
		double ti = Math.tan(Math.acos(ndoti));
		double t2i = ti * ti;
		double to = Math.tan(Math.acos(ndoto));
		double t2o = to * to;

		return 4.0 / ((1.0 + Math.sqrt(1.0 + a2 * t2i)) * (1.0 + Math.sqrt(1.0 + a2 * t2o)));
	}
	
	private double distribution(Vector3 m, Vector3 n) {
		double mdotn = m.dot(n);
		if (mdotn < 0.0) {
			return 0.0;
		}
		
		double a2 = alpha * alpha;
		double c4 = mdotn * mdotn * mdotn * mdotn;
		double t = Math.tan(Math.acos(mdotn));
		double t2 = t * t;
		double a2pt2 = a2 + t2;
		
		return a2 / (Math.PI * c4 * a2pt2 * a2pt2);
	}
	
	private SphericalCoordinates sampleMicrofacetNormal(double x1, double x2) {
		return new SphericalCoordinates(Math.atan(alpha
				* Math.sqrt(x1 / (1.0 - x1))), 2.0 * Math.PI * x2);
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
			double partial = 1.0 / (4.0 * Math.abs(hr.dot(out)));
			double pm = distribution(hr, n) * Math.abs(hr.dot(n));
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
			double d = ni * hdoti + no * hdoto;
			
			double partial = no * no * Math.abs(hdoto) / (d * d);
			double pm = distribution(ht, n) * Math.abs(ht.dot(n));
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
		Vector3 m = sampleMicrofacetNormal(ru, rv).toCartesian(x.getBasis());
		double mdoti = Math.abs(m.dot(v));
		double ndoti = Math.abs(n.dot(v));
		double mdotn = Math.abs(m.dot(n));
		
		double ni = -v.dot(n) >= 0.0 ? n1 : n2;
		double nt = -v.dot(n) >= 0.0 ? n2 : n1;
		
		double r = fresnel(v, m.dot(n) > 0.0 ? m : m.opposite());
		boolean reflected = RandomUtil.bernoulli(r, rj);
		Vector3 out = reflected ? Optics.reflect(v, m) : Optics.refract(v, n1, n2, m);
		
		double weight = (mdoti / (ndoti * mdotn)) * shadowAndMask(v, out, m, n);
		double pdf = getScatteringPDF(x, v, out, adjoint, lambda);
		
		return new ScatteredRay(new Ray3(x.getPosition(), out), lambda.getColorModel().getGray(weight, lambda), ScatteredRay.Type.GLOSSY, pdf, !reflected);
		
	}
	
	

}
