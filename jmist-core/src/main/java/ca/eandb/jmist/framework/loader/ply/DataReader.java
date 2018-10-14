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
package ca.eandb.jmist.framework.loader.ply;

import java.io.IOException;

/**
 * Reads the numeric data forming the body of a PLY-file.
 */
interface DataReader {

  /** Reads a signed byte value (char). */
  byte readChar() throws IOException;

  /** Reads an unsigned byte value (uchar). */
  int readUnsignedChar() throws IOException;

  /** Reads a signed short value (short). */
  short readShort() throws IOException;

  /** Reads an unsigned short value (ushort). */
  int readUnsignedShort() throws IOException;

  /** Reads a signed integer value (int). */
  int readInt() throws IOException;

  /** Reads an unsigned integer value (uint). */
  long readUnsignedInt() throws IOException;

  /** Reads a single-precision floating point value (float). */
  float readFloat() throws IOException;

  /** Reads a double-precision floating point value (double). */
  double readDouble() throws IOException;

}
