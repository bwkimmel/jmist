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

import java.io.IOException;

import ca.eandb.util.DoubleArray;
import ca.eandb.util.IntegerArray;
import ca.eandb.util.UnexpectedException;

/**
 * Describes a property within an element of a PLY-file.
 * @see http://paulbourke.net/dataformats/ply/
 */
public final class PropertyDescriptor {

  /** The name of the property. */
  private final String name;

  /**
   * The type used to store the length of the list, or <code>null</code> if
   * this property is a singleton.
   */
  private final PlyDataType countType;

  /** The type used to store the value (or the entries of a list). */
  private final PlyDataType dataType;

  /**
   * Creates a new <code>PropertyDescriptor</code>.  This constructor is
   * private.  Use the provided factory methods to create instances.
   * @param name The name of the property.
   * @param countType The <code>PlyDataType</code> used to store the length
   *     of a list, or <code>null</code> if this property is a singleton.
   *     This must not be a floating point type.
   * @param dataType The <code>PlyDataType</code> used to store the value, or
   *     to store the entries if this property is a list.
   * @throws IllegalArgumentException If <code>countType</code> is a floating
   *     point type.
   * @see #singleton(String, PlyDataType)
   * @see #list(String, PlyDataType, PlyDataType)
   */
  private PropertyDescriptor(String name, PlyDataType countType, PlyDataType dataType) {
    this.name = name;
    this.countType = countType;
    this.dataType = dataType;

    if (countType != null) {
      switch (countType) {
      case DOUBLE:
      case FLOAT:
        throw new IllegalArgumentException(String.format(
            "Invalid count data type (%s)", countType));
      default: /* nothing to do. */
      }
    }
  }

  /**
   * Creates a <code>PropertyDescriptor</code> for a singleton property.
   * @param name The name of the property.
   * @param dataType The <code>PlyDataType</code> used to store the value.
   * @return A new <code>PropertyDescriptor</code>.
   */
  public static PropertyDescriptor singleton(String name, PlyDataType dataType) {
    return new PropertyDescriptor(name, null, dataType);
  }

  /**
   * Creates a <code>PropertyDescriptor</code> for a list property.
   * @param name The name of the property.
   * @param countType The <code>PlyDataType</code> used to store the length
   *     of a list.  This must not be a floating point type.
   * @param dataType The <code>PlyDataType</code> used to store the entries
   *     in a list.
   * @return A new <code>PropertyDescriptor</code>.
   * @throws IllegalArgumentException if <code>countType == null</code> or if
   *     <code>countType</code> is a floating-point type.
   */
  public static PropertyDescriptor list(String name, PlyDataType countType, PlyDataType dataType) {
    if (countType == null) {
      throw new IllegalArgumentException("countType required");
    }
    return new PropertyDescriptor(name, countType, dataType);
  }

  /**
   * Gets the name of the property.
   * @return The name of the property.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets a value indicating if this property is a list (non-singleton)
   * property.
   * @return A value indicating if this property is a list (non-singleton)
   *     property.
   */
  public boolean isList() {
    return countType != null;
  }

  /**
   * Gets the <code>PlyDataType</code> used to store the length of a list.
   * @return The <code>PlyDataType</code> used to store the length of a list,
   *     or <code>null</code> if this property is a singleton.
   */
  public PlyDataType getCountType() {
    return countType;
  }

  /**
   * Gets the <code>PlyDataType</code> used to store the entries in a list.
   * @return The <code>PlyDataType</code> used to store the entries in a
   *     list.
   */
  public PlyDataType getDataType() {
    return dataType;
  }

  /**
   * Reads the number of entries from a PLY-file.
   * @param reader The <code>DataReader</code> to read from.
   * @return The length of the list, or <code>-1</code> if this property is a
   *     singleton.
   * @throws IOException If an error occurs while reading from the source.
   */
  private int readCount(DataReader reader) throws IOException {
    if (!isList()) {
      return -1;
    }

    int count;
    switch (countType) {
    case CHAR: count = reader.readChar(); break;
    case SHORT: count = reader.readShort(); break;
    case INT: count = reader.readInt(); break;
    case UCHAR: count = reader.readUnsignedChar(); break;
    case USHORT: count = reader.readUnsignedShort(); break;
    case UINT: count = (int) reader.readUnsignedInt(); break;
    default:
      throw new UnexpectedException(String.format(
          "Illegal count type (%s)", countType));
    }

    if (count < 0) {
      throw new RuntimeException("List count must be non-negative");
    }

    return count;
  }

  /**
   * Reads the property from the PLY-file.
   * @param reader The <code>DataReader</code> to read from.
   * @return The <code>PlyProperty</code> read from the file.
   * @throws IOException If an error occurs while reading from the source.
   */
  PlyProperty read(DataReader reader) throws IOException {
    int count = readCount(reader);
    if (count >= 0) {
      return readList(count, reader);
    } else {
      return readSingleton(reader);
    }
  }

  /**
   * Reads a singleton value from the PLY-file.
   * @param reader The <code>DataReader</code> to read from.
   * @return The <code>PlyProperty</code> read from the file.
   * @throws IOException If an error occurs while reading from the source.
   */
  private PlyProperty readSingleton(DataReader reader)
      throws IOException {
    switch (dataType) {
    case CHAR:
      return new PlyIntegralProperty(reader.readChar(), this);
    case SHORT:
      return new PlyIntegralProperty(reader.readShort(), this);
    case INT:
      return new PlyIntegralProperty(reader.readInt(), this);
    case UCHAR:
      return new PlyIntegralProperty(reader.readUnsignedChar(), this);
    case USHORT:
      return new PlyIntegralProperty(reader.readUnsignedShort(), this);
    case UINT:
      return new PlyIntegralProperty(reader.readUnsignedInt(), this);
    case FLOAT:
      return new PlyFloatProperty(reader.readFloat(), this);
    case DOUBLE:
      return new PlyFloatProperty(reader.readDouble(), this);
    default:
      throw new UnexpectedException(String.format(
          "Unknown data type (%s)", dataType));
    }
  }

  /**
   * Reads a list value from the PLY-file.
   * @param reader The <code>DataReader</code> to read from.
   * @return The <code>PlyProperty</code> read from the file.
   * @throws IOException If an error occurs while reading from the source.
   */
  private PlyProperty readList(int count, DataReader reader)
      throws IOException {
    switch (dataType) {
    case CHAR: {
      IntegerArray values = new IntegerArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readChar());
      }
      return new PlyIntegralListProperty(values, this);
    }
    case SHORT: {
      IntegerArray values = new IntegerArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readShort());
      }
      return new PlyIntegralListProperty(values, this);
    }
    case INT: {
      IntegerArray values = new IntegerArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readInt());
      }
      return new PlyIntegralListProperty(values, this);
    }
    case UCHAR: {
      IntegerArray values = new IntegerArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readUnsignedChar());
      }
      return new PlyIntegralListProperty(values, this);
    }
    case USHORT: {
      IntegerArray values = new IntegerArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readUnsignedShort());
      }
      return new PlyIntegralListProperty(values, this);
    }
    case UINT: {
      IntegerArray values = new IntegerArray(count);
      for (int i = 0; i < count; i++) {
        values.add((int) reader.readUnsignedInt());
      }
      return new PlyIntegralListProperty(values, this);
    }
    case FLOAT: {
      DoubleArray values = new DoubleArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readFloat());
      }
      return new PlyFloatListProperty(values, this);
    }
    case DOUBLE: {
      DoubleArray values = new DoubleArray(count);
      for (int i = 0; i < count; i++) {
        values.add(reader.readDouble());
      }
      return new PlyFloatListProperty(values, this);
    }
    default:
      throw new UnexpectedException(String.format(
          "Unknown data type (%s)", dataType));
    }
  }

}


