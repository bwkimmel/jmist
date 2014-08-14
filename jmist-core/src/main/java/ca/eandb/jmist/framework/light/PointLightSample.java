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

import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author brad
 *
 */
public final class PointLightSample implements LightSample {

  private final SurfacePoint x;

  private final Point3 position;

  private final Color intensity;

  private final boolean shadows;

  /**
   * @param x
   * @param position
   * @param intensity
   * @param shadows
   */
  public PointLightSample(SurfacePoint x, Point3 position, Color intensity,
      boolean shadows) {
    this.x = x;
    this.position = position;
    this.intensity = intensity;
    this.shadows = shadows;
  }

  /**
   * @param x
   * @param position
   * @param intensity
   */
  public PointLightSample(SurfacePoint x, Point3 position, Color intensity) {
    this(x, position, intensity, true);
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.LightSample#castShadowRay(ca.eandb.jmist.framework.VisibilityFunction3)
   */
  public boolean castShadowRay(VisibilityFunction3 vf) {
    return shadows && !vf.visibility(new Ray3(x.getPosition(), position));
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.LightSample#getRadiantIntensity()
   */
  public Color getRadiantIntensity() {
    return intensity;
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.LightSample#getDirToLight()
   */
  public Vector3 getDirToLight() {
    return x.getPosition().vectorTo(position).unit();
  }

}
