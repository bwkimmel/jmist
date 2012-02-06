/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.util.UnexpectedException;

/**
 * @author brad
 *
 */
public final class FlateCodec implements Codec {
	
	private static final FlateCodec INSTANCE = new FlateCodec();
	
	public static FlateCodec getInstance() {
		return INSTANCE;
	}
	
	private FlateCodec() {}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#compress(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void compress(IIOByteBuffer buf) {
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

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Codec#decompress(javax.imageio.stream.IIOByteBuffer)
	 */
	@Override
	public void decompress(IIOByteBuffer buf) {
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
