/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class RealRtValue extends AbstractRtValue {

	private final double value;
	
	public RealRtValue(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realValue()
	 */
	@Override
	public double realValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realArrayValue()
	 */
	@Override
	public double[] realArrayValue() {
		return new double[]{ value };
	}
	
	
	
}
