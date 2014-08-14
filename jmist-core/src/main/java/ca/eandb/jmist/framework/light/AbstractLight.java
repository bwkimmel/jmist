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
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;

/**
 * @author brad
 *
 */
public abstract class AbstractLight implements Light {

  /** Serialization version ID. */
  private static final long serialVersionUID = 4969272856518133591L;

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Light#emit(ca.eandb.jmist.framework.color.WavelengthPacket, ca.eandb.jmist.framework.Random)
   */
  public final ScatteredRay emit(WavelengthPacket lambda, Random rnd) {
    PathInfo path = new PathInfo(lambda);
    LightNode node = sample(path, rnd.next(), rnd.next(), rnd.next());
    return node.sample(rnd.next(), rnd.next(), rnd.next());
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Light#sample(ca.eandb.jmist.framework.path.PathInfo, double, double, double)
   */
  @Override
  public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Light#getSamplePDF(ca.eandb.jmist.framework.SurfacePoint, ca.eandb.jmist.framework.path.PathInfo)
   */
  @Override
  public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
    return 0;
  }

}
