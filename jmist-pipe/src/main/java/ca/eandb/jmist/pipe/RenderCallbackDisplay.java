/* Copyright (c) 2014 Bradley W. Kimmel
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
package ca.eandb.jmist.pipe;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import ca.eandb.jmist.engine.proto.RenderEngineProtos.Position;
import ca.eandb.jmist.engine.proto.RenderEngineProtos.Rectangle;
import ca.eandb.jmist.engine.proto.RenderEngineProtos.RenderCallback;
import ca.eandb.jmist.engine.proto.RenderEngineProtos.RenderCallback.DrawTile;
import ca.eandb.jmist.engine.proto.RenderEngineProtos.RenderCallback.DrawTile.PixelFormat;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.proto.RenderProtos.Size;

import com.google.protobuf.ByteString;
import com.google.protobuf.ByteString.Output;

/**
 *
 */
public final class RenderCallbackDisplay implements Display {

  private final BlockingQueue<RenderCallback> messages;

  public RenderCallbackDisplay(BlockingQueue<RenderCallback> messages) {
    this.messages = messages;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#initialize(int, int, ca.eandb.jmist.framework.color.ColorModel)
   */
  @Override
  public void initialize(int w, int h, ColorModel colorModel) {
    // nothing to do
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#fill(int, int, int, int, ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public void fill(int x, int y, int w, int h, Color color) {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#setPixel(int, int, ca.eandb.jmist.framework.color.Color)
   */
  @Override
  public void setPixel(int x, int y, Color pixel) {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#setPixels(int, int, ca.eandb.jmist.framework.Raster)
   */
  @Override
  public void setPixels(int x, int y, Raster pixels) {
    try {
      messages.put(
          RenderCallback.newBuilder()
              .setDrawTile(DrawTile.newBuilder()
                  .setFormat(PixelFormat.DOUBLE_RGBA)
                  .setData(serializePixels(pixels))
                  .setTile(Rectangle.newBuilder()
                      .setPosition(Position.newBuilder().setX(x).setY(y))
                      .setSize(Size.newBuilder()
                          .setX(pixels.getWidth())
                          .setY(pixels.getHeight()))))
              .build());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private ByteString serializePixels(Raster pixels) throws IOException {
    int w = pixels.getWidth();
    int h = pixels.getHeight();
    Output output = ByteString.newOutput(w * h * 4 * 8);
    DataOutputStream stream = new DataOutputStream(output);
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        RGB rgb = pixels.getPixel(x, y).toRGB();
        stream.writeDouble(rgb.r());
        stream.writeDouble(rgb.g());
        stream.writeDouble(rgb.b());
        stream.writeDouble(1.0);
      }
    }
    stream.flush();
    return output.toByteString();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Display#finish()
   */
  @Override
  public void finish() {
    // nothing to do
  }

}
