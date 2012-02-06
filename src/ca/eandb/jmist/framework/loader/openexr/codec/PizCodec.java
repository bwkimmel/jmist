/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.codec;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.util.UnimplementedException;

/**
 * @author brad
 *
 */
public final class PizCodec implements Codec {

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.codec.Codec#compress(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void compress(IIOByteBuffer buf) {
		// TODO Auto-generated method stub
		throw new UnimplementedException();
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.codec.Codec#decompress(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void decompress(IIOByteBuffer buf) {
		// TODO Auto-generated method stub
		throw new UnimplementedException();
	}

}
