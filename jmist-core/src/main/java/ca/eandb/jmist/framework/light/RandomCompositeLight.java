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

import java.util.Collection;

import ca.eandb.jmist.framework.Illuminable;
import ca.eandb.jmist.framework.Light;
import ca.eandb.jmist.framework.LightSample;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.path.LightNode;
import ca.eandb.jmist.framework.path.PathInfo;
import ca.eandb.jmist.framework.path.ScaledLightNode;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SeedReference;

/**
 * @author Brad Kimmel
 *
 */
public final class RandomCompositeLight extends CompositeLight {

  /**
   * Serialization version ID.
   */
  private static final long serialVersionUID = 6951460238617706023L;

  public RandomCompositeLight() {
    super();
  }

  public RandomCompositeLight(Collection<? extends Light> children) {
    super(children);
  }

  @Override
  public void illuminate(SurfacePoint x, WavelengthPacket lambda, Random rng, final Illuminable target) {
    int index = RandomUtil.discrete(0, children().size() - 1, rng);
    children().get(index).illuminate(x, lambda, rng, new Illuminable() {
      public void addLightSample(LightSample sample) {
        target.addLightSample(ScaledLightSample.create(children().size(), sample));
      }
    });
  }

  @Override
  public LightNode sample(PathInfo pathInfo, double ru, double rv, double rj) {
    SeedReference ref = new SeedReference(rj);
    int index = RandomUtil.discrete(0, children().size() - 1, ref);
    return ScaledLightNode.create(1.0 / children().size(),
        children().get(index).sample(pathInfo, ru, rv, ref.seed), rj);
  }

  @Override
  public double getSamplePDF(SurfacePoint x, PathInfo pathInfo) {
    double pdf = 0.0;
    for (Light light : children()) {
      pdf += light.getSamplePDF(x, pathInfo);
    }
    return pdf / (double) children().size();
  }

}
