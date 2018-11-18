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
package ca.eandb.jmist.framework.path;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Scene;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.shader.MinimalShadingContext;
import ca.eandb.jmist.math.Ray3;

/**
 * Abstract base class for <code>PathNode</code>s.
 * @author Brad Kimmel
 */
public abstract class AbstractPathNode implements PathNode {

  private final PathInfo pathInfo;

  private final double ru, rv, rj;

  protected AbstractPathNode(PathInfo pathInfo, double ru, double rv, double rj) {
    this.pathInfo = pathInfo;
    this.ru = ru;
    this.rv = rv;
    this.rj = rj;
  }

  public final double getRU() {
    return ru;
  }

  public final double getRV() {
    return rv;
  }

  public final double getRJ() {
    return rj;
  }

  protected final Color getWhite() {
    return ColorUtil.getWhite(pathInfo.getWavelengthPacket());
  }

  protected final Color getBlack() {
    return ColorUtil.getBlack(pathInfo.getWavelengthPacket());
  }

  protected final Color getGray(double value) {
    return ColorUtil.getGray(value, pathInfo.getWavelengthPacket());
  }

  protected final Color sample(Spectrum s) {
    return s.sample(pathInfo.getWavelengthPacket());
  }

  protected final ScatteringNode trace(ScatteredRay sr, double ru, double rv, double rj) {
    if (sr == null) {
      return null;
    }
    Scene scene = pathInfo.getScene();
    Ray3 ray = sr.getRay();
    SceneElement root = scene.getRoot();
    Intersection x = NearestIntersectionRecorder
        .computeNearestIntersection(ray, root);
    if (x != null) {
      ShadingContext context = new MinimalShadingContext();
      x.prepareShadingContext(context);
      context.getModifier().modify(context);
      return new SurfaceNode(this, sr, context, ru, rv, rj);
    } else {
      return new BackgroundNode(this, sr, ru, rv, rj);
    }
  }

  @Override
  public final ScatteringNode expand(double ru, double rv, double rj) {
    return trace(sample(ru, rv, rj), ru, rv, rj);
  }

  @Override
  public boolean isAtInfinity() {
    return getPosition().isVector();
  }

  @Override
  public final boolean isOnEyePath() {
    return !isOnLightPath();
  }

  @Override
  public final PathInfo getPathInfo() {
    return pathInfo;
  }

}
