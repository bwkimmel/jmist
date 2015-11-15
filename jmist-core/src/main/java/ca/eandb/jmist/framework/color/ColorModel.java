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
package ca.eandb.jmist.framework.color;

import java.io.Serializable;

import ca.eandb.jmist.framework.Function1;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.Raster;

/**
 * A strategy for representing colors.  By varying the <code>ColorModel</code>
 * used in a rendering job, one can perform basic rendering in RGB space, or
 * full spectral rendering, for example.
 *
 * A <code>ColorModel</code> is essentially a factory for other color-related
 * classes.
 *
 * @see Spectrum
 * @see Color
 * @see WavelengthPacket
 * @see Raster
 *
 * @author Brad Kimmel
 */
public abstract class ColorModel implements Serializable {

  /** Serialization version ID. */
  private static final long serialVersionUID = -102213996206421899L;

  /**
   * A <code>Spectrum</code> representing the absense of light.
   * @return A <code>Spectrum</code> representing the absense of light.
   */
  public abstract Spectrum getBlack();

  /**
   * A <code>Spectrum</code> representing unit reflectance.
   * @return A <code>Spectrum</code> representing unit reflectance.
   */
  public abstract Spectrum getWhite();

  /**
   * Creates a <code>Spectrum</code> approximating an RGB triple.
   * @param r The red component.
   * @param g The green component.
   * @param b The blue component.
   * @return The new <code>Spectrum</code>.
   */
  public abstract Spectrum fromRGB(double r, double g, double b);

  /**
   * Creates a <code>Spectrum</code> approximating an RGB triple.
   * @param rgb The <code>RGB</code> triple to approximate.
   * @return The new <code>Spectrum</code>.
   */
  public Spectrum fromRGB(RGB rgb) {
    return fromRGB(rgb.r(), rgb.g(), rgb.b());
  }

  /**
   * Creates a <code>Spectrum</code> approximating an CIE XYZ tristimulus
   * triple.
   * @param x The x component.
   * @param y The y component.
   * @param z The z component.
   * @return The new <code>Spectrum</code>.
   */
  public abstract Spectrum fromXYZ(double x, double y, double z);

  /**
   * Creates a <code>Spectrum</code> approximating an CIE XYZ tristimulus
   * triple.
   * @param xyz The <code>CIEXYZ</code> tristimulus triple.
   * @return The new <code>Spectrum</code>.
   */
  public Spectrum fromXYZ(CIEXYZ xyz) {
    return fromXYZ(xyz.X(), xyz.Y(), xyz.Z());
  }

  /**
   * Creates a uniform <code>Spectrum</code> with the specified intensity.
   * @param value The intensity.
   * @return The new <code>Spectrum</code>.
   */
  public abstract Spectrum getGray(double value);

  /**
   * Creates a <code>Spectrum</code> representing a continuous function.
   * @param spectrum A <code>Function1</code> that computes the value
   *     of the spectrum (intensity or reflectance), given the
   *     wavelength of light (in meters).
   * @return The new <code>Spectrum</code>.
   */
  public abstract Spectrum getContinuous(Function1 spectrum);

  /**
   * Creates a <code>Color</code> having the provided raw component values.
   * @param values An array of raw component values (must be the correct
   *     length for this <code>ColorModel</code>, as indicated by
   *     {@link #getNumChannels()}.
   * @param lambda The <code>WavelengthPacket</code> to associate wit the
   *     <code>Color</code>.
   * @return The <code>Color</code>.
   * @see Spectrum#sample(WavelengthPacket)
   * @see #getBlack()
   */
  public abstract Color fromArray(double[] values, WavelengthPacket lambda);

  /**
   * Creates a <code>Color</code> representing the absense of light.
   * Equivalent to <code>getBlack().sample(lambda)</code>.
   * @param lambda The <code>WavelengthPacket</code> at which to sample the
   *     spectrum.
   * @return The <code>Color</code>.
   * @see Spectrum#sample(WavelengthPacket)
   * @see #getBlack()
   */
  public abstract Color getBlack(WavelengthPacket lambda);

  /**
   * Creates a <code>Color</code> representing unit reflectance.
   * Equivalent to <code>getWhite().sample(lambda)</code>.
   * @param lambda The <code>WavelengthPacket</code> at which to sample the
   *     spectrum.
   * @return The <code>Color</code>.
   * @see Spectrum#sample(WavelengthPacket)
   * @see #getWhite()
   */
  public abstract Color getWhite(WavelengthPacket lambda);

  /**
   * Creates a greyscale color.
   * Equivalent to <code>getGrey(value).sample(lambda)</code>.
   * @param value The intensity.
   * @param lambda The <code>WavelengthPacket</code> at which to sample the
   *     spectrum.
   * @return The <code>Color</code>.
   * @see Spectrum#sample(WavelengthPacket)
   * @see #getGray(double)
   */
  public abstract Color getGray(double value, WavelengthPacket lambda);

  /**
   * Creates a random sample based on the spectral sensitivity of an observer
   * as represented by this <code>ColorModel</code>.  This represents the
   * source of <a href="http://www.seanet.com/~myandper/importance.htm">importance</a>
   * for illumination algorithms.
   * @param random The <code>Random</code> number generator to use.
   * @return The <code>Color</code> sample.
   */
  public abstract Color sample(Random random);

  /**
   * Creates a <code>Raster</code> representing a two-dimensional array of
   * code <code>Color</code>s which are compatible with this
   * <code>ColorModel</code>.
   * @param width The width of the raster image, in pixels.
   * @param height The height of the raster image, in pixels.
   * @return The new <code>Raster</code>.
   */
  public abstract Raster createRaster(int width, int height);

  /**
   * The number of channels for colors in this <code>ColorModel</code>.
   * @return The number of channels for colors in this <code>ColorModel</code>.
   */
  public abstract int getNumChannels();

  /**
   * Gets a label for the specified channel.
   * @param channel The index of the channel (must satisfy
   *     <code>0 &lt;= channel &lt; getNumChannels()</code>).
   * @return A label for the specified channel.
   */
  public abstract String getChannelName(int channel);

}
