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
package ca.eandb.jmist.framework.color.xyz.single;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.ProbabilityDensityFunction;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.function.ConstantFunction1;
import ca.eandb.jmist.framework.pdf.PiecewiseLinearProbabilityDensityFunction;

/**
 * A <code>ColorModel</code> that maintains a single (wavelength, value) pair
 * to represent all three XYZ channels while tracing a path.
 */
public final class SingleXYZColorModel implements ColorModel {

  /** Serialization version ID. */
  private static final long serialVersionUID = 4579926945614312541L;

  /** The single instance of <code>SingleXYZColorModel</code>. */
  private static SingleXYZColorModel instance = new SingleXYZColorModel();

  private static final ProbabilityDensityFunction Y_PDF = new PiecewiseLinearProbabilityDensityFunction(ColorUtil.XYZ_WAVELENGTHS, ColorUtil.Y_BAR);

  private final Spectrum black = new SingleXYZContinuousSpectrum(Function1.ZERO);

  private final Spectrum white = new SingleXYZContinuousSpectrum(Function1.ONE);

  /**
   * Gets the single instance of this <code>ColorModel</code>.
   * @return The single instance of this <code>ColorModel</code>.
   */
  public static SingleXYZColorModel getInstance() {
    return instance;
  }

  /** Constructor is private because this class is a singleton. */
  private SingleXYZColorModel() { /* nothing to do. */ }

  @Override
  public Spectrum getBlack() {
    return black;
  }

  @Override
  public Spectrum getWhite() {
    return white;
  }

  @Override
  public Spectrum fromRGB(double r, double g, double b) {
    CIEXYZ c = ColorUtil.convertRGB2XYZ(r, g, b);
    return fromXYZ(c.X(), c.Y(), c.Z());
  }

  @Override
  public Spectrum fromXYZ(double x, double y, double z) {
    return new XYZColor(x, y, z);
  }

  @Override
  public Spectrum getGray(double value) {
    return new SingleXYZContinuousSpectrum(new ConstantFunction1(value));
  }

  @Override
  public Spectrum getContinuous(Function1 spectrum) {
    return new SingleXYZContinuousSpectrum(spectrum);
  }

  @Override
  public Color fromArray(double[] values, WavelengthPacket lambda) {
    switch (values.length) {
    case 1: return new XYZSample(values[0], (SingleXYZWavelengthPacket) lambda);
    case 3: return fromXYZ(values[0], values[1], values[2]).sample(lambda);
    default: throw new IllegalArgumentException();
    }
  }

  @Override
  public Color getBlack(WavelengthPacket lambda) {
    return new XYZSample(0, (SingleXYZWavelengthPacket) lambda);
  }

  @Override
  public Color getWhite(WavelengthPacket lambda) {
    return new XYZSample(1, (SingleXYZWavelengthPacket) lambda);
  }

  @Override
  public Color getGray(double value, WavelengthPacket lambda) {
    return new XYZSample(value, (SingleXYZWavelengthPacket) lambda);
  }

  @Override
  public Color sample(Random random) {
    double lambda = Y_PDF.sample(random);
    double pdf = Y_PDF.evaluate(lambda);
    return new XYZSample(ColorUtil.LUMENS_PER_WATT / pdf, new SingleXYZWavelengthPacket(lambda));
  }

  @Override
  public Raster createRaster(int width, int height) {
    return new DoubleRaster(width, height, 3) {
      private static final long serialVersionUID = 7791621586618189836L;
      protected Color getPixel(double[] raster, int index) {
        return new XYZColor(raster[index], raster[index + 1], raster[index + 2]);
      }
    };
  }

  @Override
  public int getNumChannels() {
    return 3;
  }

  @Override
  public String getChannelName(int channel) {
    switch (channel) {
    case 0: return "X";
    case 1: return "Y";
    case 2: return "Z";
    default: throw new IndexOutOfBoundsException("channel must be between 0 and 2 inclusive");
    }
  }

}
