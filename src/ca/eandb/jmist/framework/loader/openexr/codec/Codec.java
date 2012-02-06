/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.codec;

import javax.imageio.stream.IIOByteBuffer;

/**
 * @author brad
 *
 */
public interface Codec {
	
	void compress(IIOByteBuffer buf);
	
	void decompress(IIOByteBuffer buf);

}
