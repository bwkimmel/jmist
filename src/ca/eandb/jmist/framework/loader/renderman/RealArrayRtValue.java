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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#toString()
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[");
		for (int i = 0; i < value.length; i++) {
			if (i > 0) {
				s.append(" ");
			}
			s.append(value[i]);
		}
		s.append("]");
		return s.toString();
	}

}
