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
package ca.eandb.jmist.framework.loader.dxf;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;


/**
 * @author Brad
 *
 */
public final class AsciiDxfReader implements DxfReader {

  private static final class AsciiDxfException extends DxfException {

    /** Serialization version ID. */
    private static final long serialVersionUID = -360078728659801927L;

    /**
     * @param message
     * @param cause
     */
    public AsciiDxfException(int lineNumber, String message, Throwable cause) {
      super(String.format("%d: %s", lineNumber, message), cause);
    }

    /**
     * @param message
     */
    public AsciiDxfException(int lineNumber, String message) {
      super(String.format("%d: %s", lineNumber, message));
    }

  }

  /**
   * @author Brad
   *
   */
  private static final class AsciiDxfElement extends AbstractDxfElement {

    private final String valueLine;

    private final int lineNumber;

    /**
     * @param groupCode
     */
    public AsciiDxfElement(int groupCode, String valueLine, int lineNumber) {
      super(groupCode);
      this.valueLine = valueLine;
      this.lineNumber = lineNumber;
    }

    @Override
    public boolean getBooleanValue() {
      try {
        return Integer.parseInt(valueLine) != 0;
      } catch (NumberFormatException e) {
        throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
      }
    }

    @Override
    public double getFloatValue() {
      try {
        return Double.parseDouble(valueLine);
      } catch (NumberFormatException e) {
        throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
      }
    }

    @Override
    public int getIntegerValue() {
      try {
        return Integer.parseInt(valueLine);
      } catch (NumberFormatException e) {
        throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
      }
    }

    @Override
    public long getLongValue() {
      try {
        return Long.parseLong(valueLine);
      } catch (NumberFormatException e) {
        throw new AsciiDxfException(lineNumber + 1, "Incorrect value type", e);
      }
    }

    @Override
    public String getStringValue() {
      return valueLine;
    }

  }

  private final LineNumberReader reader;

  private DxfElement currentElement = null;

  private boolean eof = false;

  public AsciiDxfReader(Reader reader) {
    this.reader = new LineNumberReader(reader);
  }

  @Override
  public synchronized DxfElement getCurrentElement() {
    if (!eof && currentElement == null) {
      advance();
    }
    return currentElement;
  }

  @Override
  public synchronized void advance() throws DxfException {
    if (eof) {
      currentElement = null;
      return;
    }

    int ln = 1 + reader.getLineNumber();

    String groupCodeLine, valueLine;
    try {
      groupCodeLine = reader.readLine().trim();
      valueLine = reader.readLine().trim();
    } catch (IOException e) {
      throw new AsciiDxfException(ln, "Error reading DXF stream", e);
    }

    if (groupCodeLine == null || valueLine == null) {
      throw new AsciiDxfException(ln, "Unexpected end of file");
    }

    int groupCode;
    try {
      groupCode = Integer.parseInt(groupCodeLine);
    } catch (NumberFormatException e) {
      throw new AsciiDxfException(ln, "Invalid group code", e);
    }

    if (valueLine.equals("EOF")) {
      eof = true;
    }

    currentElement = new AsciiDxfElement(groupCode, valueLine, ln);
  }

}
