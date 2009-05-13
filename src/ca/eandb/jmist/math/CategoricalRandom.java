/**
 *
 */
package ca.eandb.jmist.math;

import java.util.Arrays;

import ca.eandb.jmist.framework.Random;

/**
 * A categorical random variable (i.e., a discrete random variable that selects
 * a value with probability proportional to specified weights).
 * @author Brad Kimmel
 */
public final class CategoricalRandom {

	/**
	 * Creates a new <code>CategoricalRandom</code>.
	 * @param weights An array of the weights associated with each integer
	 * 		from zero to <code>weights.length - 1</code>.
	 * @param source The <code>Random</code> number generator to use to seed
	 * 		this <code>CategoricalRandom</code>.
	 */
	public CategoricalRandom(double[] weights, Random source) {

		this.source = source;
		this.cpf = weights.clone();

		/* Compute the cumulative probability function. */
		for (int i = 1; i < cpf.length; i++) {
			this.cpf[i] += this.cpf[i - 1];
		}

		for (int i = 0; i < cpf.length; i++) {
			this.cpf[i] /= this.cpf[cpf.length - 1];
		}

	}

	/**
	 * Creates a new <code>CategoricalRandom</code>.
	 * @param weights An array of the weights associated with each integer
	 * 		from zero to <code>weights.length - 1</code>.
	 */
	public CategoricalRandom(double[] weights) {
		this(weights, null);
	}

	/**
	 * Generates a new sample of this <code>CategoricalRandom</code>
	 * variable.
	 * @return The next sample.
	 */
	public int next() {
		int index = Arrays.binarySearch(this.cpf, source != null ? source.next() : Math.random());
		return index >= 0 ? index : -(index + 1);
	}

	/** The cumulative probability function. */
	private final double[] cpf;

	/**
	 * The <code>Random</code> number generator to use to seed this
	 * <code>CategoricalRandom</code>.
	 */
	private final Random source;

}
