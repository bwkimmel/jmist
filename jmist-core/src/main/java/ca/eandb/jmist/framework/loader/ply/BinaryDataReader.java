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

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import ca.eandb.util.io.LittleEndianDataInputStream;

/**
 * A <code>DataReader</code> that reads binary data.
 */
final class BinaryDataReader implements DataReader {

  /** The <code>DataInput</code> source to read from. */
  private final DataInput input;

  /**
   * Creates a new <code>BinaryDataReader</code> (private, use factory
   * methods to create instances).
   * @param input The <code>DataInput</code> source to read from.
   * @see #littleEndian(InputStream)
   * @see #bigEndian(InputStream)
   */
  private BinaryDataReader(DataInput input) {
    this.input = input;
  }

  /**
   * Creates a new <code>BinaryDataReader</code> that reads binary data in
   * little-endian from the provided <code>InputStream</code>.
   * @param in The <code>InputStream</code> to read from.
   */
  public static BinaryDataReader littleEndian(InputStream in) {
    return new BinaryDataReader(new LittleEndianDataInputStream(in));
  }

  /**
   * Creates a new <code>BinaryDataReader</code> that reads binary data in
   * big-endian from the provided <code>InputStream</code>.
   * @param in The <code>InputStream</code> to read from.
   */
  public static BinaryDataReader bigEndian(InputStream in) {
    return new BinaryDataReader(new DataInputStream(in));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readChar()
   */
  @Override
  public byte readChar() throws IOException {
    return input.readByte();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readUnsignedChar()
   */
  @Override
  public int readUnsignedChar() throws IOException {
    return input.readUnsignedByte();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readShort()
   */
  @Override
  public short readShort() throws IOException {
    return input.readShort();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readUnsignedShort()
   */
  @Override
  public int readUnsignedShort() throws IOException {
    return input.readUnsignedShort();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readInt()
   */
  @Override
  public int readInt() throws IOException {
    return input.readInt();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readUnsignedInt()
   */
  @Override
  public long readUnsignedInt() throws IOException {
    int signed = input.readInt();
    return signed > 0 ? (long) signed : (0x100000000L + signed);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readFloat()
   */
  @Override
  public float readFloat() throws IOException {
    return input.readFloat();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readDouble()
   */
  @Override
  public double readDouble() throws IOException {
    return input.readDouble();
  }

}
