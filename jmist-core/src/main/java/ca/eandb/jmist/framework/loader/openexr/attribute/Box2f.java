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
package ca.eandb.jmist.framework.loader.openexr.attribute;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@OpenEXRAttributeType("box2f")
public final class Box2f implements Attribute {

  private final float xMin;
  private final float yMin;
  private final float xMax;
  private final float yMax;

  public Box2f(float xMin, float yMin, float xMax, float yMax) {
    this.xMin = xMin;
    this.yMin = yMin;
    this.xMax = xMax;
    this.yMax = yMax;
  }

  public float getXMin() {
    return xMin;
  }

  public float getYMin() {
    return yMin;
  }

  public float getXMax() {
    return xMax;
  }

  public float getYMax() {
    return yMax;
  }

  public float getXSize() {
    return xMax - xMin;
  }

  public float getYSize() {
    return yMax - yMin;
  }

  public static Box2f read(DataInput in, int size) throws IOException {
    return new Box2f(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeFloat(xMin);
    out.writeFloat(yMin);
    out.writeFloat(xMax);
    out.writeFloat(yMax);
  }

}
