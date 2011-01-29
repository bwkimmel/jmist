/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
public final class IntegerDxfElement extends AbstractDxfElement {

	private final int value;
	
	/**
	 * 
	 * @param groupCode
	 * @param value
	 */
	public IntegerDxfElement(int groupCode, int value) {
		super(groupCode);
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getIntegerValue()
	 */
	@Override
	public int getIntegerValue() {
		return value;
	}

}
