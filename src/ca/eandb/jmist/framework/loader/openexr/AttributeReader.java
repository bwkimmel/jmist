/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.IOException;

/**
 * @author brad
 *
 */
public interface AttributeReader {
	
	Attribute read(DataInput in, int size) throws IOException;

}
