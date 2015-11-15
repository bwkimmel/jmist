/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2014 Bradley W. Kimmel
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
package ca.eandb.jmist.framework.loader.ply;

/**
 * A enumeration of the primitive data types supported by the PLY format.
 * @see <a href="http://paulbourke.net/dataformats/ply/">PLY - Polygon File Format</a>
 */
public enum PlyDataType {

  /** Signed 8-bit integer (char). */
  CHAR("char", 1, Byte.MIN_VALUE, Byte.MAX_VALUE),

  /** Unsigned 8-bit integer (uchar). */
  UCHAR("uchar", 1, 0, 0xFF),

  /** Signed 16-bit integer (short). */
  SHORT("short", 2, Short.MIN_VALUE, Short.MAX_VALUE),

  /** Unsigned 16-bit integer (ushort). */
  USHORT("ushort", 2, 0, 0xFFFF),

  /** Signed 32-bit integer (int). */
  INT("int", 4, Integer.MIN_VALUE, Integer.MAX_VALUE),

  /** Unsigned 32-bit integer (uint). */
  UINT("uint", 4, 0, 0xFFFFFFFFL),

  /** Single-precision 32-bit floating point (float). */
  FLOAT("float", 4, 0, 0),

  /** Double-precision 64-bit floating point (double). */
  DOUBLE("double", 8, 0, 0);

  /** The name of the data type. */
  private final String name;

  /** The size of the data type, in bytes. */
  private final int size;

  /** The minimum value (for integer types), or zero for floating point. */
  private final long maxValue;

  /** The maximum value (for integer types), or zero for floating point. */
  private final long minValue;

  /**
   * Creates a new <code>PlyDataType</code>.
   * @param name The name of the data type.
   * @param size The size of the data type, in bytes.
   * @param minValue The minimum value (for integer types), or zero for
   *     floating point types.
   * @param maxValue The maximum value (for integer types), or zero for
   *     floating point types.
   */
  private PlyDataType(String name, int size, long minValue, long maxValue) {
    this.name = name;
    this.size = size;
    this.minValue = minValue;
    this.maxValue = maxValue;
  }

  /**
   * Gets the <code>PlyDataType</code> corresponding to the data type name as
   * read from the PLY-file.
   * @param name The name of the data type as read from the PLY-file.
   * @return A <code>PlyDataType</code>.
   */
  public static PlyDataType fromString(String name) {
    switch (name.toLowerCase()) {
    case "char":
    case "int8":
      return CHAR;

    case "uchar":
    case "uint8":
      return UCHAR;

    case "short":
    case "int16":
      return SHORT;

    case "ushort":
    case "uint16":
      return USHORT;

    case "int":
    case "int32":
      return INT;

    case "uint":
    case "uint32":
      return UINT;

    case "float":
    case "float32":
      return FLOAT;

    case "double":
    case "float64":
      return DOUBLE;

    default:
      throw new IllegalArgumentException(String.format(
          "No such PlyDataType (%s)", name));
    }
  }

  /**
   * Gets the name of the data type.
   * @return The name of the data type.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the size of the data type, in bytes.
   * @return The size of the data type, in bytes.
   */
  public int getSize() {
    return size;
  }

  /**
   * Gets a value indicating if this data type represents an unsigned
   * integral type.
   * @return A value indicating if this data type represents an unsigned
   *     integral type.
   */
  public boolean isUnsigned() {
    return minValue == 0 && maxValue > 0;
  }

  /**
   * Gets a value indicating if this data type represents a signed integral
   * type.
   * @return A value indicating if this data type represents a signed
   *     integral type.
   */
  public boolean isSigned() {
    return minValue < 0;
  }

  /**
   * Gets the minimum value supported by this type (for integer types) or
   * zero for floating point types.
   * @return The minimum value supported by this type (for integer types) or
   * zero for floating point types.
   */
  public long getMinValue() {
    return minValue;
  }

  /**
   * Gets the maximum value supported by this type (for integer types) or
   * zero for floating point types.
   * @return The maximum value supported by this type (for integer types) or
   * zero for floating point types.
   */
  public long getMaxValue() {
    return maxValue;
  }

  /**
   * Gets a value indicating if this data type represents a floating point
   * type.
   * @return A value indicating if this data type represents a floating point
   *     type.
   */
  public boolean isFloatingPoint() {
    return maxValue == 0;
  }

  /**
   * Gets a value indicating if this data type represents an integral type.
   * @return A value indicating if this data type represents an integral
   *     type.
   */
  public boolean isIntegral() {
    return maxValue > 0;
  }

}
