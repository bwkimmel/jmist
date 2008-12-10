/**
 *
 */
package ca.eandb.jmist.framework;

/**
 * @author Brad Kimmel
 *
 */
public interface Observer {

	/**
	 * Acquires a colour observation.
	 * @param estimator A <code>SpectralEstimator</code> to convert into the
	 * 		target colour model.
	 * @param observation An optionally preallocated array to populate with the
	 * 		acquired observation.  If not <code>null</code>, the length must be
	 * 		equal to {@link #getNumberOfComponents()}.
	 * @return The observed colour channel responses.
	 * @throws IllegalArgumentException if <code>observation != null</code> and
	 * 		the length of <code>observation</code> is not equal to
	 * 		{@link #getNumberOfComponents()}.
	 * @see #getNumberOfComponents()
	 */
	double[] acquire(SpectralEstimator estimator, double[] observation);

	/**
	 * Gets the number of components in observations acquired by this
	 * <code>Observer</code>.
	 * @return The number of components in observations acquired by this
	 * 		<code>Observer</code>.
	 */
	int getNumberOfComponents();

}

