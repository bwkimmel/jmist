/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2018 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * A <code>Texture2</code> that is extrapolated from a <code>Raster</code>
 * image.
 * @author Brad Kimmel
 */
public final class RasterTexture2 implements Texture2 {

  /** Serialization version ID. */
  private static final long serialVersionUID = -2712131011948642676L;

  /**
   * Creates a new <code>RasterTexture2</code>.
   * @param image The <code>BufferedImage</code> to use as the basis for the
   *     new <code>Texture2</code>.
   * @param background The <code>Texture2</code> to render underneath if the
   *     image has an alpha channel.
   */
  public RasterTexture2(BufferedImage image, Texture2 background) {
    this.image = image;
    this.background = background;
  }

  public RasterTexture2(File file, Texture2 background) throws IOException {
    this(ImageIO.read(file), background);
  }

  public RasterTexture2(URL input, Texture2 background) throws IOException {
    this(ImageIO.read(input), background);
  }

  public RasterTexture2(ImageInputStream stream, Texture2 background) throws IOException {
    this(ImageIO.read(stream), background);
  }

  public RasterTexture2(InputStream input, Texture2 background) throws IOException {
    this(ImageIO.read(input), background);
  }

  /**
   * Creates a new <code>RasterTexture2</code>.
   * @param image The <code>BufferedImage</code> to use as the basis for the
   *     new <code>Texture2</code>.
   */
  public RasterTexture2(BufferedImage image) {
    this(image, Texture2.BLACK);
  }

  public RasterTexture2(File file) throws IOException {
    this(ImageIO.read(file));
  }

  public RasterTexture2(URL input) throws IOException {
    this(ImageIO.read(input));
  }

  public RasterTexture2(ImageInputStream stream) throws IOException {
    this(ImageIO.read(stream));
  }

  public RasterTexture2(InputStream input) throws IOException {
    this(ImageIO.read(input));
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    ImageIO.write(image, "png", oos);
  }

  private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    image = ImageIO.read(ois);
  }

  @Override
  public Spectrum evaluate(Point2 p) {
    double u = p.x() - Math.floor(p.x());
    double v = p.y() - Math.floor(p.y());
    int w = image.getWidth();
    int h = image.getHeight();
    int x = MathUtil.clamp((int) Math.floor(u * (double) w), 0, w - 1);
    int y = MathUtil.clamp((int) Math.floor(v * (double) h), 0, h - 1);
    int c = image.getRGB(x, y);
    int a = (c >> 24) & 0xff;

    if (a == 0) {
      return background.evaluate(p);
    } else if (a == 255) {
      return RGB.fromR8G8B8(c);
    } else {
      Spectrum bg = background.evaluate(p);
      Spectrum fg = RGB.fromR8G8B8(c);
      return Spectrum.mix(a / 255.0, bg, fg);
    }
  }

  /**
   * The <code>BufferedImage</code> that serves as the basis for this
   * <code>Texture2</code>.
   */
  private transient BufferedImage image;

  /**
   * The <code>Texture2</code> to render underneath this texture if the image
   * has an alpha channel.
   */
  private final Texture2 background;

}
