/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class IntegerArrayRtValue extends AbstractRtValue {
	
	private final int[] value;
	
	public IntegerArrayRtValue(int[] value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#intArrayValue()
	 */
	@Override
	public int[] intArrayValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realArrayValue()
	 */
	@Override
	public double[] realArrayValue() {
		double[] reals = new double[value.length];
		for (int i = 0; i < value.length; i++) {
			reals[i] = value[i];
		}
		return reals;
	}

}
