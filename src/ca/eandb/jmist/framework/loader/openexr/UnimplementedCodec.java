/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class UnimplementedCodec implements Codec {
	
	private static final UnimplementedCodec INSTANCE = new UnimplementedCodec();
	
	public static final UnimplementedCodec getInstance() {
		return INSTANCE;
	}
	
	private UnimplementedCodec() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#apply(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void apply(IIOByteBuffer buf) {
		throw new UnimplementedException("Unimplemented codec");
	}

}
