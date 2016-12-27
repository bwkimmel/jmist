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
@OpenEXRAttributeType("chromaticities")
public final class Chromaticities implements Attribute {

  public static final Chromaticities DEFAULT = new Chromaticities(
      0.6400f, 0.3300f,
      0.3000f, 0.6000f,
      0.1500f, 0.0600f,
      0.3127f, 0.3290f);
  public static final Chromaticities XYZ = new Chromaticities(
      1.0000f, 0.0000f,
      0.0000f, 1.0000f,
      0.0000f, 0.0000f,
      1.0f/3.0f, 1.0f/3.0f);

  private final float redX;
  private final float redY;
  private final float greenX;
  private final float greenY;
  private final float blueX;
  private final float blueY;
  private final float whiteX;
  private final float whiteY;

  public Chromaticities(float redX, float redY, float greenX, float greenY, float blueX, float blueY, float whiteX, float whiteY) {
    this.redX = redX;
    this.redY = redY;
    this.greenX = greenX;
    this.greenY = greenY;
    this.blueX = blueX;
    this.blueY = blueY;
    this.whiteX = whiteX;
    this.whiteY = whiteY;
  }

  public static Chromaticities read(DataInput in, int size) throws IOException {
    return new Chromaticities(
        in.readFloat(), in.readFloat(),
        in.readFloat(), in.readFloat(),
        in.readFloat(), in.readFloat(),
        in.readFloat(), in.readFloat());
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeFloat(redX);
    out.writeFloat(redY);
    out.writeFloat(greenX);
    out.writeFloat(greenY);
    out.writeFloat(blueX);
    out.writeFloat(blueY);
    out.writeFloat(whiteX);
    out.writeFloat(whiteY);
  }

}
