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
package ca.eandb.jmist.framework.material.biospec;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Material;
import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.ScatteredRay;
import ca.eandb.jmist.framework.SurfacePoint;
import ca.eandb.jmist.framework.color.Color;
import ca.eandb.jmist.framework.color.WavelengthPacket;
import ca.eandb.jmist.framework.material.OpaqueMaterial;
import ca.eandb.jmist.framework.random.SimpleRandom;
import ca.eandb.jmist.framework.random.ThreadLocalRandom;
import ca.eandb.jmist.math.Vector3;

public final class SequentialMaterial extends OpaqueMaterial {

  /** Serialization version ID. */
  private static final long serialVersionUID = -6825571743685716845L;

  private final List<Material> inner = new ArrayList<>();

  private final Random rnd = new ThreadLocalRandom(new SimpleRandom());

  public SequentialMaterial addScatterer(Material e) {
    inner.add(e);
    return this;
  }

  @Override
  public ScatteredRay scatter(SurfacePoint x, Vector3 v, boolean adjoint,
      WavelengthPacket lambda, double ru, double rv, double rj) {
    ScatteredRay sr = null;
    Color col = lambda.getColorModel().getWhite(lambda);

    for (Material e : inner) {
      sr = e.scatter(x, v, adjoint, lambda, rnd.next(), rnd.next(), rnd.next());
      if (sr == null) break;
      v = sr.getRay().direction();
      col = col.times(sr.getColor());
    }

    if (sr == null) {
      return null;
    } else if (sr.getRay().direction().dot(x.getNormal()) < 0.0) {
      return ScatteredRay.diffuse(sr.getRay(), col, 1.0);
    } else {
      return ScatteredRay.transmitDiffuse(sr.getRay(), col, 1.0);
    }
  }

}
