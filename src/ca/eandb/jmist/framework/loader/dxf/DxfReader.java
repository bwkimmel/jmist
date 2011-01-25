/**
 * 
 */
package ca.eandb.jmist.framework.loader.dxf;

/**
 * @author Brad
 *
 */
public interface DxfReader {

	DxfElement getNextElement() throws DxfException;
	
}
