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
package ca.eandb.jmist.framework.shader.ray;

import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.Texture2;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.Point2;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.SphericalCoordinates;

/**
 * A <code>RayShader</code> that shades a ray based on a <code>Texture2</code>
 * based on an angular projection.  The center of the <code>Texture2</code>
 * (i.e., (0.5,0.5)) represents the default view direction (-z).  The distance
 * outward from the center corresponds to the angle between -z and the ray
 * direction, and the angle about the center of the texture represents the
 * azimuthal angle about -z.
 *
 * @author Brad Kimmel
 */
public final class AngularEnvironmentRayShader implements RayShader {

  /** Serialization version ID. */
  private static final long serialVersionUID = 6076285080204159796L;

  /** The <code>Texture2</code> to interpret as an angular map. */
  private final Texture2 texture;

  /** The <code>Basis3</code> representing the view orientation. */
  private final Basis3 basis;

  /**
   * Creates a new <code>AngularEnvironmentRayShader</code>.
   * @param texture The <code>Texture2</code> to be interpreted as an angular
   *     map.
   */
  public AngularEnvironmentRayShader(Texture2 texture) {
    this(texture, Basis3.STANDARD);
  }

  /**
   * Creates a new <code>AngularEnvironmentRayShader</code>.
   * @param texture The <code>Texture2</code> to be interpreted as an angular
   *     map.
   * @param basis The <code>Basis3</code> representing the view orientation.
   */
  public AngularEnvironmentRayShader(Texture2 texture, Basis3 basis) {
    this.texture = texture;
    this.basis = basis;
  }

  @Override
  public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
    SphericalCoordinates sc = SphericalCoordinates.fromCartesian(ray.direction(), basis);
    double theta = -sc.azimuthal();
    double r = 0.5 * (1.0 - (Math.PI - sc.polar()) / Math.PI);
    Point2 uv = new Point2(
        0.5 + r * Math.cos(theta),
        0.5 + r * Math.sin(theta));
    return texture.evaluate(uv, lambda);
  }

}
