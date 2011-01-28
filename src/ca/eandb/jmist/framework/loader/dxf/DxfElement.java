/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
public interface DxfElement {

	int getGroupCode();
	
	boolean getBooleanValue() throws DxfException;
	
	int getIntegerValue() throws DxfException;
	
	long getLongValue() throws DxfException;
	
	double getFloatValue() throws DxfException;
	
	String getStringValue() throws DxfException;
	
}
