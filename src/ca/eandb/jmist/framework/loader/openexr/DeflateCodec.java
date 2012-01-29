/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class DeflateCodec implements Codec {
	
	private static final DeflateCodec INSTANCE = new DeflateCodec();
	
	public static DeflateCodec getInstance() {
		return INSTANCE;
	}
	
	private DeflateCodec() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#apply(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void apply(IIOByteBuffer buf) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DeflaterOutputStream inf = new DeflaterOutputStream(bytes);
			inf.write(buf.getData(), buf.getOffset(), buf.getLength());
			inf.close();
			buf.setData(bytes.toByteArray());
			buf.setOffset(0);
			buf.setLength(bytes.size());
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}

	}

}
