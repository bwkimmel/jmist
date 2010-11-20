/**
 * 
 */
package ca.eandb.jmist.framework.material.support;

import java.io.Serializable;

import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad Kimmel
 */
public interface IsotropicMicrofacetModel extends Serializable {

	double getDistributionPDF(Vector3 m, Vector3 n);
	
	double getShadowingAndMasking(Vector3 in, Vector3 out, Vector3 m, Vector3 n);
	
	SphericalCoordinates sample(double ru, double rv);
	
}
