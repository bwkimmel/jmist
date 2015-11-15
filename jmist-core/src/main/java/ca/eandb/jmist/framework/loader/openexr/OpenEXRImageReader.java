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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

/**
 * @author brad
 *
 */
public final class OpenEXRImageReader extends ImageReader {

  public OpenEXRImageReader(ImageReaderSpi originatingProvider) {
    super(originatingProvider);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#getHeight(int)
   */
  @Override
  public int getHeight(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#getImageMetadata(int)
   */
  @Override
  public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#getImageTypes(int)
   */
  @Override
  public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#getNumImages(boolean)
   */
  @Override
  public int getNumImages(boolean allowSearch) throws IOException {
    return 1;
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#getStreamMetadata()
   */
  @Override
  public IIOMetadata getStreamMetadata() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#getWidth(int)
   */
  @Override
  public int getWidth(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see javax.imageio.ImageReader#read(int, javax.imageio.ImageReadParam)
   */
  @Override
  public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

}
