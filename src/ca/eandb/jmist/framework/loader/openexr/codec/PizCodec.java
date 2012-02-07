/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.codec;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.jmist.framework.loader.openexr.attribute.Box2i;
import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class PizCodec implements Codec {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.codec.Codec#compress(javax.imageio.stream.IIOByteBuffer, ca.eandb.jmist.framework.loader.openexr.attribute.Box2i)
	 */
	@Override
	public void compress(IIOByteBuffer buf, Box2i range) {
		// TODO Auto-generated method stub
		throw new UnimplementedException();		
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.codec.Codec#decompress(javax.imageio.stream.IIOByteBuffer, ca.eandb.jmist.framework.loader.openexr.attribute.Box2i)
	 */
	@Override
	public void decompress(IIOByteBuffer buf, Box2i range) {
		// TODO Auto-generated method stub
		throw new UnimplementedException();
	}

}
