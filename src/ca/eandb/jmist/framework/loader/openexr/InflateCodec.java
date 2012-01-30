/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;
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
			
			byte[] data = bytes.toByteArray();
			for (int i = 1, n = bytes.size(); i < n; i++) {
				data[i] = (byte) (((int) data[i - 1]) + ((int) data[i]) - 128);
			}
			
			int n = bytes.size();
			int t1 = 0;
			int t2 = (n + 1) / 2;
			int s = 0;
			byte[] out = new byte[n];
			
			while (true) {
				if (s < n) {
					out[s++] = data[t1++];
				} else {
					break;
				}
				
				if (s < n) {
					out[s++] = data[t2++];
				} else {
					break;
				}
			}
			
			buf.setData(out);
			buf.setOffset(0);
			buf.setLength(n);			
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
	}

}
