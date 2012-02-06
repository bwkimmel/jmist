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
public final class IdentityCodec implements Codec {
	
	private static final IdentityCodec INSTANCE = new IdentityCodec();
	
	public static IdentityCodec getInstance() {
		return INSTANCE;
	}
	
	private IdentityCodec() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.codec.Codec#compress(javax.imageio.stream.IIOByteBuffer, ca.eandb.jmist.framework.loader.openexr.attribute.Box2i)
	 */
	@Override
	public void compress(IIOByteBuffer buf, Box2i range) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.codec.Codec#decompress(javax.imageio.stream.IIOByteBuffer, ca.eandb.jmist.framework.loader.openexr.attribute.Box2i)
	 */
	@Override
	public void decompress(IIOByteBuffer buf, Box2i range) {
		/* nothing to do. */
	}

}
