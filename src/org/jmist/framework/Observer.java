/**
 *
 */
package org.jmist.framework;

import org.jmist.toolkit.Tuple;

/**
 * @author bkimmel
 *
 */
public interface Observer {

	public static class Sample {

		public final Tuple wavelengths;
		public final double[] weights;

		public Sample(Tuple wavelengths, double[] weights) {
			assert(weights.length == wavelengths.size());
			this.wavelengths = wavelengths;
			this.weights = weights;
		}

	}

	Sample sample();

	int getNumberOfComponents();

}

