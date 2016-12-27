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
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.Intersection;
import ca.eandb.jmist.framework.NearestIntersectionRecorder;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.Spectrum;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Ray3;

/**
 * A ray shader that shades a ray according to the distance to the
 * nearest intersection along the ray.
 * @author Brad Kimmel
 */
public final class DistanceRayShader implements RayShader {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = -4530038647449382442L;

  /**
   * Creates a <code>DistanceRayShader</code>.
   * @param root The <code>SceneElement</code> to use.
   */
  public DistanceRayShader(SceneElement root) {
    this(root, null);
  }

  /**
   * Creates a <code>DistanceRayShader</code>.
   * @param root The <code>SceneElement</code> to use.
   * @param missValue The <code>Spectrum</code> to assign to rays that do not
   *     intersect with any object.
   */
  public DistanceRayShader(SceneElement root, Spectrum missValue) {
    this.root = root;
    this.missValue = missValue;
  }

  @Override
  public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
    Intersection x = NearestIntersectionRecorder.computeNearestIntersection(ray, root);
    ColorModel cm = lambda.getColorModel();

    return (x != null) ? cm.getGray(x.getDistance(), lambda) : (missValue != null) ? missValue.sample(lambda) : cm.getBlack(lambda);
  }

  /** The ray caster to use. */
  private final SceneElement root;

  private final Spectrum missValue;

}
