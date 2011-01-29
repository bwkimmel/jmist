/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
public interface DxfReader {

	DxfElement getCurrentElement();
	
	void advance() throws DxfException;
	
}
