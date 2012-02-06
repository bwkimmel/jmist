/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.codec;

import javax.imageio.stream.IIOByteBuffer;


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
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#compress(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void compress(IIOByteBuffer buf) {
		/* nothing to do. */
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#decompress(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void decompress(IIOByteBuffer buf) {
		/* nothing to do. */
	}

}
