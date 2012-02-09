/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class RealArrayRtValue extends AbstractRtValue {

	private final double[] value;
	
	public RealArrayRtValue(double[] value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realArrayValue()
	 */
	@Override
	public double[] realArrayValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realValue()
	 */
	@Override
	public double realValue() {
		return value.length == 1 ? value[0] : super.realValue();
	}
	
}
