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
package ca.eandb.jmist.framework.material.biospec;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public class TabularPerturbingMaterial extends OpaqueMaterial {

//  private final double[] wavelengths;

  /** Serialization version ID. */
  private static final long serialVersionUID = 359194554532848287L;

  private final double[] lut;

  private final Random rnd = new ThreadLocalRandom(new SimpleRandom());

  public TabularPerturbingMaterial(
      double[] exitantAngles, double[] cdf, int tableSize) {
    lut = new double[tableSize];
    for (int j = 0; j < tableSize; j++) {
      lut[j] = MathUtil.interpolate(cdf, exitantAngles, (double) j / (double) (tableSize - 1));
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.material.AbstractMaterial#scatter(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.math.Vector3, boolean, ca.eandb.jmist.framework.color.WavelengthPacket, double, double, double)
   */
  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {

    Basis3 basis = Basis3.fromW(v);
    Vector3 N = x.getNormal();
    boolean inDir = (v.dot(N) < 0.0);
    boolean outDir;

    do {
      int j = (int) Math.floor(rnd.next() * (double) lut.length);
      double theta = lut[j];
      double phi = 2.0 * Math.PI * rnd.next();
      SphericalCoordinates sc = new SphericalCoordinates(theta, phi);
      v = sc.toCartesian(basis);
      outDir = (v.dot(N) < 0.0);
    } while (inDir != outDir);

    return ScatteredRay.diffuse(new Ray3(x.getPosition(), v), lambda.getColorModel().getWhite(lambda), 1.0);
  }

}
