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
package ca.eandb.jmist.framework.display;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import ca.eandb.jdcp.JdcpUtil;
import ca.eandb.jdcp.job.HostService;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.tone.ToneMapper;
import ca.eandb.jmist.framework.tone.ToneMapperFactory;
import ca.eandb.jmist.math.Array2;
import ca.eandb.util.UnexpectedException;
import ca.eandb.util.io.FileUtil;

/**
 * A <code>Display</code> that writes to a typical, low-dynamic-range image
 * file (e.g., PNG, JPEG, etc.).
 * @author Brad Kimmel
 */
public final class ImageFileDisplay implements Display, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -5965755570476575580L;

  private static final String DEFAULT_FILENAME = "output.png";

  private static final String DEFAULT_FORMAT = FileUtil.getExtension(DEFAULT_FILENAME);

  private final String fileName;

  private final ToneMapperFactory toneMapperFactory;

  private transient Array2<CIEXYZ> image;

  public ImageFileDisplay() {
    this(DEFAULT_FILENAME, ToneMapperFactory.IDENTITY_FACTORY);
  }

  public ImageFileDisplay(String fileName) {
    this(fileName, ToneMapperFactory.IDENTITY_FACTORY);
  }

  public ImageFileDisplay(ToneMapperFactory toneMapperFactory) {
    this(DEFAULT_FILENAME, toneMapperFactory);
  }

  public ImageFileDisplay(String fileName, ToneMapperFactory toneMapperFactory) {
    this.fileName = fileName;
    this.toneMapperFactory = toneMapperFactory;
  }

  @Override
  public void fill(int x, int y, int w, int h, Color color) {
    CIEXYZ xyz = color.toXYZ();
    image.slice(x, y, w, h).setAll(xyz);
  }

  @Override
  public void finish() {
    HostService service = JdcpUtil.getHostService();
    try {
      int width = image.rows();
      int height = image.columns();
      BufferedImage bi = new BufferedImage(width, height,
          BufferedImage.TYPE_INT_RGB);

      FileOutputStream os = (service != null) ? service
          .createFileOutputStream(fileName) : new FileOutputStream(
          fileName);

      String formatName = FileUtil.getExtension(fileName);
      if (formatName.equals("")) {
        formatName = DEFAULT_FORMAT;
      }

      ToneMapper toneMapper = toneMapperFactory.createToneMapper(image);

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          CIEXYZ xyz = toneMapper.apply(image.get(x, y));
          int rgb = xyz.toRGB().toR8G8B8();
          bi.setRGB(x, y, rgb);
        }
      }

      ImageIO.write(bi, formatName, os);
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

  @Override
  public void initialize(int w, int h, ColorModel colorModel) {
    image = new Array2<CIEXYZ>(w, h);
  }

  @Override
  public void setPixel(int x, int y, Color pixel) {
    image.set(x, y, pixel.toXYZ());
  }

  @Override
  public void setPixels(int x, int y, Raster pixels) {
    int w = pixels.getWidth();
    int h = pixels.getHeight();
    Array2<CIEXYZ> tile = image.slice(x, y, w, h);
    for (int ry = 0; ry < h; ry++) {
      for (int rx = 0; rx < w; rx++) {
        CIEXYZ xyz = pixels.getPixel(rx, ry).toXYZ();
        tile.set(rx, ry, xyz);
      }
    }
  }

}
