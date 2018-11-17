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

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.RayShader;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.SimpleRandom;

public final class EnvironmentShader implements Shader {
  private final RayShader environment;
  private final boolean shadows;

  private transient ThreadLocal<Random> sampler = ThreadLocal.withInitial(SimpleRandom::new);

  public EnvironmentShader(RayShader environment, boolean shadows) {
    this.environment = environment;
    this.shadows = shadows;
  }

  @Override
  public Color shade(ShadingContext sc) {
    Random rnd = sampler.get();
    WavelengthPacket lambda = sc.getWavelengthPacket();
    ScatteredRay ray = sc.getMaterial().scatter(sc, sc.getIncident(), true, lambda, rnd.next(), rnd.next(), rnd.next());
    if (ray == null || (shadows && !sc.visibility(ray.getRay()))) {
      return sc.getColorModel().getBlack(lambda);
    }
    return environment.shadeRay(ray.getRay(), lambda).times(ray.getColor());
  }
}
