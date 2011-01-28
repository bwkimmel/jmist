/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad Kimmel
 */
public final class StringDxfElement extends AbstractDxfElement {

	private final String value;
	
	/**
	 * @param groupCode
	 * @param value
	 */
	public StringDxfElement(int groupCode, String value) {
		super(groupCode);
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getStringValue()
	 */
	@Override
	public String getStringValue() {
		return value;
	}

}
