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
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A <code>DataReader</code> that reads whitespace-separated ASCII data.
 */
final class AsciiDataReader implements DataReader {

  /** The <code>Scanner</code> to use to parse data. */
  private final Scanner scanner;

  /**
   * Creates a new <code>AsciiDataReader</code>.
   * @param in The <code>InputStream</code> to read from.
   */
  public AsciiDataReader(InputStream in) {
    this.scanner = new Scanner(in);
  }

  /**
   * Ensures that the specified value is between zero and the specified
   * maximum.
   * @param value The value to check
   * @param max The maximum allowable value
   * @return The provided <code>value</code>, if it passes validation
   * @throws InputMismatchException if <code>value &lt; 0</code> or
   *     <code>value &gt; max</code>.
   */
  private int checkUnsigned(int value, int max) {
    if (value < 0 || value > max) {
      throw new InputMismatchException("Unsigned value out of range.");
    }
    return value;
  }

  /**
   * Ensures that the specified value is between zero and the specified
   * maximum.
   * @param value The value to check
   * @param max The maximum allowable value
   * @return The provided <code>value</code>, if it passes validation
   * @throws InputMismatchException if <code>value &lt; 0</code> or
   *     <code>value &gt; max</code>.
   */
  private long checkUnsigned(long value, long max) {
    if (value < 0 || value > max) {
      throw new InputMismatchException("Unsigned value out of range.");
    }
    return value;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readChar()
   */
  @Override
  public byte readChar() throws IOException {
    return scanner.nextByte();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readUnsignedChar()
   */
  @Override
  public int readUnsignedChar() throws IOException {
    return checkUnsigned(scanner.nextInt(), 0xFF);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readShort()
   */
  @Override
  public short readShort() throws IOException {
    return scanner.nextShort();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readUnsignedShort()
   */
  @Override
  public int readUnsignedShort() throws IOException {
    return checkUnsigned(scanner.nextInt(), 0xFFFF);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readInt()
   */
  @Override
  public int readInt() throws IOException {
    return scanner.nextInt();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readUnsignedInt()
   */
  @Override
  public long readUnsignedInt() throws IOException {
    return checkUnsigned(scanner.nextLong(), 0xFFFFFFFFL);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readFloat()
   */
  @Override
  public float readFloat() throws IOException {
    return scanner.nextFloat();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.loader.ply.DataReader#readDouble()
   */
  @Override
  public double readDouble() throws IOException {
    return scanner.nextDouble();
  }

}
