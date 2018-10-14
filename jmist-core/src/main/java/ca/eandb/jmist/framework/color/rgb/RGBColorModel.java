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
package ca.eandb.jmist.framework.color.rgb;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;

/**
 * A three channel <code>ColorModel</code>.
 * @author Brad
 */
public final class RGBColorModel implements ColorModel {

  /** Serialization version ID. */
  private static final long serialVersionUID = 5180023685340681958L;

  /** The single <code>RGBColorModel</code> instance. */
  private static final RGBColorModel instance = new RGBColorModel();

  /** The names of the channels for this <code>ColorModel</code>. */
  private static final String[] CHANNEL_NAMES = { "R", "G", "B" };

  /**
   * Creates a new <code>RGBColorModel</code>.
   * This constructor is private because this class is a singleton.
   */
  private RGBColorModel() {
    /* nothing to do. */
  }

  /**
   * Gets the single <code>RGBColorModel</code> instance.
   * @return The single <code>RGBColorModel</code> instance.
   */
  public static RGBColorModel getInstance() {
    return instance;
  }

  @Override
  public Spectrum fromRGB(double r, double g, double b) {
    return new RGBColor(r, g, b);
  }

  @Override
  public Spectrum fromXYZ(double x, double y, double z) {
    RGB rgb = ColorUtil.convertXYZ2RGB(x, y, z);
    return fromRGB(rgb.r(), rgb.g(), rgb.b());
  }

  @Override
  public Spectrum getBlack() {
    return RGBColor.BLACK;
  }

  @Override
  public Spectrum getGray(double value) {
    return new RGBColor(value, value, value);
  }

  @Override
  public Spectrum getWhite() {
    return RGBColor.WHITE;
  }

  @Override
  public Spectrum getContinuous(Function1 spectrum) {
    return new RGBColor(spectrum.evaluate(650e-9), spectrum.evaluate(550e-9), spectrum.evaluate(450e-9));
  }

  @Override
  public Color fromArray(double[] values, WavelengthPacket lambda) {
    if (values.length < 3) {
      throw new IllegalArgumentException("values.length < 3");
    }
    return new RGBColor(values[0], values[1], values[2]);
  }

  @Override
  public Color getBlack(WavelengthPacket lambda) {
    return RGBColor.BLACK;
  }

  @Override
  public Color getGray(double value, WavelengthPacket lambda) {
    return new RGBColor(value, value, value);
  }

  @Override
  public Color getWhite(WavelengthPacket lambda) {
    return RGBColor.WHITE;
  }

  @Override
  public Color sample(Random random) {
    return RGBColor.WHITE;
  }

  @Override
  public int getNumChannels() {
    return 3;
  }

  @Override
  public Raster createRaster(int width, int height) {
    return new DoubleRaster(width, height, 3) {
      private static final long serialVersionUID = -2721143490321662390L;
      protected Color getPixel(double[] raster, int index) {
        return new RGBColor(raster[index], raster[index + 1], raster[index + 2]);
      }
    };
  }

  @Override
  public String getChannelName(int channel) {
    if (channel < 0 || channel >= 3) {
      throw new IllegalArgumentException("Invalid channel");
    }
    return CHANNEL_NAMES[channel];
  }

}
