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
package ca.eandb.jmist.framework.light;

import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.VisibilityFunction3;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.math.Ray3;
import ca.eandb.jmist.math.Vector3;

public final class DirectionalLightSample implements LightSample {

  private final SurfacePoint x;

  private final Vector3 direction;

  private final Color radiance;

  private final boolean shadows;

  public DirectionalLightSample(SurfacePoint x, Vector3 direction,
      Color radiance, boolean shadows) {
    this.x = x;
    this.direction = direction;
    this.radiance = radiance;
    this.shadows = shadows;
  }

  public DirectionalLightSample(SurfacePoint x, Vector3 direction,
      Color radiance) {
    this(x, direction, radiance, true);
  }

  @Override
  public boolean castShadowRay(VisibilityFunction3 vf) {
    return shadows && !vf.visibility(new Ray3(x.getPosition(), direction));
  }

  @Override
  public Color getRadiantIntensity() {
    return radiance;
  }

  @Override
  public Vector3 getDirToLight() {
    return direction;
  }

}
