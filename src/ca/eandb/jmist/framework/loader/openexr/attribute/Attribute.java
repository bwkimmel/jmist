/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataOutput;
import java.io.IOException;

/**
 * @author brad
 *
 */
public interface Attribute {
	
	void write(DataOutput out) throws IOException;

}