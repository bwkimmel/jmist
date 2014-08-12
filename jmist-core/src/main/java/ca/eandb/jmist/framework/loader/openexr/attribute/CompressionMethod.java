/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.jmist.framework.loader.openexr.codec.Codec;
import ca.eandb.jmist.framework.loader.openexr.codec.FlateCodec;
import ca.eandb.jmist.framework.loader.openexr.codec.IdentityCodec;
import ca.eandb.jmist.framework.loader.openexr.codec.PizCodec;
import ca.eandb.jmist.framework.loader.openexr.codec.UnimplementedCodec;

/**
 * @author brad
 *
 */
@OpenEXRAttributeType("compression")
public enum CompressionMethod implements Attribute {
	
	NONE(0, 1, IdentityCodec.getInstance()),
	RLE(1, 1, UnimplementedCodec.getInstance()),
	ZIPS(2, 1, FlateCodec.getInstance()),
	ZIP(3, 16, FlateCodec.getInstance()),
	PIZ(4, 32, new PizCodec()),
	PXR24(5, 16, UnimplementedCodec.getInstance()),
	B44(6, 32, UnimplementedCodec.getInstance()),
	B44A(7, 32, UnimplementedCodec.getInstance());
	
	private final int key;
	
	private final int scanLinesPerBlock;
	
	private final Codec codec;
	
	private CompressionMethod(int key, int scanLinesPerBlock, Codec codec) {
		this.key = key;
		this.scanLinesPerBlock = scanLinesPerBlock;
		this.codec = codec;
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
	
	public void compress(IIOByteBuffer buf, Box2i range) {
		codec.compress(buf, range);
	}
	
	public void decompress(IIOByteBuffer buf, Box2i range) {
		codec.decompress(buf, range);
	}

}
