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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.loader.radiance.RadiancePicture;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point2;

public final class RadianceTexture2 implements Texture2 {

  /** Serialized version ID. */
  private static final long serialVersionUID = -763379510350283868L;

  private final RadiancePicture picture;

  public RadianceTexture2(RadiancePicture picture) {
    this.picture = picture;
  }

  public RadianceTexture2(File file) throws IOException {
    this(RadiancePicture.read(file));
  }

  public RadianceTexture2(URL url) throws IOException {
    this(RadiancePicture.read(url));
  }

  public RadianceTexture2(InputStream stream) throws IOException {
    this(RadiancePicture.read(stream));
  }

  @Override
  public Spectrum evaluate(Point2 p) {
    double u = p.x() - Math.floor(p.x());
    double v = 1.0 - (p.y() - Math.floor(p.y()));
    int w = picture.getSizeX();
    int h = picture.getSizeY();
    int x = MathUtil.clamp((int) Math.floor(u * (double) w), 0, w - 1);
    int y = MathUtil.clamp((int) Math.floor(v * (double) h), 0, h - 1);

    switch (picture.getFormat()) {
      case RGBE:
        return picture.getPixelRGB(x, y);
      case XYZE:
        return picture.getPixelXYZ(x, y);
      default:
        return Spectrum.BLACK;
    }
  }

}
