/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.io.Reader;


/**
 * @author Brad
 *
 */
public interface RibReader {
	
	void read(Reader reader, RenderManContext context);
	
}
