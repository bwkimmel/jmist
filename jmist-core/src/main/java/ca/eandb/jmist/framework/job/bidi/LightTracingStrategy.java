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
package ca.eandb.jmist.framework.job.bidi;

import ca.eandb.jmist.framework.Lens;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.PathNode;
import ca.eandb.jmist.framework.path.PathUtil;
import ca.eandb.jmist.math.Point2;

/**
 * @author Brad
 *
 */
public final class LightTracingStrategy implements BidiPathStrategy {

  /** Serialization version ID. */
  private static final long serialVersionUID = 4062110263104652154L;

  private final int maxDepth;

  public LightTracingStrategy(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  @Override
  public double getWeight(PathNode lightNode, PathNode eyeNode) {
    if (lightNode == null) {
      return 0.0;
    }
    if (eyeNode == null) {
      int lightDepthDiffuse = lightNode.getDepth();
      while (lightNode.getDepth() > 0) {
        if (lightNode.isSpecular()) {
          lightDepthDiffuse--;
        }
        lightNode = lightNode.getParent();
      }
      return lightDepthDiffuse == 1 ? 1.0 : 0.0;
    }
    return eyeNode.getDepth() == 0 ? 1.0 : 0.0;
  }

  @Override
  public PathNode traceEyePath(Lens lens, Point2 p, PathInfo pathInfo,
      Random rnd) {
    return lens.sample(p, pathInfo, rnd.next(), rnd.next(), rnd.next());
  }

  @Override
  public PathNode traceLightPath(Light light, PathInfo pathInfo, Random rnd) {
    PathNode head = light.sample(pathInfo, rnd.next(), rnd.next(), rnd.next());
    return PathUtil.expand(head, maxDepth - 1, rnd);
  }

}
