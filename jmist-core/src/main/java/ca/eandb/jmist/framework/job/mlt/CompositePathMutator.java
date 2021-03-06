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
package ca.eandb.jmist.framework.job.mlt;

import java.util.ArrayList;
import java.util.List;

import ca.eandb.jmist.framework.Random;
import ca.eandb.jmist.framework.path.Path;
import ca.eandb.jmist.framework.random.CategoricalRandom;
import ca.eandb.util.DoubleArray;

public final class CompositePathMutator implements PathMutator {

  private final List<PathMutator> mutators = new ArrayList<>();

  private final DoubleArray weights = new DoubleArray();

  private CategoricalRandom random = null;

  public CompositePathMutator add(double weight, PathMutator mutator) {
    mutators.add(mutator);
    weights.add(weight);
    random = null;
    return this;
  }

  private void ensureReady() {
    if (random == null) {
      synchronized (this) {
        if (random == null) {
          random = new CategoricalRandom(weights);
        }
      }
    }
  }

  @Override
  public double getTransitionPDF(Path from, Path to) {
    ensureReady();

    double pdf = 0.0;
    for (int i = 0, n = mutators.size(); i < n; i++) {
      PathMutator mutator = mutators.get(i);
      double weight = random.getProbability(i);
      pdf += weight * mutator.getTransitionPDF(from, to);
    }
    return pdf;
  }

  @Override
  public Path mutate(Path path, Random rnd) {
    ensureReady();

    int index = random.next(rnd);
    PathMutator mutator = mutators.get(index);

    return mutator.mutate(path, rnd);
  }

}
