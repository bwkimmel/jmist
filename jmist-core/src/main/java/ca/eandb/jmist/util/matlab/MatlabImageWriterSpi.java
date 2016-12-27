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
package ca.eandb.jmist.util.matlab;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

/**
 * An <code>ImageWriterSpi</code> that creates <code>ImageWriter</code>s
 * for writing images to MATLAB MAT-files.
 * @author Brad Kimmel
 */
public final class MatlabImageWriterSpi extends ImageWriterSpi {

  /**
   * Creates a new <code>MatlabImageWriterSpi</code>.
   */
  public MatlabImageWriterSpi() {
    super(VENDOR_NAME, VERSION, NAMES, SUFFIXES, MIME_TYPES,
        MatlabImageWriterSpi.class.getCanonicalName(),
        new Class[]{ ImageOutputStream.class }, null, false, null,
        null, null, null, false, null, null, null, null);
  }

  @Override
  public boolean canEncodeImage(ImageTypeSpecifier type) {
    return true;
  }

  @Override
  public ImageWriter createWriterInstance(Object extension) throws IOException {
    return new MatlabImageWriter(this);
  }

  @Override
  public String getDescription(Locale locale) {
    return "jMIST Matlab Image Writer SPI";
  }

  /** Vendor name. */
  private static final String VENDOR_NAME = "jmist.org";

  /** Version. */
  private static final String VERSION = "0.1";

  /** The format names that this <code>ImageWriterSpi</code> handles. */
  private static final String[] NAMES = new String[] { "mat" };

  /**
   * The suffixes for the files corresponding to the formats in
   * {@link #NAMES}.
   */
  private static final String[] SUFFIXES = new String[] { "mat" };

  /**
   * The MIME types for the file formats corresponding to {@link #NAMES}.
   */
  private static final String[] MIME_TYPES = new String[] { "application/x-matlab" };

}
