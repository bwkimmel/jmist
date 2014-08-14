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
package ca.eandb.jmist.framework.scatter;

import java.util.Arrays;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePointGeometry;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.SphericalCoordinates;
import ca.eandb.jmist.math.Vector3;

/**
 * @author bwkimmel
 *
 */
public class TabularPerturbingSurfaceScatterer implements SurfaceScatterer {

  private final double[] wavelengths;
  
  private final double[][] luts;

  public TabularPerturbingSurfaceScatterer(double[] wavelengths,
      double[] exitantAngles, double[][] cdfs, int tableSize) {
    this.wavelengths = wavelengths;
    luts = new double[wavelengths.length][];
    for (int i = 0; i < wavelengths.length; i++) {
      luts[i] = new double[tableSize];
      for (int j = 0; j < tableSize; j++) {
        luts[i][j] = MathUtil.interpolate(cdfs[i], exitantAngles, (double) j / (double) (tableSize - 1));
      }
    }
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.scatter.SurfaceScatterer#scatter(ca.eandb.jmist.framework.SurfacePointGeometry, ca.eandb.jmist.math.Vector3, boolean, double, ca.eandb.jmist.framework.Random)
   */
  public Vector3 scatter(SurfacePointGeometry x, Vector3 v, boolean adjoint,
      double lambda, Random rnd) {
    
    double[] lut0;
    double[] lut1 = null;
    double t = 0.0;
    if (lambda <= wavelengths[0]) {
      lut0 = luts[0];
    } else if (lambda >= wavelengths[wavelengths.length - 1]) {
      lut0 = luts[wavelengths.length - 1];
    } else {
      int index = Arrays.binarySearch(wavelengths, lambda);
      if (index > 0) {
        lut0 = luts[index];
      } else {
        index = -index - 1;
        lut0 = luts[index - 1];
        lut1 = luts[index];
        t = (lambda - wavelengths[index - 1]) / (wavelengths[index] - wavelengths[index - 1]);
      }
    }
    
    Basis3 basis = Basis3.fromW(v);
    Vector3 N = x.getNormal();
    boolean inDir = (v.dot(N) < 0.0);
    boolean outDir;
    
    do {
      int j = (int) Math.floor(rnd.next() * (double) lut0.length);
      double theta = (lut1 != null)
          ? MathUtil.interpolate(lut0[j], lut1[j], t)
          : lut0[j];
      double phi = 2.0 * Math.PI * rnd.next();
      SphericalCoordinates sc = new SphericalCoordinates(theta, phi);
      v = sc.toCartesian(basis);
      outDir = (v.dot(N) < 0.0);
    } while (inDir != outDir);
    
    return v;
  }

}
