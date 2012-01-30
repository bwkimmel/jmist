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
			
			int n = buf.getLength();
			byte[] data = buf.getData();
			byte[] pred = new byte[n];
			
			int t1 = 0;
			int t2 = (n + 1) / 2;
			int s = buf.getOffset();
			int stop = s + n;
			
			while (true) {
				if (s < stop) {
					pred[t1++] = data[s++];
				} else {
					break;
				}
				
				if (s < stop) {
					pred[t2++] = data[s++];
				} else {
					break;
				}
			}
			
			int offset = buf.getOffset();
			int p = pred[0];
			for (int i = 1; i < n; i++) {
				int d = (int) pred[i] - p + (128 + 256);
				p = pred[i];
				pred[i] = (byte) d;
			}
			
			inf.write(pred);
			inf.close();
			buf.setData(bytes.toByteArray());
			buf.setOffset(0);
			buf.setLength(bytes.size());
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}

	}

}
