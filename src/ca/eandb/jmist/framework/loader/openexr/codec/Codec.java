/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.codec;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.jmist.framework.loader.openexr.attribute.Box2i;

/**
 * @author brad
 *
 */
public interface Codec {
	
	void compress(IIOByteBuffer buf, Box2i range);
	
	void decompress(IIOByteBuffer buf, Box2i range);

}
