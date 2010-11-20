/**
 * 
 */
package ca.eandb.jmist.framework.material.support;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class PhongMicrofacetModel implements IsotropicMicrofacetModel {

	/** Serialization version ID. */
	private static final long serialVersionUID = -5818588950337807120L;

	public static final PhongMicrofacetModel GROUND = new PhongMicrofacetModel(1.410);
	
	public static final PhongMicrofacetModel FROSTED = new PhongMicrofacetModel(1.162);
	
	public static final PhongMicrofacetModel ETCHED = new PhongMicrofacetModel(0.848);
	
	public static final PhongMicrofacetModel ANTIGLARE = new PhongMicrofacetModel(11.188);
	
	private final double alpha;
	
	public PhongMicrofacetModel(double alpha) {
		this.alpha = alpha;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel#getDistributionPDF(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public double getDistributionPDF(Vector3 m, Vector3 n) {
		double mdotn = m.dot(n);
		
		if (mdotn <= 0.0) {
			return 0.0;
		}

		double ca = Math.pow(mdotn, alpha);
		double k = (alpha + 2.0) / (2.0 * Math.PI);
		
		return k * ca;
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel#getShadowingAndMasking(ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3, ca.eandb.jmist.math.Vector3)
	 */
	@Override
	public double getShadowingAndMasking(Vector3 in, Vector3 out, Vector3 m,
			Vector3 n) {
	
		double ndoti = -n.dot(in);
		double ndoto = n.dot(out);
		double mdoti = -m.dot(in);
		double mdoto = m.dot(out);
		
		if (mdoti / ndoti <= 0.0 || mdoto / ndoto <= 0.0) {
			return 0.0;
		}
		
		double ti = Math.tan(Math.acos(Math.abs(ndoti)));
		double ai = Math.sqrt(0.5 * alpha + 1.0) / ti;
		double gi = ai < 1.6 ? (3.535 * ai + 2.181 * ai * ai) / (1.0 + 2.276 * ai + 2.577 * ai * ai) : 1.0;
		
		double to = Math.tan(Math.acos(Math.abs(ndoto)));
		double ao = Math.sqrt(0.5 * alpha + 1.0) / to;
		double go = ao < 1.6 ? (3.535 * ao + 2.181 * ao * ao) / (1.0 + 2.276 * ao + 2.577 * ao * ao) : 1.0;
		
		return gi * go;
		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.material.support.IsotropicMicrofacetModel#sample(double, double)
	 */
	@Override
	public SphericalCoordinates sample(double ru, double rv) {
		return new SphericalCoordinates(
				Math.acos(Math.pow(ru, 1.0 / (alpha + 2.0))),
				2.0 * Math.PI * rv);
	}

}
