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
package ca.eandb.jmist.framework.loader.openexr;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

/**
 * @author brad
 *
 */
public final class OpenEXRImageWriterSpi extends ImageWriterSpi {

  public OpenEXRImageWriterSpi() {
    // TODO Auto-generated constructor stub
  }

  public OpenEXRImageWriterSpi(String vendorName, String version,
      String[] names, String[] suffixes, String[] MIMETypes,
      String writerClassName, Class[] outputTypes,
      String[] readerSpiNames,
      boolean supportsStandardStreamMetadataFormat,
      String nativeStreamMetadataFormatName,
      String nativeStreamMetadataFormatClassName,
      String[] extraStreamMetadataFormatNames,
      String[] extraStreamMetadataFormatClassNames,
      boolean supportsStandardImageMetadataFormat,
      String nativeImageMetadataFormatName,
      String nativeImageMetadataFormatClassName,
      String[] extraImageMetadataFormatNames,
      String[] extraImageMetadataFormatClassNames) {
    super(vendorName, version, names, suffixes, MIMETypes, writerClassName,
        outputTypes, readerSpiNames,
        supportsStandardStreamMetadataFormat,
        nativeStreamMetadataFormatName,
        nativeStreamMetadataFormatClassName,
        extraStreamMetadataFormatNames,
        extraStreamMetadataFormatClassNames,
        supportsStandardImageMetadataFormat,
        nativeImageMetadataFormatName,
        nativeImageMetadataFormatClassName,
        extraImageMetadataFormatNames,
        extraImageMetadataFormatClassNames);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see javax.imageio.spi.ImageWriterSpi#canEncodeImage(javax.imageio.ImageTypeSpecifier)
   */
  @Override
  public boolean canEncodeImage(ImageTypeSpecifier type) {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see javax.imageio.spi.ImageWriterSpi#createWriterInstance(java.lang.Object)
   */
  @Override
  public ImageWriter createWriterInstance(Object extension)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
   */
  @Override
  public String getDescription(Locale locale) {
    // TODO Auto-generated method stub
    return null;
  }

}
