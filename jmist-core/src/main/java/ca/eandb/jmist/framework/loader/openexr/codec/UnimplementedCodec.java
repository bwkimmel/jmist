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
package ca.eandb.jmist.framework.loader.openexr.codec;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.jmist.framework.loader.openexr.attribute.Box2i;
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

  @Override
  public void compress(IIOByteBuffer buf, Box2i range) {
    throw new UnimplementedException("Unimplemented codec");
  }

  @Override
  public void decompress(IIOByteBuffer buf, Box2i range) {
    throw new UnimplementedException("Unimplemented codec");
  }

}
