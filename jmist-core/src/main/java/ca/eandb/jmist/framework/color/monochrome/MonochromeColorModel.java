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
package ca.eandb.jmist.framework.color.monochrome;

import java.io.Serializable;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;
import ca.eandb.jmist.framework.color.CIEXYZ;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.DoubleRaster;
import ca.eandb.jmist.framework.color.RGB;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.MathUtil;

/**
 * @author Brad
 *
 */
public final class MonochromeColorModel implements ColorModel {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 7290649793402973937L;

  private final double wavelength;

  private final Sample black = new Sample(0);

  private final Sample white = new Sample(1);

  private final WavelengthPacket lambda = new MonochromeWavelengthPacket();

  private final class MonochromeWavelengthPacket implements WavelengthPacket, Serializable {

    /** Serialization version ID. */
    private static final long serialVersionUID = 5347416797625974900L;

    @Override
    public ColorModel getColorModel() {
      return MonochromeColorModel.this;
    }
  }

  /**
   * @author Brad
   *
   */
  private final class Sample implements Color, Spectrum, Serializable {

    /**
     * Serialization version ID.
     */
    private static final long serialVersionUID = -5632987671182335721L;

    private final double value;

    public Sample(double value) {
      this.value = value;
    }

    @Override
    public CIEXYZ toXYZ() {
      return ColorUtil.convertSample2XYZ(wavelength, value);
    }

    /* s(non-Javadoc)
     * @see ca.eandb.jmist.framework.color.Color#toRGB()
     */
    public RGB toRGB() {
      return ColorUtil.convertSample2RGB(wavelength, value);
    }

    @Override
    public Color abs() {
      return new Sample(Math.abs(value));
    }

    @Override
    public Color divide(Color other) {
      return divide((Sample) other);
    }

    public Sample divide(Sample other) {
      return new Sample(value / other.value);
    }

    @Override
    public Color divide(double c) {
      return new Sample(value / c);
    }

    @Override
    public Color exp() {
      return new Sample(Math.exp(value));
    }

    @Override
    public Color invert() {
      return new Sample(1.0 / value);
    }

    @Override
    public double luminance() {
      return ColorUtil.convertSample2Luminance(wavelength, value);
    }

    @Override
    public Color minus(Color other) {
      return minus((Sample) other);
    }

    public Sample minus(Sample other) {
      return new Sample(value - other.value);
    }

    @Override
    public Color negative() {
      return new Sample(-value);
    }

    @Override
    public Color plus(Color other) {
      return plus((Sample) other);
    }

    public Sample plus(Sample other) {
      return new Sample(value + other.value);
    }

    @Override
    public Color pow(Color other) {
      return pow((Sample) other);
    }

    public Sample pow(Sample other) {
      return new Sample(Math.pow(value, other.value));
    }

    @Override
    public Color pow(double e) {
      return new Sample(Math.pow(value, e));
    }

    @Override
    public Color sqrt() {
      return new Sample(Math.sqrt(value));
    }

    @Override
    public Color times(Color other) {
      return times((Sample) other);
    }

    public Sample times(Sample other) {
      return new Sample(value * other.value);
    }

    @Override
    public Color times(double c) {
      return new Sample(value * c);
    }

    @Override
    public Color clamp(double min, double max) {
      return new Sample(MathUtil.clamp(value, min, max));
    }

    @Override
    public Color clamp(double max) {
      return new Sample(Math.min(value, max));
    }

    @Override
    public Color disperse(int channel) {
      if (channel != 0) {
        throw new IndexOutOfBoundsException();
      }
      return this;
    }

    @Override
    public ColorModel getColorModel() {
      return MonochromeColorModel.this;
    }

    @Override
    public double getValue(int channel) {
      if (channel != 0) {
        throw new IndexOutOfBoundsException();
      }
      return value;
    }

    @Override
    public double[] toArray() {
      return new double[]{ value };
    }

    @Override
    public WavelengthPacket getWavelengthPacket() {
      return lambda;
    }

    @Override
    public Color sample(WavelengthPacket lambda) {
      return this;
    }

  }

  public MonochromeColorModel(double wavelength) {
    this.wavelength = wavelength;
  }

  @Override
  public Spectrum fromRGB(double r, double g, double b) {
    // TODO choose reflectance at wavelength from RGB triple.
    return new Sample(g);
  }

  public Spectrum fromXYZ(double x, double y, double z) {
    // TODO choose reflectance at wavelength from XYZ triple.
    return new Sample(y);
  }

  @Override
  public Spectrum getContinuous(Function1 spectrum) {
    return new Sample(spectrum.evaluate(wavelength));
  }

  @Override
  public Spectrum getBlack() {
    return black;
  }

  @Override
  public Spectrum getGray(double value) {
    return new Sample(value);
  }

  @Override
  public Spectrum getWhite() {
    return white;
  }

  @Override
  public int getNumChannels() {
    return 1;
  }

  @Override
  public Color fromArray(double[] values, WavelengthPacket lambda) {
    if (values.length < 1) {
      throw new IllegalArgumentException("values.length < 1");
    }
    return new Sample(values[0]);
  }

  @Override
  public Color getBlack(WavelengthPacket lambda) {
    return black;
  }

  @Override
  public Color getGray(double value, WavelengthPacket lambda) {
    return new Sample(value);
  }

  @Override
  public Color getWhite(WavelengthPacket lambda) {
    return white;
  }

  @Override
  public Color sample(Random random) {
    return white;
  }

  @Override
  public Raster createRaster(int width, int height) {
    return new DoubleRaster(width, height, 1) {
      private static final long serialVersionUID = -2403693898531527409L;
      protected Color getPixel(double[] raster, int index) {
        return new Sample(raster[index]);
      }
    };
  }

  @Override
  public String getChannelName(int channel) {
    if (channel != 0) {
      throw new IllegalArgumentException("Invalid channel");
    }
    return String.format("%em", wavelength);
  }

}
