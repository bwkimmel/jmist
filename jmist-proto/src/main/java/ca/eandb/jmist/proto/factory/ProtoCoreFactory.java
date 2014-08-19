/* Copyright (c) 2014 Bradley W. Kimmel
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
package ca.eandb.jmist.proto.factory;

import java.util.List;

import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.LinearMatrix3;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;
import ca.eandb.jmist.proto.CoreProtos;

/**
 *
 */
public final class ProtoCoreFactory {

  public Point3 createPoint3(CoreProtos.Vector3 vecIn) {
    return new Point3(vecIn.getX(), vecIn.getY(), vecIn.getZ());
  }

  public Vector3 createVector3(CoreProtos.Vector3 vecIn) {
    return new Vector3(vecIn.getX(), vecIn.getY(), vecIn.getZ());
  }

  public AffineMatrix3 createAffineMatrix3(List<Double> elements) {
    switch (elements.size()) {
      case 9:
        return new AffineMatrix3(createLinearMatrix3(elements));
      case 12:
        return new AffineMatrix3(
            elements.get(0), elements.get(1), elements.get(2) , elements.get(3),
            elements.get(4), elements.get(5), elements.get(6) , elements.get(7),
            elements.get(8), elements.get(9), elements.get(10), elements.get(11));
      default:
        throw new IllegalArgumentException(String.format(
            "Expected 9 or 12 elements for affine 4x4 matrix, but got %d.",
            elements.size()));
    }
  }

  public LinearMatrix3 createLinearMatrix3(List<Double> elements) {
    if (elements.size() != 9) {
      throw new IllegalArgumentException(String.format(
          "Expected 9 elements for linear 3x3 matrix, but got %d.",
          elements.size()));
    }

    return new LinearMatrix3(
        elements.get(0), elements.get(1), elements.get(2),
        elements.get(3), elements.get(4), elements.get(5),
        elements.get(6), elements.get(7), elements.get(8));
  }

}
