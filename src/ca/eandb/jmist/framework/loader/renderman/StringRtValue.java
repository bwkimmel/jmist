/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
final class StringRtValue extends AbstractRtValue {

	private final String value;
	
	public StringRtValue(String value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringValue()
	 */
	@Override
	public String stringValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.AbstractRtValue#stringArrayValue()
	 */
	@Override
	public String[] stringArrayValue() {
		return new String[]{ value };
	}
	
}
