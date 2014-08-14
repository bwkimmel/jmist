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
package ca.eandb.jmist.framework.shader;

import java.io.IOException;
import java.io.ObjectInputStream;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.Shader;
import ca.eandb.jmist.framework.ShadingContext;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.ColorUtil;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.random.NRooksRandom;
import ca.eandb.jmist.framework.random.RandomUtil;
import ca.eandb.jmist.framework.random.SimpleRandom;

/**
 * A <code>Shader</code> that traces up to one randomly selected scattered ray.
 * @author Brad Kimmel
 */
public final class PathTracingShader implements Shader {

  /** Serialization version ID. */
  private static final long serialVersionUID = -3619786295095920623L;

  /** The default maximum path depth. */
  private static final int DEFAULT_MAX_DEPTH = 10;
  
  private static final int DEFAULT_FIRST_BOUNCE_RAYS = 1;

  /** The maximum path depth. */
  private final int maxDepth;
  
  private final int firstBounceRays;
  
  private transient ThreadLocal<Random> rnd;
  
  private transient ThreadLocal<Random> firstBounceSampler;

  /**
   * Creates a new <code>PathTracingShader</code>.
   */
  public PathTracingShader() {
    this(DEFAULT_MAX_DEPTH, DEFAULT_FIRST_BOUNCE_RAYS);
  }
  
  /**
   * Creates a new <code>PathTracingShader</code>.
   * @param maxDepth The maximum path depth.
   */
  public PathTracingShader(int maxDepth) {
    this(maxDepth, DEFAULT_FIRST_BOUNCE_RAYS);
  }

  /**
   * Creates a new <code>PathTracingShader</code>.
   * @param maxDepth The maximum path depth.
   * @param firstBounceRays The number of secondary rays to cast on the first
   *     bounce.
   */
  public PathTracingShader(int maxDepth, int firstBounceRays) {
    this.maxDepth = maxDepth;
    this.firstBounceRays = firstBounceRays;
    initialize();
  }

  /** Sets up the random number generated used by this shader. */
  private void initialize() {
    rnd = new ThreadLocal<Random>() {
      protected Random initialValue() {
        return new SimpleRandom();
      }
    };
    if (firstBounceRays > 0) {
      firstBounceSampler = new ThreadLocal<Random>() {
        protected Random initialValue() {
          return new NRooksRandom(firstBounceRays, 3, rnd.get());
        }
      };
    }
  }
  
  private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
    initialize();
  }

  /* (non-Javadoc)
   * @see ca.eandb.jmist.framework.Shader#shade(ca.eandb.jmist.framework.ShadingContext)
   */
  public Color shade(ShadingContext sc) {

    if (firstBounceRays > 0 && sc.getPathDepth() < 1) {
      Random sampler = firstBounceSampler.get();
      WavelengthPacket lambda = sc.getWavelengthPacket();
      Color shade = sc.getColorModel().getBlack(lambda);
      for (int i = 0; i < firstBounceRays; i++) {
        ScatteredRay ray = sc.getMaterial().scatter(sc, sc.getIncident(), true, sc.getWavelengthPacket(), sampler.next(), sampler.next(), sampler.next());
        if (ray != null) {
          shade = shade.plus(sc.castRay(ray).times(ray.getColor()));
        }
      }
      return shade.divide(firstBounceRays);
    } else if (sc.getPathDepth() < maxDepth) {
      ScatteredRay ray = sc.getScatteredRay();
      if (ray != null) {
        double prob = ColorUtil.getMeanChannelValue(ray.getColor());
        if (prob < 1.0) {
          if (RandomUtil.bernoulli(prob, rnd.get())) {
            ray = ScatteredRay.select(ray, prob);
          } else {
            ray = null;
          }
        }
  
        if (ray != null) {
          return sc.castRay(ray).times(ray.getColor());
        }
      }
    }

    WavelengthPacket lambda = sc.getWavelengthPacket();
    return sc.getColorModel().getBlack(lambda);

  }

}
