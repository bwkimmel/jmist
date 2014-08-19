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
package ca.eandb.jmist.proto.factory;

import com.google.common.primitives.Doubles;

import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.function.PiecewiseLinearFunction1;
import ca.eandb.jmist.proto.CoreProtos;
import ca.eandb.jmist.util.ArrayUtil;

/**
 *
 */
public final class ProtoColorFactory {

  private final ColorModel colorModel;

  public ProtoColorFactory(ColorModel colorModel) {
    this.colorModel = colorModel;
  }

  public Spectrum createSpectrum(CoreProtos.Color colorIn) {
    switch (colorIn.getType()) {
      case RGB:
        return createRGBSpectrum(colorIn);
      case XYZ:
        return createXYZSpectrum(colorIn);
      case GREY:
        return createGreySpectrum(colorIn);
      case SPECTRUM:
        return createPointwiseSpectrum(colorIn);
      default:
        throw new IllegalArgumentException(String.format(
            "Invalid color type: %d", colorIn.getType().getNumber()));
    }
  }

  private Spectrum createRGBSpectrum(CoreProtos.Color colorIn) {
    if (colorIn.getChannelsCount() != 3) {
      throw new IllegalArgumentException(String.format(
          "Expected RGB color to have 3 channels, but got %d.",
          colorIn.getChannelsCount()));
    }

    return colorModel.fromRGB(colorIn.getChannels(0),
                              colorIn.getChannels(1),
                              colorIn.getChannels(2));
  }

  private Spectrum createXYZSpectrum(CoreProtos.Color colorIn) {
    if (colorIn.getChannelsCount() != 3) {
      throw new IllegalArgumentException(String.format(
          "Expected XYZ color to have 3 channels, but got %d.",
          colorIn.getChannelsCount()));
    }

    return colorModel.fromXYZ(colorIn.getChannels(0),
                              colorIn.getChannels(1),
                              colorIn.getChannels(2));
  }

  private Spectrum createGreySpectrum(CoreProtos.Color colorIn) {
    if (colorIn.getChannelsCount() != 1) {
      throw new IllegalArgumentException(String.format(
          "Expected grey color to have 1 channel, but got %d.",
          colorIn.getChannelsCount()));
    }

    return colorModel.getGray(colorIn.getChannels(0));
  }

  private Spectrum createPointwiseSpectrum(CoreProtos.Color colorIn) {
    if (colorIn.getChannelsCount() < 1) {
      throw new IllegalArgumentException(
          "Expected at least one channel for spectrum color.");
    }

    if (colorIn.getWavelengthsCount() == colorIn.getChannelsCount()) {
      return colorModel.getContinuous(
          new PiecewiseLinearFunction1(
              Doubles.toArray(colorIn.getWavelengthsList()),
              Doubles.toArray(colorIn.getChannelsList())));
    } else if (colorIn.getWavelengthsCount() == 2) {  // uniform spacing
       return colorModel.getContinuous(
           new PiecewiseLinearFunction1(
               ArrayUtil.range(colorIn.getWavelengths(0),
                               colorIn.getWavelengths(1),
                               colorIn.getChannelsCount()),
               Doubles.toArray(colorIn.getChannelsList())));
    }

    throw new IllegalArgumentException(String.format(
        "Expected number of wavelengths to be 2 or to be equal to the number " +
        "of channels (%d) for spectrum color, but got %d.",
        colorIn.getChannelsCount(),
        colorIn.getWavelengthsCount()));
  }

}
