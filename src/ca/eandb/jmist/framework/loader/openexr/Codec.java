/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import javax.imageio.stream.IIOByteBuffer;

/**
 * @author brad
 *
 */
public interface Codec {
	
	void compress(IIOByteBuffer buf);
	
	void decompress(IIOByteBuffer buf);

}
