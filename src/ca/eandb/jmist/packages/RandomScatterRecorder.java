/**
 *
 */
package ca.eandb.jmist.packages;

import ca.eandb.jmist.framework.ScatterRecorder;
import ca.eandb.jmist.framework.ScatterResult;
import ca.eandb.jmist.util.MathUtil;

/**
 * A <code>ScatterRecorder</code> that selects a single scattering event with
 * probability equal to the weight at the specified index.  This
 * <code>ScatterRecorder</code> will reject all <code>ScatterResult</code>s
 * with probability equal to 1 minus the sum of all the weights of the recorded
 * <code>ScatterResult</code>s at the given index.
 * @see ScatterResult#weightAt(int)
 * @author Brad Kimmel
 */
public final class RandomScatterRecorder implements ScatterRecorder {

	/**
	 * Creates a new <code>RandomScatterRecorder</code> that will select a
	 * <code>ScatterResult</code> based on the first weight (index zero).
	 */
	public RandomScatterRecorder() {
		this.keySampleIndex = 0;
	}

	/**
	 * Creates a new <code>RandomScatterRecorder</code> that will select a
	 * <code>ScatterResult</code> based on the weight at the specified index.
	 * @param keySampleIndex The index of the weight to use to select a
	 * 		<code>ScatterResult</code>.
	 */
	public RandomScatterRecorder(int keySampleIndex) {
		this.keySampleIndex = keySampleIndex;
	}

	/**
	 * Reset this <code>ScatterRecorder</code> to its initial state while
	 * preserving the index to use for selecting a <code>ScatterResult</code>.
	 */
	public void reset() {
		this.rnd = Math.random();
		this.cumulative = 0.0;
		this.result = null;
	}

	/**
	 * Reset this <code>ScatterRecorder</code> to its initial state and
	 * sets the index to use for selecting a <code>ScatterResult</code>.
	 * @param keySampleIndex The index of the weight to use to select a
	 * 		<code>ScatterResult</code>.
	 */
	public void reset(int keySampleIndex) {
		this.keySampleIndex = keySampleIndex;
		this.reset();
	}

	/**
	 * Gets the selected <code>ScatterResult</code>.  This may be
	 * <code>null</code>.
	 * @return The selected <code>ScatterResult</code>, if any.
	 */
	public ScatterResult getScatterResult() {
		return this.result;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.ScatterRecorder#record(ca.eandb.jmist.framework.ScatterResult)
	 */
	public void record(ScatterResult sr) {
		if (this.result == null && this.accept(sr)) {
			this.result = sr;
			MathUtil.scale(this.result.weights(), 1.0 / sr.weightAt(this.keySampleIndex));
		}
	}

	/**
	 * Determines whether to accept the recorded <code>ScatterResult</code>.
	 * @param sr The recorded <code>ScatterResult</code>.
	 * @return A value indicating whether to accept the scattering event.
	 */
	private boolean accept(ScatterResult sr) {
		this.cumulative += sr.weightAt(this.keySampleIndex);
		return (this.rnd < this.cumulative);
	}

	/**
	 * The index of the weight to use for the probabilities of accepting a
	 * <code>ScatterResult</code>.
	 */
	private int keySampleIndex;

	/**
	 * The random value to use to select the <code>ScatterResult</code>.  The
	 * first <code>ScatterResult</code> recorded that causes the total weight
	 * (at index <code>keySampleIndex</code>) to exceed this value will be the
	 * one that is accepted.
	 */
	private double rnd = Math.random();

	/**
	 * The total weight (at index <code>keySampleIndex</code>) of all the
	 * <code>ScatterResult</code>s recorded since the last call to
	 * {@link #reset()} (or since this <code>RandomScatterRecorder</code> was
	 * constructed).  The <code>ScatterResult</code> to be accepted will be the
	 * first one recorded that causes this value to exceed <code>rnd</code>.
	 * @see #reset(), {@link #rnd}
	 */
	private double cumulative = 0.0;

	/** The <code>ScatterResult</code> that was accepted (if any). */
	private ScatterResult result = null;

}