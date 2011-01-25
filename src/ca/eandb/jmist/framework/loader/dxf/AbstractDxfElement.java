/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
public abstract class AbstractDxfElement implements DxfElement {
	
	private final int groupCode;
	
	protected AbstractDxfElement(int groupCode) {
		this.groupCode = groupCode;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfElement#getGroupCode()
	 */
	@Override
	public final int getGroupCode() {
		return groupCode;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfElement#getBooleanValue()
	 */
	@Override
	public boolean getBooleanValue() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfElement#getFloatValue()
	 */
	@Override
	public double getFloatValue() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfElement#getIntegerValue()
	 */
	@Override
	public int getIntegerValue() {
		throw new UnsupportedOperationException();
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfElement#getLongValue()
	 */
	@Override
	public long getLongValue() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.dxf.DxfElement#getStringValue()
	 */
	@Override
	public String getStringValue() {
		throw new UnsupportedOperationException();
	}

}
