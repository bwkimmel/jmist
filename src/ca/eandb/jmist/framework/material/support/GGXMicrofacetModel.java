/**
 * 
 */
package ca.eandb.jmist.framework.material.support;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 *
 */
public final class GGXMicrofacetModel implements IsotropicMicrofacetModel {

	/** Serialization version ID. */
	private static final long serialVersionUID = 4961148298787587229L;
	
	public static final GGXMicrofacetModel GROUND = new GGXMicrofacetModel(0.394);
	
	public static final GGXMicrofacetModel FROSTED = new GGXMicrofacetModel(0.454);
	
	public static final GGXMicrofacetModel ETCHED = new GGXMicrofacetModel(0.553);
	
	public static final GGXMicrofacetModel ANTIGLARE = new GGXMicrofacetModel(0.027);
	
	private final double alpha;
	
	public GGXMicrofacetModel(double alpha) {
		this.alpha = alpha;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel#getDistributionPDF(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public double getDistributionPDF(Vector3 m, Vector3 n) {
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel#getShadowingAndMasking(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public double getShadowingAndMasking(Vector3 in, Vector3 out, Vector3 m,
			Vector3 n) {
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel#sample(double, double)
	 */
	@Override
	public SphericalCoordinates sample(double ru, double rv) {
		return new SphericalCoordinates(Math.atan(alpha
				* Math.sqrt(ru / (1.0 - ru))), 2.0 * Math.PI * rv);
	}

}
