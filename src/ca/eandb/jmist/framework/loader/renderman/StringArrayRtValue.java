/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class StringArrayRtValue extends AbstractRtValue {

	private final String[] value;
	
	public StringArrayRtValue(String[] value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringArrayValue()
	 */
	@Override
	public String[] stringArrayValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringValue()
	 */
	@Override
	public String stringValue() {
		return value.length == 1 ? value[0] : super.stringValue();
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
			s.append("\"");
			s.append(value[i]);
			s.append("\"");
		}
		s.append("]");
		return s.toString();
	}

}
