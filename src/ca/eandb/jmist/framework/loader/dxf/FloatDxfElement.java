/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
public final class FloatDxfElement extends AbstractDxfElement {

	private final double value;
	
	/**
	 * 
	 * @param groupCode
	 * @param value
	 */
	public FloatDxfElement(int groupCode, double value) {
		super(groupCode);
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.AbstractDxfElement#getFloatValue()
	 */
	@Override
	public double getFloatValue() {
		return value;
	}

}
