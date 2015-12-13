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
package ca.eandb.jmist.framework.color.luminance;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.pdf.PiecewiseLinearProbabilityDensityFunction;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
public final class LuminanceColorModel implements ColorModel {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3535058346087410384L;

  private static final String CHANNEL_NAME = "Y";

  private static final ProbabilityDensityFunction Y_PDF = new PiecewiseLinearProbabilityDensityFunction(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Y_BAR);

  private static final double Y_CONST = ColorUtil.LUMENS_PER_WATT
      * MathUtil.trapz(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Y_BAR);

  /** The single <code>LuminanceColorModel</code> instance. */
  private static final ColorModel instance = new LuminanceColorModel();

  /**
   * Creates a <code>LuminanceColorModel</code>.
   * This constructor is private because this class is a singleton.
   */
  private LuminanceColorModel() {}

  /**
   * Gets the single <code>LuminanceColorModel</code> instance.
   * @return The single <code>LuminanceColorModel</code> instance.
   */
  public static ColorModel getInstance() {
    return instance;
  }

  @Override
  public Raster createRaster(int width, int height) {
    return new DoubleRaster(width, height, 1) {
      private static final long serialVersionUID = -7544627482087847173L;
      protected Color getPixel(double[] raster, int index) {
        return new LuminanceColor(raster[index]);
      }
    };
  }

  @Override
  public Color fromArray(double[] values, WavelengthPacket lambda) {
    if (values.length < 1) {
      throw new IllegalArgumentException("values.length < 1");
    }
    return new LuminanceColor(values[0], (LuminanceWavelengthPacket) lambda);
  }

  @Override
  public Spectrum fromRGB(double r, double g, double b) {
    return new LuminanceColor(ColorUtil.convertRGB2Luminance(r, g, b));
  }

  @Override
  public Spectrum fromXYZ(double x, double y, double z) {
    return new LuminanceColor(ColorUtil.convertXYZ2Luminance(x, y, z));
  }

  @Override
  public Spectrum getBlack() {
    return LuminanceColor.BLACK;
  }

  @Override
  public Color getBlack(WavelengthPacket lambda) {
    return getBlack((LuminanceWavelengthPacket) lambda);
  }

  public LuminanceColor getBlack(LuminanceWavelengthPacket lambda) {
    return lambda != null ? new LuminanceColor(0.0, lambda)
        : LuminanceColor.BLACK;
  }

  @Override
  public Spectrum getContinuous(Function1 spectrum) {
    return new LuminanceContinuousSpectrum(spectrum);
  }

  @Override
  public Spectrum getGray(double value) {
    return new LuminanceColor(value);
  }

  @Override
  public Color getGray(double value, WavelengthPacket lambda) {
    return getGray(value, (LuminanceWavelengthPacket) lambda);
  }

  public LuminanceColor getGray(double value, LuminanceWavelengthPacket lambda) {
    return new LuminanceColor(value, lambda);
  }

  @Override
  public int getNumChannels() {
    return 1;
  }

  @Override
  public Spectrum getWhite() {
    return LuminanceColor.WHITE;
  }

  @Override
  public Color getWhite(WavelengthPacket lambda) {
    return getWhite((LuminanceWavelengthPacket) lambda);
  }

  public LuminanceColor getWhite(LuminanceWavelengthPacket lambda) {
    return lambda != null ? new LuminanceColor(1.0, lambda)
        : LuminanceColor.WHITE;
  }

  @Override
  public Color sample(Random random) {
    return new LuminanceColor(Y_CONST, Y_PDF.sample(random));
  }

  @Override
  public String getChannelName(int channel) {
    if (channel != 0) {
      throw new IllegalArgumentException("Invalid channel");
    }
    return CHANNEL_NAME;
  }

}
