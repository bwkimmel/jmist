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
 * representing a hemispherical environment map.  The x-coordinate of the
 * <code>Texture2</code> is interpreted as an azimuthal angle, and the
 * y-coordinate is interpreted as a polar angle from 0 to pi/2.
 *
 * @author Brad Kimmel
 */
public final class HemisphericalEnvironmentRayShader implements RayShader {

  /** Serialization version ID. */
  private static final long serialVersionUID = 6076285080204159796L;

  /** The <code>Texture2</code> to interpret as a hemispherical map. */
  private final Texture2 texture;

  /** The <code>Basis3</code> representing the view orientation. */
  private final Basis3 basis;

  /** The <code>RayShader</code> to apply to the lower hemisphere. */
  private final RayShader background;

  /**
   * Creates a new <code>HemisphericalEnvironmentRayShader</code>.
   * @param texture The <code>Texture2</code> to interpret as a hemispherical
   *     map.
   */
  public HemisphericalEnvironmentRayShader(Texture2 texture) {
    this(texture, Basis3.STANDARD);
  }

  /**
   * Creates a new <code>HemisphericalEnvironmentRayShader</code>.
   * @param texture The <code>Texture2</code> to interpret as a hemispherical
   *     map.
   * @param basis The <code>Basis3</code> representing the view orientation.
   */
  public HemisphericalEnvironmentRayShader(Texture2 texture, Basis3 basis) {
    this(texture, basis, RayShader.BLACK);
  }

  /**
   * Creates a new <code>HemisphericalEnvironmentRayShader</code>.
   * @param texture The <code>Texture2</code> to interpret as a hemispherical
   *     map.
   * @param basis The <code>Basis3</code> representing the view orientation.
   * @param background The <code>RayShader</code> to apply to the lower
   *     hemisphere.
   */
  public HemisphericalEnvironmentRayShader(Texture2 texture, Basis3 basis, RayShader background) {
    this.texture = texture;
    this.basis = basis;
    this.background = background;
  }

  @Override
  public Color shadeRay(Ray3 ray, WavelengthPacket lambda) {
    if (ray.direction().dot(basis.w()) >= 0.0) {
      SphericalCoordinates sc = SphericalCoordinates.fromCartesian(ray.direction(), basis);
      Point2 uv = new Point2(
          (sc.azimuthal() + Math.PI) / (2.0 * Math.PI),
          2.0 * sc.polar() / Math.PI);
      return texture.evaluate(uv).sample(lambda);
    } else {
      return background.shadeRay(ray, lambda);
    }
  }

}
