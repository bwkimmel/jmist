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
package ca.eandb.jmist.framework.job.bidi;

import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.PathUtil;
import ca.eandb.jmist.framework.path.ScatteringNode;

public final class MeasurementContributionMeasure implements PathMeasure {

  /** Serialization version ID. */
  private static final long serialVersionUID = 6245725963682383802L;

  private static final PathMeasure INSTANCE = new MeasurementContributionMeasure();

  private MeasurementContributionMeasure() {}

  public static PathMeasure getInstance() {
    return INSTANCE;
  }

  @Override
  public Color evaluate(PathNode lightTail, PathNode eyeTail) {
    if (lightTail != null && eyeTail != null) {
      return evaluateInner(lightTail, eyeTail);
    } else if (lightTail == null && eyeTail instanceof ScatteringNode) {
      return evaluateEyePathOnLight((ScatteringNode) eyeTail);
    } else {
      return null;
    }
  }

  private Color evaluateInner(PathNode lightTail, PathNode eyeTail) {
    return PathUtil.join(lightTail, eyeTail);
  }

  private Color evaluateEyePathOnLight(ScatteringNode eyeTail) {
    return ColorUtil.mul(eyeTail.getCumulativeWeight(),
        eyeTail.getSourceRadiance());
  }

}
