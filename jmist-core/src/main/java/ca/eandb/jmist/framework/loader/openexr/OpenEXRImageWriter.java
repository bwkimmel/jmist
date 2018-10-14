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
package ca.eandb.jmist.framework.loader.openexr;

import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;

/**
 * @author brad
 *
 */
public final class OpenEXRImageWriter extends ImageWriter {

  public OpenEXRImageWriter(ImageWriterSpi originatingProvider) {
    super(originatingProvider);
    // TODO Auto-generated constructor stub
  }

  @Override
  public IIOMetadata convertImageMetadata(IIOMetadata inData,
      ImageTypeSpecifier imageType, ImageWriteParam param) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IIOMetadata convertStreamMetadata(IIOMetadata inData,
      ImageWriteParam param) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier image,
      ImageWriteParam param) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IIOMetadata getDefaultStreamMetadata(ImageWriteParam param) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void write(IIOMetadata streamMetadata, IIOImage image, ImageWriteParam param)
      throws IOException {
    // TODO Auto-generated method stub

  }

}
