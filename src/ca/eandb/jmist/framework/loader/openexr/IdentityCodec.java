/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

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
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#apply(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void apply(IIOByteBuffer buf) {
		/* nothing to do. */
	}

}
