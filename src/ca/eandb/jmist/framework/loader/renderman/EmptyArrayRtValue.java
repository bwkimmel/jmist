/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class EmptyArrayRtValue extends AbstractRtValue {
	
	public static final RtValue INSTANCE = new EmptyArrayRtValue();

	/** Default constructor. */
	private EmptyArrayRtValue() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#realArrayValue()
	 */
	@Override
	public double[] realArrayValue() {
		return new double[0];
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#intArrayValue()
	 */
	@Override
	public int[] intArrayValue() {
		return new int[0];
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringArrayValue()
	 */
	@Override
	public String[] stringArrayValue() {
		return new String[0];
	}
	
}
