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
public enum PixelType implements Attribute {

  UINT(0, 4),
  HALF(1, 2),
  FLOAT(2, 4);
  
  private final int key;
  
  private final int sampleSize;
  
  private PixelType(int key, int sampleSize) {
    this.key = key;
    this.sampleSize = sampleSize;
  }
  
  /**
   * Gets the size of a sample of this type, in bytes.
   * @return The size of a sample of this type, in bytes.
   */
  public int getSampleSize() {
    return sampleSize;
  }
  
  public static PixelType read(DataInput in) throws IOException {
    int key = in.readInt();
    for (PixelType type : PixelType.values()) {
      if (type.key == key) {
        return type;
      }
    }
    return null;
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(key);
  }
  
}
