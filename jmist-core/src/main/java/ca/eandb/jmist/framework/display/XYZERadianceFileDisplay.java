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
package ca.eandb.jmist.framework.display;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import ca.eandb.jdcp.JdcpUtil;
import ca.eandb.jdcp.job.HostService;
import ca.eandb.jmist.framework.Display;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.loader.radiance.RadiancePicture;
import ca.eandb.jmist.framework.loader.radiance.RadiancePicture.Format;
import ca.eandb.util.UnexpectedException;

/**
 * A <code>Display</code> that writes a radiance HDR image file using the XYZE
 * pixel format.
 * @author Brad Kimmel
 */
public final class XYZERadianceFileDisplay implements Display, Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = 7431366666341133926L;

  /** Default filename. */
  private static String DEFAULT_FILENAME = "output.hdr";

  /** The name of the file to write. */
  private final String fileName;

  /** The <code>RadiancePicture</code> image to write. */
  private transient RadiancePicture picture;

  /**
   * Creates a new <code>XYZERadianceFileDisplay</code>.
   * @param fileName The name of the file to write.
   */
  public XYZERadianceFileDisplay(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Creates a new <code>XYZERadianceFileDisplay</code>.
   */
  public XYZERadianceFileDisplay() {
    this(DEFAULT_FILENAME);
  }

  @Override
  public void initialize(int w, int h, ColorModel colorModel) {
    picture = new RadiancePicture(w, h, Format.XYZE);
  }

  @Override
  public void fill(int x, int y, int w, int h, Color color) {
    CIEXYZ xyz = color.toXYZ();
    for (int j = y; j < y + h; j++) {
      for (int i = x; i < x + w; i++) {
        picture.setPixelXYZ(i, j, xyz);
      }
    }
  }

  @Override
  public void setPixel(int x, int y, Color pixel) {
    picture.setPixelXYZ(x, y, pixel.toXYZ());
  }

  @Override
  public void setPixels(int x, int y, Raster pixels) {
    for (int j = 0, h = pixels.getHeight(); j < h; j++) {
      for (int i = 0, w = pixels.getWidth(); i < w; i++) {
        picture.setPixelXYZ(x + i, y + j, pixels.getPixel(i, j).toXYZ());
      }
    }
  }

  @Override
  public void finish() {
    HostService service = JdcpUtil.getHostService();
    try {
      FileOutputStream os = (service != null) ? service
          .createFileOutputStream(fileName) : new FileOutputStream(
          fileName);

      picture.write(os);
    } catch (IOException e) {
      throw new UnexpectedException(e);
    }
  }

}
