/**
 *
 */
package ca.eandb.jmist.framework;

import ca.eandb.jmist.toolkit.*;

/**
 * An estimator for a spectral response.  This is meant to
 * represent a spectral response that is not known explicitly,
 * but for which random samples may be taken.
 * @author Brad Kimmel
 */
public interface SpectralEstimator {

	/**
	 * Computes an estimate the spectral response at the
	 * specified wavelengths.
	 * @param wavelengths The wavelengths at which to compute
	 * 		the estimate.  This method shall not alter the elements
	 * 		of this array.
	 * @param responses [out] An array to be populated with the spectral
	 * 		responses at the specified wavelengths.  Must have
	 * 		{@code responses.length == wavelengths.length}.  The values of the
	 * 		elements of responses shall not depend on their initial values.
	 * 		Note that this requirement implies that this method is responsible
	 * 		for initializing the values of this array.  In particular, the
	 * 		implementation of this method must not assume that the elements
	 * 		have been set to zero prior to invocation.
	 * @return The spectral responses at the specified wavelengths.
	 */
	double[] sample(Tuple wavelengths, double[] responses);

}
