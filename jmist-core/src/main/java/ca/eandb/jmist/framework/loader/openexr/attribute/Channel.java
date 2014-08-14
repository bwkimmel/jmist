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
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;



/**
 * @author brad
 *
 */
public final class Channel implements Attribute {

  private final String name;

  private final PixelType pixelType;

  private final byte pLinear;

  private final int xSampling;

  private final int ySampling;

  public Channel(String name, PixelType pixelType) {
    this(name, pixelType, (byte) 0, 1, 1);
  }

  public Channel(String name, PixelType pixelType, int xSampling, int ySampling) {
    this(name, pixelType, (byte) 0, xSampling, ySampling);
  }

  public Channel(String name, PixelType pixelType, byte pLinear, int xSampling, int ySampling) {
    this.name = name;
    this.pixelType = pixelType;
    this.pLinear = pLinear;
    this.xSampling = xSampling;
    this.ySampling = ySampling;
    if (pLinear != 1 && pLinear != 0) {
      throw new IllegalArgumentException("pLinear must be 0 or 1");
    }
  }

  /**
   * @return the name
   */
  public final String getName() {
    return name;
  }

  /**
   * @return the pixelType
   */
  public final PixelType getPixelType() {
    return pixelType;
  }

  /**
   * @return the pLinear
   */
  public final byte getpLinear() {
    return pLinear;
  }

  /**
   * @return the xSampling
   */
  public final int getxSampling() {
    return xSampling;
  }

  /**
   * @return the ySampling
   */
  public final int getySampling() {
    return ySampling;
  }

  public static Channel read(DataInput in) throws IOException {
    String name = readString(in);
    if (name.isEmpty()) {
      return null;
    }
    PixelType pixelType = PixelType.read(in);
    byte pLinear = in.readByte();
    for (int i = 0; i < 3; i++) { in.readByte(); }
    int xSampling = in.readInt();
    int ySampling = in.readInt();
    return new Channel(name, pixelType, pLinear, xSampling, ySampling);
  }

  private static String readString(DataInput in) throws IOException {
    StringBuilder s = new StringBuilder();
    while (true) {
      int b = in.readByte();
      if (b <= 0) {
        break;
      }
      s.append((char) b);
    }
    return s.toString();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.openexr.Attribute#write(java.io.DataOutput)
   */
  @Override
  public void write(DataOutput out) throws IOException {
    out.writeBytes(name);
    out.writeByte(0);
    pixelType.write(out);
    out.writeByte(pLinear);
    for (int i = 0; i < 3; i++) { out.writeByte(0); }
    out.writeInt(xSampling);
    out.writeInt(ySampling);
  }

}
