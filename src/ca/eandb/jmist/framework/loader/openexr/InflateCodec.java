/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterOutputStream;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class InflateCodec implements Codec {
	
	private static final InflateCodec INSTANCE = new InflateCodec();
	
	public static InflateCodec getInstance() {
		return INSTANCE;
	}
	
	private InflateCodec() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#apply(javax.imageio.stream.IIOByteBuffer, javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void apply(IIOByteBuffer buf) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			InflaterOutputStream inf = new InflaterOutputStream(bytes);
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
