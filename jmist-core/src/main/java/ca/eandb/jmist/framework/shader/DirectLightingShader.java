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
package ca.eandb.jmist.framework.shader;

import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.math.Vector3;

public final class DirectLightingShader implements Shader {

  /** Serialization version ID. */
  private static final long serialVersionUID = -7391459165157437122L;

  @Override
  public Color shade(ShadingContext sc) {
    Material mat = sc.getMaterial();
    WavelengthPacket lambda = sc.getWavelengthPacket();
    Vector3 normal = sc.getShadingNormal();
    Color sum = sc.getColorModel().getBlack(lambda);
    for (LightSample sample : sc.getLightSamples()) {
      if (!sample.castShadowRay(sc)) {
        Vector3 in = sample.getDirToLight().opposite();
        Vector3 out = sc.getIncident().opposite();
        Color bsdf = mat.bsdf(sc, in, out, lambda);
        double dot = Math.abs(in.dot(normal));
        sum = sum.plus(sample.getRadiantIntensity().times(bsdf.times(dot)));
      }
    }
    return sum;
  }

}
