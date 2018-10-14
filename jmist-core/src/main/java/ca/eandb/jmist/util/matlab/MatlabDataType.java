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
package ca.eandb.jmist.util.matlab;

/**
 * The types of the MATLAB elements in a MAT-file.
 * @author Brad Kimmel
 */
public enum MatlabDataType {

  /** Signed 8-bit integer. */
  INT8(1,1),

  /** Unsigned 8-bit integer. */
  UINT8(2,1),

  /** Signed 16-bit integer. */
  INT16(3,2),

  /** Unsigned 16-bit integer. */
  UINT16(4,2),

  /** Signed 32-bit integer. */
  INT32(5,4),

  /** Unsigned 32-bit integer. */
  UINT32(6,4),

  /** IEEE 754 single precision floating point. */
  SINGLE(7,4),

  /** IEEE 754 double precision floating point. */
  DOUBLE(9,8),

  /** Signed 64-bit integer. */
  INT64(12,8),

  /** Unsigned 64-bit integer. */
  UINT64(13,8),

  /** An array, structure, cell, or object. */
  MATRIX(14),

  /** GZIP-compressed data. */
  COMPRESSED(15),

  /** UTF-8 String. */
  UTF8(16),

  /** UTF-16 String. */
  UTF16(17),

  /** UTF-32 String. */
  UTF32(18);

  /** The value that signifies this type in the MAT-file. */
  public final int value;

  /**
   * The size (in bytes) of items of this type (or zero if not applicable).
   */
  public final int size;

  /**
   * Creates a new <code>MatlabDataType</code> with no applicable size.
   * @param value The value that signifies this type in the MAT-file.
   */
  MatlabDataType(int value) {
    this.value = value;
    this.size = 0;
  }

  /**
   * Creates a new <code>MatlabDataType</code>.
   * @param value The value that signifies this type in the MAT-file.
   * @param size The size (in bytes) of a single item of this type.
   */
  MatlabDataType(int value, int size) {
    this.value = value;
    this.size = size;
  }

}
