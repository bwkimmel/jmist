/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
interface DxfElement {

	int getGroupCode();
	
	boolean getBooleanValue() throws DxfException;
	
	int getIntegerValue() throws DxfException;
	
	long getLongValue() throws DxfException;
	
	double getFloatValue() throws DxfException;
	
	String getStringValue() throws DxfException;
	
}
