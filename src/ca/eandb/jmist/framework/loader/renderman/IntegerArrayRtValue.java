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
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#intValue()
	 */
	@Override
	public int intValue() {
		return value.length == 1 ? value[0] : super.intValue();
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
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realValue()
	 */
	@Override
	public double realValue() {
		return value.length == 1 ? value[0] : super.realValue();
	}

}
