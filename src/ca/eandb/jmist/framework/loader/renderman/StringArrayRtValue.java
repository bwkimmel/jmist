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
	
}
