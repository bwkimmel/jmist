/**
 * 
 */
package ca.eandb.jmist.framework.loader.openexr;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.imageio.stream.IIOByteBuffer;

/**
 * @author brad
 *
 */
@OpenEXRAttributeType("compression")
public enum CompressionMethod implements Attribute {
	
	NONE(0, 1, IdentityCodec.getInstance(), IdentityCodec.getInstance()),
	RLE(1, 1, UnimplementedCodec.getInstance(), UnimplementedCodec.getInstance()),
	ZIPS(2, 1, DeflateCodec.getInstance(), InflateCodec.getInstance()),
	ZIP(3, 16, DeflateCodec.getInstance(), InflateCodec.getInstance()),
	PIZ(4, 32, UnimplementedCodec.getInstance(), UnimplementedCodec.getInstance()),
	PXR24(5, 16, UnimplementedCodec.getInstance(), UnimplementedCodec.getInstance()),
	B44(6, 32, UnimplementedCodec.getInstance(), UnimplementedCodec.getInstance()),
	B44A(7, 32, UnimplementedCodec.getInstance(), UnimplementedCodec.getInstance());
	
	private final int key;
	
	private final int scanLinesPerBlock;
	
	private final Codec compressor;
	
	private final Codec decompressor;
	
	private CompressionMethod(int key, int scanLinesPerBlock, Codec compressor, Codec decompressor) {
		this.key = key;
		this.scanLinesPerBlock = scanLinesPerBlock;
		this.compressor = compressor;
		this.decompressor = decompressor;
	}
	
	/**
	 * @return the scanLinesPerBlock
	 */
	public final int getScanLinesPerBlock() {
		return scanLinesPerBlock;
	}

	public static CompressionMethod read(DataInput in, int size) throws IOException {
		int key = in.readByte();
		for (CompressionMethod method : CompressionMethod.values()) {
			if (method.key == key) {
				return method;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(key);
	}
	
	public void compress(IIOByteBuffer buf) {
		compressor.apply(buf);
	}
	
	public void decompress(IIOByteBuffer buf) {
		decompressor.apply(buf);
	}

}
