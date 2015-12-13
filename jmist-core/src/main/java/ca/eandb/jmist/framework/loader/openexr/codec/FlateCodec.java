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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

import javax.imageio.stream.IIOByteBuffer;

import ca.eandb.jmist.framework.loader.openexr.attribute.Box2i;
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

  @Override
  public void compress(IIOByteBuffer buf, Box2i range) {
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

  @Override
  public void decompress(IIOByteBuffer buf, Box2i range) {
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
