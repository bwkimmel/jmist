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
package ca.eandb.jmist.framework.loader.dxf;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class DxfUtil {

  public static DxfReader createDxfReader(Reader reader) {
    return new AsciiDxfReader(reader);
  }

  public static Basis3 getBasisFromArbitraryAxis(Vector3 N) {
    Vector3 Wy = Vector3.J;
    Vector3 Wz = Vector3.K;
    Vector3 Ax;
    if (Math.abs(N.x()) < (1.0 / 64.0) && Math.abs(N.y()) < (1.0 / 64.0)) {
      Ax = Wy.cross(N);
    } else {
      Ax = Wz.cross(N);
    }
    return Basis3.fromWU(N, Ax);
  }

  public static void advanceToGroupCode(int groupCode, DxfReader dxf) {
    while (dxf.getCurrentElement().getGroupCode() != groupCode) {
      dxf.advance();
    }
  }

  public static void advanceToEntity(String name, DxfReader dxf) {
    while (true) {
      advanceToGroupCode(0, dxf);
      if (dxf.getCurrentElement().getStringValue().equals(name)) {
        break;
      }

      dxf.advance();
    }
  }

  public static void advanceToSection(String name, DxfReader dxf) {
    do {
      advanceToEntity("SECTION", dxf);
      advanceToGroupCode(2, dxf);
    } while (dxf.getCurrentElement().getStringValue() != name);
    dxf.advance();
  }

  public static Map<String, DxfElement> parseHeader(DxfReader dxf) {
    advanceToSection("HEADER", dxf);

    Map<String, DxfElement> header = new HashMap<String, DxfElement>();

    while (true) {
      DxfElement elem = dxf.getCurrentElement();

      if (elem.getGroupCode() == 0 && elem.getStringValue().equals("ENDSEC")) {
        break;
      } else if (elem.getGroupCode() == 9) {
        String var = elem.getStringValue();
        dxf.advance();
        header.put(var, dxf.getCurrentElement());
      }

      dxf.advance();
    }

    return header;
  }

}
